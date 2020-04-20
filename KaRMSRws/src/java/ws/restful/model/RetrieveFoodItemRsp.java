/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.FoodItem;

/**
 *
 * @author longluqian
 */
public class RetrieveFoodItemRsp {
    private FoodItem foodItem;

    public RetrieveFoodItemRsp() {
    }

    public RetrieveFoodItemRsp(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }
    
    
}
