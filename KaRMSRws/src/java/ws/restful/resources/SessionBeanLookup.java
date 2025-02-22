/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.FoodItemCategorySessionBeanLocal;
import ejb.session.stateless.FoodOrderSessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PromotionSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import ejb.session.stateless.SongCategorySessionBeanLocal;
import ejb.session.stateless.SongSessionBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author chai
 */
public class SessionBeanLookup {

    private final String ejbModuleJndiPath;

    public SessionBeanLookup() {
        this.ejbModuleJndiPath = "java:global/KaRMS/KaRMS-ejb/";
    }

    public CustomerSessionBeanLocal lookupCustomerSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (CustomerSessionBeanLocal) c.lookup(ejbModuleJndiPath + "CustomerSessionBean!ejb.session.stateless.CustomerSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public FoodOrderSessionBeanLocal lookupFoodOrderSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (FoodOrderSessionBeanLocal) c.lookup(ejbModuleJndiPath+"FoodOrderSessionBean!ejb.session.stateless.FoodOrderSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public FoodSessionBeanLocal lookupFoodSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (FoodSessionBeanLocal) c.lookup(ejbModuleJndiPath+"FoodSessionBean!ejb.session.stateless.FoodSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public FoodItemCategorySessionBeanLocal lookupFoodItemCategorySessionBeanLocal()
    {
         try {
            Context c = new InitialContext();
            return (FoodItemCategorySessionBeanLocal) c.lookup(ejbModuleJndiPath+"FoodItemCategorySessionBean!ejb.session.stateless.FoodItemCategorySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public OutletSessionBeanLocal lookupOutletSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (OutletSessionBeanLocal) c.lookup(ejbModuleJndiPath+"OutletSessionBean!ejb.session.stateless.OutletSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public ReservationSessionBeanLocal lookupReservationSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (ReservationSessionBeanLocal) c.lookup(ejbModuleJndiPath+"ReservationSessionBean!ejb.session.stateless.ReservationSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public RoomTypeSessionBeanLocal lookupRoomTypeSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RoomTypeSessionBeanLocal) c.lookup(ejbModuleJndiPath+"RoomTypeSessionBean!ejb.session.stateless.RoomTypeSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public RoomSessionBeanLocal lookupRoomSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RoomSessionBeanLocal) c.lookup(ejbModuleJndiPath+"RoomSessionBean!ejb.session.stateless.RoomSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public SongSessionBeanLocal lookupSongSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SongSessionBeanLocal) c.lookup(ejbModuleJndiPath+"SongSessionBean!ejb.session.stateless.SongSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public SongCategorySessionBeanLocal lookupSongCategorySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SongCategorySessionBeanLocal) c.lookup(ejbModuleJndiPath+"SongCategorySessionBean!ejb.session.stateless.SongCategorySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public PromotionSessionBeanLocal lookupPromotionSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PromotionSessionBeanLocal) c.lookup(ejbModuleJndiPath+"PromotionSessionBean!ejb.session.stateless.PromotionSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    public RoomRateSessionBeanLocal lookupRoomRateSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RoomRateSessionBeanLocal) c.lookup(ejbModuleJndiPath+"RoomRateSessionBean!ejb.session.stateless.RoomRateSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
