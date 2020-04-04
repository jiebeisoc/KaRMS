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
public class FoodItemNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FoodItemNotFoundException</code> without
     * detail message.
     */
    public FoodItemNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FoodItemNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FoodItemNotFoundException(String msg) {
        super(msg);
    }
}
