/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Song;
import java.util.List;
import javax.ejb.Local;
import util.exception.AddSongException;
import util.exception.ChangePasswordException;
import util.exception.CreateCustomerException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerUsernameExistException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author chai
 */
@Local
public interface CustomerSessionBeanLocal {

    public Long createNewCustomer(Customer newCustomer) throws CustomerUsernameExistException, CreateCustomerException;

    public List<Customer> retrieveAllCustomer();

    public Customer retrieveCustomerById(Long customerId);

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;
    
    public List<Song> retrieveFavouritePlaylist(Long customerId);
    
    public void addSongToFavouritePlaylist(Song songToAdd, Long customerId) throws AddSongException;
    
    public void deleteSongFromFavouritePlaylist(Song songToDelete, Long customerId);
    
    public void addFavouritePlaylistToQueue(Long customerId, Long reservationId);
 
    public void updateCustomer(Customer customerToUpdate);

    public void deleteCustomer(Long customerId);

    public void updatePayment(Long customerId, String newCardNo);

    public Customer customerLogin(String username, String password) throws InvalidLoginCredentialException;

    public void addPoints(Long customerId, int pointsToAdd);

    public void redeemPoints(Long customerId, int pointsToDeduct);
 
    public void updateDetails(String username, String name, String email, String phoneNo, String creditCardNo) throws InvalidLoginCredentialException, CustomerNotFoundException;

    public void changePassword(String username, String oldPassword, String newPassword) throws InvalidLoginCredentialException, CustomerNotFoundException, ChangePasswordException;
    
}
