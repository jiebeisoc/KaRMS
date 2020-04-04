/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteRoomRateException;

/**
 *
 * @author zihua
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Long createNewRoomRate(RoomRate newRoomRate) {
        em.persist(newRoomRate);
        em.flush();
        
        return newRoomRate.getRoomRateId();
    }

    @Override
    public List<RoomRate> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr");
        
        return query.getResultList();
    }
    
    @Override
    public List<RoomRate> retrieveAvailableRoomRates() {
        Query query = em.createQuery("SELECT rr FROM RoomRate rr WHERE rr.roomType IS NULL");
        
        return query.getResultList();
    }

    @Override
    public RoomRate retrieveRoomRateById(Long roomRateId) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        
        return roomRate;
    }

    @Override
    public void updateRoomRate(RoomRate roomRateToUpdate) {
        em.merge(roomRateToUpdate);
        em.flush();
    }

    @Override
    public void deleteRoomRate(Long roomRateId) throws DeleteRoomRateException {
        RoomRate roomRateToDelete = retrieveRoomRateById(roomRateId);
        
        if (roomRateToDelete.getRoomType() != null) {
            throw new DeleteRoomRateException();
        }
        
        em.remove(roomRateToDelete);
    }
 
}