/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Song;
import java.util.List;

/**
 *
 * @author chai
 */
public class RetrieveSongsRsp {
    
    List<Song> songs;

    public RetrieveSongsRsp() {
    }

    public RetrieveSongsRsp(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
    
    
    
}
