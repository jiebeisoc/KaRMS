/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PromotionSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Outlet;
import entity.Promotion;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import util.enumeration.ReservationStatus;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author zihua
 */
@Named(value = "reservationManagementManagedBean")
@ViewScoped
public class ReservationManagementManagedBean implements Serializable {

    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    
    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;
    
    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;
    
    @EJB(name = "PromotionSessionBeanLocal")
    private PromotionSessionBeanLocal promotionSessionBeanLocal;
    
    private List<Reservation> reservations;
    private List<RoomType> roomTypes;
    private List<Room> rooms;
    private List<Outlet> outlets;
    private List<Promotion> promotions;
    
    private Reservation newReservation;
    private Long memberNum;
    private Long roomTypeId;
    private Long roomId;
    private Long outletId;
    private Long promotionId;
    
    private List<Reservation> filteredReservations;
    private Reservation selectedReservation;
    private ReservationStatus[] statusList;
    private Date filterDateFrom;
    private Date filterDateTo;
    
    private LocalDate minDate;
    private LocalDate maxDate;
    private Date selectedDate;
    
    public ReservationManagementManagedBean() {
        newReservation = new Reservation();
    }
    
    @PostConstruct
    public void postConstruct() {
        reservations = reservationSessionBeanLocal.retrieveAllReservations();
        roomTypes = roomTypeSessionBeanLocal.retrieveAllRoomTypes();
        rooms = roomSessionBeanLocal.retrieveAllRoom();
        outlets = outletSessionBeanLocal.retrieveAllOutlets();
        promotions = new ArrayList<>();
        statusList = ReservationStatus.values();
        
        minDate = LocalDate.now();
        maxDate = LocalDate.now().plusYears(1);
    }
    
    public void createNewReservation(ActionEvent event) {
        try {
            Long reservationId = reservationSessionBeanLocal.createNewReservation(newReservation, memberNum, roomId, outletId, promotionId);
            reservations.add(newReservation);

            memberNum = null;
            roomId = null;
            outletId = null;
            promotionId = null;
            newReservation = new Reservation();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New reservation is created successfully", null));
        
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }
    
    public void onDateChange(ValueChangeEvent event) {
        selectedDate = (Date)event.getNewValue();
    }
    
    public void dateChange(AjaxBehaviorEvent event) {
        promotions = promotionSessionBeanLocal.retrievePromotionByDate(selectedDate);   
    }
    
    public void deleteReservation(ActionEvent event) {
        Reservation reservationToDelete = (Reservation)event.getComponent().getAttributes().get("reservationToDelete");
        
        reservationSessionBeanLocal.deleteReservation(reservationToDelete.getReservationId());
        reservations.remove(reservationToDelete);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reservation is deleted successfully", null));
    }
    
//    public void filterByDate(AjaxBehaviorEvent event) {
//        List<Long> filteredReservationIdsByDate = reservationSessionBeanLocal.retrieveReservationByDate(filterDateFrom, filterDateTo);
//        
//        int size = filteredReservations.size();
//        for(int i = 0; i < size; i++) {
//            Reservation reservation = filteredReservations.get(i);
//            
//            if(!filteredReservationIdsByDate.contains(reservation.getReservationId())) {
//                filteredReservations.remove(i);
//            }
//        }
//    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Reservation getNewReservation() {
        return newReservation;
    }

    public void setNewReservation(Reservation newReservation) {
        this.newReservation = newReservation;
    }

    public Long getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Long memberNum) {
        this.memberNum = memberNum;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<Outlet> outlets) {
        this.outlets = outlets;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Reservation getSelectedReservation() {
        return selectedReservation;
    }

    public void setSelectedReservation(Reservation selectedReservation) {
        this.selectedReservation = selectedReservation;
    }
    
    public void onRowSelect(SelectEvent event) {
        this.selectedReservation = (Reservation)event.getObject();
    }
    
    public void onRowUnselect(SelectEvent event) {
        this.selectedReservation = null;
    }

    public List<Reservation> getFilteredReservations() {
        return filteredReservations;
    }

    public void setFilteredReservations(List<Reservation> filteredReservations) {
        this.filteredReservations = filteredReservations;
    }

    public Date getFilterDateFrom() {
        return filterDateFrom;
    }

    public void setFilterDateFrom(Date filterDateFrom) {
        this.filterDateFrom = filterDateFrom;
    }

    public Date getFilterDateTo() {
        return filterDateTo;
    }

    public void setFilterDateTo(Date filterDateTo) {
        this.filterDateTo = filterDateTo;
    }

    public ReservationStatus[] getStatusList() {
        return statusList;
    }

    public void setStatusList(ReservationStatus[] statusList) {
        this.statusList = statusList;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
    
}
