/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Reservation;
import entity.Song;

/**
 *
 * @author zihua
 */
public class UpdateSongQueueReq {
    
    private Song song;
    private Reservation reservation;

    public UpdateSongQueueReq() {
    }

    public UpdateSongQueueReq(Song song, Reservation reservation) {
        this.song = song;
        this.reservation = reservation;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
    
}
