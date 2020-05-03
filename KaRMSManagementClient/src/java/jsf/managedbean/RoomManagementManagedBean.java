/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Employee;
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
import util.exception.CreateNewRoomException;

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
    private Long outletId;
    private Room selectedRoom;
    private Long roomTypeId;
    /**
     * Creates a new instance of RoomManagementManagedBean
     */
    public RoomManagementManagedBean() {
        newRoom = new Room();
    }
    
    @PostConstruct
    public void postConstuct() {
        Employee employee = (Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployee");
        Long outletId = null;
        if (employee.getOutlet() != null) {
            outletId = employee.getOutlet().getOutletId();
        }
        rooms = roomSessionBeanLocal.retrieveAllRoom(outletId);
        roomTypes = roomTypeSessionBeanLocal.retrieveAllRoomTypes();
        outlets = outletSessionBeanLocal.retrieveAllOutlets();
    }
    
    public void createNewRoom(ActionEvent event) {
        try {
            Long roomId = roomSessionBeanLocal.createNewRoom(newRoom, roomTypeId, outletId);
            rooms.add(newRoom);
            roomTypeId = null;
            outletId = null;
            newRoom = new Room();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New room created successfully", null));
        } catch (CreateNewRoomException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating new room: " + ex.getMessage(), null));
        }
    }
    
    public void updateRoom() {
        roomSessionBeanLocal.updateRoom(selectedRoom);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room updated successfully", null));
    }
    
    public void deleteRoom(ActionEvent event) {
        Room roomToDelete = (Room)event.getComponent().getAttributes().get("roomToDelete");
        
        roomSessionBeanLocal.deleteRoom(roomToDelete.getRoomId());
        
        for (Room r: rooms) {
            if (r.getRoomId() == roomToDelete.getRoomId()) {
                r.setIsDisabled(Boolean.TRUE);
            }
        }
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Room " + roomToDelete.getRoomNum() + " is disabled", null));
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

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }
    
}