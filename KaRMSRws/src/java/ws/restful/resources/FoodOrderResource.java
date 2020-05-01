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
import entity.Employee;
import entity.FoodItem;
import entity.FoodOrderTransaction;
import entity.FoodOrderTransactionLineItem;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.FoodItemNotFoundException;
import util.exception.FoodOrderTransactionAlreadyVoidedRefundedException;
import util.exception.FoodOrderTransactionNotFoundException;
import util.exception.InvalidLoginCredentialException;
import ws.restful.model.CancelFoodOrderTransactionRsp;
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
    @Path("createFoodOrderTransaction")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFoodOrderTransaction(CreateFoodOrderTransactionReq createFoodOrderTransactionReq) {
        
        System.err.println("reach here 1");
   
        if(createFoodOrderTransactionReq != null)
        {
            try
            {
         
                Customer customer = customerSessionBeanLocal.customerLogin(createFoodOrderTransactionReq.getUsername(), createFoodOrderTransactionReq.getPassword());
                System.out.println("********** FoodOrderResource.createFoodOrderTransaction(): Customer " + customer.getUsername() + " login remotely via web service");
                
               
                FoodOrderTransaction foodOrderTransaction  = foodOrderSessionBeanLocal.createNewFoodOrderTransaction(createFoodOrderTransactionReq.getNewFoodOrderTransaction().getCustomerEntity().getCustomerId(), createFoodOrderTransactionReq.getNewFoodOrderTransaction());
    
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
        System.out.println("ws.restful.resources.FoodOrderResource.retrieveAllPastFoodOrders()");
        List<FoodOrderTransaction> list = foodOrderSessionBeanLocal.retrieveAllFoodOrderTransactionsByCustomerID(customerId);
        
        for(FoodOrderTransaction t:list ){
            t.getCustomerEntity().getFoodOrderTransactionEntities().clear();
            t.getCustomerEntity().getFavouritePlaylist().clear();
            t.getOutlet().getReservations().clear();
            t.getOutlet().getRooms().clear();
            t.getOutlet().getReviews().clear();
            t.getOutlet().setEmployee(new Employee());
        
            List <FoodOrderTransactionLineItem> lineItems = t.getFoodOrderTransactionLineItemEntities();
            
            for(FoodOrderTransactionLineItem singleLineItem: lineItems){
                FoodItem foodItem = singleLineItem.getFoodItem();
            if(foodItem.getCategoryEntity().getParentCategoryEntity() != null)
            {
                foodItem.getCategoryEntity().getParentCategoryEntity().getSubCategoryEntities().clear();
            }

            foodItem.getCategoryEntity().getFoodItems().clear();
                
            }
           
            
        }
       
        return Response.status(Response.Status.OK).entity(new RetrievePastFoodOrderTransactionRsp(list)).build();
    }
    
    
    @Path("cancelFoodOrderTransaction/{transactionId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelFoodOrderTransaction(@PathParam("transactionId") Long transactionId){
        System.out.println("ws.restful.resources.FoodOrderResource.cancelFoodOrderTransaction()");
        try {
            foodOrderSessionBeanLocal.voidRefundFoodOrderTransaction(transactionId);
             CancelFoodOrderTransactionRsp rsp = new CancelFoodOrderTransactionRsp("Food order transaction cancelled successfully, refund will be back to you in a few days.");
             return Response.status(Response.Status.OK).entity(rsp).build();
            
        } catch (FoodOrderTransactionNotFoundException ex) {
           ErrorRsp errorRsp = new ErrorRsp("Invalid cancel food order transaction request: "+ ex.getMessage());
           return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();      
        } catch (FoodOrderTransactionAlreadyVoidedRefundedException ex) {
           ErrorRsp errorRsp = new ErrorRsp("Invalid cancel food order transaction request: "+ ex.getMessage());
           return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();       
        }
    
    }
    
    
}
