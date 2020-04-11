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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import util.exception.DeleteRoomTypeException;

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
    }
    
    public void createNewRoomType(ActionEvent event) {
        
        Long roomTypeId = roomTypeSessionBeanLocal.createNewRoomType(newRoomType, roomRateIdsNew);
        roomTypes.add(newRoomType);
        
        newRoomType = new RoomType();
        roomRateIdsNew = null;
        roomRates = roomRateSessionBeanLocal.retrieveAllRoomRates();
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New room type created successfully", null));
    }    
    
    public void updateRoomType() {
        roomTypeSessionBeanLocal.updateRoomType(selectedRoomType, roomRateIdsUpdate);

        roomRateIdsUpdate = selectedRoomType.getRoomRateIds();
        for (RoomRate rr: selectedRoomType.getRoomRates()) {
            roomRates.add(rr);
        }
        roomRateIdsUpdate.clear();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room Type updated successfully", null));
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
        roomRateIdsUpdate = selectedRoomType.getRoomRateIds();

        System.out.println("room rate id update size: " + roomRateIdsUpdate.size());
        System.out.println("Room rate size total: " + roomRates.size());
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
    
}