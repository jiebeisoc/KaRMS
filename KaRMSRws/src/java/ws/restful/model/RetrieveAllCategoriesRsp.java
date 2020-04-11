/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.FoodItemCategory;
import java.util.List;

/**
 *
 * @author longluqian
 */
public class RetrieveAllCategoriesRsp {
    
   private List<FoodItemCategory> list;

    public RetrieveAllCategoriesRsp() {
    }

    public RetrieveAllCategoriesRsp(List<FoodItemCategory> list) {
        this.list = list;
    }

    public List<FoodItemCategory> getList() {
        return list;
    }

    public void setList(List<FoodItemCategory> list) {
        this.list = list;
    }
   
   
    
    
}
