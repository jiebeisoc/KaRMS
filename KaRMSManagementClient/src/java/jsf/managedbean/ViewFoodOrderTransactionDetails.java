/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FoodOrderSessionBeanLocal;
import entity.FoodOrderTransaction;
import entity.FoodOrderTransactionLineItem;
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
import util.exception.FoodOrderTransactionAlreadyVoidedRefundedException;
import util.exception.FoodOrderTransactionNotFoundException;

/**
 *
 * @author longluqian
 */
@Named(value = "viewFoodOrderTransactionDetails")
@ViewScoped
public class ViewFoodOrderTransactionDetails implements Serializable{

    @EJB(name = "FoodOrderSessionBeanLocal")
    private FoodOrderSessionBeanLocal foodOrderSessionBeanLocal;

    
    private FoodOrderTransaction selectedFoodOrderTransaction;
    private String backMode;
    private List<FoodOrderTransactionLineItem> lineItemList;
    
   
    
    /**
     * Creates a new instance of ViewFoodOrderTransactionDetails
     */
    public ViewFoodOrderTransactionDetails() {
    }
    
    @PostConstruct
    public void postConstruct(){
        selectedFoodOrderTransaction = (FoodOrderTransaction)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("transactionToView");
        backMode = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("backMode");
        lineItemList=selectedFoodOrderTransaction.getFoodOrderTransactionLineItemEntities();
     
    }
    
     public void back(ActionEvent event) throws IOException
    {
        if(backMode == null || backMode.trim().length() == 0)
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect("viewFoodOrderTransactions.xhtml");
        }
        else
        {
            FacesContext.getCurrentInstance().getExternalContext().redirect(backMode + ".xhtml");
        }
    }
     
      public void confirmTransaction(ActionEvent event){
        Long transactionIdToConfirm = selectedFoodOrderTransaction.getFoodOrderTransactionId();
        try {
            foodOrderSessionBeanLocal.confirmFoodOrderTransaction(transactionIdToConfirm);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Confirm Transaction successfully!", null));
        } catch (FoodOrderTransactionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error while confirm transaction: " + ex.getMessage(), null));
        }
    }
    
    public void servedFoodOrder(ActionEvent event){
          Long transactionIdToServe = selectedFoodOrderTransaction.getFoodOrderTransactionId();
        try {
            foodOrderSessionBeanLocal.servedFoodOrder(transactionIdToServe);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Order Served successfully!", null));
        } catch (FoodOrderTransactionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Serve Food Order: " + ex.getMessage(), null));
        }
        
    }
    
    public void refundFoodOrder(ActionEvent event){
        Long transactionIdToRefund = selectedFoodOrderTransaction.getFoodOrderTransactionId();
        try {
            foodOrderSessionBeanLocal.voidRefundFoodOrderTransaction(transactionIdToRefund);
        } catch (FoodOrderTransactionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Food Order Refunded successfully!", null));
        } catch (FoodOrderTransactionAlreadyVoidedRefundedException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Rrefund Food Order: " + ex.getMessage(), null));
        }
    }

    public FoodOrderTransaction getSelectedFoodOrderTransaction() {
        return selectedFoodOrderTransaction;
    }

    public void setSelectedFoodOrderTransaction(FoodOrderTransaction selectedFoodOrderTransaction) {
        this.selectedFoodOrderTransaction = selectedFoodOrderTransaction;
    }

    public List<FoodOrderTransactionLineItem> getLineItemList() {
        return lineItemList;
    }

    public void setLineItemList(List<FoodOrderTransactionLineItem> lineItemList) {
        this.lineItemList = lineItemList;
    }

   
    
}
