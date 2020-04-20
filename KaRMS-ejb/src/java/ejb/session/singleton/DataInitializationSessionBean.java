/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PromotionSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Customer;
import entity.Employee;
import entity.FoodItemCategory;
import entity.Outlet;
import entity.Promotion;
import entity.Reservation;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.ReservationStatus;
import util.enumeration.RoomRateType;
import util.exception.CreateCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author chai
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @EJB(name = "PromotionSessionBeanLocal")
    private PromotionSessionBeanLocal promotionSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB(name = "RoomSessionBeanLocal")
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @EJB(name = "RoomTypeSessionBeanLocal")
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB(name = "RoomRateSessionBeanLocal")
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;
    
    DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public DataInitializationSessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstuct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("manager");
        } catch (EmployeeNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {
        System.err.println("********Reach Initialization Data*******************");
        
        // Add temporary dummy customer
        try {
            customerSessionBeanLocal.createNewCustomer(new Customer(1l, "Customer", "90001234", "1234567812345678", "customer1", "password", new Date(), "customer1@gmail.com"));
        } catch (CustomerUsernameExistException | CreateCustomerException ex) {
            System.out.println(ex.getMessage());
        }
        
        try {
            Date openingHour = timeFormat.parse("12:00");
            Date closingHour = timeFormat.parse("00:00");
            
            outletSessionBeanLocal.createNewOutlet(new Outlet("AMK Hub", "Ang Mo Kio", "+65001234", openingHour, closingHour));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Bedok Point", "Bedok", "+65001235", openingHour, closingHour));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Causeway Point", "Woodlands", "+65001236", openingHour, closingHour));
            outletSessionBeanLocal.createNewOutlet(new Outlet("JCube", "Jurong East", "+65001237", openingHour, closingHour));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Junction 8", "Bishan", "+65001238", openingHour, closingHour));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Tampines Hub", "Tampines", "+65001239", openingHour, closingHour));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Sembawang Shopping Centre", "Sembawang", "+65001240", openingHour, closingHour));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Star Vista", "Buona Vista", "+65001241", openingHour, closingHour));
        } catch (ParseException ex) {
            System.out.println("Wrong Format");
        }
        
        employeeSessionBeanLocal.createNewEmployee(new Employee("manager", "password", AccessRightEnum.MANAGER), null);
        employeeSessionBeanLocal.createNewEmployee(new Employee("angmokio", "password", AccessRightEnum.CASHIER), 1l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("bedok", "password", AccessRightEnum.CASHIER), 2l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("woodlands", "password", AccessRightEnum.CASHIER), 3l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("jurongeast", "password", AccessRightEnum.CASHIER), 4l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("bishan", "password", AccessRightEnum.CASHIER), 5l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("tampines", "password", AccessRightEnum.CASHIER), 6l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("sembawang", "password", AccessRightEnum.CASHIER), 7l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("buonavista", "password", AccessRightEnum.CASHIER), 8l);
                    
        try {
            Date nonPeakStart = timeFormat.parse("12:00");
            Date nonPeakEnd = timeFormat.parse("17:59");
            Date peakStart = timeFormat.parse("18:00");
            Date peakEnd = timeFormat.parse("00:00");
            
            //Small
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[S] Weekday Non Peak $8", nonPeakStart, nonPeakEnd, new BigDecimal("8"), RoomRateType.WKDAYNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[S] Weekend Non Peak $10", nonPeakStart, nonPeakEnd, new BigDecimal("10"), RoomRateType.WKENDNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[S] Weekday Peak $14", peakStart, peakEnd, new BigDecimal("14"), RoomRateType.WKDAYPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[S] Weekend Peak $16", peakStart, peakEnd, new BigDecimal("16"), RoomRateType.WKENDPEAK));
            
            //Medium
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[M] Weekday Non Peak $10", nonPeakStart, nonPeakEnd, new BigDecimal("10"), RoomRateType.WKDAYNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[M] Weekend Non Peak $12", nonPeakStart, nonPeakEnd, new BigDecimal("12"), RoomRateType.WKENDNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[M] Weekday Peak $16", peakStart, peakEnd, new BigDecimal("16"), RoomRateType.WKDAYPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[M] Weekend Peak $18", peakStart, peakEnd, new BigDecimal("18"), RoomRateType.WKENDPEAK));
            
            //Large
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[L] Weekday Non Peak $12", nonPeakStart, nonPeakEnd, new BigDecimal("12"), RoomRateType.WKDAYNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[L] Weekend Non Peak $14", nonPeakStart, nonPeakEnd, new BigDecimal("14"), RoomRateType.WKENDNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[L] Weekday Peak $18", peakStart, peakEnd, new BigDecimal("18"), RoomRateType.WKDAYPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("[L] Weekend Peak $20", peakStart, peakEnd, new BigDecimal("20"), RoomRateType.WKENDPEAK));         
            
        } catch (ParseException ex) {
            System.out.println("Wrong Format");
        }
        
        List<Long> roomRateIds = new ArrayList<>();
        roomRateIds.add(1l);
        roomRateIds.add(2l);
        roomRateIds.add(3l);
        roomRateIds.add(4l);
        
        roomTypeSessionBeanLocal.createNewRoomType(new RoomType("Small", 4, "Small"), roomRateIds);
        
        roomRateIds.clear();
        roomRateIds.add(5l);
        roomRateIds.add(6l);
        roomRateIds.add(7l);
        roomRateIds.add(8l);
        
        roomTypeSessionBeanLocal.createNewRoomType(new RoomType("Medium", 6, "Medium"), roomRateIds);
        
        roomRateIds.clear();
        roomRateIds.add(9l);
        roomRateIds.add(10l);
        roomRateIds.add(11l);
        roomRateIds.add(12l);
        
        roomTypeSessionBeanLocal.createNewRoomType(new RoomType("Large", 8, "Large"), roomRateIds);
    
        roomSessionBeanLocal.createNewRoom(new Room("S01"), 1l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("S01"), 1l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("S01"), 1l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("S01"), 1l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("S01"), 1l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("S01"), 1l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("S01"), 1l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("S01"), 1l, 8l);
        
        roomSessionBeanLocal.createNewRoom(new Room("S02"), 1l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("S02"), 1l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("S02"), 1l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("S02"), 1l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("S02"), 1l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("S02"), 1l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("S02"), 1l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("S02"), 1l, 8l);
        
        roomSessionBeanLocal.createNewRoom(new Room("S03"), 1l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("S03"), 1l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("S03"), 1l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("S03"), 1l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("S03"), 1l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("S03"), 1l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("S03"), 1l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("S03"), 1l, 8l);
        
        roomSessionBeanLocal.createNewRoom(new Room("M04"), 2l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("M04"), 2l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("M04"), 2l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("M04"), 2l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("M04"), 2l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("M04"), 2l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("M04"), 2l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("M04"), 2l, 8l);
        
        roomSessionBeanLocal.createNewRoom(new Room("M05"), 2l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("M05"), 2l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("M05"), 2l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("M05"), 2l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("M05"), 2l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("M05"), 2l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("M05"), 2l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("M05"), 2l, 8l);
        
        roomSessionBeanLocal.createNewRoom(new Room("M06"), 2l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("M06"), 2l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("M06"), 2l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("M06"), 2l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("M06"), 2l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("M06"), 2l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("M06"), 2l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("M06"), 2l, 8l);
        
        roomSessionBeanLocal.createNewRoom(new Room("L07"), 3l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("L07"), 3l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("L07"), 3l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("L07"), 3l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("L07"), 3l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("L07"), 3l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("L07"), 3l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("L07"), 3l, 8l);
        
        roomSessionBeanLocal.createNewRoom(new Room("L08"), 3l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("L08"), 3l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("L08"), 3l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("L08"), 3l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("L08"), 3l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("L08"), 3l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("L08"), 3l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("L08"), 3l, 8l);
        
        roomSessionBeanLocal.createNewRoom(new Room("L09"), 3l, 1l);
        roomSessionBeanLocal.createNewRoom(new Room("L09"), 3l, 2l);
        roomSessionBeanLocal.createNewRoom(new Room("L09"), 3l, 3l);
        roomSessionBeanLocal.createNewRoom(new Room("L09"), 3l, 4l);
        roomSessionBeanLocal.createNewRoom(new Room("L09"), 3l, 5l);
        roomSessionBeanLocal.createNewRoom(new Room("L09"), 3l, 6l);
        roomSessionBeanLocal.createNewRoom(new Room("L09"), 3l, 7l);
        roomSessionBeanLocal.createNewRoom(new Room("L09"), 3l, 8l);
        
        //Add Promotions
        promotionSessionBeanLocal.createNewPromotion(new Promotion("Promotion 1", 0.1, new GregorianCalendar(2020, Calendar.APRIL, 1).getTime(), new GregorianCalendar(2020, Calendar.APRIL, 30).getTime(), "Promotion 1"));
        promotionSessionBeanLocal.createNewPromotion(new Promotion("Promotion 2", 0.2, new GregorianCalendar(2020, Calendar.APRIL, 15).getTime(), new GregorianCalendar(2020, Calendar.MAY, 15).getTime(), "Promotion 2"));
        promotionSessionBeanLocal.createNewPromotion(new Promotion("Promotion 3", 0.3, new GregorianCalendar(2020, Calendar.MAY, 20).getTime(), new GregorianCalendar(2020, Calendar.JUNE, 20).getTime(), "Promotion 3"));
        
        //Add FoodItem Categories
        FoodItemCategory foodItemCategory1 = new FoodItemCategory("Snacks","All snacks that you are craving");
        FoodItemCategory foodItemCategory2 = new FoodItemCategory("Potato Chips","All time favorites");
        FoodItemCategory foodItemCategory3 = new FoodItemCategory("Instant Noodle","Kids Favorites");
        
        List<FoodItemCategory> subCategoryEntities = new LinkedList<FoodItemCategory>();
        subCategoryEntities.add(foodItemCategory2);
        subCategoryEntities.add(foodItemCategory3);
        foodItemCategory1.setSubCategoryEntities(subCategoryEntities);
        
        foodItemCategory2.setParentCategoryEntity(foodItemCategory1);
        foodItemCategory3.setParentCategoryEntity(foodItemCategory1);
        
        
        FoodItemCategory foodItemCategory4 = new FoodItemCategory("Main Course","Our experienced chef provides authentic dishes");
           
        em.persist(foodItemCategory1);
        em.persist(foodItemCategory2);
        em.persist(foodItemCategory3);
        em.persist(foodItemCategory4);
        em.flush();
        
        try {
            Reservation newReservation = new Reservation(new GregorianCalendar(2020, Calendar.APRIL, 2, 15, 0).getTime(), 1, 3, ReservationStatus.COMPLETED);
            newReservation.setTotalPrice(BigDecimal.valueOf(7.2));
            newReservation.setDateReserved(new GregorianCalendar(2020, Calendar.APRIL, 1, 12, 34).getTime());
            reservationSessionBeanLocal.createNewReservation(newReservation, 1l, 1l, 1l, 1l);
            
            newReservation = new Reservation(new GregorianCalendar(2020, Calendar.APRIL, 13, 18, 0).getTime(), 2, 6, ReservationStatus.COMPLETED);
            newReservation.setTotalPrice(BigDecimal.valueOf(28.8));
            newReservation.setDateReserved(new GregorianCalendar(2020, Calendar.APRIL, 13, 14, 11).getTime());
            newReservation.setWalkInPhoneNo("90001234");
            reservationSessionBeanLocal.createNewReservation(newReservation, 26l, 2l, 1l);
            
            newReservation = new Reservation(new GregorianCalendar(2020, Calendar.APRIL, 25, 20, 0).getTime(), 3, 8, ReservationStatus.PAID);
            newReservation.setTotalPrice(BigDecimal.valueOf(48));
            newReservation.setDateReserved(new GregorianCalendar(2020, Calendar.APRIL, 18, 13, 32).getTime());
            reservationSessionBeanLocal.createNewReservation(newReservation, 1l, 51l, 3l, 2l);
            
            newReservation = new Reservation(new GregorianCalendar(2020, Calendar.MAY, 1, 13, 0).getTime(), 1, 3, ReservationStatus.PAID);
            newReservation.setTotalPrice(BigDecimal.valueOf(8));
            newReservation.setDateReserved(new GregorianCalendar(2020, Calendar.APRIL, 28, 16, 16).getTime());
            reservationSessionBeanLocal.createNewReservation(newReservation, 1l, 4l, 4l, 2l);
            
            newReservation = new Reservation(new GregorianCalendar(2020, Calendar.MAY, 18, 18, 0).getTime(), 2, 2, ReservationStatus.NOTPAID);
            newReservation.setTotalPrice(BigDecimal.valueOf(28));
            newReservation.setDateReserved(new GregorianCalendar(2020, Calendar.MAY, 18, 17, 45).getTime());
            newReservation.setWalkInPhoneNo("90006789");
            reservationSessionBeanLocal.createNewReservation(newReservation, 5l, 5l, null);
            
            newReservation = new Reservation(new GregorianCalendar(2020, Calendar.JUNE, 1, 22, 0).getTime(), 2, 5, ReservationStatus.NOTPAID);
            newReservation.setTotalPrice(BigDecimal.valueOf(22.4));
            newReservation.setDateReserved(new GregorianCalendar(2020, Calendar.MAY, 15, 21, 51).getTime());
            reservationSessionBeanLocal.createNewReservation(newReservation, 1l, 27l, 3l, 3l);
            
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}
