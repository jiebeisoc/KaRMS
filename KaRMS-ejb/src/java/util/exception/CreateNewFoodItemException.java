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
public class CreateNewFoodItemException extends Exception {

    /**
     * Creates a new instance of <code>CreateNewFoodItemException</code> without
     * detail message.
     */
    public CreateNewFoodItemException() {
    }

    /**
     * Constructs an instance of <code>CreateNewFoodItemException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CreateNewFoodItemException(String msg) {
        super(msg);
    }
}
