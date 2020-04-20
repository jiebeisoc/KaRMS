/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import ejb.session.stateless.SongSessionBeanLocal;
import entity.Employee;
import entity.FoodItemCategory;
import entity.Outlet;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import entity.Song;
import entity.SongCategory;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.enumeration.RoomRateType;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author chai
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB(name = "SongSessionBeanLocal")
    private SongSessionBeanLocal songSessionBeanLocal;

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
        
        // Add outlets
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
        
        // Add employee for each outlet
        employeeSessionBeanLocal.createNewEmployee(new Employee("manager", "password", AccessRightEnum.MANAGER), null);
        employeeSessionBeanLocal.createNewEmployee(new Employee("angmokio", "password", AccessRightEnum.CASHIER), 1l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("bedok", "password", AccessRightEnum.CASHIER), 2l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("woodlands", "password", AccessRightEnum.CASHIER), 3l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("jurongeast", "password", AccessRightEnum.CASHIER), 4l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("bishan", "password", AccessRightEnum.CASHIER), 5l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("tampines", "password", AccessRightEnum.CASHIER), 6l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("sembawang", "password", AccessRightEnum.CASHIER), 7l);
        employeeSessionBeanLocal.createNewEmployee(new Employee("buonavista", "password", AccessRightEnum.CASHIER), 8l);
        
        // Add room rate
        try {
            Date nonPeakStart = timeFormat.parse("12:00");
            Date nonPeakEnd = timeFormat.parse("17:59");
            Date peakStart = timeFormat.parse("18:00");
            Date peakEnd = timeFormat.parse("00:00");
            
            //Small
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekday Non Peak $8", nonPeakStart, nonPeakEnd, new BigDecimal("8"), RoomRateType.WKDAYNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekend Non Peak $10", nonPeakStart, nonPeakEnd, new BigDecimal("10"), RoomRateType.WKENDNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekday Peak $14", peakStart, peakEnd, new BigDecimal("14"), RoomRateType.WKDAYPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekend Peak $16", peakStart, peakEnd, new BigDecimal("16"), RoomRateType.WKENDPEAK));
            
            //Medium
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekday Non Peak $10", nonPeakStart, nonPeakEnd, new BigDecimal("10"), RoomRateType.WKDAYNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekend Non Peak $12", nonPeakStart, nonPeakEnd, new BigDecimal("12"), RoomRateType.WKENDNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekday Peak $16", peakStart, peakEnd, new BigDecimal("16"), RoomRateType.WKDAYPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekend Peak $18", peakStart, peakEnd, new BigDecimal("18"), RoomRateType.WKENDPEAK));
            
            //Large
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekday Non Peak $12", nonPeakStart, nonPeakEnd, new BigDecimal("12"), RoomRateType.WKDAYNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekend Non Peak $14", nonPeakStart, nonPeakEnd, new BigDecimal("14"), RoomRateType.WKENDNONPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekday Peak $18", peakStart, peakEnd, new BigDecimal("18"), RoomRateType.WKDAYPEAK));
            roomRateSessionBeanLocal.createNewRoomRate(new RoomRate("Weekend Peak $20", peakStart, peakEnd, new BigDecimal("20"), RoomRateType.WKENDPEAK));         
            
        } catch (ParseException ex) {
            System.out.println("Wrong Format");
        }
        
        // Add room type
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
    
        // Add room
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
        
        
        SongCategory songCategory1 = new SongCategory("Male Singer");       
        SongCategory songCategory2 = new SongCategory("Female Singer");
        SongCategory songCategory3 = new SongCategory("Chinese Song");
        SongCategory songCategory4 = new SongCategory("English Song");
        SongCategory songCategory5 = new SongCategory("Rock Music");
        SongCategory songCategory6 = new SongCategory("Jazz Music");
        SongCategory songCategory7 = new SongCategory("Pop Music");
        SongCategory songCategory8 = new SongCategory("Country Music");
               
        em.persist(songCategory1);
        em.flush();
        em.persist(songCategory2);
        em.flush();
        em.persist(songCategory3);
        em.flush();
        em.persist(songCategory4);
        em.flush();
        em.persist(songCategory5);
        em.flush();
        em.persist(songCategory6);
        em.flush();
        em.persist(songCategory7);
        em.flush();
        em.persist(songCategory8);
        em.flush();
        
        List<Long> songCategoryIds = new ArrayList<>();
        songCategoryIds.add(1l);
        songCategoryIds.add(4l);
        songCategoryIds.add(7l);
        songSessionBeanLocal.createNewSong(new Song("Perfect", "Ed Sheeran"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Shape of You", "Ed Sheeran"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Thinking out Loud", "Ed Sheeran"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Just the Way You Are", "Bruno Mars"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Marry Me", "Bruno Mars"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Count on Me", "Bruno Mars"), songCategoryIds);
        
        songCategoryIds.clear();
        songCategoryIds.add(2l);
        songCategoryIds.add(4l);
        songCategoryIds.add(7l);
        songSessionBeanLocal.createNewSong(new Song("Love Story", "Taylor Swift"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Blank Space", "Taylor Swift"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Shake it Off", "Taylor Swift"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Shallow", "Lady Gaga"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Bad Romance", "Lady Gaga"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Poker Face", "Lady Gaga"), songCategoryIds);
        
        songCategoryIds.clear();
        songCategoryIds.add(1l);
        songCategoryIds.add(3l);
        songCategoryIds.add(7l);
        songSessionBeanLocal.createNewSong(new Song("Love Confession", "Jay Chou"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("All the Way North", "Jay Chou"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Listen to Mother's Words", "Jay Chou"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Those Were The Days", "JJ Lin"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Practice Love", "JJ Lin"), songCategoryIds);   
        songSessionBeanLocal.createNewSong(new Song("If Only", "JJ Lin"), songCategoryIds);
        
        songCategoryIds.clear();
        songCategoryIds.add(2l);
        songCategoryIds.add(3l);
        songCategoryIds.add(7l);
        songSessionBeanLocal.createNewSong(new Song("A Little Happiness", "Hebe Tien"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Angel Devil", "Hebe Tien"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Leave Me Alone", "Hebe Tien"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Miss Similar", "G.E.M."), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Long After", "G.E.M."), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Away", "G.E.M."), songCategoryIds);
        
        songCategoryIds.clear();
        songCategoryIds.add(1l);
        songCategoryIds.add(4l);
        songCategoryIds.add(5l);
        songSessionBeanLocal.createNewSong(new Song("Stairway to Heaven", "Led Zepplin"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Livin' On A Prayer", "Bon Jovi"), songCategoryIds);
        
        songCategoryIds.clear();
        songCategoryIds.add(2l);
        songCategoryIds.add(4l);
        songCategoryIds.add(5l);
        songSessionBeanLocal.createNewSong(new Song("Miss Independent", "Kelly Clarkson"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Lady Marmalade", "Christina Aguilera"), songCategoryIds);
        
        songCategoryIds.clear();
        songCategoryIds.add(1l);
        songCategoryIds.add(4l);
        songCategoryIds.add(6l);
        songSessionBeanLocal.createNewSong(new Song("So What", "Miles Davis"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("All Blues", "Miles Davis"), songCategoryIds);
        
        songCategoryIds.clear();
        songCategoryIds.add(1l);
        songCategoryIds.add(4l);
        songCategoryIds.add(8l);
        songSessionBeanLocal.createNewSong(new Song("Body Like a Back Road", "Sam Hunt"), songCategoryIds);
        songSessionBeanLocal.createNewSong(new Song("Die a Happy Man", "Thomas Rhett"), songCategoryIds);
        
    }
}
