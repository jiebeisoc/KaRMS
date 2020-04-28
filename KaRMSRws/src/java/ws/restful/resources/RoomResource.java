/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import entity.Customer;
import entity.Room;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.exception.ExceedClosingHoursException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoAvailableRoomException;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveRoomListRsp;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("Room")
public class RoomResource {

    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final RoomSessionBeanLocal roomSessionBeanLocal;
    
    private final CustomerSessionBeanLocal customerSessionBeanLocal;

    /**
     * Creates a new instance of RoomResource
     */
    public RoomResource() {
        sessionBeanLookup = new SessionBeanLookup();
        
        roomSessionBeanLocal = sessionBeanLookup.lookupRoomSessionBeanLocal();
        customerSessionBeanLocal = sessionBeanLookup.lookupCustomerSessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.RoomResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveRoomByOutletAndRoomType")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveRoomByOutletAndRoomType(@QueryParam("outletId") Long outletId, @QueryParam("roomTypeId") Long roomTypeId) {
        
        List<Room> rooms = roomSessionBeanLocal.retrieveRoomByOutletAndRoomType(outletId, roomTypeId);
        
        for (Room r: rooms) {
            r.getReservations().clear();
            r.setOutlet(null);
            r.setRoomType(null);
        }
        
        return Response.status(Status.OK).entity(new RetrieveRoomListRsp(rooms)).build();
        
    }
    
    @Path("retrieveAvailableRooms")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAvailableRooms(@QueryParam("username") String username, @QueryParam("password") String password,
                                            @QueryParam("time") Long time, @QueryParam("duration") int duration,
                                            @QueryParam("outletId") Long outletId, @QueryParam("roomTypeId") Long roomTypeId) {
        try {
            Customer customer = customerSessionBeanLocal.customerLogin(username, password);
            System.out.println("********** RoomResource.retrieveAvailableRooms()");
            
            List<Room> rooms = roomSessionBeanLocal.retrieveAvailableRooms(time, duration, outletId, roomTypeId);
            
            for (Room r: rooms) {
            r.getReservations().clear();
            r.setOutlet(null);
            r.setRoomType(null);
            }
            
            return Response.status(Status.OK).entity(new RetrieveRoomListRsp(rooms)).build();
   
        } catch (InvalidLoginCredentialException ex) { 
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
   
        } catch (NoAvailableRoomException | ExceedClosingHoursException ex) { 
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
   
        } catch (Exception ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }  
    }


}
