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
public class PromotionNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PromotionNotFoundException</code> without
     * detail message.
     */
    public PromotionNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PromotionNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PromotionNotFoundException(String msg) {
        super(msg);
    }
}
