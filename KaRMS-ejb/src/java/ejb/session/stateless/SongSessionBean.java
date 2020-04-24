/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import entity.Song;
import entity.SongCategory;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author zihua
 */
@Stateless
public class SongSessionBean implements SongSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
   
    @Override
    public Song retrieveSongById(Long songId) {
        Song song = em.find(Song.class, songId);
        
        return song;
    }
    
    @Override
    public List<Song> viewAllSongs() {
        Query query = em.createQuery("SELECT s FROM Song s");
        
        return query.getResultList();
    }

    @Override
    public List<Song> viewSongByCategory(Long categoryId) {
        
        SongCategory songCategory = em.find(SongCategory.class, categoryId);           
        
        List<Song> songs = viewAllSongs();
        
        List<Song> filteredSongs = new ArrayList<>();
        
        for (Song s: songs) {
            if (s.getSongCategories().contains(songCategory)) {
                filteredSongs.add(s);            
            }
        }
        
        return filteredSongs;
    }

    @Override
    public void addSongToQueue(Long songId, Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        List<Song> songQueue = reservation.getSongQueue();
        
        Song songToAdd = em.find(Song.class, songId);
        songQueue.add(songToAdd);
    }

    @Override
    public List<Song> viewSongBySinger(String singer) {
        Query query = em.createQuery("SELECT s FROM Song s WHERE s.singer = :inSinger");
        query.setParameter("inSinger", singer);
        
        return query.getResultList();
    }

    @Override
    public List<Song> viewSongBySongTitle(String songTitle) {
        Query query = em.createQuery("SELECT s FROM Song s WHERE songTitle = :inSongTitle");
        query.setParameter("inSongTitle", songTitle);
        
        return query.getResultList();
    }
    
    @Override
    public Long createNewSong(Song newSong, List<Long> songCategoryIds) {
        em.persist(newSong);
        em.flush();
        
        for (Long id: songCategoryIds) {
            SongCategory songCategory = em.find(SongCategory.class, id);
            newSong.getSongCategories().add(songCategory);
            songCategory.getSongs().add(newSong);
        }      
        
        return newSong.getSongId();
    }

    @Override
    public List<String> retrieveSingers() {
        Query query = em.createQuery("SELECT DISTINCT s.singer FROM Song s");
        
        return query.getResultList();
    }
}
