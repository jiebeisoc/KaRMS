/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author user
 */
public class MemberNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>MemberNotFoundException</code> without
     * detail message.
     */
    public MemberNotFoundException() {
    }

    /**
     * Constructs an instance of <code>MemberNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public MemberNotFoundException(String msg) {
        super(msg);
    }
}
