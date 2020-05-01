/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Room;
import java.util.List;

/**
 *
 * @author chai
 */
public class RetrieveRoomListRsp {
    List<Room> rooms;

    public RetrieveRoomListRsp() {
    }

    public RetrieveRoomListRsp(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    
}
