/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FoodItem;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewFoodItemException;
import util.exception.DeleteFoodItemException;
import util.exception.FoodItemInsufficientQuantityOnHandException;
import util.exception.FoodItemNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateFoodItemException;

/**
 *
 * @author longluqian
 */
@Local
public interface FoodSessionBeanLocal {



    public void creditQuantityOnHand(Long foodItemId, Integer quantityToCredit) throws FoodItemNotFoundException;

    public FoodItem createNewFoodItem(FoodItem newFoodItem, Long categoryId) throws InputDataValidationException, CreateNewFoodItemException, CategoryNotFoundException;

    public List<FoodItem> retrieveAllFoodItems();

    public List<FoodItem> searchFoodItemsByName(String searchString);

    public List<FoodItem> filterFoodItemsByCategory(Long categoryId) throws CategoryNotFoundException;

    public FoodItem retrieveFoodItemById(Long foodItemId) throws FoodItemNotFoundException;

    public FoodItem retrieveFoodItemBySkuCode(String skuCode) throws FoodItemNotFoundException;

    public void updateFoodItem(FoodItem foodItem, Long categoryId) throws FoodItemNotFoundException, CategoryNotFoundException, UpdateFoodItemException, InputDataValidationException;

    public void deleteFoodItem(Long foodItemId) throws FoodItemNotFoundException, DeleteFoodItemException;

    public void debitQuantityOnHand(Long foodItemId, Integer quantityToDebit) throws FoodItemNotFoundException, FoodItemInsufficientQuantityOnHandException;
    
}
