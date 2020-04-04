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
public class SaleTransactionAlreadyVoidedRefundedException extends Exception {

    /**
     * Creates a new instance of
     * <code>SaleTransactionAlreadyVoidedRefundedException</code> without detail
     * message.
     */
    public SaleTransactionAlreadyVoidedRefundedException() {
    }

    /**
     * Constructs an instance of
     * <code>SaleTransactionAlreadyVoidedRefundedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public SaleTransactionAlreadyVoidedRefundedException(String msg) {
        super(msg);
    }
}
