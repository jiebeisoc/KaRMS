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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.security.CryptographicHelper;

/**
 *
 * @author chai
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotNull
    @Column(nullable = false)
    private String name;
    @NotNull
    @Column(nullable = false)
    private String phoneNo;
    private String creditCardNo;
    @NotNull
    @Column(nullable = false, unique = true)
    private String username;
    @NotNull
    @Column(nullable = false)
    private String password;
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;
    private int points;
    private String salt;
    
    @OneToMany(mappedBy = "customer")
    private List<Reservation> reservations;
    
    @ManyToMany
    private List<Song> favouritePlaylist;
    
    //Added by Luqian
    @OneToMany(mappedBy = "customerEntity")
    private List<FoodOrderTransaction> foodOrderTransactionEntities;

    public Customer() {
        this.points = 0;
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.points = 0;
        this.reservations = new ArrayList<>();
        this.foodOrderTransactionEntities = new ArrayList<>();
        this.favouritePlaylist = new ArrayList<>();
    }

    public Customer(String name, String phoneNo, String creditCardNo, String username, String password, Date birthday, String email) {
        this();
        this.name = name;
        this.phoneNo = phoneNo;
        this.creditCardNo = creditCardNo;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.email = email;
        
        setPassword(password);
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return the creditCardNo
     */
    public String getCreditCardNo() {
        return creditCardNo;
    }

    /**
     * @param creditCardNo the creditCardNo to set
     */
    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        if(password != null) {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        }
        else {
            this.password = null;
        }
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<FoodOrderTransaction> getFoodOrderTransactionEntities() {
        return foodOrderTransactionEntities;
    }

    public void setFoodOrderTransactionEntities(List<FoodOrderTransaction> foodOrderTransactionEntities) {
        this.foodOrderTransactionEntities = foodOrderTransactionEntities;
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Song> getFavouritePlaylist() {
        return favouritePlaylist;
    }

    public void setFavouritePlaylist(List<Song> favouritePlaylist) {
        this.favouritePlaylist = favouritePlaylist;
    }
    
}
