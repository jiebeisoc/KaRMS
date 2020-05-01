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
import java.util.Arrays;
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
import org.primefaces.event.UnselectEvent;
import util.enumeration.ReservationStatus;
import util.exception.DeleteReservationException;
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
    private List<ReservationStatus> statusList;
        
    private Reservation newReservation;
    private String phoneNo;
    private BigDecimal totalPrice;
    private String roomNum;
    private Long roomTypeIdUpdate;
    private Long roomIdUpdate;
    private Long outletIdUpdate;
    private Long promotionIdUpdate;
    private Date dateUpdate;
    private int durationUpdate;
    private String roomNumUpdate;
    
    private Boolean isAvailable;
    private Boolean payNow;
    
    private Date minDate;
    private Date maxDate;
    private int minTime;
    private int maxTime;
    
    private Employee employee;

    public ReservationManagementManagedBean() {
        newReservation = new Reservation();
    }
    
    @PostConstruct
    public void postConstruct() {
        employee = (Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployee");
        Long outletId = null;
        // if not manager
        if (employee.getOutlet() != null) {
            outletId = employee.getOutlet().getOutletId();
        }
        
        reservations = reservationSessionBeanLocal.retrieveAllReservations(outletId);
        roomTypes = roomTypeSessionBeanLocal.retrieveAllRoomTypes();
        rooms = roomSessionBeanLocal.retrieveAllRoom(outletId);
        outlets = outletSessionBeanLocal.retrieveAllOutlets();
        promotions = new ArrayList<>();
        statusList = Arrays.asList(ReservationStatus.values());
        totalPrice = new BigDecimal("0.00"); 
        durationUpdate = 1;
        
        isAvailable = false;
        payNow = false;
    }
    
    public void onCreateNewReservation(ActionEvent event) {
        this.selectedReservation = null;
        this.roomTypeIdUpdate = null;
        this.outletIdUpdate = employee.getOutlet().getOutletId();
        this.promotionIdUpdate = null;
        promotions = new ArrayList<>();
        dateUpdate = null;
        durationUpdate = 1;
        newReservation = new Reservation();
        isAvailable = false;
        payNow = false;
        
        // Default minHour and maxHour based on 12pm-12am
        minTime = 12;
        maxTime = 23;
        
        Calendar cal = Calendar.getInstance();
        
        if (cal.get(Calendar.MINUTE) >= 1) {
            cal.set(Calendar.MINUTE, 0);
        }
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
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
        
    public void onChange(AjaxBehaviorEvent event) {
        isAvailable = false;
    }
    
    public void onDateChange(ValueChangeEvent event) {
        dateUpdate = (Date)event.getNewValue();
    }
    
    public void dateChange(SelectEvent event) {
        isAvailable = false;
    }

    public void onDurationChange(ValueChangeEvent event) {
        durationUpdate = (int)event.getNewValue();
    }
    
    public void checkAvailableRoom(ActionEvent event) {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateUpdate);
        // if exceed closing hour
        if ((cal.get(Calendar.HOUR_OF_DAY) + durationUpdate - 1) > maxTime) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Time exceeded closing hour", null));
        } else {
            try {
                roomIdUpdate = reservationSessionBeanLocal.retrieveAvailableRoom(newReservation, outletIdUpdate, roomTypeIdUpdate);
                isAvailable = true;

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room is available!", null));

            } catch (NoAvailableRoomException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        }
    }
        
    public void calculateTotalPrice() {
        totalPrice = reservationSessionBeanLocal.calculateTotalPrice(dateUpdate, durationUpdate, roomTypeIdUpdate, promotionIdUpdate);
        totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        
        if (newReservation != null) {
            newReservation.setTotalPrice(totalPrice);
        } 
        
        if (selectedReservation != null) {
            selectedReservation.setTotalPrice(totalPrice);
        }
    
    }
    
    public void onNext(ActionEvent event) {
        roomNum = roomSessionBeanLocal.retrieveRoomById(roomIdUpdate).getRoomNum();

        promotions = promotionSessionBeanLocal.retrievePromotionByDate(dateUpdate);
        calculateTotalPrice();
    }
            
    public void onPromotionChange(ValueChangeEvent event) {
        promotionIdUpdate = (Long)event.getNewValue();
    }
    
    public void createNewReservation(ActionEvent event) {
        newReservation.setDateReserved(new Date());
        
        if (payNow == true) {
            newReservation.setStatus(ReservationStatus.PAID);
        }

        Long reservationId = reservationSessionBeanLocal.createNewReservation(newReservation, roomIdUpdate, outletIdUpdate, promotionIdUpdate);
        reservations.add(newReservation);

        totalPrice = new BigDecimal("0.00");
        phoneNo = null;
        roomIdUpdate = null;
        roomTypeIdUpdate = null;
        outletIdUpdate = null;
        promotionIdUpdate = null;
        newReservation = new Reservation();
        isAvailable = false;
        payNow = false;

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New reservation is created successfully (Room Number: " + roomNum + ")", null));
    }
    
    public void onUpdate(AjaxBehaviorEvent event) {
        isAvailable = false;
        calculateTotalPrice();
    }
    
    public void onDateUpdate(SelectEvent event) {
        isAvailable = false;
        calculateTotalPrice();
        promotions = promotionSessionBeanLocal.retrievePromotionByDate(dateUpdate);  
    }
    
    public void checkUpdate() {
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateUpdate);
        // if exceed closing hour
        if ((cal.get(Calendar.HOUR_OF_DAY) + durationUpdate - 1) > maxTime) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Time exceeded closing hour", null));
        } else {
            selectedReservation.setDate(dateUpdate);
            selectedReservation.setDuration(durationUpdate);

            if (selectedReservation.getStatus() == ReservationStatus.NOTPAID) {
                try { 
                    roomIdUpdate = reservationSessionBeanLocal.retrieveAvailableRoom(selectedReservation, outletIdUpdate, roomTypeIdUpdate);
                    roomNumUpdate = roomSessionBeanLocal.retrieveRoomById(roomIdUpdate).getRoomNum();
                    isAvailable = true;
                    
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room is available!", null));

                } catch (NoAvailableRoomException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
                }
            } else if (selectedReservation.getStatus() == ReservationStatus.PAID) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reservation is paid, cannot be updated", null));
            } else if (selectedReservation.getStatus() == ReservationStatus.COMPLETED) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reservation is completed, cannot be updated", null));
            }
        }
        
    }
      
    public void updateReservation() {
        
        reservationSessionBeanLocal.updateReservation(selectedReservation, roomIdUpdate, outletIdUpdate, promotionIdUpdate);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reservation updated successfully (Room Number: " + roomNumUpdate + ")", null));
    }
    
    public void deleteReservation(ActionEvent event) {
        
        if (selectedReservation.getStatus() != ReservationStatus.COMPLETED) {
            try {
                Reservation reservationToDelete = (Reservation)event.getComponent().getAttributes().get("reservationToDelete");

                reservationSessionBeanLocal.deleteReservation(reservationToDelete.getReservationId());
                reservations.remove(reservationToDelete);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reservation is deleted successfully", null));

            } catch (DeleteReservationException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reservation is completed, cannot be deleted", null));
        }
    }

    public void onRowSelect(SelectEvent event) {
        this.selectedReservation = (Reservation)event.getObject();   
        this.roomTypeIdUpdate = selectedReservation.getRoom().getRoomType().getRoomTypeId();
        this.outletIdUpdate = selectedReservation.getOutlet().getOutletId();
        if (selectedReservation.getPromotion() != null) {
            this.promotionIdUpdate = selectedReservation.getPromotion().getPromotionId();
        } else {
            this.promotionIdUpdate = null;
        }
        
        dateUpdate = selectedReservation.getDate();
        durationUpdate = selectedReservation.getDuration();
        roomNumUpdate = selectedReservation.getRoom().getRoomNum();
        promotions = promotionSessionBeanLocal.retrievePromotionByDate(dateUpdate); 
        isAvailable = false;
        
        minTime = 12;
        maxTime = 23;
        
        Calendar cal = Calendar.getInstance();
        
        if (cal.get(Calendar.MINUTE) >= 1) {
            cal.set(Calendar.MINUTE, 0);
        }
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
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
    
    public void onRowUnselect(UnselectEvent event) {
        this.selectedReservation = null;
        this.roomTypeIdUpdate = null;
        this.outletIdUpdate = null;
        this.promotionIdUpdate = null;
        promotions = new ArrayList<>();
        dateUpdate = null;
        durationUpdate = 1;
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

    public List<ReservationStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<ReservationStatus> statusList) {
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

    public String getRoomNumUpdate() {
        return roomNumUpdate;
    }

    public void setRoomNumUpdate(String roomNumUpdate) {
        this.roomNumUpdate = roomNumUpdate;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Boolean getPayNow() {
        return payNow;
    }

    public void setPayNow(Boolean payNow) {
        this.payNow = payNow;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
