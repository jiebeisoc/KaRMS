/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Promotion;
import java.util.List;
import javax.ejb.Local;
import util.exception.PromotionNotFoundException;

/**
 *
 * @author longluqian
 */
@Local
public interface PromotionSessionBeanLocal {

    public Promotion createNewPromotion(Promotion newPromotion);

    public List<Promotion> retrieveAllPromotion();

    public Promotion retrievePromotionById(Long promotionId);

    public Promotion retrievePromotionByName(String promotionName) throws PromotionNotFoundException;

    public void updatePromotion(Promotion promotionToUpdate);

    public void deletePromotion(Long promotionId);
    
}
