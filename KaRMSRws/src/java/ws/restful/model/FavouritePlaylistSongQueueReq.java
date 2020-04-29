/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

/**
 *
 * @author zihua
 */
public class FavouritePlaylistSongQueueReq {
    
    private Long customerId;
    private Long reservationId;

    public FavouritePlaylistSongQueueReq() {
    }

    public FavouritePlaylistSongQueueReq(Long customerId, Long reservationId) {
        this.customerId = customerId;
        this.reservationId = reservationId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
    
    
}
