/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import entity.Customer;
import entity.Reservation;
import entity.Song;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.exception.AddSongException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteReservationException;
import util.exception.InvalidLoginCredentialException;
import ws.restful.model.CalculateTotalPriceRsp;
import ws.restful.model.CreateReservationReq;
import ws.restful.model.CreateReservationRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.FavouritePlaylistSongQueueReq;
import ws.restful.model.RetrieveReservationListRsp;
import ws.restful.model.RetrieveReservationRsp;
import ws.restful.model.RetrieveSongsRsp;
import ws.restful.model.UpdateReservationReq;
import ws.restful.model.UpdateSongQueueReq;

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
    
    private final CustomerSessionBeanLocal customerSessionBeanLocal;

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
        customerSessionBeanLocal = sessionBeanLookup.lookupCustomerSessionBeanLocal();
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
                
                r.getCustomer().getFoodOrderTransactionEntities().clear();
                r.getCustomer().getReservations().clear();
                r.getCustomer().getFavouritePlaylist().clear();
                
                r.getRoom().getReservations().clear();
                r.getRoom().setOutlet(null);
                r.getRoom().getRoomType().getRooms().clear();
                r.getRoom().getRoomType().getRoomRates().clear();
                
                r.getOutlet().getRooms().clear();
                r.getOutlet().getReservations().clear();
                r.getOutlet().getReviews().clear();
                r.getOutlet().setEmployee(null);
                
                if (r.getReview() != null) {
                    r.getReview().setReservation(null);
                    r.getReview().setOutlet(null);
                } else {
                    r.setReview(null);
                }

                if (!r.getSongQueue().isEmpty()) {
                    for (Song song: r.getSongQueue()) {
                        song.getSongCategories().clear();
                    }
                } else {
                    r.setSongQueue(null);
                }
                
                System.out.println(r.getReservationId());
            }

            return Response.status(Status.OK).entity(new RetrieveReservationListRsp(reservations)).build();

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
                
                r.getCustomer().getFoodOrderTransactionEntities().clear();
                r.getCustomer().getReservations().clear(); 
                r.getCustomer().getFavouritePlaylist().clear();
                
                r.getRoom().getReservations().clear();
                r.getRoom().setOutlet(null);
                r.getRoom().getRoomType().getRooms().clear();
                r.getRoom().getRoomType().getRoomRates().clear();
                
                r.getOutlet().getRooms().clear();
                r.getOutlet().getReservations().clear();
                r.getOutlet().getReviews().clear();
                r.getOutlet().setEmployee(null);
                
                if (r.getReview() != null) {
                    r.getReview().setReservation(null);
                    r.getReview().setOutlet(null);
                } else {
                    r.setReview(null);
                }

                if (!r.getSongQueue().isEmpty()) {
                    for (Song song: r.getSongQueue()) {
                        song.getSongCategories().clear();
                    }
                } else {
                    r.setSongQueue(null);
                }
                
                System.out.println(r.getReservationId());
            }

            return Response.status(Status.OK).entity(new RetrieveReservationListRsp(reservations)).build();
            
        } catch (ParseException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveUpcomingReservationByCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveUpcomingReservationByCustomer(@QueryParam("username") String username, @QueryParam("password") String password) {
        
        try {
            Customer customer = customerSessionBeanLocal.customerLogin(username, password);
            System.out.println("********** ReservationResource.retrieveUpcomingReservationByCustomer()");
            
            List<Reservation> reservations = reservationSessionBeanLocal.retrieveUpcomingReservationByCustomer(customer.getCustomerId());
            
            for (Reservation r : reservations) {
                
                r.getCustomer().getFoodOrderTransactionEntities().clear();
                r.getCustomer().getReservations().clear();
                r.getCustomer().getFavouritePlaylist().clear();
                
                r.getRoom().getReservations().clear();
                r.getRoom().setOutlet(null);
                r.getRoom().getRoomType().getRooms().clear();
                r.getRoom().getRoomType().getRoomRates().clear();
                
                r.getOutlet().getRooms().clear();
                r.getOutlet().getReservations().clear();
                r.getOutlet().getReviews().clear();
                r.getOutlet().setEmployee(null);
                
                if (r.getReview() != null) {
                    r.getReview().setReservation(null);
                    r.getReview().setOutlet(null);
                } else {
                    r.setReview(null);
                }

                if (!r.getSongQueue().isEmpty()) {
                    for (Song song: r.getSongQueue()) {
                        song.getSongCategories().clear();
                    }
                } else {
                    r.setSongQueue(null);
                }
                
            }
            
            return Response.status(Status.OK).entity(new RetrieveReservationListRsp(reservations)).build();
            
        } catch (InvalidLoginCredentialException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
        
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        } 
    }
    
    @Path("retrievePastReservationByCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePastReservationByCustomer(@QueryParam("username") String username, @QueryParam("password") String password) {
        
        try {
            Customer customer = customerSessionBeanLocal.customerLogin(username, password);
            System.out.println("********** ReservationResource.retrievePastReservationByCustomer()");
            
            List<Reservation> reservations = reservationSessionBeanLocal.retrievePastReservationByCustomer(customer.getCustomerId());
            
            for (Reservation r : reservations) {
                
                r.getCustomer().getFoodOrderTransactionEntities().clear();
                r.getCustomer().getReservations().clear();
                r.getCustomer().getFavouritePlaylist().clear();
                
                r.getRoom().getReservations().clear();
                r.getRoom().setOutlet(null);
                r.getRoom().getRoomType().getRooms().clear();
                r.getRoom().getRoomType().getRoomRates().clear();
                
                r.getOutlet().getRooms().clear();
                r.getOutlet().getReservations().clear();
                r.getOutlet().getReviews().clear();
                r.getOutlet().setEmployee(null);
                
                if (r.getReview() != null) {
                    r.getReview().setReservation(null);
                    r.getReview().setOutlet(null);
                } else {
                    r.setReview(null);
                }

                if (!r.getSongQueue().isEmpty()) {
                    for (Song song: r.getSongQueue()) {
                        song.getSongCategories().clear();
                    }
                } else {
                    r.setSongQueue(null);
                }
                
            }
            
            return Response.status(Status.OK).entity(new RetrieveReservationListRsp(reservations)).build();
            
        } catch (InvalidLoginCredentialException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
        
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        } 
    }
        
    @Path("retrieveReservation/{reservationId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveReservation(@QueryParam("username") String username, @QueryParam("password") String password,
                                            @PathParam("reservationId") Long reservationId) {
        try {
            Customer customer = customerSessionBeanLocal.customerLogin(username, password);
            System.out.println("********** ReservationResource.retrieveReservation()");
            
            Reservation reservation = reservationSessionBeanLocal.retrieveReservationById(reservationId);
            
            reservation.getCustomer().getFoodOrderTransactionEntities().clear();
            reservation.getCustomer().getReservations().clear();
            reservation.getCustomer().getFavouritePlaylist().clear();
                
            reservation.getRoom().getReservations().clear();
            reservation.getRoom().setOutlet(null);
            reservation.getRoom().getRoomType().getRooms().clear();
            reservation.getRoom().getRoomType().getRoomRates().clear();

            reservation.getOutlet().getRooms().clear();
            reservation.getOutlet().getReservations().clear();
            reservation.getOutlet().getReviews().clear();
            reservation.getOutlet().setEmployee(null);

            if (reservation.getReview() != null) {
                reservation.getReview().setReservation(null);
                reservation.getReview().setOutlet(null);
            } else {
                reservation.setReview(null);
            }

            if (!reservation.getSongQueue().isEmpty()) {
                for (Song song: reservation.getSongQueue()) {
                    song.getSongCategories().clear();
                }
            } else {
                reservation.setSongQueue(null);
            }
            
            return Response.status(Status.OK).entity(new RetrieveReservationRsp(reservation)).build();
   
        } catch (InvalidLoginCredentialException ex) { 
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
   
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }  
    }
    
    @Path("retrieveSongQueue")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSongQueue(@QueryParam("reservationId") Long reservationId) {
        
        System.out.println("******** ReservationResource.retrieveSongQueue()");
        
        List<Song> songs = reservationSessionBeanLocal.retrieveSongQueue(reservationId);
        
        for (Song song: songs) {
            song.getSongCategories().clear();
        }
        
        return Response.status(Status.OK).entity(new RetrieveSongsRsp(songs)).build();
    }
    
    @Path("addSongToQueue")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSongToQueue(UpdateSongQueueReq updateSongQueueReq) {
        
        try { 
        
            System.out.println("******** ReservationResource.addSongToQueue()");

            reservationSessionBeanLocal.addSongToQueue(updateSongQueueReq.getSong(), updateSongQueueReq.getReservation());

            return Response.status(Response.Status.OK).build();
 
        } catch (AddSongException ex) {
            
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
            
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("deleteSongFromQueue")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSongFromQueue(UpdateSongQueueReq updateSongQueueReq) {
        
        try { 
        
            System.out.println("******** ReservationResource.deleteSongFromQueue()");

            reservationSessionBeanLocal.deleteSongFromQueue(updateSongQueueReq.getSong(), updateSongQueueReq.getReservation());

            return Response.status(Response.Status.OK).build();
 
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("addQueueToFavouritePlaylist")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addQueueToFavouritePlaylist(FavouritePlaylistSongQueueReq favouritePlaylistSongQueueReq) {
        
        try { 
        
            System.out.println("******** ReservationResource.addQueueToFavouritePlaylist()");

            reservationSessionBeanLocal.addQueueToFavouritePlaylist(favouritePlaylistSongQueueReq.getCustomerId(), favouritePlaylistSongQueueReq.getReservationId());

            return Response.status(Response.Status.OK).build();
 
        }  catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("saveQueueAsFavouritePlaylist")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveQueueAsFavouritePlaylist(FavouritePlaylistSongQueueReq favouritePlaylistSongQueueReq) {
        
        try { 
        
            System.out.println("******** ReservationResource.saveQueueAsFavouritePlaylist()");

            reservationSessionBeanLocal.saveQueueAsFavouritePlaylist(favouritePlaylistSongQueueReq.getCustomerId(), favouritePlaylistSongQueueReq.getReservationId());

            return Response.status(Response.Status.OK).build();
 
        }  catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("calculateTotalPrice")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateTotalPrice(@QueryParam("time") Long time, @QueryParam("duration") int duration,
                                            @QueryParam("roomTypeId") Long roomTypeId, @QueryParam("promotionId") Long promotionId) {
        try {
            System.out.println("********** ReservationResource.calculateTotalPrice()");
            
            BigDecimal totalPrice = reservationSessionBeanLocal.calculateTotalPrice(time, duration, roomTypeId, promotionId);
            
            return Response.status(Status.OK).entity(new CalculateTotalPriceRsp(totalPrice)).build();
            
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        } 
    }
    
    /**
     * PUT method for updating or creating an instance of ReservationResource
     * @param content representation for the resource
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReservation(CreateReservationReq createReservationReq) {
        
        if (createReservationReq != null) {
            try {
                Customer customer = customerSessionBeanLocal.customerLogin(createReservationReq.getUsername(), createReservationReq.getPassword());
                System.out.println("******** Reservation created");
                
                Long reservationId = reservationSessionBeanLocal.createNewReservation(createReservationReq.getNewReservation(), customer.getCustomerId(), 
                        createReservationReq.getRoomId(), createReservationReq.getOutletId(), createReservationReq.getPromotionId(), 
                        createReservationReq.getStatus());
                
                CreateReservationRsp createReservationRsp = new CreateReservationRsp(reservationId);
                return Response.status(Status.OK).entity(createReservationRsp).build();
            
            } catch (InvalidLoginCredentialException ex) {
                
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
                return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
   
            } catch (CustomerNotFoundException ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
                return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
            
            } catch (Exception ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid create new reservation request");
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
        
    }
    
    @Path("updateReservation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReservation(UpdateReservationReq updateReservationReq) {
        
        if(updateReservationReq != null) {
            
            try {
                Customer customer = customerSessionBeanLocal.customerLogin(updateReservationReq.getUsername(), updateReservationReq.getPassword());
                System.out.println("********** ReservationResource.updateReservation()");

                reservationSessionBeanLocal.updateReservation(updateReservationReq.getReservation(), updateReservationReq.getRoomId(), 
                        updateReservationReq.getOutletId(), updateReservationReq.getPromotionId());
                
                return Response.status(Response.Status.OK).build();
            
            } catch (InvalidLoginCredentialException ex) {
                
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
                return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
   
            }  catch (Exception ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
            
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid update reservation request");
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    
    @Path("{reservationId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteReservation(@QueryParam("username") String username, @QueryParam("password") String password, 
                                        @PathParam("reservationId") Long reservationId) {
        try {
            Customer customer = customerSessionBeanLocal.customerLogin(username, password);
            System.out.println("********** ReservationResource.deleteReservation");
            
            reservationSessionBeanLocal.deleteReservation(reservationId);
            
            return Response.status(Status.OK).build();
        
        } catch (InvalidLoginCredentialException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
        
        } catch (DeleteReservationException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
            
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
        
    }

}
