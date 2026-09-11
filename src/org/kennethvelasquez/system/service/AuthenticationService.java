/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

import java.sql.SQLException;
import org.kennethvelasquez.system.controller.AuthenticationController;
import org.kennethvelasquez.system.model.User;
import org.kennethvelasquez.system.repository.UserRepository;
import org.kennethvelasquez.system.utils.ToolBCrypt;

/**
 *
 * @author STEPHRYS
 */
public class AuthenticationService {
    
    private UserRepository userRepo = new UserRepository();
    private String messageError;
    
    public AuthenticationService(){
        
    }
    
    public AuthenticationStatus userLogin(String userData, String password){
        User searchUser;
        try {
            searchUser = userRepo.searchByEmailOrUser(userData);
            if( searchUser ==null )
                return AuthenticationStatus.ERROR_USER_NOT_FOUND;
        } catch (SQLException e) {
            messageError = e.getMessage();
            return AuthenticationStatus.ERROR_USER_SEARCH;
        }
        try {
            User userLogued = switch(searchUser.getTypeEncrypt()){
                case 1->userRepo.loginUnprotected(userData, password);
                    
                case 2->userRepo.loginMD5(userData, password);
                    
                case 3->{
                    ToolBCrypt encript = new ToolBCrypt();
                    String savedPassword = searchUser.getPassword();
                    boolean passwordValid = encript.validatePassword(password, savedPassword);
                    if( passwordValid == true )
                        yield searchUser;
                    else 
                        yield null;
                }
                default-> null;
            };
            if( userLogued == null)
                return AuthenticationStatus.ERROR_CREDENTIALS;
            
            AuthenticationController.setUserLogued(userLogued);
            return AuthenticationStatus.LOGIN_SUCCESS;
        } catch (SQLException e) {
            messageError = e.getMessage();
            return AuthenticationStatus.ERROR_LOGIN;
        } catch (Exception e) {
            messageError = e.getMessage();
            return AuthenticationStatus.ERROR_LOGIN;
        }
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
    
}
