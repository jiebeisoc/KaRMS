/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Reservation;
import entity.RoomType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.RoomTypeNotFoundException;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveReservationRsp;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("Reservation")
public class ReservationResource {

    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final ReservationSessionBeanLocal reservationSessionBeanLocal;
    
    private final RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;
    
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Creates a new instance of ReservationResource
     */
    public ReservationResource() {
        sessionBeanLookup = new SessionBeanLookup();
        
        reservationSessionBeanLocal = sessionBeanLookup.lookupReservationSessionBeanLocal();
        roomTypeSessionBeanLocal = sessionBeanLookup.lookupRoomTypeSessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.ReservationResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveReservationByDate")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReservationByDate(@QueryParam("date") String dateString,
            @QueryParam("outletId") Long outletId, 
            @QueryParam("roomTypeId") Long roomTypeId) {
        System.out.println("******** ReservationResource.retrieveReservationByDate()");
        
        try {
            Date dateFrom = formatter.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFrom);
            cal.add(Calendar.DATE, 1);
            Date dateTo = cal.getTime();

            List<Reservation> reservations = reservationSessionBeanLocal.retrieveReservationByDateOutletAndRoomType(dateFrom, dateTo, outletId, roomTypeId);

            for (Reservation r : reservations) {
                r.setRoom(null);
                r.setOutlet(null);
                r.setReview(null);
                r.getSongQueue().clear();
                r.setPromotion(null);
                r.setCustomer(null);
                System.out.println(r.getReservationId());
            }

            return Response.status(Status.OK).entity(new RetrieveReservationRsp(reservations)).build();

        } catch (ParseException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        
        
    }
    
    @Path("retrieveReservationByRoomAndDate")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReservationByRoomAndDate(@QueryParam("date") String dateString, 
            @QueryParam("roomId") Long roomId) {
        
        System.out.println("******** ReservationResource.retrieveReservationByRoomAndDate()");
        try {
            Date dateFrom = formatter.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFrom);
            cal.add(Calendar.DATE, 1);
            Date dateTo = cal.getTime();
            
            List<Reservation> reservations = reservationSessionBeanLocal.retrieveReservationByRoomAndDate(dateFrom, dateTo, roomId);
            
            for (Reservation r : reservations) {
                r.setRoom(null);
                r.setOutlet(null);
                r.setReview(null);
                r.getSongQueue().clear();
                r.setPromotion(null);
                r.setCustomer(null);
                System.out.println(r.getReservationId());
            }

            return Response.status(Status.OK).entity(new RetrieveReservationRsp(reservations)).build();
            
        } catch (ParseException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of ReservationResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    

}