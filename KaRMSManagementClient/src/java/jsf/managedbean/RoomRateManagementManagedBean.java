/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.RoomRateSessionBeanLocal;
import entity.RoomRate;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author chai
 */
@Named(value = "roomRateManagementManagedBean")
@ViewScoped
public class RoomRateManagementManagedBean implements Serializable {

    @EJB(name = "RoomRateSessionBeanLocal")
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;    
    
    DateFormat timeFormat = new SimpleDateFormat("HH");
    
    private List<RoomRate> roomRates;
    
    private RoomRate newRoomRate;
    private RoomRate selectedRoomRateToView;
    private RoomRate selectedRoomRateToUpdate;
    private int start;
    private int end;

    /**
     * Creates a new instance of RoomRateManagementManagedBean
     */
    public RoomRateManagementManagedBean() {
        newRoomRate = new RoomRate();
    }
    
    @PostConstruct
    public void postConstruct() {
        roomRates = roomRateSessionBeanLocal.retrieveAllRoomRates();
    }
    
    public void createNewRoomRate(ActionEvent event) {
        
        Long roomId = roomRateSessionBeanLocal.createNewRoomRate(newRoomRate);
        roomRates.add(newRoomRate);
        
        newRoomRate = new RoomRate();
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New room rate created successfully", null));
    }
    
    public void viewRoomRate(ActionEvent event) {
        selectedRoomRateToView = (RoomRate)event.getComponent().getAttributes().get("roomRateToView");
    }
    
    public void doUpdateRoomRate(ActionEvent event) {
        selectedRoomRateToUpdate = (RoomRate)event.getComponent().getAttributes().get("roomRateToUpdate");
    }

    public void updateRoomRate() {
        roomRateSessionBeanLocal.updateRoomRate(selectedRoomRateToUpdate);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room Rate updated successfully", null));
    }
    
    public void deleteRoomRate(ActionEvent event) {
        RoomRate roomRateToDelete = (RoomRate)event.getComponent().getAttributes().get("roomRateToDelete");
        roomRateSessionBeanLocal.deleteRoomRate(roomRateToDelete.getRoomRateId());
        roomRates.remove(roomRateToDelete);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room Rate deleted successfully", null));
    }
    
    public RoomRate getNewRoomRate() {
        return newRoomRate;
    }

    public void setNewRoomRate(RoomRate newRoomRate) {
        this.newRoomRate = newRoomRate;
    }

    public RoomRate getSelectedRoomRateToView() {
        return selectedRoomRateToView;
    }

    public void setSelectedRoomRateToView(RoomRate selectedRoomRateToView) {
        this.selectedRoomRateToView = selectedRoomRateToView;
    }

    public RoomRate getSelectedRoomRateToUpdate() {
        return selectedRoomRateToUpdate;
    }

    public void setSelectedRoomRateToUpdate(RoomRate selectedRoomRateToUpdate) {
        this.selectedRoomRateToUpdate = selectedRoomRateToUpdate;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }
    
}
