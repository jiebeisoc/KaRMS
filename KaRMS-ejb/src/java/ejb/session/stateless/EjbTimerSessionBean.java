/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.ReservationStatus;

/**
 *
 * @author zihua
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal {

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    
    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    @Schedule(hour = "*", info = "completeReservationTimer")
    public void completeReservationTimer()
    {
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        System.out.println("********** EjbTimerSession.completeReservationTimer(): Timeout at " + timeStamp);
        
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveReservationsToBeCompleted(currentDate);
        
        for(Reservation reservation: reservations)
        {
            cal.setTime(reservation.getDate());
            cal.add(Calendar.HOUR_OF_DAY, reservation.getDuration());
            Date completionDate = cal.getTime();
            
            if (completionDate.before(currentDate) || completionDate.equals(currentDate)) {
                reservation.setStatus(ReservationStatus.COMPLETED);
                
                if (reservation.getCustomer() != null) {
                    Customer customer = reservation.getCustomer();
                    int pointsAwarded = reservation.getTotalPrice().intValue();
                    customer.setPoints(customer.getPoints() + pointsAwarded); 
                    em.merge(customer);
                }
                
                em.merge(reservation);
                em.flush();
            }
        }
    }
    
}
