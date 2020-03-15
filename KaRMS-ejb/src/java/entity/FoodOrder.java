/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.FoodOrderStatus;


/**
 *
 * @author longluqian
 */
@Entity
public class FoodOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodOrderId;
    @NotNull
    @Column(nullable = false)
    private double totalPrice;
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCreated;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private FoodOrderStatus status;
    
    @OneToMany
    private List<Food> foodItems;
    
    @ManyToOne
    private Customer customer;

    public FoodOrder() {
        this.status = FoodOrderStatus.BOOKED;
        this.foodItems = new LinkedList<Food>();
    }
    
    public Long getFoodOrderId() {
        return foodOrderId;
    }

    public void setFoodOrderId(Long foodOrderId) {
        this.foodOrderId = foodOrderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (foodOrderId != null ? foodOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the foodOrderId fields are not set
        if (!(object instanceof FoodOrder)) {
            return false;
        }
        FoodOrder other = (FoodOrder) object;
        if ((this.foodOrderId == null && other.foodOrderId != null) || (this.foodOrderId != null && !this.foodOrderId.equals(other.foodOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FoodOrder[ id=" + foodOrderId + " ]";
    }

    public double getTotalPrice() {
       
        return totalPrice;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Food> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<Food> foodItems) {
        this.foodItems = foodItems;
    }

    public FoodOrderStatus getStatus() {
        return status;
    }

    public void setStatus(FoodOrderStatus status) {
        this.status = status;
    }
    
}
