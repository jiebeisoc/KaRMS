/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSession.completeReservationTimer(): Timeout at " + timeStamp);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
        System.out.println("date: " + cal.get(Calendar.DATE));
        Date date = cal.getTime();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveUpcomingReservations(date);
        
        for(Reservation reservation: reservations)
        {
            cal.setTime(reservation.getDate());
            int completedHour = cal.get(Calendar.HOUR_OF_DAY) + reservation.getDuration();
            if (completedHour == hour) {
                reservation.setStatus(ReservationStatus.COMPLETED);
                
                em.merge(reservation);
                em.flush();
            }
        }
    }
    
}
