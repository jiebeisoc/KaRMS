package ejb.session.stateless;

import entity.Customer;
import entity.FoodOrderTransaction;
import entity.FoodOrderTransactionLineItem;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewSaleTransactionException;
import util.exception.FoodItemInsufficientQuantityOnHandException;
import util.exception.FoodItemNotFoundException;
import util.exception.SaleTransactionAlreadyVoidedRefundedException;
import util.exception.SaleTransactionNotFoundException;



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
    
    
    

    
    @Override
    public FoodOrderTransaction createNewFoodOrderTransaction(Long customerId, FoodOrderTransaction newSaleTransactionEntity) throws CreateNewSaleTransactionException
    {
       
        if(newSaleTransactionEntity != null)
        {
            try
            {
                Customer customerEntity = customerSessionBeanLocal.retrieveCustomerById(customerId);
                newSaleTransactionEntity.setCustomerEntity(customerEntity);
                customerEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);

                entityManager.persist(newSaleTransactionEntity);

                for(FoodOrderTransactionLineItem saleTransactionLineItemEntity:newSaleTransactionEntity.getFoodOrderTransactionLineItemEntities())
                {
                    foodSessionBeanLocal.debitQuantityOnHand(saleTransactionLineItemEntity.getFoodItem().getFoodItemId(), saleTransactionLineItemEntity.getQuantity());
                    entityManager.persist(saleTransactionLineItemEntity);
                }

                entityManager.flush();

                return newSaleTransactionEntity;
            }
            catch(FoodItemNotFoundException | FoodItemInsufficientQuantityOnHandException ex)
            {
                // The line below rolls back all changes made to the database.
               // eJBContext.setRollbackOnly();
                throw new CreateNewSaleTransactionException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewSaleTransactionException("Sale transaction information not provided");
        }
    }
    
    
    
    @Override
    public List<FoodOrderTransaction> retrieveAllSaleTransactions()
    {
        Query query = entityManager.createQuery("SELECT st FROM SaleTransactionEntity st");
        
        return query.getResultList();
    }
    
    @Override
    public List<FoodOrderTransaction> retrieveAllFoodOrderTransactionsByCustomerID(Long customerId)
    {
        Query query = entityManager.createQuery("SELECT st FROM SaleTransactionEntity st where st.customerEntity.customerId = :customerID");
        query.setParameter("customerID", customerId);
        
        List<FoodOrderTransaction> list = query.getResultList();
        for(FoodOrderTransaction se: list){
           se.getFoodOrderTransactionLineItemEntities().size();    
        }
        return list;
    }
    
    
    
    
    
    
    // Added in v4.1
    
    @Override
    public List<FoodOrderTransactionLineItem> retrieveSaleTransactionLineItemsByProductId(Long productId)
    {
        Query query = entityManager.createNamedQuery("selectAllSaleTransactionLineItemsByProductId");
        query.setParameter("inProductId", productId);
        
        return query.getResultList();
    }
    
    
    
    @Override
    public FoodOrderTransaction retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException
    {
        FoodOrderTransaction saleTransactionEntity = entityManager.find(FoodOrderTransaction.class, saleTransactionId);
        
        if(saleTransactionEntity != null)
        {
            saleTransactionEntity.getFoodOrderTransactionLineItemEntities().size();
            
            return saleTransactionEntity;
        }
        else
        {
            throw new SaleTransactionNotFoundException("Sale Transaction ID " + saleTransactionId + " does not exist!");
        }                
    }
    
    
    
    @Override
    public void updateSaleTransaction(FoodOrderTransaction saleTransactionEntity)
    {
        entityManager.merge(saleTransactionEntity);
    }
    
    
    
    // Updated in v4.1
    
    @Override
    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException
    {
        FoodOrderTransaction saleTransactionEntity = retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
        
        if(!saleTransactionEntity.getVoidRefund())
        {
            for(FoodOrderTransactionLineItem saleTransactionLineItemEntity:saleTransactionEntity.getFoodOrderTransactionLineItemEntities())
            {
                try
                {
                    foodSessionBeanLocal.creditQuantityOnHand(saleTransactionLineItemEntity.getFoodItem().getFoodItemId(), saleTransactionLineItemEntity.getQuantity());
                }
                catch(FoodItemNotFoundException ex)
                {
                    ex.printStackTrace(); // Ignore exception since this should not happen
                }                
            }
            
            saleTransactionEntity.setVoidRefund(true);
        }
        else
        {
            throw new SaleTransactionAlreadyVoidedRefundedException("The sale transaction has aready been voided/refunded");
        }
    }
    
    
    
    @Override
    public void deleteSaleTransaction(FoodOrderTransaction saleTransactionEntity)
    {
        throw new UnsupportedOperationException();
    }

 
  


   
    
    
}
