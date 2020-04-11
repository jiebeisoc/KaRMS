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
public class CreateNewFoodOrderTransactionException extends Exception {

    /**
     * Creates a new instance of
     * <code>CreateNewFoodOrderTransactionException</code> without detail
     * message.
     */
    public CreateNewFoodOrderTransactionException() {
    }

    /**
     * Constructs an instance of
     * <code>CreateNewFoodOrderTransactionException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewFoodOrderTransactionException(String msg) {
        super(msg);
    }
}
