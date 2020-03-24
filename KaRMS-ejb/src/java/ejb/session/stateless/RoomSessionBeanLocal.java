/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author chai
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createNewRoom(Room newRoom, Long roomType, Long outletId);

    public List<Room> retrieveAllRoom();

    public Room retrieveRoomById(Long roomId);

    public List<Room> retrieveRoomByRoomType(String roomType);
   
    public void updateRoom(Room roomToUpdate);

    public void deleteRoom(Long roomId);
    
}
