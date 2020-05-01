/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.RoomRate;
import entity.RoomType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import util.enumeration.RoomRateType;
import util.exception.CreateNewRoomTypeException;
import util.exception.DeleteRoomTypeException;
import util.exception.UpdateRoomTypeException;

/**
 *
 * @author chai
 */
@Named(value = "roomTypeManagementManagedBean")
@ViewScoped
public class RoomTypeManagementManagedBean implements Serializable {

    @EJB(name = "RoomRateSessionBeanLocal")
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    private List<RoomRate> roomRates;
    private List<RoomType> roomTypes;
    
    private RoomType newRoomType;
    private List<Long> roomRateIdsNew;
    
    private RoomType selectedRoomType;
    private List<Long> roomRateIdsUpdate;
    
    private List<RoomRate> wkDayPeakRate;
    private List<RoomRate> wkDayNonPeakRate;
    private List<RoomRate> wkEndPeakRate;
    private List<RoomRate> wkEndNonPeakRate;
    
    private Long wkDayPeakIdNew;
    private Long wkDayNonPeakIdNew;
    private Long wkEndPeakIdNew;
    private Long wkEndNonPeakIdNew;
    
    private Long wkDayPeakId;
    private Long wkDayNonPeakId;
    private Long wkEndPeakId;
    private Long wkEndNonPeakId;
    
    /**
     * Creates a new instance of RoomTypeManagementManagedBean
     */
    public RoomTypeManagementManagedBean() {
        newRoomType = new RoomType();
    }
    
    @PostConstruct
    public void postConstruct() {
        roomRates = roomRateSessionBeanLocal.retrieveAllRoomRates();
        roomTypes = roomTypeSessionBeanLocal.retrieveAllRoomTypes();
        wkDayPeakRate = roomRateSessionBeanLocal.retrieveRoomRatesByType(RoomRateType.WKDAYPEAK);
        wkDayNonPeakRate = roomRateSessionBeanLocal.retrieveRoomRatesByType(RoomRateType.WKDAYNONPEAK);
        wkEndPeakRate = roomRateSessionBeanLocal.retrieveRoomRatesByType(RoomRateType.WKENDPEAK);
        wkEndNonPeakRate = roomRateSessionBeanLocal.retrieveRoomRatesByType(RoomRateType.WKENDNONPEAK);
        
    }
    
    public void createNewRoomType(ActionEvent event) {
        try {
            roomRateIdsNew = new ArrayList<>();
            roomRateIdsNew.add(wkDayPeakIdNew);
            roomRateIdsNew.add(wkDayNonPeakIdNew);
            roomRateIdsNew.add(wkEndPeakIdNew);
            roomRateIdsNew.add(wkEndNonPeakIdNew);
            
            Long roomTypeId = roomTypeSessionBeanLocal.createNewRoomType(newRoomType, roomRateIdsNew);
            roomTypes.add(newRoomType);

            newRoomType = new RoomType();
            roomRateIdsNew = new ArrayList<>();
            wkDayPeakIdNew = null;
            wkDayNonPeakIdNew = null;
            wkEndPeakIdNew = null;
            wkEndNonPeakIdNew = null;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New room type created successfully", null));
        } catch (CreateNewRoomTypeException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating new room type: " + ex.getMessage(), null));
        }
    }    
    
    public void updateRoomType() {
        try {
            roomRateIdsUpdate = new ArrayList<>();
            roomRateIdsUpdate.add(wkDayPeakId);
            roomRateIdsUpdate.add(wkDayNonPeakId);
            roomRateIdsUpdate.add(wkEndPeakId);
            roomRateIdsUpdate.add(wkEndNonPeakId);
            roomTypeSessionBeanLocal.updateRoomType(selectedRoomType, roomRateIdsUpdate);

            roomRateIdsUpdate = roomTypeSessionBeanLocal.retrieveRoomRateIds(selectedRoomType.getRoomTypeId());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room Type updated successfully", null));
        } catch (UpdateRoomTypeException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating " + selectedRoomType.getName() + ": " + ex.getMessage(),  null));
        }
    }
    
    public void deleteRoomType(ActionEvent event) {
        RoomType roomTypeToDelete = (RoomType)event.getComponent().getAttributes().get("roomTypeToDelete");
        
        try {
            roomTypeSessionBeanLocal.deleteRoomType(roomTypeToDelete.getRoomTypeId());
            roomTypes.remove(roomTypeToDelete);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room Type deleted successfully", null));
        } catch (DeleteRoomTypeException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting " + roomTypeToDelete.getName(), null));
        }
    }
    
    public void onRowSelect(SelectEvent event) {
        selectedRoomType = (RoomType)event.getObject();
        List<RoomRate> selectedRoomRate = selectedRoomType.getRoomRates();
        roomRateIdsUpdate = roomTypeSessionBeanLocal.retrieveRoomRateIds(selectedRoomType.getRoomTypeId());
        
        for (RoomRate rr: selectedRoomRate) {
            if (rr.getRoomRateType().equals("WKDAYPEAK")) {
                wkDayPeakId = rr.getRoomRateId();
            } else if (rr.getRoomRateType().equals("WKENDPEAK")) {
                wkEndPeakId = rr.getRoomRateId();
            } else if (rr.getRoomRateType().equals("WKDAYNONPEAK")) {
                wkDayNonPeakId = rr.getRoomRateId();
            }  else { // Weekend Non Peak
                wkEndNonPeakId = rr.getRoomRateId();
            }
        }
    }
    
    public void onRowUnselect(SelectEvent event) {
        selectedRoomType = null;
        roomRateIdsUpdate.clear();
    }
    
    public void initialiseState() {
        roomRateIdsUpdate.clear();
    }
    
    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }
    
    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public RoomType getNewRoomType() {
        return newRoomType;
    }

    public void setNewRoomType(RoomType newRoomType) {
        this.newRoomType = newRoomType;
    }

    public List<Long> getRoomRateIdsNew() {
        return roomRateIdsNew;
    }

    public void setRoomRateIdsNew(List<Long> roomRateIdsNew) {
        this.roomRateIdsNew = roomRateIdsNew;
    }

    public RoomType getSelectedRoomType() {
        return selectedRoomType;
    }

    public void setSelectedRoomType(RoomType selectedRoomType) {
        this.selectedRoomType = selectedRoomType;
    }

    public List<Long> getRoomRateIdsUpdate() {
        return roomRateIdsUpdate;
    }

    public void setRoomRateIdsUpdate(List<Long> roomRateIdsUpdate) {
        this.roomRateIdsUpdate = roomRateIdsUpdate;
    }

    public List<RoomRate> getWkDayPeakRate() {
        return wkDayPeakRate;
    }

    public void setWkDayPeakRate(List<RoomRate> wkDayPeakRate) {
        this.wkDayPeakRate = wkDayPeakRate;
    }

    public List<RoomRate> getWkDayNonPeakRate() {
        return wkDayNonPeakRate;
    }

    public void setWkDayNonPeakRate(List<RoomRate> wkDayNonPeakRate) {
        this.wkDayNonPeakRate = wkDayNonPeakRate;
    }

    public List<RoomRate> getWkEndPeakRate() {
        return wkEndPeakRate;
    }

    public void setWkEndPeakRate(List<RoomRate> wkEndPeakRate) {
        this.wkEndPeakRate = wkEndPeakRate;
    }

    public List<RoomRate> getWkEndNonPeakRate() {
        return wkEndNonPeakRate;
    }

    public void setWkEndNonPeakRate(List<RoomRate> wkEndNonPeakRate) {
        this.wkEndNonPeakRate = wkEndNonPeakRate;
    }

    public Long getWkDayPeakId() {
        return wkDayPeakId;
    }

    public void setWkDayPeakId(Long wkDayPeakId) {
        this.wkDayPeakId = wkDayPeakId;
    }

    public Long getWkDayNonPeakId() {
        return wkDayNonPeakId;
    }

    public void setWkDayNonPeakId(Long wkDayNonPeakId) {
        this.wkDayNonPeakId = wkDayNonPeakId;
    }

    public Long getWkEndPeakId() {
        return wkEndPeakId;
    }

    public void setWkEndPeakId(Long wkEndPeakId) {
        this.wkEndPeakId = wkEndPeakId;
    }

    public Long getWkEndNonPeakId() {
        return wkEndNonPeakId;
    }

    public void setWkEndNonPeakId(Long wkEndNonPeakId) {
        this.wkEndNonPeakId = wkEndNonPeakId;
    }

    public Long getWkDayPeakIdNew() {
        return wkDayPeakIdNew;
    }

    public void setWkDayPeakIdNew(Long wkDayPeakIdNew) {
        this.wkDayPeakIdNew = wkDayPeakIdNew;
    }

    public Long getWkDayNonPeakIdNew() {
        return wkDayNonPeakIdNew;
    }

    public void setWkDayNonPeakIdNew(Long wkDayNonPeakIdNew) {
        this.wkDayNonPeakIdNew = wkDayNonPeakIdNew;
    }

    public Long getWkEndPeakIdNew() {
        return wkEndPeakIdNew;
    }

    public void setWkEndPeakIdNew(Long wkEndPeakIdNew) {
        this.wkEndPeakIdNew = wkEndPeakIdNew;
    }

    public Long getWkEndNonPeakIdNew() {
        return wkEndNonPeakIdNew;
    }

    public void setWkEndNonPeakIdNew(Long wkEndNonPeakIdNew) {
        this.wkEndNonPeakIdNew = wkEndNonPeakIdNew;
    }

    
    
}