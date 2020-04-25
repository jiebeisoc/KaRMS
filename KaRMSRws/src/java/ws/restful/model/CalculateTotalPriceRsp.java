/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import java.math.BigDecimal;

/**
 *
 * @author zihua
 */
public class CalculateTotalPriceRsp {
    
    BigDecimal totalPrice;

    public CalculateTotalPriceRsp() {
    }

    public CalculateTotalPriceRsp(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    
    
}
