/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import entity.Review;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zihua
 */
@Local
public interface ReviewSessionBeanLocal {

    public Long createNewReview(Review newReview);

    public Review retrieveReviewById(Long reviewId);

    public List<Review> viewAllReviews();

    public List<Review> viewReviewByOutlet(Outlet outlet);

    public void deleteReview(Long reviewId);
    
}
