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
public class SaleTransactionNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>SaleTransactionNotFoundException</code>
     * without detail message.
     */
    public SaleTransactionNotFoundException() {
    }

    /**
     * Constructs an instance of <code>SaleTransactionNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SaleTransactionNotFoundException(String msg) {
        super(msg);
    }
}
