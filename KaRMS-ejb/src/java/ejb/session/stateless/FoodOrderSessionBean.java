package ejb.session.stateless;

import entity.Customer;
import entity.FoodItem;
import entity.FoodOrderTransaction;
import entity.FoodOrderTransactionLineItem;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewFoodOrderTransactionException;
import util.exception.FoodItemInsufficientQuantityOnHandException;
import util.exception.FoodItemNotFoundException;
import util.exception.FoodOrderTransactionAlreadyVoidedRefundedException;
import util.exception.FoodOrderTransactionNotFoundException;



@Stateless
@Local(FoodOrderSessionBeanLocal.class)


public class FoodOrderSessionBean implements FoodOrderSessionBeanLocal
{       

   

    @EJB
     CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @EJB
    FoodSessionBeanLocal foodSessionBeanLocal;
    
    
    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager entityManager;
     
     
  
    public FoodOrderSessionBean()
    {
    }
    
    /**
     *
     * @param customerId
     * @param newFoodOrderTransactionEntity
     * @return
     * @throws CreateNewFoodOrderTransactionException
     */
    @Override
    public FoodOrderTransaction createNewFoodOrderTransaction(Long customerId, FoodOrderTransaction newFoodOrderTransactionEntity) throws CreateNewFoodOrderTransactionException
    {
       
        if(newFoodOrderTransactionEntity != null)
        {
            try
            {
                Customer customerEntity = customerSessionBeanLocal.retrieveCustomerById(customerId);
                newFoodOrderTransactionEntity.setCustomerEntity(customerEntity);
                customerEntity.getFoodOrderTransactionEntities().add(newFoodOrderTransactionEntity);

                entityManager.persist(newFoodOrderTransactionEntity);

                for(FoodOrderTransactionLineItem foodOrderTransactionLineItemEntity:newFoodOrderTransactionEntity.getFoodOrderTransactionLineItemEntities())
                {
                    
                    long foodItemId= foodOrderTransactionLineItemEntity.getFoodItem().getFoodItemId();
                    FoodItem foodItem = entityManager.find(FoodItem.class, foodItemId);
                    foodOrderTransactionLineItemEntity.setFoodItem(foodItem);
                    foodSessionBeanLocal.debitQuantityOnHand(foodOrderTransactionLineItemEntity.getFoodItem().getFoodItemId(), foodOrderTransactionLineItemEntity.getQuantity());
                    entityManager.persist(foodOrderTransactionLineItemEntity);
                }

                entityManager.flush();

                return newFoodOrderTransactionEntity;
            }
            catch(FoodItemNotFoundException | FoodItemInsufficientQuantityOnHandException ex)
            {
              
                throw new CreateNewFoodOrderTransactionException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewFoodOrderTransactionException("Food Order transaction information not provided");
        }
    }
    
    
    
    @Override
    public List<FoodOrderTransaction> retrieveAllFoodOrderTransactions()
    {
        Query query = entityManager.createQuery("SELECT ft FROM FoodOrderTransaction ft");
        
        return query.getResultList();
    }
    
    @Override
    public List<FoodOrderTransaction> retrieveAllFoodOrderTransactionsByCustomerID(Long customerId)
    {
        Query query = entityManager.createQuery("SELECT ft FROM FoodOrderTransaction ft where ft.customerEntity.customerId = :customerID");
        query.setParameter("customerID", customerId);
        
        List<FoodOrderTransaction> list = query.getResultList();
        for(FoodOrderTransaction se: list){
           se.getFoodOrderTransactionLineItemEntities().size();    
        }
        return list;
    }
    
    
    
    
    
    
    // Added in v4.1
    
    @Override
    public List<FoodOrderTransactionLineItem> retrieveFoodOrderTransactionLineItemsByFoodItemId(Long foodItemId)
    {
        Query query = entityManager.createNamedQuery("selectAllFoodOrderTransactionLineItemsByFoodItemId");
        query.setParameter("inProductId", foodItemId);
        
        return query.getResultList();
    }
    
    
    
    @Override
    public FoodOrderTransaction retrieveFoodOrderTransactionByFoodOrderTransactionId(Long foodOrderTransactionId) throws FoodOrderTransactionNotFoundException
    {
        FoodOrderTransaction foodOrderTransactionEntity = entityManager.find(FoodOrderTransaction.class, foodOrderTransactionId);
        
        if(foodOrderTransactionEntity != null)
        {
            foodOrderTransactionEntity.getFoodOrderTransactionLineItemEntities().size();
            
            return foodOrderTransactionEntity;
        }
        else
        {
            throw new FoodOrderTransactionNotFoundException("Food Order Transaction ID " + foodOrderTransactionId + " does not exist!");
        }                
    }
    
    
    
    @Override
    public void updateFoodOrderTransaction(FoodOrderTransaction foodOrderTransactionEntity)
    {
        entityManager.merge(foodOrderTransactionEntity);
    }
    
    
    
    // Updated in v4.1
    
    @Override
    public void voidRefundFoodOrderTransaction(Long foodOrderTransactionId) throws FoodOrderTransactionNotFoundException, FoodOrderTransactionAlreadyVoidedRefundedException
    {
        FoodOrderTransaction foodOrderTransactionEntity = retrieveFoodOrderTransactionByFoodOrderTransactionId(foodOrderTransactionId);
        
        if(!foodOrderTransactionEntity.getVoidRefund())
        {
            for(FoodOrderTransactionLineItem foodOrderTransactionLineItemEntity:foodOrderTransactionEntity.getFoodOrderTransactionLineItemEntities())
            {
                try
                {
                    foodSessionBeanLocal.creditQuantityOnHand(foodOrderTransactionLineItemEntity.getFoodItem().getFoodItemId(), foodOrderTransactionLineItemEntity.getQuantity());
                }
                catch(FoodItemNotFoundException ex)
                {
                    ex.printStackTrace();
                }                
            }
            
            foodOrderTransactionEntity.setVoidRefund(true);
        }
        else
        {
            throw new FoodOrderTransactionAlreadyVoidedRefundedException("The food order transaction has aready been voided/refunded");
        }
    }
    
    
    
    public void deleteFoodOrderTransaction(FoodOrderTransaction foodOrderTransactionEntity)
    {
        throw new UnsupportedOperationException();
    }

 
  


   
    
    
}
