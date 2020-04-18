/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import entity.RoomType;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DeleteRoomTypeException;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author zihua
 */
@Stateless
public class RoomTypeSessionBean implements RoomTypeSessionBeanLocal {

    @EJB(name = "RoomRateSessionBeanLocal")
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Long createNewRoomType(RoomType newRoomType, List<Long> roomRateIds) {
        em.persist(newRoomType);
        
        if (roomRateIds != null && (!roomRateIds.isEmpty())) {
            for (Long id : roomRateIds) {
                RoomRate roomRate = roomRateSessionBeanLocal.retrieveRoomRateById(id);
                newRoomType.getRoomRates().add(roomRate);
                roomRate.setRoomType(newRoomType);
            }
        }
        em.flush();
        
        return newRoomType.getRoomTypeId();
    }

    @Override
    public List<RoomType> retrieveAllRoomTypes() {
        Query query = em.createQuery("SELECT rt FROM RoomType rt");
        
        return query.getResultList();
    }
    
    @Override
    public List<Long> retrieveRoomRateIds(Long roomTypeId) {
        RoomType roomType = retrieveRoomTypeById(roomTypeId);
        
        Query query = em.createQuery("SELECT rr.roomRateId FROM RoomRate rr WHERE rr.roomType.roomTypeId =:inRoomType");
        query.setParameter("inRoomType", roomType.getRoomTypeId());
        
        return query.getResultList();
    }

    @Override
    public RoomType retrieveRoomTypeById(Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        return roomType;
    }
    
    @Override
    public RoomType retrieveRoomTypeByName(String name) throws RoomTypeNotFoundException {
        Query query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.name = :inName");
        query.setParameter("inName", name);
        try {
            return (RoomType)query.getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new RoomTypeNotFoundException();
        }
    }
    
    @Override
    public void updateRoomType(RoomType roomTypeToUpdate, List<Long> roomRateIds) {
        em.merge(roomTypeToUpdate);
        
        roomTypeToUpdate.getRoomRates().clear();
        
        for (Long id : roomRateIds) {
            RoomRate roomRate = roomRateSessionBeanLocal.retrieveRoomRateById(id);
            roomTypeToUpdate.getRoomRates().add(roomRate);
        }
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
