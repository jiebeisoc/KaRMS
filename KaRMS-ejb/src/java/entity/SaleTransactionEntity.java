package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.EntityInstanceMissingInCollectionException;



@Entity

public class SaleTransactionEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    // Updated in v4.1 - JPA annotations for persistent attributes
    // Updated in v4.2 - Bean Validation annotations for input data validation
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTransactionId;
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
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;    
    @Column(nullable = false)
    @NotNull
    private Boolean voidRefund;
    
    
  
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Customer customerEntity;

    
    
    public SaleTransactionEntity()
    {
        saleTransactionLineItemEntities = new ArrayList<>();
        voidRefund = false;
    }
    
    
    
    public SaleTransactionEntity(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date transactionDateTime, Boolean voidRefund)
    {
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.voidRefund = voidRefund;        
    }

    
    
    public SaleTransactionEntity(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date transactionDateTime, List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities, Boolean voidRefund)
    {
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;        
        this.voidRefund = voidRefund;        
    }

    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.saleTransactionId != null ? this.saleTransactionId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof SaleTransactionEntity)) 
        {
            return false;
        }
        
        SaleTransactionEntity other = (SaleTransactionEntity) object;
        
        if ((this.saleTransactionId == null && other.saleTransactionId != null) || (this.saleTransactionId != null && !this.saleTransactionId.equals(other.saleTransactionId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.SaleTransactionEntity[ saleTransactionId=" + this.saleTransactionId + " ]";
    }
    
    
    
    public void addSaleTransactionLineItemEntity(SaleTransactionLineItemEntity saleTransactionLineItemEntity) throws EntityInstanceExistsInCollectionException
    {
        if(!this.saleTransactionLineItemEntities.contains(saleTransactionLineItemEntity))
        {
            this.saleTransactionLineItemEntities.add(saleTransactionLineItemEntity);
        }
        else
        {
            throw new EntityInstanceExistsInCollectionException("Sale Transaction Line Item already exist");
        }
    }
    
    
    
    public void removeSaleTransactionLineItemEntity(SaleTransactionLineItemEntity saleTransactionLineItemEntity) throws EntityInstanceMissingInCollectionException
    {
        if(this.saleTransactionLineItemEntities.contains(saleTransactionLineItemEntity))
        {
            this.saleTransactionLineItemEntities.remove(saleTransactionLineItemEntity);
        }
        else
        {
            throw new EntityInstanceMissingInCollectionException("Sale Transaction Line Item missing");
        }
    }
    
    
    
    public Long getSaleTransactionId() {
        return saleTransactionId;
    }

    public void setSaleTransactionId(Long saleTransactionId) {
        this.saleTransactionId = saleTransactionId;
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

    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        saleTransactionLineItemEntities.size();
        return saleTransactionLineItemEntities;
    }

    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
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
            this.customerEntity.getSaleTransactionEntities().remove(this);
        }
        
        this.customerEntity = customer;
        
        if(this.customerEntity != null)
        {
            if(!this.customerEntity.getSaleTransactionEntities().contains(this))
            {
                this.customerEntity.getSaleTransactionEntities().add(this);
            }
        }
    }
}