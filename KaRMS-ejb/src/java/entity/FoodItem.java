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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;



@Entity

public class FoodItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodItemId;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(max = 64)
    private String name;
    @Column(length = 128)
    @Size(max = 128)
    private String description;
    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer quantityOnHand;
    @Column(nullable = false, unique = true, length = 7)
    @NotNull
    @Size(min = 7, max = 7)
    private String skuCode;

    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal unitPrice;
    
 
    @Column(nullable = false)
    @NotNull
    @Positive
    @Min(1)
    @Max(5)
    private Integer foodItemRating;
    
  
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private FoodItemCategory categoryEntity;
    


    
    
    public FoodItem() 
    {
       
        quantityOnHand = 0;
    
        unitPrice = new BigDecimal("0.00");
        foodItemRating = 1;
        
  
    }

    public FoodItem(String skuCode,String name, String description, Integer quantityOnHand, BigDecimal unitPrice, Integer productRating) {
        this();
        this.skuCode=skuCode;
        this.name = name;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.unitPrice = unitPrice;
        this.foodItemRating = productRating;
    }

    

    
    
    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (this.foodItemId != null ? this.foodItemId.hashCode() : 0);
        
        return hash;
    }

    
    
    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof FoodItem)) 
        {
            return false;
        }
        
        FoodItem other = (FoodItem) object;
        
        if ((this.foodItemId == null && other.foodItemId != null) || (this.foodItemId != null && !this.foodItemId.equals(other.foodItemId))) 
        {
            return false;
        }
        
        return true;
    }

    
    
    @Override
    public String toString() 
    {
        return "entity.pos.FoodItem[ FoodItemID=" + this.foodItemId + " ]";
    }

    
    
    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }
    


    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }


    public Integer getFoodItemRating() {
        return foodItemRating;
    }

    // Added in v5.0
    public void setFoodItemRating(Integer foodItemRating) {
        this.foodItemRating = foodItemRating;
    }
    
    // Added in v5.0
    public FoodItemCategory getCategoryEntity() {
        return categoryEntity;
    }

    // Added in v5.0
    public void setCategoryEntity(FoodItemCategory categoryEntity) 
    {
        if(this.categoryEntity != null)
        {
            if(this.categoryEntity.getFoodItems().contains(this))
            {
                this.categoryEntity.getFoodItems().remove(this);
            }
        }
        
        this.categoryEntity = categoryEntity;
        
        if(this.categoryEntity != null)
        {
            if(!this.categoryEntity.getFoodItems().contains(this))
            {
                this.categoryEntity.getFoodItems().add(this);
            }
        }
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }


}