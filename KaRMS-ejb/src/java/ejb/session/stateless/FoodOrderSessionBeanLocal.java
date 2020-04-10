package ejb.session.stateless;

import entity.FoodOrderTransaction;
import entity.FoodOrderTransactionLineItem;
import java.util.List;
import util.exception.CreateNewSaleTransactionException;
import util.exception.SaleTransactionAlreadyVoidedRefundedException;
import util.exception.SaleTransactionNotFoundException;




public interface FoodOrderSessionBeanLocal
{
    FoodOrderTransaction createNewFoodOrderTransaction(Long staffId, FoodOrderTransaction newSaleTransactionEntity) throws CreateNewSaleTransactionException;

    List<FoodOrderTransaction> retrieveAllSaleTransactions();
    
    List<FoodOrderTransactionLineItem> retrieveSaleTransactionLineItemsByProductId(Long productId);

    FoodOrderTransaction retrieveSaleTransactionBySaleTransactionId(Long saleTransactionId) throws SaleTransactionNotFoundException;
    
    void updateSaleTransaction(FoodOrderTransaction saleTransactionEntity);

    void voidRefundSaleTransaction(Long saleTransactionId) throws SaleTransactionNotFoundException, SaleTransactionAlreadyVoidedRefundedException;
    
    void deleteSaleTransaction(FoodOrderTransaction saleTransactionEntity); 

    public List<FoodOrderTransaction> retrieveAllFoodOrderTransactionsByCustomerID(Long customerId );
}
