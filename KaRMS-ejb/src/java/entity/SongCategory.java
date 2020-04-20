/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author zihua
 */
@Entity
public class SongCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songCategoryId;
    @NotNull
    @Column(nullable = false)
    private String name;
    
    @ManyToMany(mappedBy = "songCategories")
    private List<Song> songs;

    public SongCategory() {
        this.songs = new ArrayList<>();
    }

    public SongCategory(String name) {
        this();
        this.name = name;
    }

    public Long getSongCategoryId() {
        return songCategoryId;
    }

    public void setSongCategoryId(Long songCategoryId) {
        this.songCategoryId = songCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (songCategoryId != null ? songCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the songCategoryId fields are not set
        if (!(object instanceof SongCategory)) {
            return false;
        }
        SongCategory other = (SongCategory) object;
        if ((this.songCategoryId == null && other.songCategoryId != null) || (this.songCategoryId != null && !this.songCategoryId.equals(other.songCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SongCategory[ id=" + songCategoryId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
    
}
