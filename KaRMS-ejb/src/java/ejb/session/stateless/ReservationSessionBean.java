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
import entity.RoomRate;
import entity.RoomType;
import entity.Song;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.ReservationStatus;
import util.exception.AddSongException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteReservationException;
import util.exception.NoAvailableRoomException;

/**
 *
 * @author zihua
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanLocal {

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

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

    //Retrieve an available room for new reservation    
    @Override
    public Long retrieveAvailableRoom(Reservation reservation, Long outletId, Long roomTypeId) throws NoAvailableRoomException {
        Date startDateTime = reservation.getDate();
        int duration = reservation.getDuration();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateTime);
        cal.add(Calendar.HOUR, +duration);
        Date endDateTime = cal.getTime();
        
        Long roomId = null;
        Boolean isAvailable = false;
        
        List<Room> availableRooms = roomSessionBeanLocal.retrieveRoomByOutletAndRoomType(outletId, roomTypeId);
        
        for (Room r: availableRooms) {
            isAvailable = roomSessionBeanLocal.isRoomAvailable(r, startDateTime, endDateTime);
            if (isAvailable == true) {
                roomId = r.getRoomId();
                break;
            }
        }
        
        if (isAvailable == false) {
            throw new NoAvailableRoomException("No room is available!");
        }
        
        return roomId;
    }
            
    //Create new reservation for a member
    @Override
    public Long createNewReservation(Reservation newReservation, Long customerId, Long roomId, Long outletId, Long promotionId, String status) throws CustomerNotFoundException {
        
        newReservation.setDateReserved(new Date());

        if (status.equalsIgnoreCase("PAID")) {
            newReservation.setStatus(ReservationStatus.PAID);
        } else if (status.equalsIgnoreCase("NOTPAID")) {
            newReservation.setStatus(ReservationStatus.NOTPAID);
        } else {
            newReservation.setStatus(ReservationStatus.COMPLETED);
        }
        
        if (customerId != null) {
            Customer customer = customerSessionBeanLocal.retrieveCustomerById(customerId);
            customer.setPoints(customer.getPoints() - newReservation.getPointsRedeemed());
            
            customer.getReservations().add(newReservation);
            newReservation.setCustomer(customer);
        }
        
        if (roomId != null) {
            Room room = roomSessionBeanLocal.retrieveRoomById(roomId);
            room.getReservations().add(newReservation);
            newReservation.setRoom(room);
        }
        
        if (outletId != null) {
            Outlet outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
            outlet.getReservations().add(newReservation);
            newReservation.setOutlet(outlet);
        }
        
        if (promotionId != null || promotionId == 0) {
            Promotion promotion = promotionSessionBeanLocal.retrievePromotionById(promotionId);
            newReservation.setPromotion(promotion);
        }
        
        em.persist(newReservation);
        em.flush();
        
        return newReservation.getReservationId();
    }
    
    //Create new reservation for walk-in and call-in customers
    @Override
    public Long createNewReservation(Reservation newReservation, Long roomId, Long outletId, Long promotionId) {
        
        System.out.println("Date: " + newReservation.getDate());
        System.out.println("Duration: " + newReservation.getDuration());
        System.out.println("Number of People: " + newReservation.getNumOfPeople());
        System.out.println("Total Price: " + newReservation.getTotalPrice());
        System.out.println("Status: " + newReservation.getStatus());
        System.out.println("Date Reserved: " + newReservation.getDateReserved());
        System.out.println("Phone No: " + newReservation.getWalkInPhoneNo());
        System.out.println("PromoId: " + promotionId);
        
        newReservation.setCustomer(null);
        
        if (roomId != null) {
            Room room = roomSessionBeanLocal.retrieveRoomById(roomId);
            room.getReservations().add(newReservation);
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
        
        em.persist(newReservation);
        em.flush();
        
        return newReservation.getReservationId();
    }
    
    //Calculate total price for a reservation
    @Override
    public BigDecimal calculateTotalPrice(Date date, int duration, Long roomTypeId, Long promotionId) {
        
        BigDecimal totalPrice = new BigDecimal("0.00");
        
        if (date != null && duration != 0 && roomTypeId != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            String roomRateType;

            if (dayOfWeek == 1 || dayOfWeek == 7) {
                roomRateType = "WKEND";
            } else {
                roomRateType = "WKDAY";
            }

            //non-peak is before 6pm, peak is 6pm onwards
            if (hourOfDay < 18) {
                roomRateType += "NONPEAK";
            } else {
                roomRateType += "PEAK";
            }

            BigDecimal roomRate = BigDecimal.ZERO;
            List<RoomRate> roomRates = roomTypeSessionBeanLocal.retrieveRoomTypeById(roomTypeId).getRoomRates();
            for (RoomRate rr: roomRates) {
                String type = rr.getRoomRateType();
                if (roomRateType.equals(type)) {
                    roomRate = rr.getRate();
                    break;
                }
            }
            
            if (promotionId == null || promotionId == 0) {
                totalPrice = roomRate.multiply(new BigDecimal(duration));
            } else {
                double promotionDiscount = promotionSessionBeanLocal.retrievePromotionById(promotionId).getDiscountRate();
                totalPrice = roomRate.multiply(new BigDecimal(duration)).multiply(new BigDecimal(1 - promotionDiscount));
                System.out.println("totalPrice: " + totalPrice);
            }
        }
        
        return totalPrice;
    }
    
    //Calculate total price for an online reservation
    @Override
    public BigDecimal calculateTotalPrice(Long time, int duration, Long roomTypeId, Long promotionId) {
        
        Date date = new Date(time.longValue()); 
        BigDecimal totalPrice = new BigDecimal("0.00");
        
        if (date != null && duration != 0 && roomTypeId != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
            String roomRateType;
            //System.out.println("dayOfWeek: " + dayOfWeek);
            //System.out.println("hourOfDay: " + hourOfDay);

            if (dayOfWeek == 1 || dayOfWeek == 7) {
                roomRateType = "WKEND";
            } else {
                roomRateType = "WKDAY";
            }

            //non-peak is before 6pm, peak is 6pm onwards
            if (hourOfDay < 18) {
                roomRateType += "NONPEAK";
            } else {
                roomRateType += "PEAK";
            }

            BigDecimal roomRate = BigDecimal.ZERO;
            List<RoomRate> roomRates = roomTypeSessionBeanLocal.retrieveRoomTypeById(roomTypeId).getRoomRates();
            for (RoomRate rr: roomRates) {
                String type = rr.getRoomRateType();
                if (roomRateType.equals(type)) {
                    roomRate = rr.getRate();
                    break;
                }
            }

            if (promotionId == null || promotionId == 0) {
                totalPrice = roomRate.multiply(new BigDecimal(duration));
            } else {
                double promotionDiscount = promotionSessionBeanLocal.retrievePromotionById(promotionId).getDiscountRate();
                totalPrice = roomRate.multiply(new BigDecimal(duration)).multiply(new BigDecimal(1 - promotionDiscount));
            }
        }
        
        return totalPrice;
    }

    //View all reservations
    @Override
    public List<Reservation> retrieveAllReservations(Long outletId) {
        if (outletId == null) {
            Query query = em.createQuery("SELECT r FROM Reservation r");
        
            return query.getResultList();
        } else {
            Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.outlet.outletId = :inOutletId");
            query.setParameter("inOutletId", outletId);
            
            return query.getResultList();
        }
    }
    
    @Override
    public List<Reservation> retrieveReservationsToBeCompleted(Date dateBefore, Date currentDate) {
        
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.date BETWEEN :inDateFrom AND :inDateTo");
        query.setParameter("inDateFrom", dateBefore);
        query.setParameter("inDateTo", currentDate);
        
        return query.getResultList();
    }

    //View reservation details
    @Override
    public Reservation retrieveReservationById(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        return reservation;
    }
    
    //Retrieve a member's upcoming reservations
    @Override
    public List<Reservation> retrieveUpcomingReservationByCustomer(Long customerId) {
        Date currentDate = new Date();
        
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE (r.customer.customerId = :inCustomerId) AND (r.date >= :inDate)");
        query.setParameter("inCustomerId", customerId);
        query.setParameter("inDate", currentDate);
        
        return query.getResultList();
    }
    
    //Retrieve a member's past reservations
    @Override
    public List<Reservation> retrievePastReservationByCustomer(Long customerId) {
        Date currentDate = new Date();
        
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE (r.customer.customerId = :inCustomerId) AND (r.date < :inDate)");
        query.setParameter("inCustomerId", customerId);
        query.setParameter("inDate", currentDate);
        
        return query.getResultList();
    }
 
    //Retrieve reservations by status
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
    
    @Override
    public List<Reservation> retrieveReservationByDateOutletAndRoomType(Date dateFrom, Date dateTo, Long outletId, Long roomTypeId) {
        Outlet outlet = outletSessionBeanLocal.retrieveOutletById(outletId);
        RoomType roomType = roomTypeSessionBeanLocal.retrieveRoomTypeById(roomTypeId);
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.date BETWEEN :inDateFrom AND :inDateTo AND r.outlet = :inOutlet AND r.room.roomType = :inRoomType");
        query.setParameter("inDateFrom", dateFrom);
        query.setParameter("inDateTo", dateTo);
        query.setParameter("inOutlet", outlet);
        query.setParameter("inRoomType", roomType);
        
        return query.getResultList();
    }
    
    @Override
    public List<Reservation> retrieveReservationByRoomAndDate(Date dateFrom, Date dateTo, Long roomId) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.date BETWEEN :inDateFrom AND :inDateTo AND r.room.roomId = :inRoomId ORDER BY r.date");
        query.setParameter("inDateFrom", dateFrom);
        query.setParameter("inDateTo", dateTo);
        query.setParameter("inRoomId", roomId);
        
        return query.getResultList();
    }
    
    //Retrieve reservation for settling payment by cash
    @Override
    public List<Reservation> retrieveReservationByDateAndStatus(Date currentDate, Long outletId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, 1);
        Date endDate = cal.getTime();
        if (outletId == null) {
            Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.date BETWEEN :inDateFrom AND :inDateTo AND r.status = :inStatus");
            query.setParameter("inDateFrom", currentDate);
            query.setParameter("inDateTo", endDate);
            query.setParameter("inStatus", ReservationStatus.NOTPAID);

            return query.getResultList();
        }  else {
            Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.date BETWEEN :inDateFrom AND :inDateTo AND r.status = :inStatus AND r.outlet.outletId = :inOutletId");
            query.setParameter("inDateFrom", currentDate);
            query.setParameter("inDateTo", endDate);            
            query.setParameter("inStatus", ReservationStatus.NOTPAID);
            query.setParameter("inOutletId", outletId);

            return query.getResultList();
        }
    }
    
    //Retrieve reservation by walk-in customer's phone no
    @Override
    public List<Reservation> retrieveReservationByPhoneNo(String phoneNo) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.walkInPhoneNo = :inWalkInPhoneNo");
        query.setParameter("inWalkInPhoneNo", phoneNo);
        
        return query.getResultList();
    }
    
    @Override
    public List<Song> retrieveSongQueue(Long reservationId) {
        Reservation reservation = retrieveReservationById(reservationId);
        
        return reservation.getSongQueue();
    }
    
    @Override
    public void addSongToQueue(Song songToAdd, Reservation reservation) throws AddSongException {

        List<Song> songQueue = reservation.getSongQueue();
        songQueue.add(songToAdd);
        
        if (songQueue.contains(songToAdd)) {
            
            throw new AddSongException("Song is already in the song queue!");
                    
        } else {
            songQueue.add(songToAdd);    
        }
        
        reservation.setSongQueue(songQueue);
        em.merge(reservation);
        em.flush();
        
    }
    
    @Override
    public void deleteSongFromQueue(Song songToDelete, Reservation reservation) {
        
        List<Song> songQueue = reservation.getSongQueue();
        
        songQueue.remove(songToDelete);
        reservation.setSongQueue(songQueue);
        em.merge(reservation);
        em.flush();        
    }
    
    @Override
    public void addQueueToFavouritePlaylist(Long customerId, Long reservationId) {
        
        Reservation reservation = retrieveReservationById(reservationId);
        Customer customer = em.find(Customer.class, customerId);
        
        List<Song> songQueue = reservation.getSongQueue();
        List<Song> favouritePlaylist = customer.getFavouritePlaylist();
        
        for (Song song: songQueue) {
            if (!favouritePlaylist.contains(song)) {
                favouritePlaylist.add(song);
            }
        }
        
        customer.setFavouritePlaylist(favouritePlaylist);
        em.merge(customer);
        em.flush();
    }
    
    @Override
    public void saveQueueAsFavouritePlaylist(Long customerId, Long reservationId) {
        
        Reservation reservation = retrieveReservationById(reservationId);
        Customer customer = em.find(Customer.class, customerId);
        
        List<Song> songQueue = reservation.getSongQueue();
        List<Song> favouritePlaylist = customer.getFavouritePlaylist();
        
        customer.setFavouritePlaylist(songQueue);
        em.merge(customer);
        em.flush();
    }
    
    //Settle reservation payment
    @Override
    public void payReservation(Long reservationId) {
        Reservation reservation = retrieveReservationById(reservationId);
        
        reservation.setStatus(ReservationStatus.PAID);
    }
    
    // Update reservation and status
    @Override
    public void updateReservation(Reservation reservationToUpdate, Long roomIdUpdate, Long outletIdUpdate, Long promotionIdUpdate) {
        
        System.out.println("Date: " + reservationToUpdate.getDate());
        System.out.println("Duration: " + reservationToUpdate.getDuration());
        System.out.println("Number of People: " + reservationToUpdate.getNumOfPeople());
        System.out.println("Note: " + reservationToUpdate.getNote());
        System.out.println("Points: " + reservationToUpdate.getPointsRedeemed());
        System.out.println("Total Price: " + reservationToUpdate.getTotalPrice());
        System.out.println("Status: " + reservationToUpdate.getStatus());
        System.out.println("Date Reserved: " + reservationToUpdate.getDateReserved());
        System.out.println("Phone No: " + reservationToUpdate.getWalkInPhoneNo());
        System.out.println("RoomId: " + roomIdUpdate);
        System.out.println("OutletId: " + outletIdUpdate);
        System.out.println("PromoId: " + promotionIdUpdate); 
        
        if (roomIdUpdate != null && (!reservationToUpdate.getRoom().getRoomId().equals(roomIdUpdate))) {
            Room roomToUpdate = roomSessionBeanLocal.retrieveRoomById(roomIdUpdate);
            reservationToUpdate.setRoom(roomToUpdate);
        }
        
        if (outletIdUpdate != null) {
            Outlet outletToUpdate = outletSessionBeanLocal.retrieveOutletById(outletIdUpdate);
            reservationToUpdate.setOutlet(outletToUpdate);
        }
        
        if (promotionIdUpdate != null || promotionIdUpdate != 0) {
            System.out.println("promoId: " + promotionIdUpdate);
            Promotion promotion = promotionSessionBeanLocal.retrievePromotionById(promotionIdUpdate);
            System.out.println("rId: " + reservationToUpdate);
            reservationToUpdate.setPromotion(promotion);
        }
        
        em.merge(reservationToUpdate);
        em.flush();
    }

    // Delete reservation
    @Override
    public void deleteReservation(Long reservationId) throws DeleteReservationException {
        Reservation reservationToDelete = retrieveReservationById(reservationId);
        Date currentDate = new Date();
        Date reservationDate = reservationToDelete.getDate();
        
        if (currentDate.before(reservationDate)) {
        
            Customer customer = reservationToDelete.getCustomer();
            reservationToDelete.setCustomer(null);
            customer.getReservations().remove(reservationToDelete);
            
            Room room = reservationToDelete.getRoom();
            room.getReservations().remove(reservationToDelete);

            Outlet outlet = reservationToDelete.getOutlet();
            outlet.getReservations().remove(reservationToDelete);
            
            if (reservationToDelete.getReview() != null) {
                reservationToDelete.setReview(null);
            } 
            
            if (reservationToDelete.getPromotion() != null) {
                reservationToDelete.setPromotion(null);
            }
            
            if (!reservationToDelete.getSongQueue().isEmpty()) {
                reservationToDelete.getSongQueue().clear();
            }

            em.remove(reservationToDelete);
        } else {
            throw new DeleteReservationException("Reservation cannot be deleted as it has started!");
        }
    }
    
}
