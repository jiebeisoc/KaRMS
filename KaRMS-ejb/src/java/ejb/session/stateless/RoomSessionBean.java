/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author chai
 */
@Stateless
public class RoomSessionBean implements RoomSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewRoom(Room newRoom) {
        em.persist(newRoom);
        em.flush();
        
        return newRoom.getRoomId();
    }
    
    @Override
    public List<Room> retrieveAllRoom() {
        Query query = em.createQuery("SELECT r FROM Room r");
        
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
    public void updateRoom(Room roomToUpdate) {
        em.merge(roomToUpdate);
        em.flush();
    }
    
    @Override
    public void deleteRoom(Long roomId) {
        Room roomToDelete = retrieveRoomById(roomId);
        
        roomToDelete.setIsDisabled(Boolean.TRUE);
        roomToDelete.setOutlet(null);
        roomToDelete.setRoomType(null);
        
        em.remove(roomToDelete);
    }
     
}
