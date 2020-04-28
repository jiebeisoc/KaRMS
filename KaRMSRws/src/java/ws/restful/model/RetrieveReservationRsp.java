/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Reservation;

/**
 *
 * @author zihua
 */
public class RetrieveReservationRsp {
    
    private Reservation reservation;

    public RetrieveReservationRsp() {
    }

    public RetrieveReservationRsp(Reservation reservation) {
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
}