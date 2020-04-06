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
    
     private Long customerId;
     private String username;
     private String password;
    private FoodOrderTransaction newFoodOrderTransaction;

    public CreateFoodOrderTransactionReq() {
        
    }

    public CreateFoodOrderTransactionReq(Long customerId, String username, String password, FoodOrderTransaction newFoodOrderTransaction) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.newFoodOrderTransaction = newFoodOrderTransaction;
    }

  

   
    public FoodOrderTransaction getNewFoodOrderTransaction() {
        return newFoodOrderTransaction;
    }

   

    public void setNewFoodOrderTransaction(FoodOrderTransaction newFoodOrderTransaction) {
        this.newFoodOrderTransaction = newFoodOrderTransaction;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    
    
}
