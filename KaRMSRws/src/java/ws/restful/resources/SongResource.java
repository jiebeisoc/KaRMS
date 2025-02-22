/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.SongSessionBeanLocal;
import entity.Song;
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
import ws.restful.model.RetrieveSongsRsp;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("Song")
public class SongResource {

    private final SessionBeanLookup sessionBeanLookup;
    
    private final SongSessionBeanLocal songSessionBeanLocal;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of SongResource
     */
    public SongResource() {
        sessionBeanLookup = new SessionBeanLookup();
        songSessionBeanLocal = sessionBeanLookup.lookupSongSessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.SongResource
     * @return an instance of java.lang.String
     */
    @Path("viewAllSongs")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewAllSongs() {
        System.out.println("******** SongResource.viewAllSongs()");
        List<Song> songs = songSessionBeanLocal.viewAllSongs();
        
        for (Song s: songs) {
            s.getSongCategories().clear();
        }
        
        return Response.status(Status.OK).entity(new RetrieveSongsRsp(songs)).build();
    }
    
    @Path("viewSongByCategory")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewSongByCategory(@QueryParam("songCategoryId") Long songCategoryId) {
        
        System.out.println("******** SongResource.viewSongByCategory()");
        List<Song> songs = songSessionBeanLocal.viewSongByCategory(songCategoryId);
        
        for (Song s: songs) {
            s.getSongCategories().clear();
        }
        
        return Response.status(Status.OK).entity(new RetrieveSongsRsp(songs)).build();
    }
    
    @Path("viewFavouritePlaylistByCategory")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewFavouritePlaylistByCategory(@QueryParam("customerId") Long customerId, @QueryParam("songCategoryId") Long songCategoryId) {
        
        System.out.println("******** SongResource.viewFavouritePlaylistByCategory()");
        List<Song> songs = songSessionBeanLocal.viewFavouritePlaylistByCategory(customerId, songCategoryId);
        
        for (Song s: songs) {
            s.getSongCategories().clear();
        }
        
        return Response.status(Status.OK).entity(new RetrieveSongsRsp(songs)).build();
    }
    
    @Path("viewSongQueueByCategory")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewSongQueueByCategory(@QueryParam("reservationId") Long reservationId, @QueryParam("songCategoryId") Long songCategoryId) {
        
        System.out.println("******** SongResource.viewSongQueueByCategory()");
        List<Song> songs = songSessionBeanLocal.viewSongQueueByCategory(reservationId, songCategoryId);
        
        for (Song s: songs) {
            s.getSongCategories().clear();
        }
        
        return Response.status(Status.OK).entity(new RetrieveSongsRsp(songs)).build();
    }

}
