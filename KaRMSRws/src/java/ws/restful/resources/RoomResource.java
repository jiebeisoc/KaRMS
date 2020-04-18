/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.RoomSessionBeanLocal;
import entity.Room;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.model.RetrieveRoomByOutletAndRoomTypeRsp;

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

    /**
     * Creates a new instance of RoomResource
     */
    public RoomResource() {
        sessionBeanLookup = new SessionBeanLookup();
        
        roomSessionBeanLocal = sessionBeanLookup.lookupRoomSessionBeanLocal();
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
        
        return Response.status(Status.OK).entity(new RetrieveRoomByOutletAndRoomTypeRsp(rooms)).build();
        
    }


}
