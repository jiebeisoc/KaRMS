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
public class UpdateFoodItemException extends Exception {

    /**
     * Creates a new instance of <code>UpdateFoodItemException</code> without
     * detail message.
     */
    public UpdateFoodItemException() {
    }

    /**
     * Constructs an instance of <code>UpdateFoodItemException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateFoodItemException(String msg) {
        super(msg);
    }
}
