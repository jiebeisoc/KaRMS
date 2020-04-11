package ejb.session.stateless;

import entity.FoodItem;
import entity.FoodItemCategory;

import entity.FoodOrderTransactionLineItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewFoodItemException;
import util.exception.DeleteFoodItemException;
import util.exception.FoodItemInsufficientQuantityOnHandException;
import util.exception.FoodItemNotFoundException;
import util.exception.InputDataValidationException;

import util.exception.UpdateFoodItemException;



@Stateless
@Local(FoodSessionBeanLocal.class)


public class FoodSessionBean implements FoodSessionBeanLocal
{
    
    

    @EJB
    private FoodItemCategorySessionBeanLocal foodCategorySessionBeanLocal;

   
    @EJB
    private FoodOrderSessionBeanLocal saleTransactionEntitySessionBeanLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;
 
    
    
    
    public FoodSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
  
    
    @Override
    public FoodItem createNewFoodItem(FoodItem newFoodItem, Long categoryId) throws InputDataValidationException, CreateNewFoodItemException, CategoryNotFoundException
    {
        Set<ConstraintViolation<FoodItem>>constraintViolations = validator.validate(newFoodItem);
        
        if(constraintViolations.isEmpty())
        {  
           
                if(categoryId == null)
                {
                    throw new CreateNewFoodItemException("The new food item must be associated a leaf category");
                }
                
                FoodItemCategory categoryEntity = foodCategorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
                
                if(!categoryEntity.getSubCategoryEntities().isEmpty())
                {
                    throw new CreateNewFoodItemException("Selected category for the new food item is not a leaf category");
                }
                
                em.persist(newFoodItem);
                newFoodItem.setCategoryEntity(categoryEntity);
                
               
                em.flush();

                return newFoodItem;
            }
            
        
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<FoodItem> retrieveAllFoodItems()
    {
       
        Query query = em.createQuery("SELECT f FROM FoodItem f ORDER BY f.skuCode ASC");        
        List<FoodItem> foodItems = query.getResultList();
        
        for(FoodItem foodItem:foodItems)
        {
            foodItem.getCategoryEntity();
           
        }
        
        return foodItems;
    }
    
    
    

    
    @Override
    public List<FoodItem> searchFoodItemsByName(String searchString)
    {
        Query query = em.createQuery("SELECT f FROM FoodItem f WHERE f.name LIKE :inSearchString ORDER BY f.name ASC");
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<FoodItem> foodItems = query.getResultList();
        
       for(FoodItem foodItem:foodItems)
        {
            foodItem.getCategoryEntity();
           
        }
        
        return foodItems;
    }
    
    
    
   
    
    @Override
    public List<FoodItem> filterFoodItemsByCategory(Long categoryId) throws CategoryNotFoundException
    {
        List<FoodItem> foodItems = new ArrayList<>();
        FoodItemCategory categoryEntity = foodCategorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
        
        if(categoryEntity.getSubCategoryEntities().isEmpty())
        {
            foodItems = categoryEntity.getFoodItems();            
        }
        else
        {
            for(FoodItemCategory subCategoryEntity:categoryEntity.getSubCategoryEntities())
            {
                foodItems.addAll(addSubCategoryFoodItems(subCategoryEntity));
            }
        }
        
        for(FoodItem productEntity:foodItems)
        {
            productEntity.getCategoryEntity();
           
        }
        
        Collections.sort(foodItems, new Comparator<FoodItem>()
            {
                public int compare(FoodItem pe1, FoodItem pe2) {
                    return pe1.getSkuCode().compareTo(pe2.getSkuCode());
                }
            });

        return foodItems;
    }
    
    
    
    
    
    @Override
    public FoodItem retrieveFoodItemById(Long foodItemId) throws FoodItemNotFoundException
    {
        FoodItem foodItem = em.find(FoodItem.class, foodItemId);
        
        if(foodItem != null)
        {
            foodItem.getCategoryEntity();
            
            return foodItem;
        }
        else
        {
            throw new FoodItemNotFoundException("Food Item ID " + foodItemId + " does not exist!");
        }               
    }
    
     @Override
    public FoodItem retrieveFoodItemBySkuCode(String skuCode) throws FoodItemNotFoundException
    {
        Query query = em.createQuery("SELECT p FROM FoodItem p WHERE p.skuCode = :inSkuCode");
        query.setParameter("inSkuCode", skuCode);
        
        try
        {
            FoodItem foodItem = (FoodItem)query.getSingleResult();            
            foodItem.getCategoryEntity();
          
            
            return foodItem;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FoodItemNotFoundException("Sku Code " + skuCode + " does not exist!");
        }
    }
    
    
    
    
    
    
    
    // Updated in v4.1
    // Updated in v4.2 to include reorderQuantity and bean validation
    // Updated in v5.0 to include association with new category entity
    // Updated in v5.1 with category entity and tag entity processing
    
    @Override
    public void updateFoodItem(FoodItem foodItem, Long categoryId) throws FoodItemNotFoundException, CategoryNotFoundException, UpdateFoodItemException, InputDataValidationException
    {
        if(foodItem != null && foodItem.getFoodItemId()!= null)
        {
            Set<ConstraintViolation<FoodItem>>constraintViolations = validator.validate(foodItem);
        
            if(constraintViolations.isEmpty())
            {
                FoodItem foodItemToUpdate = retrieveFoodItemById(foodItem.getFoodItemId());

            if(foodItemToUpdate.getSkuCode().equals(foodItem.getSkuCode()))
                {
               
                    if(categoryId != null && (!foodItemToUpdate.getCategoryEntity().getCategoryId().equals(categoryId)))
                    {
                        FoodItemCategory categoryEntityToUpdate = foodCategorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
                        
                        if(!categoryEntityToUpdate.getSubCategoryEntities().isEmpty())
                        {
                            throw new UpdateFoodItemException("Selected category for the new food item is not a leaf category");
                        }
                        
                        foodItemToUpdate.setCategoryEntity(categoryEntityToUpdate);
                    }
                    
                
                  
                    foodItemToUpdate.setName(foodItem.getName());
                    foodItemToUpdate.setDescription(foodItem.getDescription());
                    foodItemToUpdate.setQuantityOnHand(foodItem.getQuantityOnHand());
                    foodItemToUpdate.setUnitPrice(foodItem.getUnitPrice());
                    foodItemToUpdate.setFoodItemRating((foodItem.getFoodItemRating()));
               }
                else
                {
                    throw new UpdateFoodItemException("SKU Code of Food Item to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new FoodItemNotFoundException("Food Item ID not provided for food item to be updated");
        }
    }
    
    
    
    // Updated in v4.1
    // Updated in v5.0 to include disassociation with new category entity
    // Updated in v5.1 with category entity and tag entity processing
    
    @Override
    public void deleteFoodItem(Long foodItemId) throws FoodItemNotFoundException, DeleteFoodItemException
    {
        FoodItem foodItemToRemove = retrieveFoodItemById(foodItemId);
        
        List<FoodOrderTransactionLineItem> foodOrderTransactionLineItemEntities = saleTransactionEntitySessionBeanLocal.retrieveFoodOrderTransactionLineItemsByFoodItemId(foodItemId);
        
        if(foodOrderTransactionLineItemEntities.isEmpty())
        {
            foodItemToRemove.getCategoryEntity().getFoodItems().remove(foodItemToRemove);
            
            
            em.remove(foodItemToRemove);
        }
        else
        {
            throw new DeleteFoodItemException("Food Item ID " + foodItemId + " is associated with existing sale transaction line item(s) and cannot be deleted!");
        }
    }
    
    
    

    
    @Override
    public void debitQuantityOnHand(Long foodItemId, Integer quantityToDebit) throws FoodItemNotFoundException, FoodItemInsufficientQuantityOnHandException
    {
        FoodItem foodItem = retrieveFoodItemById(foodItemId);
        
        if(foodItem.getQuantityOnHand() >= quantityToDebit)
        {
            foodItem.setQuantityOnHand(foodItem.getQuantityOnHand() - quantityToDebit);
        }
        else
        {
            throw new FoodItemInsufficientQuantityOnHandException("Food Item " + foodItem.getSkuCode() + " quantity on hand is " + foodItem.getQuantityOnHand() + " versus quantity to debit of " + quantityToDebit);
        }
    }
    
    
 
    @Override
    public void creditQuantityOnHand(Long foodItemId, Integer quantityToCredit) throws FoodItemNotFoundException
    {
        FoodItem foodItem = retrieveFoodItemById(foodItemId);
        foodItem.setQuantityOnHand(foodItem.getQuantityOnHand() + quantityToCredit);
    }
    
    

    
    private List<FoodItem> addSubCategoryFoodItems(FoodItemCategory categoryEntity)
    {
        List<FoodItem> foodItems = new ArrayList<>();
                
        if(categoryEntity.getSubCategoryEntities().isEmpty())
        {
            return categoryEntity.getFoodItems();
        }
        else
        {
            for(FoodItemCategory subCategoryEntity:categoryEntity.getSubCategoryEntities())
            {
                foodItems.addAll(addSubCategoryFoodItems(subCategoryEntity));
            }
            
            return foodItems;
        }
    }
    
    
    
    // Added in v4.2
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FoodItem>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

  

  

  
}