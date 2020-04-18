/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Reservation;
import java.util.List;

/**
 *
 * @author chai
 */
public class RetrieveReservationRsp {
    
    private List<Reservation> reservations;

    public RetrieveReservationRsp() {
    }

    public RetrieveReservationRsp(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

}