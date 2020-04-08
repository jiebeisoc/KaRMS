/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.FoodOrderTransaction;
import java.util.List;

/**
 *
 * @author longluqian
 */
public class RetrievePastFoodOrderTransactionRsp {
    
    private List<FoodOrderTransaction> foodOrderTransactionList;

    public RetrievePastFoodOrderTransactionRsp() {
    }

    public RetrievePastFoodOrderTransactionRsp(List<FoodOrderTransaction> foodOrderTransactionList) {
        this.foodOrderTransactionList = foodOrderTransactionList;
    }

    public List<FoodOrderTransaction> getFoodOrderTransactionList() {
        return foodOrderTransactionList;
    }

    public void setFoodOrderTransactionList(List<FoodOrderTransaction> foodOrderTransactionList) {
        this.foodOrderTransactionList = foodOrderTransactionList;
    }
    
    
}
