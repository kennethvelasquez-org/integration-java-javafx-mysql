/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.kennethvelasquez.system.utils.AlertInformation;
import org.kennethvelasquez.system.utils.Validations;

import org.kennethvelasquez.system.utils.ViewFactory;

/**
 *
 * @author STEPHRYS
 */
public class RegisterController implements Initializable{
    @FXML
    private HBox hboxOptionsSecurity;

    @FXML private TextField txtName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtUser;
    @FXML private TextField txtEmail;
    @FXML private PasswordField pwdPassword;
    @FXML private PasswordField pwdConfirmPassword;
    private Integer optionSecurity=0;
    private ToggleGroup tgOptionsSecurity;
    private RadioButton rbNoSecure;
    private RadioButton rbMD5;
    private RadioButton rbBCrypt;
    private ViewFactory viewFacto = new ViewFactory();

    private Validations validate = new Validations();
    private AlertInformation alertInfo = new AlertInformation();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tgOptionsSecurity = new ToggleGroup();
        
        rbNoSecure = new RadioButton("Sin seguridad");
        rbMD5 = new RadioButton("MD5");
        rbBCrypt = new RadioButton("BCrypt");

        buildEvents();
        tgOptionsSecurity.getToggles().addAll(rbNoSecure,rbMD5, rbBCrypt);
        hboxOptionsSecurity.getChildren().addAll(rbNoSecure, rbMD5, rbBCrypt);
    }
    
    private void buildEvents(){
        rbNoSecure.setOnMouseClicked((event) -> {
            optionSecurity =1;
        });
        rbMD5.setOnMouseClicked((event) -> {
            optionSecurity =2;
        });
        rbBCrypt.setOnMouseClicked((event) -> {
            optionSecurity =3;
        });
    }
    
    
    @FXML
    private void onCancelRegister(MouseEvent event){
        viewFacto.loginView();
    }
    
    @FXML
    private void onRegisterUser(MouseEvent event){
        String name = txtName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String user = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        String password = pwdPassword.getText().trim();
        String confirmPassword = pwdConfirmPassword.getText().trim();
        //Validacion campos vacios
        if (validate.isEmptyText(name) || validate.isEmptyText(lastName) 
                || validate.isEmptyText(user) || validate.isEmptyText(email) 
                || validate.isEmptyText(password) || validate.isEmptyText(confirmPassword)) {
            alertInfo.viewAlert("CAMPOS VACIOS", "Error de campos vacios", 
                    "Ha dejado campos vacios en el formulario\nASEGURESE DE LLENAR TODOS LOS CAMPOS",
                    "WARN");
            return;
        }  
        //validacion de elegir el tipo de serguridad en la clave
        if (optionSecurity==null || optionSecurity == 0) {
            alertInfo.viewAlert("SEGURIDAD DE CONTRASEÑA", "Error al elegir la seguridad de contraseña", 
                    "ELIJA UNA OPCION CON LA QUE SE ENCRIPTARÁ LA CONTRASEÑA",
                    "ERR");
            return;
        } 
        //Valdiar el correo electronico 
        if(validate.isValidEmail(email)==false){
            alertInfo.viewAlert("EMAIL INCORRECTO", "Error de formato de email", 
                    "El email ingresado no es correcto\n"
                    + "ASEGURESE DE INGRESAR UN EMAIL VALIDO\n"
                    + "Ejemplo: pepito@gmail.com",
                    "WARN");
            return;
        }
        
        String msg = "";
        //El uso de !validate es lo equivalente logicamente a → !true lo mismo a =false 
        //si es !false es true, por ende cuando hay error es !false = true 
        if (!validate.isValidLengthText(name, 70)) {
            msg = "El campo Nombre no puede exceder los 70 caracteres.";
        }

        if (!validate.isValidLengthText(lastName, 70)) {
            msg = "El campo Apellido no puede exceder los 70 caracteres.";
        }

        if (!validate.isValidLengthText(email, 70)) {
            msg = "El campo Email no puede exceder los 70 caracteres.";
        }

        if (!validate.isValidLengthText(user, 70)) {
            msg = "El campo Usuario no puede exceder los 70 caracteres.";
        }

        if (!validate.isValidLengthText(password, 70)) {
            msg = "El campo Contraseña no puede exceder los 70 caracteres.";
        }
        
        if( !msg.trim().equals("")){
            alertInfo.viewAlert("ERROR DE TEXTO", "Error en la cantidad de letras", 
                    msg,
                    "ERR");
            return ;
        }
        
        if( validate.isEqualsText(password, confirmPassword) ==false){
            alertInfo.viewAlert("ERROR DE CONTRASEÑAS", "Error de contraseñas desiguales", 
                    "Las contraseñas ingresadas no coinciden.",
                    "ERR");
            return ;
        }
        
    }

    
}
