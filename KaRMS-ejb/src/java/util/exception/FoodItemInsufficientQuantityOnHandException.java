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
public class FoodItemInsufficientQuantityOnHandException extends Exception {

    /**
     * Creates a new instance of
     * <code>FoodItemInsufficientQuantityOnHandException</code> without detail
     * message.
     */
    public FoodItemInsufficientQuantityOnHandException() {
    }

    /**
     * Constructs an instance of
     * <code>FoodItemInsufficientQuantityOnHandException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FoodItemInsufficientQuantityOnHandException(String msg) {
        super(msg);
    }
}
