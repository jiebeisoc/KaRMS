
package ejb.session.stateless;

import entity.Reservation;
import entity.Song;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.enumeration.ReservationStatus;
import util.exception.AddSongException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteReservationException;
import util.exception.NoAvailableRoomException;

public interface ReservationSessionBeanLocal {

    public Long retrieveAvailableRoom(Reservation reservation, Long outletId, Long roomTypeId) throws NoAvailableRoomException;
    
    public Long createNewReservation(Reservation newReservation, Long customerId, Long roomId, Long outletId, Long promotionId, String status) throws CustomerNotFoundException;
    
    public Long createNewReservation(Reservation newReservation, Long roomId, Long outletId, Long promotionId);

    public BigDecimal calculateTotalPrice(Date date, int duration, Long roomTypeId, Long promotionId);
    
    public BigDecimal calculateTotalPrice(Long time, int duration, Long roomTypeId, Long promotionId);
    
    public List<Reservation> retrieveAllReservations(Long outletId);
    
    public List<Reservation> retrieveReservationsToBeCompleted(Date currentDate);

    public Reservation retrieveReservationById(Long reservationId);
    
    public List<Reservation> retrieveUpcomingReservationByCustomer(Long customerId);
    
    public List<Reservation> retrievePastReservationByCustomer(Long customerId);

    public List<Reservation> retrieveReservationByStatus(ReservationStatus status);

    public List<Long> retrieveReservationByDate(Date dateFrom, Date dateTo);
    
    public List<Reservation> retrieveReservationByPhoneNo(String phoneNo);
    
    public List<Song> retrieveSongQueue(Long reservationId);
    
    public void addSongToQueue(Song songToAdd, Reservation reservation) throws AddSongException;
    
    public void deleteSongFromQueue(Song songToDelete, Reservation reservation);
    
    public void addQueueToFavouritePlaylist(Long customerId, Long reservationId);
    
    public void saveQueueAsFavouritePlaylist(Long customerId, Long reservationId);
    
    public void updateReservation(Reservation reservationToUpdate, Long roomIdUpdate, Long outletIdUpdate, Long promotionIdUpdate);

    public void deleteReservation(Long reservationId) throws DeleteReservationException;

    public List<Reservation> retrieveReservationByDateAndStatus(Date currentDate, Long outletId);

    public void payReservation(Long reservationId);

    public List<Reservation> retrieveReservationByDateOutletAndRoomType(Date dateFrom, Date dateTo, Long outletId, Long roomTypeId);

    public List<Reservation> retrieveReservationByRoomAndDate(Date dateFrom, Date dateTo, Long roomId);

}
