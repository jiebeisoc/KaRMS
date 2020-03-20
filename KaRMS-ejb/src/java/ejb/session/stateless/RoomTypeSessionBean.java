/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteRoomTypeException;

/**
 *
 * @author zihua
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Long createNewRoomType(RoomType newRoomType) {
        em.persist(newRoomType);
        em.flush();
        
        return newRoomType.getRoomTypeId();
    }

    @Override
    public List<RoomType> retrieveAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        
        return query.getResultList();
    }

    @Override
    public RoomType retrieveRoomTypeById(Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        return roomType;
    }
    
    @Override
    public void updateRoomType(RoomType roomTypeToUpdate) {
        em.merge(roomTypeToUpdate);
        em.flush();
    }

    @Override
    public void deleteRoomType(Long roomTypeId) throws DeleteRoomTypeException {
        RoomType roomTypeToDelete = retrieveRoomTypeById(roomTypeId);
        
        if (roomTypeToDelete.getRooms().isEmpty()) {
            for (RoomRate rr: roomTypeToDelete.getRoomRates()) {
            rr.setRoomType(null);
            }

            em.remove(roomTypeToDelete);
        } else {
            throw new DeleteRoomTypeException();
        }
                   
    }
   
}
