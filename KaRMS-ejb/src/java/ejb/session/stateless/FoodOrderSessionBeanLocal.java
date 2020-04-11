package ejb.session.stateless;

import entity.FoodOrderTransaction;
import entity.FoodOrderTransactionLineItem;
import java.util.List;
import util.exception.CreateNewFoodOrderTransactionException;
import util.exception.FoodOrderTransactionAlreadyVoidedRefundedException;
import util.exception.FoodOrderTransactionNotFoundException;




public interface FoodOrderSessionBeanLocal
{

    public FoodOrderTransaction createNewFoodOrderTransaction(Long customerId, FoodOrderTransaction newFoodOrderTransactionEntity) throws CreateNewFoodOrderTransactionException;

    public List<FoodOrderTransaction> retrieveAllFoodOrderTransactions();

    public List<FoodOrderTransaction> retrieveAllFoodOrderTransactionsByCustomerID(Long customerId);

    public List<FoodOrderTransactionLineItem> retrieveFoodOrderTransactionLineItemsByFoodItemId(Long foodItemId);

    public FoodOrderTransaction retrieveFoodOrderTransactionByFoodOrderTransactionId(Long foodOrderTransactionId) throws FoodOrderTransactionNotFoundException;

    public void updateFoodOrderTransaction(FoodOrderTransaction foodOrderTransactionEntity);

    public void voidRefundFoodOrderTransaction(Long foodOrderTransactionId) throws FoodOrderTransactionNotFoundException, FoodOrderTransactionAlreadyVoidedRefundedException;
   
}
