/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.RoomType;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.model.RetrieveAllRoomTypeRsp;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("RoomType")
public class RoomTypeResource {

    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    /**
     * Creates a new instance of RoomTypeResource
     */
    public RoomTypeResource() {
        sessionBeanLookup = new SessionBeanLookup();
        
        roomTypeSessionBeanLocal = sessionBeanLookup.lookupRoomTypeSessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.RoomTypeResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveAllRoomType")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllRoomType() {
        System.out.println("******** RoomTyepResource.retrieveAllRoomType()");
        List<RoomType> roomTypes = roomTypeSessionBeanLocal.retrieveAllRoomTypes();
        
        for (RoomType rt: roomTypes) {
            rt.getRoomRates().clear();
            rt.getRooms().clear();            
        }
        
        return Response.status(Status.OK).entity(new RetrieveAllRoomTypeRsp(roomTypes)).build();
    }

}
