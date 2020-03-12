
package ejb.session.stateless;

import entity.Reservation;
import java.util.Date;
import java.util.List;

public interface ReservationSessionBeanLocal {

    public Long createNewReservation(Reservation newReservation);

    public List<Reservation> retrieveAllReservations();

    public Reservation retrieveReservationById(Long reservationId);

    public List<Reservation> retrieveReservationByDate(Date date);

    public List<Reservation> retrieveReservationByPaid(Boolean hasPaid);
    
    public void updateReservation(Reservation reservationToUpdate);

    public void deleteReservation(Long reservationId);


   
}
