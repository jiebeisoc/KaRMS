/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Song;

/**
 *
 * @author zihua
 */
public class UpdateFavouritePlayListReq {
    
    private Song song;
    private Long customerId;

    public UpdateFavouritePlayListReq() {
    }

    public UpdateFavouritePlayListReq(Song song, Long customerId) {
        this.song = song;
        this.customerId = customerId;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

}
