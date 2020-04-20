/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.FoodOrderSessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import entity.Customer;
import entity.FoodItem;
import entity.FoodOrderTransaction;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.FoodItemNotFoundException;
import util.exception.InvalidLoginCredentialException;
import ws.restful.model.CreateFoodOrderTransactionReq;
import ws.restful.model.CreateFoodOrderTransactionRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllFoodItemRsp;
import ws.restful.model.RetrieveFoodItemRsp;
import ws.restful.model.RetrievePastFoodOrderTransactionRsp;

/**
 * REST Web Service
 *
 * @author longluqian
 */
@Path("FoodOrder")
public class FoodOrderResource {

    @Context
    private UriInfo context;

    private final SessionBeanLookup sessionBeanLookup;
    
    private final FoodOrderSessionBeanLocal foodOrderSessionBeanLocal;
    private final FoodSessionBeanLocal foodSessionBeanLocal;
    private final CustomerSessionBeanLocal customerSessionBeanLocal;
    
    

    public FoodOrderResource() {
        this.sessionBeanLookup = new SessionBeanLookup();
        this.foodOrderSessionBeanLocal = sessionBeanLookup.lookupFoodOrderSessionBeanLocal();
        this.foodSessionBeanLocal = sessionBeanLookup.lookupFoodSessionBeanLocal();
        this.customerSessionBeanLocal = sessionBeanLookup.lookupCustomerSessionBeanLocal();
        
       
    }
    
    
    
    
    
    
    /**
     * Retrieves representation of an instance of ws.restful.resources.FoodOrderResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveAllFoodItems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllFoodItems() {
       List<FoodItem> foodItemList = foodSessionBeanLocal.retrieveAllFoodItems();
        for(FoodItem foodItem:foodItemList)
            {
                if(foodItem.getCategoryEntity().getParentCategoryEntity() != null)
                {
                    foodItem.getCategoryEntity().getParentCategoryEntity().getSubCategoryEntities().clear();
                }
                
                foodItem.getCategoryEntity().getFoodItems().clear();
                
          
            }
        return Response.status(Response.Status.OK).entity(new RetrieveAllFoodItemRsp(foodItemList)).build();
    }
    
    @Path("retrieveFoodItem/{foodItemId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveFoodItem(@PathParam("foodItemId") Long foodItemId)
    {
        try
        {
             FoodItem foodItem = foodSessionBeanLocal.retrieveFoodItemById(foodItemId);
          
            
            if(foodItem.getCategoryEntity().getParentCategoryEntity() != null)
            {
                foodItem.getCategoryEntity().getParentCategoryEntity().getSubCategoryEntities().clear();
            }

            foodItem.getCategoryEntity().getFoodItems().clear();
       
            return Response.status(Status.OK).entity(new RetrieveFoodItemRsp(foodItem)).build();
        }
       
        catch(FoodItemNotFoundException ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    

    
    
    /**
     * PUT method for updating or creating an instance of FoodOrderResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFoodOrderTransaction(CreateFoodOrderTransactionReq createFoodOrderTransactionReq) {
        if(createFoodOrderTransactionReq != null)
        {
            try
            {
                Customer customer = customerSessionBeanLocal.customerLogin(createFoodOrderTransactionReq.getUsername(), createFoodOrderTransactionReq.getPassword());
                System.out.println("********** FoodOrderResource.createFoodOrderTransaction(): Customer " + customer.getUsername() + " login remotely via web service");
                
               
                FoodOrderTransaction foodOrderTransaction  = foodOrderSessionBeanLocal.createNewFoodOrderTransaction(createFoodOrderTransactionReq.getCustomerId(), createFoodOrderTransactionReq.getNewFoodOrderTransaction());
                CreateFoodOrderTransactionRsp createFoodOrderTransactionRsp = new CreateFoodOrderTransactionRsp(foodOrderTransaction.getFoodOrderTransactionId());
                
                return Response.status(Response.Status.OK).entity(createFoodOrderTransactionRsp).build();
            }
            catch(InvalidLoginCredentialException ex)
            {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
                return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
            }
            
            catch(Exception ex)
            {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        }
        else
        {
            ErrorRsp errorRsp = new ErrorRsp("Invalid create new food order transaction request");
            
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
        
    }
    
    
    /**
     * Retrieves representation of an instance of ws.restful.resources.FoodOrderResource
     * @return an instance of java.lang.String
     */
    @Path("retrieveAllPastFoodOrders/{customerId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response retrieveAllPastFoodOrders(@PathParam("customerId") Long customerId) {
        List<FoodOrderTransaction> list = foodOrderSessionBeanLocal.retrieveAllFoodOrderTransactionsByCustomerID(customerId);
       
        return Response.status(Response.Status.OK).entity(new RetrievePastFoodOrderTransactionRsp(list)).build();
    }
    
    
}
