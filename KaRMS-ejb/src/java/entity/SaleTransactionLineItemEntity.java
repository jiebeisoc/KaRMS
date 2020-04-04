package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;



@Entity


@NamedQueries({
    @NamedQuery(name = "selectAllSaleTransactionLineItemsByProductId", query = "SELECT stli FROM SaleTransactionLineItemEntity stli WHERE stli.foodItem.foodItemId = :inProductId")
})

public class SaleTransactionLineItemEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTransactionLineItemId;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer serialNumber;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FoodItem foodItem;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal unitPrice;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal subTotal;

    
    
    public SaleTransactionLineItemEntity()
    {
    }
    
    
    
    public SaleTransactionLineItemEntity(Integer serialNumber, FoodItem foodItem, Integer quantity, BigDecimal unitPrice, BigDecimal subTotal)
    {
        this.serialNumber = serialNumber;
        this.foodItem = foodItem;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }
    
    
    
    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.saleTransactionLineItemId != null ? this.saleTransactionLineItemId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof SaleTransactionLineItemEntity)) 
        {
            return false;
        }
        
        SaleTransactionLineItemEntity other = (SaleTransactionLineItemEntity) object;
        
        if ((this.saleTransactionLineItemId == null && other.saleTransactionLineItemId != null) || (this.saleTransactionLineItemId != null && !this.saleTransactionLineItemId.equals(other.saleTransactionLineItemId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.SaleTransactionLineItemEntity[ saleTransactionLineItemId=" + this.saleTransactionLineItemId + " ]";
    }

    
    
    public Long getSaleTransactionLineItemId() {
        return saleTransactionLineItemId;
    }

    public void setSaleTransactionLineItemId(Long saleTransactionLineItemId) {
        this.saleTransactionLineItemId = saleTransactionLineItemId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

  

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }   

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }
}