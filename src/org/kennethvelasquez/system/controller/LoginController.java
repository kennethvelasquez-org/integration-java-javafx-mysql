/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.kennethvelasquez.system.service.AuthenticationService;
import org.kennethvelasquez.system.service.AuthenticationStatus;
import org.kennethvelasquez.system.utils.AlertInformation;
import org.kennethvelasquez.system.utils.Validations;
import org.kennethvelasquez.system.utils.ViewFactory;

/**
 *
 * @author STEPHRYS
 */
public class LoginController implements Initializable{
            
    private ViewFactory viewFacto = new ViewFactory();
    @FXML private TextField txtDataUser;
    @FXML private PasswordField pwdPassword;
    private Validations validate = new Validations();
    private AlertInformation alertInfo = new AlertInformation();
    private AuthenticationService authService = new AuthenticationService();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    private void onRegisterUser(MouseEvent event){
        viewFacto.registerView();
    }
    
    @FXML
    private void onLogin(ActionEvent event){
        String dataUser = txtDataUser.getText().trim();
        String password = pwdPassword.getText().trim();
        if (validate.isEmptyText(dataUser)  || validate.isEmptyText(password)) {
            alertInfo.viewAlert("CAMPOS VACIOS", "Error de campos vacios", 
                    "Ha dejado campos vacios en el formulario\nASEGURESE DE LLENAR TODOS LOS CAMPOS",
                    "WARN");
            return;
        }  
        //Entra en el flujo si es un correo lo que se está ingresando
        if( dataUser.contains("@")){
            //Realizamos la validacion
            if(validate.isValidEmail(dataUser)==false){
                alertInfo.viewAlert("EMAIL INCORRECTO", "Error de formato de Correo", 
                    "El correo ingresado no es correcto\n"
                    + "ASEGURESE DE INGRESAR UN CORREO VALIDO\n"
                    + "Ejemplo: pepito@gmail.com",
                    "WARN");
                return;
            }
            
        }
        
        String msg="";
        if (!validate.isValidLengthText(password, 70)) {
            msg = "El campo Contraseña no puede exceder los 70 caracteres.";
        }
        if (!validate.isValidLengthText(dataUser, 70)) {
            msg = "El campo campo Usuario o Correo no puede exceder los 70 caracteres.";
        }
        
        if( !msg.trim().equals("")){
            alertInfo.viewAlert("ERROR DE TEXTO", "Error en los campos ingresados", 
                    msg,
                    "ERR");
            return ;
        }
        
        AuthenticationStatus loginStatus = authService.userLogin(dataUser, password);
        switch (loginStatus) {
            case LOGIN_SUCCESS -> {
                alertInfo.viewAlert("BIENVENIDO", "Inicio de sesión correcto", 
                "Hola, " + AuthenticationController.getUserLogued().getUser(), "INFO");
                viewFacto.dashboardView();
            }
            case ERROR_CREDENTIALS->
                alertInfo.viewAlert("DATOS INCORRECTOS", "Error de Credenciales",
                        "El usuario/correo y contraseña ingresado no coincide\nValide sus credenciales",
                        "ERR");
            case ERROR_LOGIN->
                alertInfo.viewAlert("INICIO DE SESIÓN", "Error al iniciar sesión", 
                    "Ocurrió un error al momento de iniciar sesión.\n"+authService.getMessageError(),
                    "ERR");
            case ERROR_USER_NOT_FOUND ->
                 alertInfo.viewAlert("USUARIO NO ENCONTRADO", "Error",
                         "El usuario o correo no existe", "ERR");
            case ERROR_USER_SEARCH->                
                alertInfo.viewAlert("ERROR BUSQUEDA DE USUARIO", "Error al comprobar usuario", 
                    "Ocurrió un error al momento de validar existencia de usuario.\n"+authService.getMessageError(),
                    "ERR");
        }
    }
}
