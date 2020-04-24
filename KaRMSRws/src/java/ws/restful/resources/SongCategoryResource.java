/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.SongCategorySessionBeanLocal;
import entity.SongCategory;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ws.restful.model.RetrieveSongCategoryRsp;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("SongCategory")
public class SongCategoryResource {

    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final SongCategorySessionBeanLocal songCategorySessionBeanLocal;

    /**
     * Creates a new instance of SongCategoryResource
     */
    public SongCategoryResource() {
        sessionBeanLookup = new SessionBeanLookup();
        songCategorySessionBeanLocal = sessionBeanLookup.lookupSongCategorySessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.SongCategoryResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllSongCategories() {
        System.out.println("******** SongCategoryResource.retrieveAllSongCategories()");
        List<SongCategory> songCategories = songCategorySessionBeanLocal.retrieveAllSongCategory();
        
        for (SongCategory sc: songCategories) {
            sc.getSongs().clear();
        }

        return Response.status(Status.OK).entity(new RetrieveSongCategoryRsp(songCategories)).build();
        
    }

}
