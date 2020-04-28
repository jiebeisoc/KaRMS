package jsf.managedbean;

import ejb.session.stateless.FoodItemCategorySessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import entity.FoodItem;
import entity.FoodItemCategory;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewFoodItemException;
import util.exception.DeleteFoodItemException;
import util.exception.FoodItemNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateFoodItemException;



@Named
@ViewScoped

public class FoodItemManagedBean implements Serializable
{

    @EJB
    private FoodItemCategorySessionBeanLocal foodItemCategorySessionBeanLocal;

    @EJB
    private FoodSessionBeanLocal foodSessionBeanLocal;
     
    private List<FoodItem> foodItems;
    private List<FoodItem> filteredFoodItems;
    
    private FoodItem newFoodItem;
    private Long categoryIdNew;
    private List<FoodItemCategory> categoryEntities;
    
    private FoodItem selectedFoodItemToUpdate;
    private Long categoryIdUpdate;
    
  
    
    @Inject
    private ViewFoodItemManagedBean viewFoodItemManagedBean;

    
    
    
    public FoodItemManagedBean()
    {
        newFoodItem = new FoodItem();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
       
        foodItems = foodSessionBeanLocal.retrieveAllFoodItems();
        categoryEntities = foodItemCategorySessionBeanLocal.retrieveAllLeafCategories();
    
    }
    
    
    
    public void viewFoodItemDetails(ActionEvent event) throws IOException
    {
        FoodItem selectedFoodItemToView = (FoodItem)event.getComponent().getAttributes().get("selectedFoodItemToView");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedFoodItemToView", selectedFoodItemToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewFoodItemDetailsFoodOrder.xhtml");
    }
    
    
    
    public void createNewFoodItem(ActionEvent event)
    {        
        if(categoryIdNew == 0)
        {
            categoryIdNew = null;
        }                
        
        try
        {
            FoodItem fe = foodSessionBeanLocal.createNewFoodItem(newFoodItem, categoryIdNew);
            foodItems.add(fe);
            
            if(filteredFoodItems != null)
            {
                filteredFoodItems.add(fe);
            }
            
            newFoodItem = new FoodItem();
            categoryIdNew = null;
         
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New food item created successfully (Food Item ID: " + fe.getFoodItemId()+ ")", null));
        }
        catch(InputDataValidationException | CreateNewFoodItemException | CategoryNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new product: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void doUpdateFoodItem(ActionEvent event)
    {
        selectedFoodItemToUpdate = (FoodItem)event.getComponent().getAttributes().get("foodItemToUpdate");
        
        categoryIdUpdate = selectedFoodItemToUpdate.getCategoryEntity().getCategoryId();
        
    }
    
    
    
    public void updateFoodItem(ActionEvent event)
    {        
        if(categoryIdUpdate  == 0)
        {
            categoryIdUpdate = null;
        }                
        
        try
        {
            foodSessionBeanLocal.updateFoodItem(selectedFoodItemToUpdate, categoryIdUpdate);
                        
            for(FoodItemCategory ce:categoryEntities)
            {
                if(ce.getCategoryId().equals(categoryIdUpdate))
                {
                    selectedFoodItemToUpdate.setCategoryEntity(ce);
                    break;
                }                
            }
            
           

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Item updated successfully", null));
        }
        catch(FoodItemNotFoundException | CategoryNotFoundException |UpdateFoodItemException | InputDataValidationException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating food item: " + ex.getMessage(), null));
        }

    }
    
    
    
    public void deleteFoodItem(ActionEvent event)
    {
        try
        {
            FoodItem foodItemTodelete = (FoodItem)event.getComponent().getAttributes().get("foodItemToDelete");
            
            System.err.println("foodItemToDeleteID: "+foodItemTodelete.getFoodItemId());
            
            foodSessionBeanLocal.deleteFoodItem(foodItemTodelete.getFoodItemId());
            
            foodItems.remove(foodItemTodelete);
            
            if(filteredFoodItems != null)
            {
                filteredFoodItems.remove(foodItemTodelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Item deleted successfully", null));
        }
        catch(FoodItemNotFoundException | DeleteFoodItemException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting food item: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    
    
 
    
    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }



    
    public Long getCategoryIdNew() {
        return categoryIdNew;
    }

    public void setCategoryIdNew(Long categoryIdNew) {
        this.categoryIdNew = categoryIdNew;
    }


    public Long getCategoryIdUpdate() {
        return categoryIdUpdate;
    }

    public void setCategoryIdUpdate(Long categoryIdUpdate) {
        this.categoryIdUpdate = categoryIdUpdate;
    }


    public List<FoodItem> getFilteredFoodItems() {
        return filteredFoodItems;
    }

    public FoodItem getNewFoodItem() {
        return newFoodItem;
    }

    public FoodItem getSelectedFoodItemToUpdate() {
        return selectedFoodItemToUpdate;
    }

 
    public void setFilteredFoodItems(List<FoodItem> filteredFoodItems) {
        this.filteredFoodItems = filteredFoodItems;
    }

    public void setNewFoodItem(FoodItem newFoodItem) {
        this.newFoodItem = newFoodItem;
    }

    public void setCategoryEntities(List<FoodItemCategory> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public void setSelectedFoodItemToUpdate(FoodItem selectedFoodItemToUpdate) {
        this.selectedFoodItemToUpdate = selectedFoodItemToUpdate;
    }

    public List<FoodItemCategory> getCategoryEntities() {
        return categoryEntities;
    }

  

    public ViewFoodItemManagedBean getViewFoodItemManagedBean() {
        return viewFoodItemManagedBean;
    }

    public void setViewFoodItemManagedBean(ViewFoodItemManagedBean viewFoodItemManagedBean) {
        this.viewFoodItemManagedBean = viewFoodItemManagedBean;
    }

  


 
}