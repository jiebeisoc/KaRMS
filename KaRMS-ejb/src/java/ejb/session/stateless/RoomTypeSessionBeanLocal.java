/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteRoomTypeException;

/**
 *
 * @author zihua
 */
@Local
public interface RoomTypeSessionBeanLocal {

    public Long createNewRoomType(RoomType newRoomType, List<Long> roomRateIds);

    public List<RoomType> retrieveAllRoomTypes();

    public RoomType retrieveRoomTypeById(Long roomTypeId);

    public void updateRoomType(RoomType roomTypeToUpdate, List<Long> roomRateIds);

    public void deleteRoomType(Long roomTypeId) throws DeleteRoomTypeException;


}
