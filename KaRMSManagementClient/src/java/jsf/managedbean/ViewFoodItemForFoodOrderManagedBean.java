/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.FoodItem;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author longluqian
 */
@Named(value = "viewFoodItemForFoodOrderManagedBean")
@ViewScoped
public class ViewFoodItemForFoodOrderManagedBean implements Serializable{
    
    private FoodItem foodItemToView;
    private String backMode;
    
    /**
     * Creates a new instance of ViewFoodItemForFoodOrderManagedBean
     */
    public ViewFoodItemForFoodOrderManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
    foodItemToView = (FoodItem)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("selectedFoodItemToView");
    backMode = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("backMode");
    }
    
     public void back(ActionEvent event) throws IOException
    {
        if(backMode == null || backMode.trim().length() == 0)
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect("browseAllFoodItems.xhtml");
        }
        else
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect(backMode + ".xhtml");
        }
    }

    public FoodItem getFoodItemToView() {
        return foodItemToView;
    }

    public void setFoodItemToView(FoodItem foodItemToView) {
        this.foodItemToView = foodItemToView;
    }

 
    
    
}
