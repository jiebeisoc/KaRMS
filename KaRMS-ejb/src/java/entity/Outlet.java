/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

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
    @NotNull
    @Column(nullable = false)
    private String outletName;
    @NotNull
    @Column(nullable = false)
    private String outletAddress;
    @NotNull
    @Column(nullable = false)
    private String outletPhone;
    @Temporal(TemporalType.TIME)
    @NotNull
    @Column(nullable = false)
    private Date openingHours;
    @Temporal(TemporalType.TIME)
    @NotNull
    @Column(nullable = false)
    private Date closingHours;
    @NotNull
    @Column(nullable = false)
    private Boolean isDisabled;
    
    @OneToMany(mappedBy = "outlet")
    private List<Room> rooms;
    
    @OneToMany(mappedBy = "outlet")
    private List<Reservation> reservations;
    
    @OneToMany(mappedBy = "outlet")
    private List<Review> reviews;
    
    @OneToOne (mappedBy = "outlet")
    private Employee employee;

    public Outlet() {
        this.isDisabled = Boolean.FALSE;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public Outlet(String outletName, String outletAddress, String outletPhone, Date openingHours, Date closingHours) {
        this();
        this.outletName = outletName;
        this.outletAddress = outletAddress;
        this.outletPhone = outletPhone;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
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
    
    public String getOutletNameTrim() {
        return outletName.replaceAll("\\s","");
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public String getOutletPhone() {
        return outletPhone;
    }

    public void setOutletPhone(String outletPhone) {
        this.outletPhone = outletPhone;
    }

    public Date getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Date openingHours) {
        this.openingHours = openingHours;
    }

    public Date getClosingHours() {
        return closingHours;
    }

    public void setClosingHours(Date closingHours) {
        this.closingHours = closingHours;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

}
