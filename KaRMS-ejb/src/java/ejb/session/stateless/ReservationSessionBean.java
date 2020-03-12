/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.ReservationStatus;

/**
 *
 * @author zihua
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    //Create new reservation
    @Override
    public Long createNewReservation(Reservation newReservation) {
        em.persist(newReservation);
        em.flush();
        
        return newReservation.getReservationId();
    }

    //View all reservations
    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        
        return query.getResultList();
    }

    //View reservation details
    @Override
    public Reservation retrieveReservationById(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        return reservation;
    }

    //View reservation details
    @Override
    public List<Reservation> retrieveReservationByDate(Date date) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.date = :inDate");
        query.setParameter("inDate", date);
        
        return query.getResultList();
    }
    
    //View reservation details
    @Override
    public List<Reservation> retrieveReservationByStatus(ReservationStatus status) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.status = :inStatus");
        query.setParameter("inStatus", status);
        
        return query.getResultList();
    }
    
    // Update reservation and status
    @Override
    public void updateReservation(Reservation reservationToUpdate) {
        em.merge(reservationToUpdate);
        em.flush();
    }

    // Delete reservation
    @Override
    public void deleteReservation(Long reservationId) {
        Reservation reservationToDelete = retrieveReservationById(reservationId);
        
        em.remove(reservationToDelete);
    }
    
    
    
    
    
    
    
    
    
}
