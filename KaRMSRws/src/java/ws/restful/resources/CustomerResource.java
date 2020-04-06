/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CustomerSessionBeanLocal;
import entity.Customer;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.ChangePasswordException;
import util.exception.CreateCustomerException;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentialException;
import ws.restful.model.ChangePasswordReq;
import ws.restful.model.CreateCustomerReq;
import ws.restful.model.CreateCustomerRsp;
import ws.restful.model.CustomerLoginRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.UpdateCustomerReq;

/**
 * REST Web Service
 *
 * @author chai
 */
@Path("Customer")
public class CustomerResource {

    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookup;
    
    private final CustomerSessionBeanLocal customerSessionBeanLocal;

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
        sessionBeanLookup = new SessionBeanLookup();
        
        customerSessionBeanLocal = sessionBeanLookup.lookupCustomerSessionBeanLocal();
    }

    /**
     * Retrieves representation of an instance of ws.restful.resources.CustomerResource
     * @return an instance of java.lang.String
     */
    @Path("customerLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerLogin(@QueryParam("username") String username, 
                                @QueryParam("password") String password) {
        try {
            System.out.println("******** Customer login");
            Customer customer = customerSessionBeanLocal.customerLogin(username, password);

            return Response.status(Status.OK).entity(new CustomerLoginRsp(customer)).build();
            
        } catch (InvalidLoginCredentialException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of CustomerResource
     * @param content representation for the resource
     */
    @Path("createNewCustomer")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCustomer(CreateCustomerReq createCustomerReq) {
        
        if (createCustomerReq != null) {
            try {
                System.out.println("******** Customer created");
                Long customerId = customerSessionBeanLocal.createNewCustomer(createCustomerReq.getNewCustomer());
                CreateCustomerRsp createCustomerRsp = new CreateCustomerRsp(customerId);
                return Response.status(Status.OK).entity(createCustomerRsp).build();
            } catch (CustomerUsernameExistException | CreateCustomerException ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");
            
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    
    @Path("updateDetails")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomerDetails(UpdateCustomerReq updateCustomerReq) {
        if (updateCustomerReq != null) {
            try {
                Customer customer = customerSessionBeanLocal.customerLogin(updateCustomerReq.getUsername(), updateCustomerReq.getPassword());
                System.out.println("******** CustomerResource.updateCustomerDetails()");
                
                customerSessionBeanLocal.updateDetails(updateCustomerReq.getUsername(), updateCustomerReq.getCustomer().getName(), 
                        updateCustomerReq.getCustomer().getEmail(), updateCustomerReq.getCustomer().getPhoneNo(), updateCustomerReq.getCustomer().getCreditCardNo());
                
                return Response.status(Response.Status.OK).build();                
            } catch (InvalidLoginCredentialException ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();
            } catch (Exception ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");
            
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    
    @Path("changePassword")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordReq changePasswordReq) {
        if (changePasswordReq != null) {
            try {
                Customer customer = customerSessionBeanLocal.customerLogin(changePasswordReq.getUsername(), changePasswordReq.getPassword());
                System.out.println("******** CustomerResource.changePassword()");
                
                customerSessionBeanLocal.changePassword(changePasswordReq.getUsername(), changePasswordReq.getOldPassword(), changePasswordReq.getNewPassword());
                
                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException | ChangePasswordException ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();
            } catch (Exception ex) {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");
            
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    
    @Path("{customerId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@QueryParam("username") String username,
                                        @QueryParam("password") String password) {
        try {
            Customer customer = customerSessionBeanLocal.customerLogin(username, password);
            System.out.println("********* CustomerResource.deleteCustomer()");
            
            customerSessionBeanLocal.deleteCustomer(customer.getCustomerId());
            
            return Response.status(Status.OK).build();
            
        } catch (InvalidLoginCredentialException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();
        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
}
