/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import org.kennethvelasquez.system.utils.ViewFactory;

/**
 *
 * @author STEPHRYS
 */
public class RegisterController implements Initializable{
    @FXML
    private HBox hboxOptionsSecurity;
    private ToggleGroup tgOptionsSecurity;
    private RadioButton rbNoSecure;
    private RadioButton rbMD5;
    private RadioButton rbBCrypt;
    private ViewFactory viewFacto = new ViewFactory();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tgOptionsSecurity = new ToggleGroup();
        
        rbNoSecure = new RadioButton("Sin seguridad");
        rbMD5 = new RadioButton("MD5");
        rbBCrypt = new RadioButton("BCrypt");
        
        tgOptionsSecurity.getToggles().addAll(rbNoSecure,rbMD5, rbBCrypt);
        hboxOptionsSecurity.getChildren().addAll(rbNoSecure, rbMD5, rbBCrypt);
    }
    
    
    @FXML
    private void onCancelRegister(MouseEvent event){
        viewFacto.loginView();
    }
    
}
