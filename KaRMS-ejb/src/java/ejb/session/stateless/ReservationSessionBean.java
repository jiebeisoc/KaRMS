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
import util.exception.CustomerNotFoundException;
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
        
        Query query = em.createQuery("SELECT r FROM Room r WHERE r.outlet.outletId = :inOutletId AND r.roomType.roomTypeId = :inRoomTypeId");
        List<Room> availableRooms = query.getResultList();
        
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
    
    //Create new reservation
    @Override
    public Long createNewReservation(Reservation newReservation, Long memberNum, Long roomId, Long outletId, Long promotionId) throws CustomerNotFoundException {
        
        em.persist(newReservation);
        
        if (memberNum != null) {
            Long customerId = customerSessionBeanLocal.retrieveCustomerByMemberNum(memberNum).getCustomerId();
            Customer customer = customerSessionBeanLocal.retrieveCustomerById(customerId);
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
        
        if (promotionId != null) {
            Promotion promotion = promotionSessionBeanLocal.retrievePromotionById(promotionId);
            newReservation.setPromotion(promotion);
        }
        
        em.flush();
        
        return newReservation.getReservationId();
    }
    
    @Override
    public BigDecimal calculateTotalPrice(Date date, int duration, Long roomTypeId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        String roomRateType;
        
        if (dayOfWeek < 6) {
            roomRateType = "WKDAY";
        } else {
            roomRateType = "WKEND";
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
        
        BigDecimal totalPrice = roomRate.multiply(new BigDecimal(duration));
        return totalPrice;
    }

    //View all reservations
    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");
        
        return query.getResultList();
    }

    //View reservation details
    @Override
    public Reservation retrieveReservationById(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        return reservation;
    }
    
    //View reservation details
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
    
    // Update reservation and status
    @Override
    public void updateReservation(Reservation reservationToUpdate, Long roomIdUpdate, Long outletIdUpdate, Long promotionIdUpdate) {
        em.merge(reservationToUpdate);
        
        if (roomIdUpdate != null && (!reservationToUpdate.getRoom().getRoomId().equals(roomIdUpdate))) {
            Room roomToUpdate = roomSessionBeanLocal.retrieveRoomById(roomIdUpdate);
            
            List<Reservation> reservations = roomToUpdate.getReservations();
            for (Reservation r: reservations) {
                if (r.getReservationId() == reservationToUpdate.getReservationId()) {
                    roomToUpdate.getReservations().remove(r);
                    roomToUpdate.getReservations().add(reservationToUpdate);
                    break;
                }
            }
            
            reservationToUpdate.setRoom(roomToUpdate);
        }
        
        if (outletIdUpdate != null) {
            Outlet outletToUpdate = outletSessionBeanLocal.retrieveOutletById(outletIdUpdate);
            
            List<Reservation> reservations = outletToUpdate.getReservations();
            for (Reservation r: reservations) {
                if (r.getReservationId() == reservationToUpdate.getReservationId()) {
                    outletToUpdate.getReservations().remove(r);
                    outletToUpdate.getReservations().add(reservationToUpdate);
                    break;
                }
            }
            
            reservationToUpdate.setOutlet(outletToUpdate);
        }
        
        if (promotionIdUpdate != null) {
            Promotion promotion = promotionSessionBeanLocal.retrievePromotionById(promotionIdUpdate);
            reservationToUpdate.setPromotion(promotion);
        }
        
        em.flush();
    }

    // Delete reservation
    @Override
    public void deleteReservation(Long reservationId) {
        Reservation reservationToDelete = retrieveReservationById(reservationId);
        
        em.remove(reservationToDelete);
    }
    
    
    
    
    
    
    
    
    
}
