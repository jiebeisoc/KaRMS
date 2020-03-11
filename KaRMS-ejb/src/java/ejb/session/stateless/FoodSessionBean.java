/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Food;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.FoodNotFoundException;

/**
 *
 * @author longluqian
 */
@Stateless
public class FoodSessionBean implements FoodSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    
    @Override
    public long createNewFood(Food newFood){
        em.persist(newFood);
        em.flush();
        
        return newFood.getFoodId();
    }
    
    @Override
    public List<Food> retrieveAllFood() {
        Query query = em.createQuery("SELECT f FROM Food f");
        
        return query.getResultList();
    }
 
    @Override
    public Food retrieveFoodById(Long foodId) {
       Food food = em.find(Food.class, foodId);
       
       return food;
    }
    
    @Override
    public Food retrieveFoodByFoodname(String foodName) throws FoodNotFoundException {
        Query query = em.createQuery("SELECT f FROM Food f WHERE f.username = :inUsername");
        query.setParameter("inUsername", foodName);
        
        try {
            return (Food)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FoodNotFoundException("Food " + foodName + "does not exist!");
        }
    }
    
    @Override
    public void updateFood(Food foodToUpdate) {
        em.merge(foodToUpdate);
        em.flush();
    }
    
    @Override
    public void deleteFood(Long foodId) {
        Food customerToDelete = retrieveFoodById(foodId);
        
        em.remove(customerToDelete);
    }
    
 
    
   
}

    
    
    
    
    
