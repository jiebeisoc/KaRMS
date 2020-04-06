/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.FoodItem;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author longluqian
 */
@Named(value = "viewFoodItemManagedBean")
@ViewScoped
public class ViewFoodItemManagedBean implements Serializable{
private FoodItem foodItemToView;
    /**
     * Creates a new instance of viewFoodItemManagedBean
     */
    public ViewFoodItemManagedBean() {
        foodItemToView = new FoodItem();
    }
    
    @PostConstruct
    public void postConstruct(){
        
    }

    public FoodItem getFoodItemToView() {
        return foodItemToView;
    }

    public void setFoodItemToView(FoodItem foodItemToView) {
        this.foodItemToView = foodItemToView;
    }
    
    
}
