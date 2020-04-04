/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author zihua
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
   @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {      
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (Employee)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee username " + username + " does not exist!");
        }
    }

    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        
        if(username.isEmpty() || password.isEmpty()) {
            throw new InvalidLoginCredentialException("Missing Login Credential!");
        } else {
            try {
                Employee employee = retrieveEmployeeByUsername(username);
                
                if (employee.getPassword().equals(password)) {
                    return employee;
                } else {
                    throw new InvalidLoginCredentialException("Password is incorrect!");
                }
                
            } catch (EmployeeNotFoundException ex) {
                throw new InvalidLoginCredentialException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Long createNewEmployee(Employee newEmployee) {
        em.persist(newEmployee);
        em.flush();
        
        return newEmployee.getEmployeeId();
    }    
    
}
