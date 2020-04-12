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
import entity.Employee;
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
    private String phoneNo;
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
    private int minTime;
    private int maxTime;

    public ReservationManagementManagedBean() {
        newReservation = new Reservation();
    }
    
    @PostConstruct
    public void postConstruct() {
        Employee employee = (Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployee");
        Long outletId = null;
        if (employee.getOutlet() != null) {
            outletId = employee.getOutlet().getOutletId();
        }
        reservations = reservationSessionBeanLocal.retrieveAllReservations();
        roomTypes = roomTypeSessionBeanLocal.retrieveAllRoomTypes();
        rooms = roomSessionBeanLocal.retrieveAllRoom(outletId);
        outlets = outletSessionBeanLocal.retrieveAllOutlets();
        promotions = new ArrayList<>();
        statusList = ReservationStatus.values();
        totalPrice = new BigDecimal("0.00"); 
    }
    
    public void onCreateNewReservation(ActionEvent event) {
        minTime = 12;
        maxTime = 23;
        
        Calendar cal = Calendar.getInstance();
        
        if (cal.get(Calendar.MINUTE) >= 1) {
            cal.set(Calendar.MINUTE, 0);
            
            cal.add(Calendar.HOUR, 1);
        }
        cal.set(Calendar.SECOND, 0);
        
        if (cal.get(Calendar.HOUR_OF_DAY) > maxTime) {
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, minTime);
        } else if (cal.get(Calendar.HOUR_OF_DAY) < minTime) {
            cal.set(Calendar.HOUR_OF_DAY, minTime);
        }
            
        minDate = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        maxDate = cal.getTime();
        
    }
    
    public void createNewReservation(ActionEvent event) {
        try {
            roomId = reservationSessionBeanLocal.retrieveAvailableRoom(newReservation, outletId, roomTypeId);
            String roomNum = roomSessionBeanLocal.retrieveRoomById(roomId).getRoomNum();
            
            newReservation.setDateReserved(new Date());
            
            Long reservationId = reservationSessionBeanLocal.createNewReservation(newReservation, roomId, outletId, promotionId);
            reservations.add(newReservation);

            totalPrice = new BigDecimal("0.00");
            phoneNo = null;
            roomId = null;
            outletId = null;
            promotionId = null;
            newReservation = new Reservation();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New reservation is created successfully (Room Number: " + roomNum + ")", null));
        
        } catch (NoAvailableRoomException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }
   
    public void onRoomTypeChange(ValueChangeEvent event) {
        roomTypeIdUpdate = (Long)event.getNewValue();
    }

    public void dateChange(SelectEvent event) {
        dateUpdate = (Date)event.getObject();
        promotions = promotionSessionBeanLocal.retrievePromotionByDate(dateUpdate);
        calculateTotalPrice();
    }

    public void onDurationChange(ValueChangeEvent event) {
        durationUpdate = (int)event.getNewValue();
    }
        
    public void onPromotionChange(ValueChangeEvent event) {
        promotionIdUpdate = (Long)event.getNewValue();
    }
    
    public void onOutletChange(ValueChangeEvent event) {
        outletIdUpdate = (Long)event.getNewValue();
    }
    
    public void outletChange(AjaxBehaviorEvent event) {
        Outlet outlet = outletSessionBeanLocal.retrieveOutletById(outletIdUpdate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(outlet.getOpeningHours());
        minTime = cal.get(Calendar.HOUR_OF_DAY);
        cal.setTime(outlet.getClosingHours());
        maxTime = cal.get(Calendar.HOUR_OF_DAY) - 1; 

    }
     
    public void calculateTotalPrice() {
        totalPrice = reservationSessionBeanLocal.calculateTotalPrice(dateUpdate, durationUpdate, roomTypeIdUpdate, promotionIdUpdate);
        totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_DOWN);
        
        if (newReservation != null) {
            newReservation.setTotalPrice(totalPrice);
        } 
        
        if (selectedReservation != null) {
            selectedReservation.setTotalPrice(totalPrice);
        }
    
    }
       
    public void updateReservation() {
        try { 
            roomIdUpdate = reservationSessionBeanLocal.retrieveAvailableRoom(selectedReservation, outletIdUpdate, roomTypeIdUpdate);
            String roomNum = roomSessionBeanLocal.retrieveRoomById(roomIdUpdate).getRoomNum();
            
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
        
        dateUpdate = selectedReservation.getDate();
        durationUpdate = selectedReservation.getDuration();
        promotions = promotionSessionBeanLocal.retrievePromotionByDate(dateUpdate); 
        
        minTime = 12;
        maxTime = 23;
        
        Calendar cal = Calendar.getInstance();
        
        if (cal.get(Calendar.MINUTE) >= 1) {
            cal.set(Calendar.MINUTE, 0);
            
            cal.add(Calendar.HOUR, 1);
        }
        cal.set(Calendar.SECOND, 0);
        
        if (cal.get(Calendar.HOUR_OF_DAY) > maxTime) {
            cal.add(Calendar.DATE, 1);
            cal.set(Calendar.HOUR_OF_DAY, minTime);
        } else if (cal.get(Calendar.HOUR_OF_DAY) < minTime) {
            cal.set(Calendar.HOUR_OF_DAY, minTime);
        }
            
        minDate = cal.getTime();
        cal.add(Calendar.YEAR, 1);
        maxDate = cal.getTime();
    }
    
    public void onRowUnselect(SelectEvent event) {
        this.selectedReservation = null;
        this.roomTypeIdUpdate = null;
        this.outletIdUpdate = null;
        this.promotionIdUpdate = null;
        promotions = new ArrayList<>();
        dateUpdate = null;
        durationUpdate = 0;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
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
