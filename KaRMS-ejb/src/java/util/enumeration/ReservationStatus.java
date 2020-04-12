/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.enumeration;

/**
 *
 * @author zihua
 */
public enum ReservationStatus {
    PAID("Paid"),
    NOTPAID("NotPaid"),
    COMPLETED("Completed");
    
    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
}
