package ejb.session.stateless;

import entity.FoodItemCategory;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;



@Stateless
@Local(FoodItemCategorySessionBeanLocal.class)




public class FoodItemCategorySessionBean implements FoodItemCategorySessionBeanLocal
{
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

     @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager entityManager;
    
    
    public FoodItemCategorySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public FoodItemCategory createNewCategoryEntity(FoodItemCategory newCategoryEntity, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException
    {
        Set<ConstraintViolation<FoodItemCategory>>constraintViolations = validator.validate(newCategoryEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                if(parentCategoryId != null)
                {
                    FoodItemCategory parentCategoryEntity = retrieveCategoryByCategoryId(parentCategoryId);

                    if(!parentCategoryEntity.getFoodItems().isEmpty())
                    {
                        throw new CreateNewCategoryException("Parent category cannot be associated with any food items");
                    }

                    newCategoryEntity.setParentCategoryEntity(parentCategoryEntity);
                }
                
                entityManager.persist(newCategoryEntity);
                entityManager.flush();

                return newCategoryEntity;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewCategoryException("Category with same name already exist");
                }
                else
                {
                    throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<FoodItemCategory> retrieveAllCategories()
    {
        Query query = entityManager.createQuery("SELECT c FROM FoodItemCategory c ORDER BY c.name ASC");
        List<FoodItemCategory> categoryEntities = query.getResultList();
        
        for(FoodItemCategory categoryEntity:categoryEntities)
        {
            categoryEntity.getParentCategoryEntity();
            categoryEntity.getSubCategoryEntities().size();
            categoryEntity.getFoodItems().size();
        }
        
        return categoryEntities;
    }
    
    
    
    @Override
    public List<FoodItemCategory> retrieveAllRootCategories()
    {
        Query query = entityManager.createQuery("SELECT c FROM FoodItemCategory c WHERE c.parentCategoryEntity IS NULL ORDER BY c.name ASC");
        List<FoodItemCategory> rootCategoryEntities = query.getResultList();
        
        for(FoodItemCategory rootCategoryEntity:rootCategoryEntities)
        {            
            lazilyLoadSubCategories(rootCategoryEntity);
            
            rootCategoryEntity.getFoodItems().size();
        }
        
        return rootCategoryEntities;
    }
    
    
    
    @Override
    public List<FoodItemCategory> retrieveAllLeafCategories()
    {
        Query query = entityManager.createQuery("SELECT c FROM FoodItemCategory c WHERE c.subCategoryEntities IS EMPTY ORDER BY c.name ASC");
        List<FoodItemCategory> leafCategoryEntities = query.getResultList();
        
        for(FoodItemCategory leafCategoryEntity:leafCategoryEntities)
        {            
            leafCategoryEntity.getFoodItems().size();
        }
        
        return leafCategoryEntities;
    }
    
    
    
    @Override
    public List<FoodItemCategory> retrieveAllCategoriesWithoutProduct()
    {
        Query query = entityManager.createQuery("SELECT c FROM FoodItemCategory c WHERE c.foodItems IS EMPTY ORDER BY c.name ASC");
        List<FoodItemCategory> rootCategoryEntities = query.getResultList();
        
        for(FoodItemCategory rootCategoryEntity:rootCategoryEntities)
        {
            rootCategoryEntity.getParentCategoryEntity();            
        }
        
        return rootCategoryEntities;
    }
    
    
    
    @Override
    public FoodItemCategory retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException
    {
        FoodItemCategory categoryEntity = entityManager.find(FoodItemCategory.class, categoryId);
        
        if(categoryEntity != null)
        {
            return categoryEntity;
        }
        else
        {
            throw new CategoryNotFoundException("Category ID " + categoryId + " does not exist!");
        }               
    }
    
    
    
    @Override
    public void updateCategory(FoodItemCategory categoryEntity, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException
    {
        Set<ConstraintViolation<FoodItemCategory>> constraintViolations = validator.validate(categoryEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(categoryEntity.getCategoryId()!= null)
            {
                FoodItemCategory categoryEntityToUpdate = retrieveCategoryByCategoryId(categoryEntity.getCategoryId());
                
                Query query = entityManager.createQuery("SELECT c FROM FoodItemCategory c WHERE c.name = :inName AND c.categoryId <> :inCategoryId");
                query.setParameter("inName", categoryEntity.getName());
                query.setParameter("inCategoryId", categoryEntity.getCategoryId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateCategoryException("The name of the category to be updated is duplicated");
                }
                
                categoryEntityToUpdate.setName(categoryEntity.getName());
                categoryEntityToUpdate.setDescription(categoryEntity.getDescription());                               
                
                if(parentCategoryId != null)
                {
                    if(categoryEntityToUpdate.getCategoryId().equals(parentCategoryId))
                    {
                        throw new UpdateCategoryException("Category cannot be its own parent");
                    }
                    else if(categoryEntityToUpdate.getParentCategoryEntity() == null || (!categoryEntityToUpdate.getParentCategoryEntity().getCategoryId().equals(parentCategoryId)))
                    {
                        FoodItemCategory parentCategoryEntityToUpdate = retrieveCategoryByCategoryId(parentCategoryId);
                        
                        if(!parentCategoryEntityToUpdate.getFoodItems().isEmpty())
                        {
                            throw new UpdateCategoryException("Parent category cannot have any product associated with it");
                        }
                        
                        categoryEntityToUpdate.setParentCategoryEntity(parentCategoryEntityToUpdate);
                    }
                }
                else
                {
                    categoryEntityToUpdate.setParentCategoryEntity(null);
                }                
            }
            else
            {
                throw new CategoryNotFoundException("Category ID not provided for category to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException
    {
        FoodItemCategory categoryEntityToRemove = retrieveCategoryByCategoryId(categoryId);
        
        if(!categoryEntityToRemove.getSubCategoryEntities().isEmpty())
        {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing sub-categories and cannot be deleted!");
        }
        else if(!categoryEntityToRemove.getFoodItems().isEmpty())
        {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing products and cannot be deleted!");
        }
        else
        {
            categoryEntityToRemove.setParentCategoryEntity(null);
            
            entityManager.remove(categoryEntityToRemove);
        }                
    }
    
    
    
    private void lazilyLoadSubCategories(FoodItemCategory categoryEntity)
    {
        for(FoodItemCategory ce:categoryEntity.getSubCategoryEntities())
        {
            lazilyLoadSubCategories(ce);
        }
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FoodItemCategory>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
