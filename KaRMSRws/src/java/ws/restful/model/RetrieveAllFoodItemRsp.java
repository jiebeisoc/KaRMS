/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.FoodItem;
import java.util.List;

/**
 *
 * @author longluqian
 */
public class RetrieveAllFoodItemRsp {
    private List<FoodItem> foodItemEntities;

    public RetrieveAllFoodItemRsp() {
    }

    public RetrieveAllFoodItemRsp(List<FoodItem> foodItemEntities) {
        this.foodItemEntities = foodItemEntities;
    }

    public void setFoodItemEntities(List<FoodItem> foodItemEntities) {
        this.foodItemEntities = foodItemEntities;
    }

    public List<FoodItem> getFoodItemEntities() {
        return foodItemEntities;
    }
    
    
    
}
