/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Song;
import entity.SongCategory;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zihua
 */
@Local
public interface SongSessionBeanLocal {
    
    public Song retrieveSongById(Long songId);
    
    public List<Song> viewAllSongs();

    public List<Song> viewSongByCategory(List<SongCategory> category);

    public void addSongToQueue(Long songId, Long reservationId);

    public List<Song> viewSongBySinger(String singer);

    public List<Song> viewSongBySongTitle(String songTitle);




}
