/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FoodOrderSessionBeanLocal;
import entity.Employee;
import entity.FoodOrderTransaction;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.FoodOrderTransactionNotFoundException;

/**
 *
 * @author longluqian
 */
@Named(value = "foodOrderTransactionManagedBean")
@ViewScoped
public class FoodOrderTransactionManagedBean implements Serializable{

    @EJB(name = "FoodOrderSessionBeanLocal")
    private FoodOrderSessionBeanLocal foodOrderSessionBeanLocal;

    private List<FoodOrderTransaction> foodOrderTransactionList;
    private FoodOrderTransaction selectedFoodOrderTransaction;
     
    
    
    public FoodOrderTransactionManagedBean() {
    
     }
    
    @PostConstruct
    public void PostConstruct(){
        Employee currentEmployee=(Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployee");
        this.foodOrderTransactionList=this.foodOrderSessionBeanLocal.retrieveAllFoodOrderTransactionsByOutletId(currentEmployee.getOutlet().getOutletId()); 
    }
    
    public void viewTransactionDetails(ActionEvent event){

        selectedFoodOrderTransaction = (FoodOrderTransaction)event.getComponent().getAttributes().get("transactionToView");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("transactionToView", selectedFoodOrderTransaction);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewFoodOrderTransactionDetails.xhtml");
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while checking transaction "+selectedFoodOrderTransaction.getFoodOrderTransactionId()+": " + ex.getMessage(), null));
        }
    }
    
   
    
    public void viewFoodOrderTransactionDetails(ActionEvent event){
     
        Long transactionToView = (Long)event.getComponent().getAttributes().get("transactionId");
        try {
            selectedFoodOrderTransaction = foodOrderSessionBeanLocal.retrieveFoodOrderTransactionByFoodOrderTransactionId(transactionToView);
        } catch (FoodOrderTransactionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While View Transaction Details: " + ex.getMessage(), null));
        }
        
    
    
    }

    public List<FoodOrderTransaction> getFoodOrderTransactionList() {
        return foodOrderTransactionList;
    }

    public void setFoodOrderTransactionList(List<FoodOrderTransaction> foodOrderTransactionList) {
        this.foodOrderTransactionList = foodOrderTransactionList;
    }

    public FoodOrderTransaction getSelectedFoodOrderTransaction() {
        return selectedFoodOrderTransaction;
    }

    public void setSelectedFoodOrderTransaction(FoodOrderTransaction selectedFoodOrderTransaction) {
        this.selectedFoodOrderTransaction = selectedFoodOrderTransaction;
    }
}
