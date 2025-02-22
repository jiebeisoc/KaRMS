/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import util.enumeration.RoomRateType;

/**
 *
 * @author zihua
 */
@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    @NotNull
    @Column(nullable = false)
    private String name;
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @NotNull
    @Column(nullable = false)
    private BigDecimal rate;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private RoomRateType roomRateType;
    
    @ManyToOne
    @JoinColumn(nullable = true)
    private RoomType roomType;

    public RoomRate() {
    }

    public RoomRate(String name, Date startTime, Date endTime, BigDecimal rate, RoomRateType roomRateType) {
        this();
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rate = rate;
        this.roomRateType = roomRateType;
    }

    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRate)) {
            return false;
        }
        RoomRate other = (RoomRate) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + roomRateId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRoomRateType() {
        return roomRateType.name();
    }

    public void setRoomRateType(String type) {
        if (type.equals("WKDAYPEAK")) {
            this.roomRateType = RoomRateType.WKDAYPEAK;
        } else if (type.equals("WKENDPEAK")) {
            this.roomRateType = RoomRateType.WKENDPEAK;
        } else if (type.equals("WKDAYNONPEAK")) {
            this.roomRateType = RoomRateType.WKDAYNONPEAK;
        }  else { // Weekend Non Peak
            this.roomRateType = RoomRateType.WKENDNONPEAK;
        }
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
}
