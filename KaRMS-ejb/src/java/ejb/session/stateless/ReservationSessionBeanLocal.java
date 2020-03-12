
package ejb.session.stateless;

import entity.Reservation;
import java.util.Date;
import java.util.List;
import util.enumeration.ReservationStatus;

public interface ReservationSessionBeanLocal {

    public Long createNewReservation(Reservation newReservation);

    public List<Reservation> retrieveAllReservations();

    public Reservation retrieveReservationById(Long reservationId);

    public List<Reservation> retrieveReservationByDate(Date date);

    public List<Reservation> retrieveReservationByStatus(ReservationStatus status);
    
    public void updateReservation(Reservation reservationToUpdate);

    public void deleteReservation(Long reservationId);

    


   
}
