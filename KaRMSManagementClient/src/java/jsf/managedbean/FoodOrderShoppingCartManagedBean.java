/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.FoodOrderSessionBeanLocal;
import ejb.session.stateless.FoodSessionBeanLocal;
import entity.Employee;
import entity.FoodItem;
import entity.FoodOrderTransaction;
import entity.FoodOrderTransactionLineItem;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.enumeration.FoodOrderStatus;
import util.exception.CreateNewFoodOrderTransactionException;
import util.exception.FoodItemNotFoundException;

/**
 *
 * @author longluqian
 */
@Named(value = "foodOrderShoppingCartManagedBean")
@SessionScoped
public class FoodOrderShoppingCartManagedBean implements Serializable {

    @EJB(name = "FoodSessionBeanLocal")
    private FoodSessionBeanLocal foodSessionBeanLocal;

    @EJB(name = "FoodOrderSessionBeanLocal")
    private FoodOrderSessionBeanLocal foodOrderSessionBeanLocal;

    private List<FoodOrderTransactionLineItem> foodOrderTransactionLineItemList;
    private Integer totalLineItem;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private String creditCardNo;
  

    private Long customerId;
    private Integer selectedQuantity;
    private FoodOrderTransactionLineItem selectedLineItemToUpdate;

    public FoodOrderShoppingCartManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        foodOrderTransactionLineItemList = new ArrayList<>();
        totalLineItem = 0;
        totalQuantity = 0;
        setTotalAmount(new BigDecimal("0.00"));
        creditCardNo="0000 0000 0000 0000";
    }

    public void addToCart(ActionEvent event) {
        try {
            Long foodItemId = (Long) event.getComponent().getAttributes().get("foodItemId");
            FoodItem foodItem = foodSessionBeanLocal.retrieveFoodItemById(foodItemId);
            //Testing
            System.out.println("FoodItem ID: " + foodItemId);
            System.out.println("Quantity: " + selectedQuantity);

            boolean exist = false;
            for (FoodOrderTransactionLineItem f : foodOrderTransactionLineItemList) {
                if (Objects.equals(f.getFoodItem().getFoodItemId(), foodItemId)) {
                    exist = true;
                    f.setQuantity(selectedQuantity + f.getQuantity());
                    BigDecimal subtotalPlus = foodItem.getUnitPrice().multiply(new BigDecimal(selectedQuantity));
                    f.setSubTotal(subtotalPlus.add(f.getSubTotal()));
                    totalAmount = totalAmount.add(subtotalPlus);
                    totalQuantity += selectedQuantity;
                    selectedQuantity = 0;
                    break;
                }
            }
            if (exist == false) {
                totalLineItem++;
                BigDecimal subtotal = foodItem.getUnitPrice().multiply(new BigDecimal(selectedQuantity));
                foodOrderTransactionLineItemList.add(new FoodOrderTransactionLineItem(foodItem, selectedQuantity, foodItem.getUnitPrice(), subtotal));
                totalQuantity += selectedQuantity;
                setTotalAmount(getTotalAmount().add(subtotal));
                selectedQuantity = 0;

            }

        } catch (FoodItemNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Food Item Not Found: " + ex.getMessage(), null));
        }

    }

    public void doUpdateQuantity(ActionEvent event) {
        Long foodItemId = (Long) event.getComponent().getAttributes().get("foodItemId");
        for (FoodOrderTransactionLineItem f : foodOrderTransactionLineItemList) {
            if (f.getFoodItem().getFoodItemId() == foodItemId) {
                this.selectedLineItemToUpdate = f;
            }
        }
    }

    public void updateQuantity(ActionEvent event) {
        if (selectedLineItemToUpdate != null) {
            //update subtotal
            //update total amount;
            //update total quantity
            selectedLineItemToUpdate.setSubTotal(new BigDecimal(this.selectedLineItemToUpdate.getQuantity()).multiply(this.selectedLineItemToUpdate.getUnitPrice()));
            this.totalAmount = new BigDecimal(0.00);
            this.totalQuantity = 0;
            for (FoodOrderTransactionLineItem f : foodOrderTransactionLineItemList) {
                this.totalAmount = this.totalAmount.add(f.getSubTotal());
                this.totalQuantity += f.getQuantity();
            }
        }
    }

    public void deleteFoodTransactionLineItem(ActionEvent event) {
        Long foodItemId = (Long) event.getComponent().getAttributes().get("foodItemId");
        for (FoodOrderTransactionLineItem f : foodOrderTransactionLineItemList) {
            if (f.getFoodItem().getFoodItemId() == foodItemId) {
                totalAmount=totalAmount.subtract(f.getSubTotal());
                totalLineItem--;
                totalQuantity-=f.getQuantity();
                this.foodOrderTransactionLineItemList.remove(f);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted successfully", null));
                break;
            }

        }

    }
    
    public void doCheckout(ActionEvent event){
     Employee currentEmployee=(Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployee");

     FoodOrderTransaction newFoodOrderTransactionEntity = new FoodOrderTransaction();
     
     newFoodOrderTransactionEntity.setTotalAmount(totalAmount);
     newFoodOrderTransactionEntity.setFoodOrderTransactionLineItemEntities(foodOrderTransactionLineItemList);
     newFoodOrderTransactionEntity.setOutlet(currentEmployee.getOutlet());
     newFoodOrderTransactionEntity.setTotalLineItem(totalLineItem);
     newFoodOrderTransactionEntity.setTotalQuantity(totalQuantity);
 
        try {
            FoodOrderTransaction newFoodOrderTransaction = this.foodOrderSessionBeanLocal.createNewFoodOrderTransaction(null,newFoodOrderTransactionEntity);
            clearCart();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Transaction "+newFoodOrderTransaction.getFoodOrderTransactionId() +"Created Successfully", null));
            
        
        } catch (CreateNewFoodOrderTransactionException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to Create New Food Order Transaction : " + ex.getMessage(), null));
        }
           
    }
    
    public void checkoutPage(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("checkout.xhtml");
    }
    
    public void clearCart(){
        foodOrderTransactionLineItemList = new ArrayList<>();
        totalLineItem = 0;
        totalQuantity = 0;
        setTotalAmount(new BigDecimal("0.00"));
        creditCardNo="0000 0000 0000 0000";
        
    }

    public List<FoodOrderTransactionLineItem> getFoodOrderTransactionLineItemList() {
        return foodOrderTransactionLineItemList;
    }

    public void setFoodOrderTransactionLineItemList(List<FoodOrderTransactionLineItem> foodOrderTransactionLineItemList) {
        this.foodOrderTransactionLineItemList = foodOrderTransactionLineItemList;
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(Integer selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public FoodOrderTransactionLineItem getSelectedFoodOrderTransaction() {
        return selectedLineItemToUpdate;
    }

    public void setSelectedFoodOrderTransaction(FoodOrderTransactionLineItem selectedLineItemToUpdate) {
        this.selectedLineItemToUpdate = selectedLineItemToUpdate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public FoodOrderTransactionLineItem getSelectedLineItemToUpdate() {
        return selectedLineItemToUpdate;
    }

    public void setSelectedLineItemToUpdate(FoodOrderTransactionLineItem selectedLineItemToUpdate) {
        this.selectedLineItemToUpdate = selectedLineItemToUpdate;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

}
