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
public class UpdateReservationReq {

    private String username;
    private String password;
    private Long roomId;
    private Long outletId;
    private Long promotionId;
    private Reservation Reservation; 

    public UpdateReservationReq() {
    }

    public UpdateReservationReq(String username, String password, Long roomId, Long outletId, Long promotionId) {
        this.username = username;
        this.password = password;
        this.roomId = roomId;
        this.outletId = outletId;
        this.promotionId = promotionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Reservation getReservation() {
        return Reservation;
    }

    public void setReservation(Reservation Reservation) {
        this.Reservation = Reservation;
    }
  
}
