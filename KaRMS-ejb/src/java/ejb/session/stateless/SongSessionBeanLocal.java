/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Song;
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

    public List<Song> viewSongByCategory(Long categoryId);
    
    public List<Song> viewFavouritePlaylistByCategory(Long customerId, Long categoryId);
    
    public List<Song> viewSongQueueByCategory(Long reservationId, Long categoryId);
    
    public List<Song> viewSongBySinger(String singer);

    public List<Song> viewSongBySongTitle(String songTitle);

    public Long createNewSong(Song newSong, List<Long> songCategoryIds);

    public List<String> retrieveSingers();

}
