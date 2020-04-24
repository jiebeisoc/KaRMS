/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.SongCategory;
import java.util.List;

/**
 *
 * @author chai
 */
public class RetrieveSongCategoryRsp {
   List<SongCategory> songCategories;

    public RetrieveSongCategoryRsp() {
    }
   
    public RetrieveSongCategoryRsp(List<SongCategory> songCategories) {
        this.songCategories = songCategories;
    }

    public List<SongCategory> getSongCategories() {
        return songCategories;
    }

    public void setSongCategories(List<SongCategory> songCategories) {
        this.songCategories = songCategories;
    }
    
   
}
