
package ejb.session.stateless;

import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.enumeration.ReservationStatus;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteReservationException;
import util.exception.NoAvailableRoomException;

public interface ReservationSessionBeanLocal {

    public Long retrieveAvailableRoom(Reservation reservation, Long outletId, Long roomTypeId) throws NoAvailableRoomException;

    public Long createNewReservation(Reservation newReservation, Long customerId, Long roomId, Long outletId, Long promotionId) throws CustomerNotFoundException;
    
    public Long createNewReservation(Reservation newReservation, Long roomId, Long outletId, Long promotionId);

    public BigDecimal calculateTotalPrice(Date date, int duration, Long roomTypeId, Long promotionId);
        
    public List<Reservation> retrieveAllReservations(Long outletId);

    public Reservation retrieveReservationById(Long reservationId);

    public List<Reservation> retrieveReservationByStatus(ReservationStatus status);

    public List<Long> retrieveReservationByDate(Date dateFrom, Date dateTo);
    
    public List<Reservation> retrieveReservationByPhoneNo(String phoneNo);
    
    public void updateReservation(Reservation reservationToUpdate, Long roomIdUpdate, Long outletIdUpdate, Long promotionIdUpdate);

    public void deleteReservation(Long reservationId) throws DeleteReservationException;

    public List<Reservation> retrieveReservationByDateAndStatus(Date currentDate, Long outletId);

    public void payReservation(Long reservationId);

    public List<Reservation> retrieveReservationObjByDate(Date dateFrom, Date dateTo, Long outletId);

}
