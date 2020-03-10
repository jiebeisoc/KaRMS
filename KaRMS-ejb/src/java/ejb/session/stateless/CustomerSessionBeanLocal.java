/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author chai
 */
@Local
public interface CustomerSessionBeanLocal {

    public Long createNewCustomer(Customer newCustomer);

    public List<Customer> retrieveAllCustomer();

    public Customer retrieveCustomerById(Long customerId);

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public void updateCustomer(Customer customerToUpdate);

    public void deleteCustomer(Long customerId);

    public void updatePayment(Long customerId, String newCardNo);

    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;
    
}