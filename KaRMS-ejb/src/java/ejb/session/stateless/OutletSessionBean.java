/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import entity.Room;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanLocal {

    @PersistenceContext(unitName = "KaRMS-ejbPU")
    private EntityManager em;

    
    @Override
    public Long createNewOutlet(Outlet newOutlet, Employee employee)
    {
        em.persist(newOutlet);
        em.flush();
        
        em.persist(employee);
        em.flush();
        
        newOutlet.setEmployee(employee);
        employee.setOutlet(newOutlet);
        
        return newOutlet.getOutletId();
    }
    
    
    @Override
    public List<Outlet> retrieveAllOutlets() 
    {
        Query query = em.createQuery("SELECT o from Outlet o");
        return query.getResultList();
    }
    
    @Override
    public Outlet retrieveOutletById(Long outletId)
    {
        Outlet outlet = em.find(Outlet.class, outletId);
        
        return outlet;
    }
    
    // is this method still necessary? or can just use get() in Outlet.class
    @Override
    public void viewOutletOpeningHours(Outlet outlet)
    {
        outlet.getOpeningHours();
        
    }
    
    @Override
    public void updateOutlet(Outlet outletToUpdate)
    {
        em.merge(outletToUpdate);
        em.flush();
        
    }
    
    @Override
    public void deleteOutlet(Long outletId)
    {
        Outlet outletToDelete = retrieveOutletById(outletId);
        
        for (Room r: outletToDelete.getRooms()) {
            em.remove(r);
        }
        
        outletToDelete.setIsDisabled(Boolean.TRUE);
    }
    

}
