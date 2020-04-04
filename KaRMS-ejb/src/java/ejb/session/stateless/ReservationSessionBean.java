/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Outlet;
import entity.Promotion;
import entity.Reservation;
import entity.Room;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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

    @EJB(name = "PromotionSessionBeanLocal")
    private PromotionSessionBeanLocal promotionSessionBeanLocal;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    //Create new reservation
    @Override
    public Long createNewReservation(Reservation newReservation, Long customerId, Long roomId, Long outletId, Long promotionId) {
        
        em.persist(newReservation);
        
        if (customerId != null) {
            Customer customer = customerSessionBeanLocal.retrieveCustomerById(customerId);
            customer.getReservations().add(newReservation);
            newReservation.setCustomer(customer);
        }
        
        if (roomId != null) {
            Room room = roomSessionBeanLocal.retrieveRoomById(roomId);
            newReservation.setRoom(room);
        }
        
        if (outletId != null) {
            Outlet outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
            outlet.getReservations().add(newReservation);
            newReservation.setOutlet(outlet);
        }
        
        if (promotionId != null) {
            Promotion promotion = promotionSessionBeanLocal.retrievePromotionById(promotionId);
            newReservation.setPromotion(promotion);
        }
        
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
    public List<Reservation> retrieveReservationByStatus(ReservationStatus status) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.status = :inStatus");
        query.setParameter("inStatus", status);
        
        return query.getResultList();
    }
    
    //Filter reservation by date range
    @Override
    public List<Long> retrieveReservationByDate(Date dateFrom, Date dateTo) {
        Query query = em.createQuery("SELECT r.reservationId FROM Reservation r WHERE r.date BETWEEN :inDateFrom AND :inDateTo");
        query.setParameter("inDateFrom", dateFrom);
        query.setParameter("inDateTo", dateTo);
        
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
