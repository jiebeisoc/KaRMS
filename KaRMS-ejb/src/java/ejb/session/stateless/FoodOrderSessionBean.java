/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodOrder;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.FoodOrderStatus;
import util.exception.FoodOrderNotFoundException;

/**
 *
 * @author longluqian
 */
@Stateless
public class FoodOrderSessionBean implements FoodOrderSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

  
    @Override
    public long createNewFoodOrder(FoodOrder newFoodOrder){
        em.persist(newFoodOrder);
        em.flush();
        
        return newFoodOrder.getFoodOrderId();
    }
    
    @Override
    public List<FoodOrder> retrieveAllFood() {
        Query query = em.createQuery("SELECT f FROM FoodOrder f");
        
        return query.getResultList();
    }
 
    @Override
    public FoodOrder retrieveFoodOrderById(Long foodOrderId) {
       FoodOrder foodOrder = em.find(FoodOrder.class, foodOrderId);
       
       return foodOrder;
    }
    
    @Override
    public FoodOrder retrieveFoodOrderByCustomerPhone(String customerPhoneNo) throws FoodOrderNotFoundException{
        Query query = em.createQuery("SELECT f FROM FoodOrder f WHERE f.customer.phoneNo = :customerPhoneNo");
        query.setParameter("customerPhoneNo",customerPhoneNo);
        
        return (FoodOrder)query.getSingleResult();
    }
    
    @Override
    public void updateFoodOrder(FoodOrder foodOrderToUpdate) {
        em.merge(foodOrderToUpdate);
        em.flush();
    }
    
    @Override
    public void deleteFoodOrder(Long foodOrderId) {
        FoodOrder customerToDelete = retrieveFoodOrderById(foodOrderId);
        
        em.remove(customerToDelete);
    }
    
    public List<FoodOrder> viewAllPendingFoodOrders(){
        Query query = em.createQuery("SELECT po FROM FoodOrder po WHERE po.confirmedByStaff = false");
        return query.getResultList();
    }
    
    public FoodOrderStatus confirmFoodOrder(Long foodOrderId){
        FoodOrder foodOrder = em.find(FoodOrder.class, foodOrderId);
        foodOrder.setStatus(FoodOrderStatus.CONFIRMED);
        
        em.flush();
        return foodOrder.getStatus();     
    }

    
 
    
   
}

    
    
    
    
    

