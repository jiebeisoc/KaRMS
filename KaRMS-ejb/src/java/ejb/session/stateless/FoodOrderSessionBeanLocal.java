/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodOrder;
import java.util.List;
import javax.ejb.Local;
import util.exception.FoodOrderNotFoundException;

/**
 *
 * @author longluqian
 */
@Local
public interface FoodOrderSessionBeanLocal {

    public long createNewFoodOrder(FoodOrder newFoodOrder);

    public List<FoodOrder> retrieveAllFood();

    public FoodOrder retrieveFoodOrderById(Long foodOrderId);

    public FoodOrder retrieveFoodOrderByCustomerPhone(String customerPhoneNo) throws FoodOrderNotFoundException;

    public void updateFoodOrder(FoodOrder foodOrderToUpdate);

    public void deleteFoodOrder(Long foodOrderId);
    
}
