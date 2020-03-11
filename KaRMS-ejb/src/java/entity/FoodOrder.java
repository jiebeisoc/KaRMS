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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long FoodOrderId;
  
    private double totalPrice;
    
    @OneToMany
    private List<Food> foodItems;
    
    private Date timeCreated;
    
    @ManyToOne
    private Customer customer;
    
    private boolean confirmedByStaff;
    
    private boolean finished;

    public FoodOrder() {
        this.foodItems = new LinkedList<Food>();
    }

    
    public Long getFoodOrderId() {
        return FoodOrderId;
    }

    public void setFoodOrderId(Long FoodOrderId) {
        this.FoodOrderId = FoodOrderId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (FoodOrderId != null ? FoodOrderId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the FoodOrderId fields are not set
        if (!(object instanceof FoodOrder)) {
            return false;
        }
        FoodOrder other = (FoodOrder) object;
        if ((this.FoodOrderId == null && other.FoodOrderId != null) || (this.FoodOrderId != null && !this.FoodOrderId.equals(other.FoodOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FoodOrder[ id=" + FoodOrderId + " ]";
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

    public boolean isConfirmedByStaff() {
        return confirmedByStaff;
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

    public void setConfirmedByStaff(boolean confirmedByStaff) {
        this.confirmedByStaff = confirmedByStaff;
    }

    public List<Food> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<Food> foodItems) {
        this.foodItems = foodItems;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
}
