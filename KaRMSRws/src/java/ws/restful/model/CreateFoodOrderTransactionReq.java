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
public class CreateFoodOrderTransactionReq {
    
     
     private FoodOrderTransaction newFoodOrderTransaction;
     
     private Long customerId;
     private String username;
     private String password;
 
    public CreateFoodOrderTransactionReq() {
        
    }

    public CreateFoodOrderTransactionReq(FoodOrderTransaction newFoodOrderTransaction, Long customerId, String username, String password) {
        this.newFoodOrderTransaction = newFoodOrderTransaction;
        this.customerId = customerId;
        this.username = username;
        this.password = password;
    }

   

  

   
    public FoodOrderTransaction getNewFoodOrderTransaction() {
        return newFoodOrderTransaction;
    }

   

    public void setNewFoodOrderTransaction(FoodOrderTransaction newFoodOrderTransaction) {
        this.newFoodOrderTransaction = newFoodOrderTransaction;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   
    
}
