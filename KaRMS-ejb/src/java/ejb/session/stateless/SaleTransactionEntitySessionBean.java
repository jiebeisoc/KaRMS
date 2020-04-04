package ejb.session.stateless;

import entity.Customer;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
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
@Local(SaleTransactionEntitySessionBeanLocal.class)


public class SaleTransactionEntitySessionBean implements SaleTransactionEntitySessionBeanLocal
{       

   

    @EJB
     CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @EJB
    FoodSessionBeanLocal foodSessionBeanLocal;
    
    
    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager entityManager;
     
     
  
    public SaleTransactionEntitySessionBean()
    {
    }
    
    
    

    
    @Override
    public SaleTransactionEntity createNewSaleTransaction(Long customerId, SaleTransactionEntity newSaleTransactionEntity) throws CreateNewSaleTransactionException
    {
       
        if(newSaleTransactionEntity != null)
        {
            try
            {
                Customer customerEntity = customerSessionBeanLocal.retrieveCustomerById(customerId);
                newSaleTransactionEntity.setCustomerEntity(customerEntity);
                customerEntity.getSaleTransactionEntities().add(newSaleTransactionEntity);

                entityManager.persist(newSaleTransactionEntity);

                for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:newSaleTransactionEntity.getSaleTransactionLineItemEntities())
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
    public List<SaleTransactionEntity> retrieveAllSaleTransactions()
    {
        Query query = entityManager.createQuery("SELECT st FROM SaleTransactionEntity st");
        
        return query.getResultList();
    }
    
    @Override
    public List<SaleTransactionEntity> retrieveAllSaleTransactionsByCustomerID(String customerId)
    {
        Query query = entityManager.createQuery("SELECT st FROM SaleTransactionEntity st where st.customerEntity.customerId = :customerID");
        query.setParameter("customerID", customerId);
        
        List<SaleTransactionEntity> list = query.getResultList();
        for(SaleTransactionEntity se: list){
           se.getSaleTransactionLineItemEntities().size();    
        }
        return list;
    }
    
    
    
    
    
    
    // Added in v4.1
    
    @Override
    public List<SaleTransactionLineItemEntity> retrieveSaleTransactionLineItemsByProductId(Long productId)
    {
        Query query = entityManager.createNamedQuery("selectAllSaleTransactionLineItemsByProductId");
        query.setParameter("inProductId", productId);
        
        return query.getResultList();
    }
    
    
    
    @Override
    public SaleTransactionEntity retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException
    {
        SaleTransactionEntity saleTransactionEntity = entityManager.find(SaleTransactionEntity.class, saleTransactionId);
        
        if(saleTransactionEntity != null)
        {
            saleTransactionEntity.getSaleTransactionLineItemEntities().size();
            
            return saleTransactionEntity;
        }
        else
        {
            throw new SaleTransactionNotFoundException("Sale Transaction ID " + saleTransactionId + " does not exist!");
        }                
    }
    
    
    
    @Override
    public void updateSaleTransaction(SaleTransactionEntity saleTransactionEntity)
    {
        entityManager.merge(saleTransactionEntity);
    }
    
    
    
    // Updated in v4.1
    
    @Override
    public void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException
    {
        SaleTransactionEntity saleTransactionEntity = retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
        
        if(!saleTransactionEntity.getVoidRefund())
        {
            for(SaleTransactionLineItemEntity saleTransactionLineItemEntity:saleTransactionEntity.getSaleTransactionLineItemEntities())
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
    public void deleteSaleTransaction(SaleTransactionEntity saleTransactionEntity)
    {
        throw new UnsupportedOperationException();
    }

 
  


   
    
    
}
