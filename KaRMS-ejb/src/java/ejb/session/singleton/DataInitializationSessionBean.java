/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import entity.FoodItemCategory;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author chai
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    
    
    
    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    public DataInitializationSessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstuct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("manager");
        } catch (EmployeeNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {
        
        System.err.println("********Reach Initialization Data*******************");
        employeeSessionBeanLocal.createNewEmployee(new Employee("manager", "password"));
        
        //Add FoodItem Categories
        FoodItemCategory foodItemCategory1 = new FoodItemCategory("Snacks","All snacks that you are craving");
        FoodItemCategory foodItemCategory2 = new FoodItemCategory("Potato Chips","All time favorites");
        FoodItemCategory foodItemCategory3 = new FoodItemCategory("Instant Noodle","Kids Favorites");
        
        List<FoodItemCategory> subCategoryEntities = new LinkedList<FoodItemCategory>();
        subCategoryEntities.add(foodItemCategory2);
        subCategoryEntities.add(foodItemCategory3);
        foodItemCategory1.setSubCategoryEntities(subCategoryEntities);
        
        foodItemCategory2.setParentCategoryEntity(foodItemCategory1);
        foodItemCategory3.setParentCategoryEntity(foodItemCategory1);
        
        
        FoodItemCategory foodItemCategory4 = new FoodItemCategory("Main Course","Our experienced chef provides authentic dishes");
           
        em.persist(foodItemCategory1);
        em.persist(foodItemCategory2);
        em.persist(foodItemCategory3);
        em.persist(foodItemCategory4);
        em.flush();
        
       
        
        
        
        
        
    }
}
