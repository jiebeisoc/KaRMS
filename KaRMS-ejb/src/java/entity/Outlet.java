/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author user
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    private String outletName;
    private String outletAddress;
    private List<Room> rooms;
    private int outletPhone;
    private String openingHours;

    public Outlet() {
    }

    public Outlet(Long outletId, String outletName, String outletAddress, List<Room> rooms, int outletPhone, String openingHours) {
        this.outletId = outletId;
        this.outletName = outletName;
        this.outletAddress = outletAddress;
        this.rooms = rooms;
        this.outletPhone = outletPhone;
        this.openingHours = openingHours;
    }



    
    
    
    
    
    
    
    
    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getOutletId() != null ? getOutletId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.getOutletId() == null && other.getOutletId() != null) || (this.getOutletId() != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + getOutletId() + " ]";
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public int getOutletPhone() {
        return outletPhone;
    }

    public void setOutletPhone(int outletPhone) {
        this.outletPhone = outletPhone;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }
    
}
