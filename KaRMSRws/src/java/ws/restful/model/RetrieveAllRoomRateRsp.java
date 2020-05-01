/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.RoomRate;
import java.util.List;

/**
 *
 * @author chai
 */
public class RetrieveAllRoomRateRsp {
    List<RoomRate> roomRates;

    public RetrieveAllRoomRateRsp() {
    }

    public RetrieveAllRoomRateRsp(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }

    public List<RoomRate> getRoomRates() {
        return roomRates;
    }

    public void setRoomRates(List<RoomRate> roomRates) {
        this.roomRates = roomRates;
    }
    
    
    
}
