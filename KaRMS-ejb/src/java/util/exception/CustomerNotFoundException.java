/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author chai
 */
public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
