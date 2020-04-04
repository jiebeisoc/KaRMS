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
public class DeleteFoodItemException extends Exception {

    /**
     * Creates a new instance of <code>DeleteFoodItemException</code> without
     * detail message.
     */
    public DeleteFoodItemException() {
    }

    /**
     * Constructs an instance of <code>DeleteFoodItemException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteFoodItemException(String msg) {
        super(msg);
    }
}
