/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author zihua
 */
public class ExceedClosingHoursException extends Exception {

    public ExceedClosingHoursException() {
    }

    public ExceedClosingHoursException(String message) {
        super(message);
    }

}
