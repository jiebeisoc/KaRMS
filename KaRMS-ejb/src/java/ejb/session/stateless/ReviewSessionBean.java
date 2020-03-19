/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import entity.Review;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author zihua
 */
@Stateless
public class ReviewSessionBean implements ReviewSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Long createNewReview(Review newReview) {
        em.persist(newReview);
        em.flush();
        
        return newReview.getReviewId();
    }
    
    @Override
    public Review retrieveReviewById(Long reviewId) {
        Review review = em.find(Review.class, reviewId);
        
        return review;
    }
    
    @Override
    public List<Review> viewAllReviews() {
        Query query = em.createQuery("SELECT r FROM Review r");
        
        return query.getResultList();
    }

    @Override
    public List<Review> viewReviewByOutlet(Outlet outlet) {
        Query query = em.createQuery("SELECT r FROM Review r WHERE outlet = :inOutlet");
        query.setParameter("inOutlet", outlet);
        
        return query.getResultList();
    }

    @Override
    public void deleteReview(Long reviewId) {
        Review reviewToDelete = retrieveReviewById(reviewId);
        
        em.remove(reviewToDelete);
    }

}
