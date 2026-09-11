/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import org.kennethvelasquez.system.model.User;

/**
 *
 * @author STEPHRYS
 */
public class AuthenticationController {
    private static User userLogued;
    
    private AuthenticationController(){
        
    }
    
    public AuthenticationController(User userLogued){
        AuthenticationController.userLogued = userLogued;
    }

    public static User getUserLogued() {
        return userLogued;
    }

    public static void setUserLogued(User userLogued) {
        AuthenticationController.userLogued = userLogued;
    }
    
}
