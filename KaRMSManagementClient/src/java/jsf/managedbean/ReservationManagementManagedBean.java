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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
import util.exception.NoAvailableRoomException;

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

    private List<Reservation> filteredReservations;
    private Reservation selectedReservation;
    private ReservationStatus[] statusList;
    private Date filterDateFrom;
    private Date filterDateTo;
        
    private Reservation newReservation;
    private Long memberNum;
    private Long roomTypeId;
    private Long roomId;
    private Long outletId;
    private Long promotionId;
    private BigDecimal totalPrice;
     
    private Long roomTypeIdUpdate;
    private Long roomIdUpdate;
    private Long outletIdUpdate;
    private Long promotionIdUpdate;
    private Date dateUpdate;
    private int durationUpdate;    
    
    private Date minDate;
    private Date maxDate;

    private Long changedRoomTypeId;
    
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
        totalPrice = new BigDecimal("0.00"); 
    }
    
    public void onCreateNewReservation(ActionEvent event) {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.MINUTE) >= 1) {
            cal.add(Calendar.HOUR, 1);
        }
        cal.set(Calendar.MINUTE, 0);
        System.out.println("time: " + cal.getTime());
        minDate = cal.getTime();
        System.out.println("minDate: " + minDate);
        cal.add(Calendar.YEAR, 1);
        maxDate = cal.getTime();
    }
    
    public void createNewReservation(ActionEvent event) {
        try {
            roomId = reservationSessionBeanLocal.retrieveAvailableRoom(newReservation, outletId, roomTypeId);
            int roomNum = roomSessionBeanLocal.retrieveRoomById(roomId).getRoomNum();
            
            Long reservationId = reservationSessionBeanLocal.createNewReservation(newReservation, memberNum, roomId, outletId, promotionId);
            reservations.add(newReservation);

            memberNum = null;
            roomId = null;
            outletId = null;
            promotionId = null;
            newReservation = new Reservation();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New reservation is created successfully (Room Number: " + roomNum + ")", null));
        
        } catch (NoAvailableRoomException | CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }
    
    public void calculateTotalPrice() {
        if (durationUpdate != 0 && roomTypeIdUpdate != 0L) {
            totalPrice = reservationSessionBeanLocal.calculateTotalPrice(dateUpdate, durationUpdate, roomTypeIdUpdate);
        }
        
        if (selectedReservation != null) {
            selectedReservation.setTotalPrice(totalPrice);
        }
    }
        
    public void onRoomTypeChange(ValueChangeEvent event) {
        roomTypeIdUpdate = (Long)event.getNewValue();
    }
    
    public void roomTypeChange(AjaxBehaviorEvent event) {
        calculateTotalPrice();  
    }
    
    public void onDateChange(ValueChangeEvent event) {
        dateUpdate = (Date)event.getNewValue();
    }
    
    public void dateChange(AjaxBehaviorEvent event) {
        promotions = promotionSessionBeanLocal.retrievePromotionByDate(dateUpdate);
        calculateTotalPrice();
    }
    
    public void onDurationChange(ValueChangeEvent event) {
        durationUpdate = (int)event.getNewValue();
    }
    
    public void durationChange(AjaxBehaviorEvent event) {
        calculateTotalPrice();  
    }
    
    public void updateReservation() {
        try { 
            roomIdUpdate = reservationSessionBeanLocal.retrieveAvailableRoom(selectedReservation, outletIdUpdate, roomTypeIdUpdate);
            int roomNum = roomSessionBeanLocal.retrieveRoomById(roomIdUpdate).getRoomNum();

            reservationSessionBeanLocal.updateReservation(selectedReservation, roomIdUpdate, outletIdUpdate, promotionIdUpdate);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reservation updated successfully (Room Number: " + roomNum + ")", null));
        
        } catch (NoAvailableRoomException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
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

    public void onRowSelect(SelectEvent event) {
        this.selectedReservation = (Reservation)event.getObject();
        this.roomTypeIdUpdate = selectedReservation.getRoom().getRoomType().getRoomTypeId();
        this.outletIdUpdate = selectedReservation.getOutlet().getOutletId();
        this.promotionIdUpdate = selectedReservation.getPromotion().getPromotionId();
    }
    
    public void onRowUnselect(SelectEvent event) {
        this.selectedReservation = null;
        this.roomTypeIdUpdate = null;
        this.outletIdUpdate = null;
        this.promotionIdUpdate = null;
    }
    
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

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Long getChangedRoomTypeId() {
        return changedRoomTypeId;
    }

    public void setChangedRoomTypeId(Long changedRoomTypeId) {
        this.changedRoomTypeId = changedRoomTypeId;
    }
    
    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public int getDurationUpdate() {
        return durationUpdate;
    }

    public void setDurationUpdate(int durationUpdate) {
        this.durationUpdate = durationUpdate;
    }

    public Long getRoomTypeIdUpdate() {
        return roomTypeIdUpdate;
    }

    public void setRoomTypeIdUpdate(Long roomTypeIdUpdate) {
        this.roomTypeIdUpdate = roomTypeIdUpdate;
    }

    public Long getRoomIdUpdate() {
        return roomIdUpdate;
    }

    public void setRoomIdUpdate(Long roomIdUpdate) {
        this.roomIdUpdate = roomIdUpdate;
    }

    public Long getOutletIdUpdate() {
        return outletIdUpdate;
    }

    public void setOutletIdUpdate(Long outletIdUpdate) {
        this.outletIdUpdate = outletIdUpdate;
    }

    public Long getPromotionIdUpdate() {
        return promotionIdUpdate;
    }

    public void setPromotionIdUpdate(Long promotionIdUpdate) {
        this.promotionIdUpdate = promotionIdUpdate;
    }
    
}
