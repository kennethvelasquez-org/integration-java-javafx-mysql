/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.kennethvelasquez.system.utils.ViewFactory;

/**
 * FXML Controller class
 *
 * @author STEPHRYS
 */
public class UserViewController implements Initializable {

    /**
     * Instancia de la factoría de vistas ({@link ViewFactory}) para instanciar escenas y componentes FXML.
     */
    private ViewFactory viewFacto = new ViewFactory();
    
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnCreate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRead;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnUpdate;

    @FXML
    private CheckBox cbBCrypt;

    @FXML
    private CheckBox cbMD5;

    @FXML
    private CheckBox cbUnprotected;

    @FXML
    private ComboBox<?> cmbRol;

    @FXML
    private ComboBox<?> cmbStatus;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colEncrypt;

    @FXML
    private TableColumn<?, ?> colIdUser;

    @FXML
    private TableColumn<?, ?> colLastName;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colRol;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    private TableColumn<?, ?> colUser;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtIdUser;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUser;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    private void onDashboard(MouseEvent event){
        viewFacto.dashboardView();
    }
    
    void onCancel(ActionEvent event) {

    }

    @FXML
    void onCreate(ActionEvent event) {

    }


    @FXML
    void onDelete(ActionEvent event) {

    }

    @FXML
    void onRead(ActionEvent event) {

    }

    @FXML
    void onSearch(ActionEvent event) {

    }

    @FXML
    void onUpdate(ActionEvent event) {

    }

}
