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
public class DeletePromotionException extends Exception {

    /**
     * Creates a new instance of <code>DeletePromotionException</code> without
     * detail message.
     */
    public DeletePromotionException() {
    }

    /**
     * Constructs an instance of <code>DeletePromotionException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeletePromotionException(String msg) {
        super(msg);
    }
}
