/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.kennethvelasquez.system.utils.ViewFactory;

/**
 *
 * @author STEPHRYS
 */
public class LoginController implements Initializable{
            
    private ViewFactory viewFacto = new ViewFactory();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    private void onRegisterUser(MouseEvent event){
        viewFacto.registerView();
    }
}
