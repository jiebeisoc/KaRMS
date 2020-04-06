/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.FoodOrderTransaction;

/**
 *
 * @author longluqian
 */
public class CreateFoodOrderTransactionRsp {
    
    private Long foodOrderTransactionId;
  

    public CreateFoodOrderTransactionRsp() {
        
    }

    public CreateFoodOrderTransactionRsp(Long foodOrderTransactionId) {
        this.foodOrderTransactionId = foodOrderTransactionId;
    }

    public Long getFoodOrderTransactionId() {
        return foodOrderTransactionId;
    }

    public void setFoodOrderTransactionId(Long foodOrderTransactionId) {
        this.foodOrderTransactionId = foodOrderTransactionId;
    }

   
    
    
    
    
    
}
