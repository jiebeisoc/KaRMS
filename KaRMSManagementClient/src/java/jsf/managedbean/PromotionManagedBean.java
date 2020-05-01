/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PromotionSessionBeanLocal;
import entity.Promotion;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.DeletePromotionException;

/**
 *
 * @author longluqian
 */
@Named(value = "promotionManagedBean")
@ViewScoped
public class PromotionManagedBean implements Serializable {

    @EJB(name = "PromotionSessionBeanLocal")
    private PromotionSessionBeanLocal promotionSessionBeanLocal;

    private List<Promotion> promotionList;
    private List<Promotion> filteredPromotionList;
    private Promotion newPromotion;
    private Promotion selectedPromotionToUpdate;

    public PromotionManagedBean() {
        newPromotion = new Promotion();

    }

    @PostConstruct
    public void postConstruct() {
        promotionList = promotionSessionBeanLocal.retrieveAllPromotion();
    }

    public void createNewPromotion(ActionEvent event) {
        Promotion p = promotionSessionBeanLocal.createNewPromotion(newPromotion);
        if (filteredPromotionList != null) {
            filteredPromotionList.add(p);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Promotion created successfully (Promotion ID: " + p.getPromotionId() + ")", null));

        newPromotion = new Promotion();
        promotionList.add(p);

    }

    public void doUpdatePromotion(ActionEvent event) {
        selectedPromotionToUpdate = (Promotion) event.getComponent().getAttributes().get("promotionToUpdate");
        System.err.println(selectedPromotionToUpdate.getEnabled() + "***************");
    }

    public void updatePromotion(ActionEvent event) {
        promotionSessionBeanLocal.updatePromotion(selectedPromotionToUpdate);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Promotion updated successfully", null));
    }

    public void deletePromotion(ActionEvent event) {
        Promotion promotionToDelete = (Promotion) event.getComponent().getAttributes().get("promotionToDelete");
        try {
            promotionSessionBeanLocal.deletePromotion(promotionToDelete.getPromotionId());
            promotionList.remove(promotionToDelete);
            if (filteredPromotionList != null) {
                filteredPromotionList.remove(promotionToDelete);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Promotion deleted successfully", null));
        } catch (DeletePromotionException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage()+" (being disabled)", null));
            promotionList.remove(promotionToDelete);
            Promotion newPromotion = promotionSessionBeanLocal.retrievePromotionById(promotionToDelete.getPromotionId());
            promotionList.add(newPromotion);
            if (filteredPromotionList != null) {
                filteredPromotionList.remove(promotionToDelete);
                filteredPromotionList.add(newPromotion);
            }
        }

    }

    public List<Promotion> getPromotionList() {
        return promotionList;
    }

    public List<Promotion> getFilteredPromotionList() {
        return filteredPromotionList;
    }

    public Promotion getNewPromotion() {
        return newPromotion;
    }

    public Promotion getSelectedPromotionToUpdate() {
        return selectedPromotionToUpdate;
    }

    public void setPromotionList(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    public void setFilteredPromotionList(List<Promotion> filteredPromotionList) {
        this.filteredPromotionList = filteredPromotionList;
    }

    public void setNewPromotion(Promotion newPromotion) {
        this.newPromotion = newPromotion;
    }

    public void setSelectedPromotionToUpdate(Promotion selectedPromotionToUpdate) {
        this.selectedPromotionToUpdate = selectedPromotionToUpdate;
    }

}
