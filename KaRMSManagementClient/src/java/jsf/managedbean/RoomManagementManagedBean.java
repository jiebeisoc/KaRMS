/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Outlet;
import entity.Room;
import entity.RoomType;
import java.io.Serializable;
import java.util.List;
import javax.faces.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author chai
 */
@Named(value = "roomManagementManagedBean")
@ViewScoped
public class RoomManagementManagedBean implements Serializable {

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    private List<Room> rooms;
    private List<RoomType> roomTypes;
    private List<Outlet> outlets;
    
    private Room newRoom;
    private Room selectedRoom;
    private Long roomTypeId;
    private Long outletId;
    /**
     * Creates a new instance of RoomManagementManagedBean
     */
    public RoomManagementManagedBean() {
        newRoom = new Room();
    }
    
    @PostConstruct
    public void postConstuct() {
        rooms = roomSessionBeanLocal.retrieveAllRoom();
        roomTypes = roomTypeSessionBeanLocal.retrieveAllRoomTypes();
        outlets = outletSessionBeanLocal.retrieveAllOutlets();
    }
    
    public void createNewRoom(ActionEvent event) {
        Long roomId = roomSessionBeanLocal.createNewRoom(newRoom, roomTypeId, null);
        rooms.add(newRoom);
        roomTypeId = null;
        
        newRoom = new Room();
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New room created successfully", null));
    }
    
    public void deleteRoom(ActionEvent event) {
        Room roomToDelete = (Room)event.getComponent().getAttributes().get("roomToDelete");
        
        roomSessionBeanLocal.deleteRoom(roomToDelete.getRoomId());
        rooms.remove(roomToDelete);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<Outlet> outlets) {
        this.outlets = outlets;
    }

    public Room getNewRoom() {
        return newRoom;
    }

    public void setNewRoom(Room newRoom) {
        this.newRoom = newRoom;
    }

    public Room getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(Room selectedRoom) {
        this.selectedRoom = selectedRoom;
    }
    
    public void onRowSelect(SelectEvent event) {
        selectedRoom = (Room)event.getObject();
    }
    
    public void onRowUnselect(SelectEvent event) {
        selectedRoom = null;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
    
}