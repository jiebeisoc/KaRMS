/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.FoodItemCategorySessionBeanLocal;
import entity.FoodItemCategory;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllCategoriesRsp;

/**
 * REST Web Service
 *
 * @author longluqian
 */
@Path("FoodItemCategory")
public class FoodItemCategoryResource {

    @Context
    private UriInfo context;
    private final SessionBeanLookup sessionBeanLookup;
    private final FoodItemCategorySessionBeanLocal foodItemCategorySessionBeanLocal;

    /**
     * Creates a new instance of FoodItemCategoryResource
     */
    public FoodItemCategoryResource() {
        sessionBeanLookup = new SessionBeanLookup();
        foodItemCategorySessionBeanLocal = sessionBeanLookup.lookupFoodItemCategorySessionBeanLocal();

    }

    /**
     * Retrieves representation of an instance of
     * ws.restful.resources.FoodItemCategoryResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories() {
        try {
            List<FoodItemCategory> categories = foodItemCategorySessionBeanLocal.retrieveAllCategories();

            for (FoodItemCategory categoryEntity : categories) {
                if (categoryEntity.getParentCategoryEntity() != null) {
                    categoryEntity.getParentCategoryEntity().getSubCategoryEntities().clear();
                }

                categoryEntity.getSubCategoryEntities().clear();
                categoryEntity.getFoodItems().clear();
            }

            return Response.status(Response.Status.OK).entity(new RetrieveAllCategoriesRsp(categories)).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of
     * FoodItemCategoryResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
