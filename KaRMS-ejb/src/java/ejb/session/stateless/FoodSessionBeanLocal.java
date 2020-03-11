/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Food;
import java.util.List;
import javax.ejb.Local;
import util.exception.FoodNotFoundException;

/**
 *
 * @author longluqian
 */
@Local
public interface FoodSessionBeanLocal {

    public long createNewFood(Food newFood);

    public List<Food> retrieveAllFood();

    public Food retrieveFoodById(Long foodId);

    public Food retrieveFoodByFoodname(String foodName) throws FoodNotFoundException;

    public void updateFood(Food foodToUpdate);

    public void deleteFood(Long foodId);
    
}
