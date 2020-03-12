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

    @Override
    public Long createNewReservation(Reservation newReservation) {
        em.persist(newReservation);
        em.flush();
        
        return newReservation.getReservationId();
    }

    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        
        return query.getResultList();
    }

    @Override
    public Reservation retrieveReservationById(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        return reservation;
    }

    @Override
    public List<Reservation> retrieveReservationByDate(Date date) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.date = :inDate");
        query.setParameter("inDate", date);
        
        return query.getResultList();
    }
    
    @Override
    public List<Reservation> retrieveReservationByPaid(Boolean hasPaid) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.hasPaid = :inPaid");
        query.setParameter("inPaid", hasPaid);
        
        return query.getResultList();
    }
    
    @Override
    public void updateReservation(Reservation reservationToUpdate) {
        em.merge(reservationToUpdate);
        em.flush();
    }

    @Override
    public void deleteReservation(Long reservationId) {
        Reservation reservationToDelete = retrieveReservationById(reservationId);
        
        em.remove(reservationToDelete);
    }
    
    
    
    
    
    
    
    
    
}
