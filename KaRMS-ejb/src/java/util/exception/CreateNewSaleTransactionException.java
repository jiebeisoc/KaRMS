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
public class CreateNewSaleTransactionException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewSaleTransactionException</code>
     * without detail message.
     */
    public CreateNewSaleTransactionException() {
    }

    /**
     * Constructs an instance of <code>CreateNewSaleTransactionException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewSaleTransactionException(String msg) {
        super(msg);
    }
}
