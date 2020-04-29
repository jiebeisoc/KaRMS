/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.OutletSessionBeanLocal;
import entity.Outlet;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.model.RetrieveAllOutletsRsp;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("Outlet")
public class OutletResource {

    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final OutletSessionBeanLocal outletSessionBeanLocal;

    /**
     * Creates a new instance of OutletResource
     */
    public OutletResource() {
        sessionBeanLookup = new SessionBeanLookup();
        
        outletSessionBeanLocal = sessionBeanLookup.lookupOutletSessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.OutletResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveAllOutlets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllOutlets() {
        System.out.println("******** OutletResource.retrieveOutlets()");
        List<Outlet> outlets = outletSessionBeanLocal.retrieveAllOutlets();
        
        for (Outlet o : outlets) {
            o.getRooms().clear();
            o.getReservations().clear();
            o.getReviews().clear();
            o.setEmployee(null);
        }
        
        return Response.status(Status.OK).entity(new RetrieveAllOutletsRsp(outlets)).build();
    }

}
