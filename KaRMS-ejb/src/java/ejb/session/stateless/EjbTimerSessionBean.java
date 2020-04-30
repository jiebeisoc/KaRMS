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
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        
        cal.add(Calendar.HOUR_OF_DAY, -12);
        Date dateBefore = cal.getTime();
        
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveReservationsToBeCompleted(dateBefore, currentDate);
        
        for(Reservation reservation: reservations)
        {
            cal.setTime(reservation.getDate());
            int completedHour = cal.get(Calendar.HOUR_OF_DAY) + reservation.getDuration();
            if (completedHour == currentHour) {
                reservation.setStatus(ReservationStatus.COMPLETED);
                
                int pointsAwarded = reservation.getTotalPrice().intValue();
                Customer customer = reservation.getCustomer();
                customer.setPoints(customer.getPoints() + pointsAwarded);
                
                em.merge(customer);
                em.merge(reservation);
                em.flush();
            }
        }
    }
    
}
