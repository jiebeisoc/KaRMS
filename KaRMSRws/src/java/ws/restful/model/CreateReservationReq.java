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
public class CreateReservationReq {
    
    private String username;
    private String password;
    private Long roomId;
    private Long outletId;
    private Long promotionId;
    private String status;
    private Reservation newReservation;

    public CreateReservationReq() {
    }

    public CreateReservationReq(String username, String password, Long roomId, Long outletId, Long promotionId, int pointsRedeemed, String status) {
        
        this.username = username;
        this.password = password;
        this.roomId = roomId;
        this.outletId = outletId;
        this.promotionId = promotionId;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reservation getNewReservation() {
        return newReservation;
    }

    public void setNewReservation(Reservation newReservation) {
        this.newReservation = newReservation;
    }
    
}
