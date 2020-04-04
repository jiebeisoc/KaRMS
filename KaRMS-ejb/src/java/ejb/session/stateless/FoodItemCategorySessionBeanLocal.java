package ejb.session.stateless;


import entity.FoodItemCategory;
import java.util.List;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;



public interface FoodItemCategorySessionBeanLocal 
{
    FoodItemCategory createNewCategoryEntity(FoodItemCategory newCategoryEntity, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException;
    
    List<FoodItemCategory> retrieveAllCategories();
    
    List<FoodItemCategory> retrieveAllRootCategories();
    
    List<FoodItemCategory> retrieveAllLeafCategories();
    
    List<FoodItemCategory> retrieveAllCategoriesWithoutProduct();
    
    FoodItemCategory retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;
    
    void updateCategory(FoodItemCategory categoryEntity, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException;
    
    void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;
}
