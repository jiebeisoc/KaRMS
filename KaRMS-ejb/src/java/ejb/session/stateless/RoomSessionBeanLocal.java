/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Room;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author chai
 */
@Local
public interface RoomSessionBeanLocal {

    public Long createNewRoom(Room newRoom, Long roomTypeId, Long outletId);

    public List<Room> retrieveAllRoom(Long outletId);

    public Room retrieveRoomById(Long roomId);

    public List<Room> retrieveRoomByRoomType(String roomType);
   
    public void updateRoom(Room roomToUpdate);

    public void deleteRoom(Long roomId);

    public boolean isRoomAvailable(Room room, Date startDateTime, Date endDateTime);
    
}
