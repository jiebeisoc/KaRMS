/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.PromotionSessionBeanLocal;
import entity.Promotion;
import java.util.Date;
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
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrievePromotionRsp;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("Promotion")
public class PromotionResource {

    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final PromotionSessionBeanLocal promotionSessionBeanLocal;

    /**
     * Creates a new instance of PromotionResource
     */
    public PromotionResource() {
        sessionBeanLookup = new SessionBeanLookup();
        promotionSessionBeanLocal = sessionBeanLookup.lookupPromotionSessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.PromotionResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePromotions() {
        System.out.println("******** PromotionResource.retrievePromotions()");

        List<Promotion> promotions = promotionSessionBeanLocal.retrievePromotionByDate(new Date());

        return Response.status(Status.OK).entity(new RetrievePromotionRsp(promotions)).build();
    }
    
    @Path("retrievePromotionsByTime")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePromotionsByTime(@QueryParam("time") Long time) {
        
        try {
        
            System.out.println("******** PromotionResource.retrievePromotionsByDate()");

            List<Promotion> promotions = promotionSessionBeanLocal.retrievePromotionsByTime(time);

            return Response.status(Status.OK).entity(new RetrievePromotionRsp(promotions)).build();
        
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        } 
    }
}
