/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.RoomType;
import java.util.List;

/**
 *
 * @author chai
 */
public class RetrieveAllRoomTypeRsp {
    List<RoomType> roomTypes;

    public RetrieveAllRoomTypeRsp() {
    }

    public RetrieveAllRoomTypeRsp(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public List<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }
    
    
}
