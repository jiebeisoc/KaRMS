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
public class FoodNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FoodNotFoundException</code> without
     * detail message.
     */
    public FoodNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FoodNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FoodNotFoundException(String msg) {
        super(msg);
    }
}
