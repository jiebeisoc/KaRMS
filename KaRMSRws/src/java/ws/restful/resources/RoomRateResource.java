/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.RoomRateSessionBeanLocal;
import entity.RoomRate;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.model.RetrieveAllRoomRateRsp;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("RoomRate")
public class RoomRateResource {

    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;
    
    private final RoomRateSessionBeanLocal roomRateSessionBeanLocal;
    
    /**
     * Creates a new instance of RoomRateResource
     */
    public RoomRateResource() {
        sessionBeanLookup = new SessionBeanLookup();
        
        roomRateSessionBeanLocal = sessionBeanLookup.lookupRoomRateSessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.RoomRateResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveAllRoomRate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRoomRate() {
        System.out.println("******** RoomRateResource.retrieveAllRoomRate()");
        List<RoomRate> roomRates = roomRateSessionBeanLocal.retrieveAllRoomRates();
        
        for (RoomRate rr: roomRates) {
            if (rr.getRoomType() != null) {
                rr.getRoomType().getRoomRates().clear();
                rr.getRoomType().getRooms().clear();
            }
        }
        
        return Response.status(Status.OK).entity(new RetrieveAllRoomRateRsp(roomRates)).build();
    }

}
