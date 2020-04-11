/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author longluqian
 */
public class FoodOrderTransactionAlreadyVoidedRefundedException extends Exception {

    /**
     * Creates a new instance of
     * <code>FoodOrderTransactionAlreadyVoidedRefundedException</code> without
     * detail message.
     */
    public FoodOrderTransactionAlreadyVoidedRefundedException() {
    }

    /**
     * Constructs an instance of
     * <code>FoodOrderTransactionAlreadyVoidedRefundedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FoodOrderTransactionAlreadyVoidedRefundedException(String msg) {
        super(msg);
    }
}
