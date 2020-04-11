package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import util.enumeration.FoodOrderStatus;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;



@Entity

public class FoodOrderTransaction implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodOrderTransactionId;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalLineItem;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalQuantity;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal totalAmount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date transactionDateTime;    
    @OneToMany
    private List<FoodOrderTransactionLineItem> foodOrderTransactionLineItemEntities;    
    @Column(nullable = false)
    @NotNull
    private Boolean voidRefund;
     @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private FoodOrderStatus foodOrderStatus;
    
  
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Customer customerEntity;

    
    
    public FoodOrderTransaction()
    {
        foodOrderTransactionLineItemEntities = new ArrayList<>();
        voidRefund = false;
    }
    
    
    
    public FoodOrderTransaction(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date transactionDateTime, Boolean voidRefund)
    {
        this();
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.voidRefund = voidRefund;        
    }

    
    
    public FoodOrderTransaction(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date transactionDateTime, List<FoodOrderTransactionLineItem> saleTransactionLineItemEntities, Boolean voidRefund)
    {
        this();
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.foodOrderTransactionLineItemEntities = saleTransactionLineItemEntities;        
        this.voidRefund = voidRefund;        
    }

    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.foodOrderTransactionId != null ? this.foodOrderTransactionId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof FoodOrderTransaction)) 
        {
            return false;
        }
        
        FoodOrderTransaction other = (FoodOrderTransaction) object;
        
        if ((this.foodOrderTransactionId == null && other.foodOrderTransactionId != null) || (this.foodOrderTransactionId != null && !this.foodOrderTransactionId.equals(other.foodOrderTransactionId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.SaleTransactionEntity[ saleTransactionId=" + this.foodOrderTransactionId + " ]";
    }
    
    
    
    public void addSaleTransactionLineItemEntity(FoodOrderTransactionLineItem saleTransactionLineItemEntity) throws EntityInstanceExistsInCollectionException
    {
        if(!this.foodOrderTransactionLineItemEntities.contains(saleTransactionLineItemEntity))
        {
            this.foodOrderTransactionLineItemEntities.add(saleTransactionLineItemEntity);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("Sale Transaction Line Item already exist");
        }
    }
    
    
    
    public void removeSaleTransactionLineItemEntity(FoodOrderTransactionLineItem saleTransactionLineItemEntity) throws EntityInstanceMissingInCollectionException
    {
        if(this.foodOrderTransactionLineItemEntities.contains(saleTransactionLineItemEntity))
        {
            this.foodOrderTransactionLineItemEntities.remove(saleTransactionLineItemEntity);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("Sale Transaction Line Item missing");
        }
    }
    
    
    
    public Long getFoodOrderTransactionId() {
        return foodOrderTransactionId;
    }

    public void setFoodOrderTransactionId(Long foodOrderTransactionId) {
        this.foodOrderTransactionId = foodOrderTransactionId;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public List<FoodOrderTransactionLineItem> getFoodOrderTransactionLineItemEntities() {
        foodOrderTransactionLineItemEntities.size();
        return foodOrderTransactionLineItemEntities;
    }

    public void setFoodOrderTransactionLineItemEntities(List<FoodOrderTransactionLineItem> foodOrderTransactionLineItemEntities) {
        this.foodOrderTransactionLineItemEntities = foodOrderTransactionLineItemEntities;
    }    

    public Boolean getVoidRefund() {
        return voidRefund;
    }

    public void setVoidRefund(Boolean voidRefund) {
        this.voidRefund = voidRefund;
    }

    
    
    
    public Customer getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(Customer customer) 
    {
        if(this.customerEntity != null)
        {
            this.customerEntity.getFoodOrderTransactionEntities().remove(this);
        }
        
        this.customerEntity = customer;
        
        if(this.customerEntity != null)
        {
            if(!this.customerEntity.getFoodOrderTransactionEntities().contains(this))
            {
                this.customerEntity.getFoodOrderTransactionEntities().add(this);
            }
        }
    }
}