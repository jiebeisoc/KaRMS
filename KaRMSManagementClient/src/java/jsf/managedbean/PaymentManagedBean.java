/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.Employee;
import entity.Reservation;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author chai
 */
@Named(value = "paymentManagedBean")
@ViewScoped
public class PaymentManagedBean implements Serializable {

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    
    private List<Reservation> unpaidReservations;
    
    private Reservation selectedReservation;
    
    private String payment;
    
    private Employee employee;

    /**
     * Creates a new instance of PaymentManagedBean
     */
    public PaymentManagedBean() {
    }
    
    @PostConstruct
    public void postConstuct() {
        employee = (Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployee");
        Long outletId = null;
        if (employee.getOutlet() != null) {
            outletId = employee.getOutlet().getOutletId();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date currentDate = formatter.parse(formatter.format(new Date()));
            
            unpaidReservations = reservationSessionBeanLocal.retrieveReservationByDateAndStatus(currentDate, outletId);
        } catch (ParseException ex) {
            System.out.println("Wrong format");
        }
    }
    
    public void payReservation(ActionEvent event) {
        
        Reservation reservationToPay = (Reservation)event.getComponent().getAttributes().get("reservationToPay");
        
        reservationSessionBeanLocal.payReservation(reservationToPay.getReservationId());
        
        unpaidReservations.remove(reservationToPay);
    }
    
    public List<Reservation> getUnpaidReservations() {
        return unpaidReservations;
    }

    public void setUnpaidReservations(List<Reservation> unpaidReservations) {
        this.unpaidReservations = unpaidReservations;
    }

    public Reservation getSelectedReservation() {
        return selectedReservation;
    }

    public void setSelectedReservation(Reservation selectedReservation) {
        this.selectedReservation = selectedReservation;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
    
}
