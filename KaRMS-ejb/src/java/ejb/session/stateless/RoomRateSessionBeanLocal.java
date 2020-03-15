/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author zihua
 */
@Local
public interface RoomRateSessionBeanLocal {

    public Long createNewRoomRate(RoomRate newRoomRate);

    public List<RoomRate> retrieveAllRoomRates();

    public RoomRate retrieveRoomRateById(Long roomRateId);

    public void updateRoomRate(RoomRate roomRateToUpdate);

    public void deleteRoomRate(Long roomRateId);

}