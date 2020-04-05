/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CreateCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author chai
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewCustomer(Customer newCustomer) throws CustomerUsernameExistException, CreateCustomerException {
        if (newCustomer.getBirthday().after(new Date())) {
            throw new CreateCustomerException("Invalid birtday!");
        }
        try {
            em.persist(newCustomer);
            em.flush();
            
        } catch (PersistenceException ex) {
            throw new CustomerUsernameExistException();
        }
        
        return newCustomer.getCustomerId();
    }
    
    @Override
    public List<Customer> retrieveAllCustomer() {
        Query query = em.createQuery("SELECT c FROM Customer c");
        
        return query.getResultList();
    }
    
    @Override
    public Customer retrieveCustomerById(Long customerId) {
       Customer customer = em.find(Customer.class, customerId);
       
       return customer;
    }
    
    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (Customer)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer username " + username + " does not exist!");
        }
    }
    
    @Override
    public Customer retrieveCustomerByMemberNum(Long memberNum) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.memberNum = :inMemberNum");
        query.setParameter("inMemberNum", memberNum);
        
        try {
            return (Customer)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer member number " + memberNum + " does not exist!");
        }
    }
    
    @Override
    public void updateCustomer(Customer customerToUpdate) {
        em.merge(customerToUpdate);
        em.flush();
    }
    
    @Override
    public void deleteCustomer(Long customerId) {
        Customer customerToDelete = retrieveCustomerById(customerId);

        em.remove(customerToDelete);
    }
    
    @Override
    public void updatePayment(Long customerId, String newCardNo) {
        Customer customerToUpdate = retrieveCustomerById(customerId);
        
        customerToUpdate.setCreditCardNo(newCardNo);
    }
    
    @Override
    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException {
        if (username.isEmpty() || password.isEmpty()) {
            throw new InvalidLoginCredentialException("Missing Login Credential!");
        } else {
            try {
                Customer customer = retrieveCustomerByUsername(username);
                
                if (customer.getPassword().equals(password)) {
                    return customer;
                } else {
                    throw new InvalidLoginCredentialException("Password is incorrect!");
                }
            } catch (CustomerNotFoundException ex) {
                throw new InvalidLoginCredentialException(ex.getMessage());
            }
        }
    }
    
    @Override
    public void addPoints(Long customerId, int pointsToAdd) {
        Customer customer = retrieveCustomerById(customerId);
        
        customer.setPoints(customer.getPoints() + pointsToAdd);
    }
    
    @Override
    public void redeemPoints(Long customerId, int pointsToDeduct) {
        Customer customer = retrieveCustomerById(customerId);
        
        customer.setPoints(customer.getPoints() - pointsToDeduct);
    }
}
