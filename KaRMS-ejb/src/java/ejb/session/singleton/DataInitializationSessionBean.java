/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import entity.Employee;
import entity.FoodItem;
import entity.FoodItemCategory;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewFoodItemException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;

/**
 *
 * @author chai
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private FoodSessionBeanLocal foodSessionBeanLocal;

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
        
        try {
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
            
            FoodItem a = new FoodItem("FOOD001", "FoodItemA", "description for food item A", 10 ,BigDecimal.valueOf(3.4), 2);
            FoodItem b = new FoodItem("FOOD002", "FoodItemB", "description for food item B", 10 ,BigDecimal.valueOf(2.4), 3);
            FoodItem c = new FoodItem("FOOD003", "FoodItemC", "description for food item C", 10 ,BigDecimal.valueOf(3.0), 5);
            FoodItem d = new FoodItem("FOOD004", "FoodItemD", "description for food item D", 8 ,BigDecimal.valueOf(4.4), 4);
            FoodItem e = new FoodItem("FOOD005", "FoodItemE", "description for food item E", 20 ,BigDecimal.valueOf(3.2), 2);
            FoodItem f = new FoodItem("FOOD006", "FoodItemF", "description for food item F", 10 ,BigDecimal.valueOf(2.4), 3);
            FoodItem g = new FoodItem("FOOD007", "FoodItemG", "description for food item G", 11 ,BigDecimal.valueOf(5.0), 5);
            FoodItem h = new FoodItem("FOOD008", "FoodItemhz", "description for food item H", 30 ,BigDecimal.valueOf(4.4), 4);
            
            
            foodSessionBeanLocal.createNewFoodItem(a, 2L);
            foodSessionBeanLocal.createNewFoodItem(b, 2L);
            foodSessionBeanLocal.createNewFoodItem(c, 3L);
            foodSessionBeanLocal.createNewFoodItem(d, 4L);
            foodSessionBeanLocal.createNewFoodItem(e, 2L);
            foodSessionBeanLocal.createNewFoodItem(f, 3L);
            foodSessionBeanLocal.createNewFoodItem(g, 2L);
            foodSessionBeanLocal.createNewFoodItem(h, 4L);
            
            
            
            
            
            
            
            
        } catch (InputDataValidationException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CreateNewFoodItemException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CategoryNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
       
        
        
        
        
        
    }
}
