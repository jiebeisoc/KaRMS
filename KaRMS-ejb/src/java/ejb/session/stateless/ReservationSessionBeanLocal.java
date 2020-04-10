
package ejb.session.stateless;

import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.enumeration.ReservationStatus;
import util.exception.CustomerNotFoundException;
import util.exception.NoAvailableRoomException;

public interface ReservationSessionBeanLocal {

    public Long retrieveAvailableRoom(Reservation reservation, Long outletId, Long roomTypeId) throws NoAvailableRoomException;

    public Long createNewReservation(Reservation newReservation, Long customerId, Long roomId, Long outletId, Long promotionId) throws CustomerNotFoundException;

    public BigDecimal calculateTotalPrice(Date date, int duration, Long roomTypeId);
        
    public List<Reservation> retrieveAllReservations();

    public Reservation retrieveReservationById(Long reservationId);

    public List<Reservation> retrieveReservationByStatus(ReservationStatus status);

    public List<Long> retrieveReservationByDate(Date dateFrom, Date dateTo);
    
    public void updateReservation(Reservation reservationToUpdate, Long roomIdUpdate, Long outletIdUpdate, Long promotionIdUpdate);

    public void deleteReservation(Long reservationId);

}
