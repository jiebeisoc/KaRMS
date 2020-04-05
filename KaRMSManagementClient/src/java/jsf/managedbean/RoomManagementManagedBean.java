/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.RoomSessionBeanLocal;
import entity.Room;
import java.io.Serializable;
import java.util.List;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author chai
 */
@Named(value = "roomManagementManagedBean")
@ViewScoped
public class RoomManagementManagedBean implements Serializable {

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    private List<Room> rooms;
    private Room newRoom;
    /**
     * Creates a new instance of RoomManagementManagedBean
     */
    public RoomManagementManagedBean() {
        newRoom = new Room();
    }
    
    @PostConstruct
    public void postConstuct() {
        roomSessionBeanLocal.retrieveAllRoom();
    }
    
    public void createNewRoom(ActionEvent event) {
        Long roomId = roomSessionBeanLocal.createNewRoom(newRoom);
        
        newRoom = new Room();
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New room created successfully", null));
    }
    
    public void deleteRoom(ActionEvent event) {
        Room roomToDelete = (Room)event.getComponent().getAttributes().get("roomToDelete");
        
        roomSessionBeanLocal.deleteRoom(roomToDelete.getRoomId());
        rooms.remove(roomToDelete);
    }
}
