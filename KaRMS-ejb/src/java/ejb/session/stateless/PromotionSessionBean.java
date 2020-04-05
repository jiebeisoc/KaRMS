/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Promotion;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.PromotionNotFoundException;

/**
 *
 * @author longluqian
 */
@Stateless
public class PromotionSessionBean implements PromotionSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

  @Override
    public long createNewPromotion(Promotion newPromotion){
        em.persist(newPromotion);
        em.flush();
        
        return newPromotion.getPromotionId();
    }
    
    @Override
    public List<Promotion> retrieveAllPromotion() {
        Query query = em.createQuery("SELECT f FROM Promotion f");
        
        return query.getResultList();
    }
 
    @Override
    public Promotion retrievePromotionById(Long promotionId) {
       Promotion promotion = em.find(Promotion.class, promotionId);
       
       return promotion;
    }
    
    @Override
    public Promotion retrievePromotionByName(String promotionName) throws PromotionNotFoundException {
        Query query = em.createQuery("SELECT p FROM Promotion p WHERE p.promotionName = :promotionName");
        query.setParameter("promotionName", promotionName);
        
        try {
            return (Promotion)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PromotionNotFoundException("Promotion " + promotionName + "does not exist!");
        }
    }
    
    @Override
    public List<Promotion> retrievePromotionByDate(Date date) {
        Query query = em.createQuery("SELECT p FROM Promotion p WHERE p.validFrom <= :inDate AND p.validUntil >= :inDate");
        query.setParameter("inDate", date);
        
        return query.getResultList();
    }
    
    @Override
    public void updatePromotion(Promotion promotionToUpdate) {
        em.merge(promotionToUpdate);
        em.flush();
    }
    
    @Override
    public void deletePromotion(Long promotionId) {
        Promotion promotionToDelete = retrievePromotionById(promotionId);
        
        em.remove(promotionToDelete);
    }
    
    
    


}
