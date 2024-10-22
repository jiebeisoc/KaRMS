/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author longluqian
 */
@Entity
public class Promotion implements Serializable {

    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;
    @NotNull
    @Column(nullable = false)
    private String promotionName;
    @NotNull
    @Column(nullable = false)
    private double discountRate;
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date validFrom;
    @Column(nullable = true)
    @Temporal(TemporalType.DATE)
    private Date validUntil;
    @NotNull
    @Column(nullable = false)
    private boolean enabled;
    private String description;

    public Promotion() {
        this.enabled = Boolean.TRUE;
    }

    public Promotion(String promotionName, double discountRate, Date validFrom, Date validUntil, String description) {
        this();
        this.promotionName = promotionName;
        this.discountRate = discountRate;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.description = description;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (promotionId != null ? promotionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the promotionId fields are not set
        if (!(object instanceof Promotion)) {
            return false;
        }
        Promotion other = (Promotion) object;
        if ((this.promotionId == null && other.promotionId != null) || (this.promotionId != null && !this.promotionId.equals(other.promotionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Promotion[ id=" + promotionId + " ]";
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public boolean getEnabled() {
         System.out.println("************************ 到getEnabled");  
      
                
        return enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

   

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public void setEnabled(boolean enabled) {
          System.out.println("************************ 到setEnabled");  
        this.enabled = enabled;
    }
    
}
