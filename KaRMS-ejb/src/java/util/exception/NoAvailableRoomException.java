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
public class NoAvailableRoomException extends Exception {

    public NoAvailableRoomException() {
    }

    public NoAvailableRoomException(String message) {
        super(message);
    }

}
