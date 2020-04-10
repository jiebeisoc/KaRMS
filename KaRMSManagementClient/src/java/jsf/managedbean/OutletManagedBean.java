/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OutletSessionBeanLocal;
import entity.Outlet;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author chai
 */
@Named(value = "outletManagedBean")
@ViewScoped
public class OutletManagedBean implements Serializable {

    @EJB(name = "OutletSessionBeanLocal")
    private OutletSessionBeanLocal outletSessionBeanLocal;
    
    private List<Outlet> outlets;
    
    private Outlet newOutlet;
    private Outlet selectedOutlet;
    

    /**
     * Creates a new instance of OutletManagedBean
     */
    public OutletManagedBean() {
        newOutlet = new Outlet();
    }
    
    @PostConstruct
    public void postConstruct() {
        outlets = outletSessionBeanLocal.retrieveAllOutlets();
    }
    
    public void createNewOutlet(ActionEvent event) {
        Long outletId = outletSessionBeanLocal.createNewOutlet(newOutlet);
        outlets.add(newOutlet);
        
        newOutlet = new Outlet();
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New outlet created successfully", null));
    }
    
    public void updateOutlet() {
        outletSessionBeanLocal.updateOutlet(selectedOutlet);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Outlet updated successfully", null));
    }
    
    public void deleteOutlet(ActionEvent event) {
        Outlet outletToDelete = (Outlet)event.getComponent().getAttributes().get("outletToDelete");
        
        outletSessionBeanLocal.deleteOutlet(outletToDelete.getOutletId());
        outlets.remove(outletToDelete);
    }
    
    public void onRowSelect(SelectEvent event) {
        selectedOutlet = (Outlet)event.getObject();
    }
    
    public void onRowUnselect(SelectEvent event) {
        selectedOutlet = null;
    }

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<Outlet> outlets) {
        this.outlets = outlets;
    }

    public Outlet getNewOutlet() {
        return newOutlet;
    }

    public void setNewOutlet(Outlet newOutlet) {
        this.newOutlet = newOutlet;
    }

    public Outlet getSelectedOutlet() {
        return selectedOutlet;
    }

    public void setSelectedOutlet(Outlet selectedOutlet) {
        this.selectedOutlet = selectedOutlet;
    }
    
}
