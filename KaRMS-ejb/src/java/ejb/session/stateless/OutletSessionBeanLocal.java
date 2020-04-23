/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface OutletSessionBeanLocal {

    public Long createNewOutlet(Outlet newOutlet, Employee employee);

    public List<Outlet> retrieveAllOutlets();

    public Outlet retrieveOutletById(Long outletId);

    public void updateOutlet(Outlet outletToUpdate);

    public void deleteOutlet(Long outletId);

    public void viewOutletOpeningHours(Outlet outlet);
    
}
