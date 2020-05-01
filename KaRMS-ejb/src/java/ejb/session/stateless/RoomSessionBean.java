/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import entity.Reservation;
import entity.Room;
import entity.RoomType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewRoomException;
import util.exception.ExceedClosingHoursException;
import util.exception.NoAvailableRoomException;

/**
 *
 * @author chai
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanLocal {

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;
    

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewRoom(Room newRoom, Long roomTypeId, Long outletId) throws CreateNewRoomException {
        em.persist(newRoom);
        
        if (roomTypeId != null) {
            RoomType roomType = roomTypeSessionBeanLocal.retrieveRoomTypeById(roomTypeId);
            newRoom.setRoomType(roomType);
            roomType.getRooms().add(newRoom);
        } else {
            throw new CreateNewRoomException("Room Type is required");
        }
        
        if (outletId != null) {
            Outlet outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
            newRoom.setOutlet(outlet);
            outlet.getRooms().add(newRoom);
        } else {
            throw new CreateNewRoomException("Outlet is required");
        }
        
        em.flush();
        
        return newRoom.getRoomId();
    }
    
    @Override
    public List<Room> retrieveAllRoom(Long outletId) {
        if (outletId == null) {
            Query query = em.createQuery("SELECT r FROM Room r");
            
            return query.getResultList();
        } else {
            Query query = em.createQuery("Select r FROM Room r WHERE r.outlet.outletId = :inOutletId");
            query.setParameter("inOutletId", outletId);
            
            return query.getResultList();
        }
    }
    
    @Override
    public List<Room> retrieveRoomByOutletAndRoomType(Long outletId, Long roomTypeId) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.outlet.outletId = :inOutletId AND r.roomType.roomTypeId = :inRoomTypeId AND r.isDisabled = :inIsDisabled");
        query.setParameter("inOutletId", outletId);
        query.setParameter("inRoomTypeId", roomTypeId);
        query.setParameter("inIsDisabled", Boolean.FALSE);
        
        return query.getResultList();
    }
    
    @Override
    public Room retrieveRoomById(Long roomId) {
        Room room = em.find(Room.class, roomId);
        
        return room;
    }
    
    @Override
    public List<Room> retrieveRoomByRoomType(String roomType) {
        Query query = em.createQuery("SELECT r FROM Room r WHERE roomType = :inRoomType");
        query.setParameter("inRoomType", roomType);
        
        return query.getResultList();
    }

    @Override
    public boolean isRoomAvailable(Room room, Date startDateTime, Date endDateTime) {
        boolean isAvailable = true;
        List<Reservation> reservations = room.getReservations();
        
        for (Reservation r: reservations) {
            Date rStartDateTime = r.getDate();
            int rDuration = r.getDuration();
            Calendar cal = Calendar.getInstance();
            cal.setTime(rStartDateTime);
            cal.add(Calendar.HOUR, +rDuration);
            Date rEndDateTime = cal.getTime();

            //once overlapped, break
            if (startDateTime.before(rEndDateTime) && endDateTime.after(rStartDateTime)) {
                isAvailable = false;
                break;
            }
        }
        return isAvailable;
    }    
    
    //Retrieve available rooms for new reservation (using duration and date)    
    @Override
    public List<Room> retrieveAvailableRooms(Long time, int duration, Long outletId, Long roomTypeId) throws NoAvailableRoomException, ExceedClosingHoursException {

        Date startDateTime = new Date(time.longValue());
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateTime);
        
        if ((cal.get(Calendar.HOUR_OF_DAY) + duration - 1) > 23) {
            throw new ExceedClosingHoursException("Time exceeded closing hours!");
        }

        cal.add(Calendar.HOUR, +duration);
        Date endDateTime = cal.getTime();
        
        List<Room> rooms = retrieveRoomByOutletAndRoomType(outletId, roomTypeId);
        List<Room> availableRooms = new ArrayList<>();
        
        for (Room r: rooms) {
            Boolean isAvailable = isRoomAvailable(r, startDateTime, endDateTime);
            if (isAvailable == true) {
                availableRooms.add(r);
            }
        }
        
        if (availableRooms.isEmpty()) {
            throw new NoAvailableRoomException("No rooms are available at this timing!");
        }
        
        return availableRooms;
    }
    
    @Override
    public void updateRoom(Room roomToUpdate) {
        em.merge(roomToUpdate);
        em.flush();
    }
    
    @Override
    public void deleteRoom(Long roomId) {
        Room roomToDelete = retrieveRoomById(roomId);
        
        roomToDelete.setIsDisabled(Boolean.TRUE);
        
        roomToDelete.getRoomType().getRooms().remove(roomToDelete);

    }
     
}