
package ejb.session.stateless;

import entity.Reservation;
import java.util.Date;
import java.util.List;
import util.enumeration.ReservationStatus;

public interface ReservationSessionBeanLocal {

    public Long createNewReservation(Reservation newReservation, Long customerId, Long roomId, Long outletId, Long promotionId);

    public List<Reservation> retrieveAllReservations();

    public Reservation retrieveReservationById(Long reservationId);

    public List<Reservation> retrieveReservationByStatus(ReservationStatus status);

    public List<Long> retrieveReservationByDate(Date dateFrom, Date dateTo);
    
    public void updateReservation(Reservation reservationToUpdate);

    public void deleteReservation(Long reservationId);

}
