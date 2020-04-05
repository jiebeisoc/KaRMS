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
public class CustomerUsernameExistException extends Exception {

    public CustomerUsernameExistException() {
    }

    public CustomerUsernameExistException(String message) {
        super(message);
    }
    
}
