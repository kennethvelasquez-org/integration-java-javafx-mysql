/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.kennethvelasquez.system.model.ApplicationStatus;
import org.kennethvelasquez.system.model.dto.UserDTO;
import org.kennethvelasquez.system.service.UserService;
import org.kennethvelasquez.system.service.UserStatus;
import org.kennethvelasquez.system.utils.AlertInformation;
import org.kennethvelasquez.system.utils.ViewFactory;

/**
 * FXML Controller class
 *
 * @author STEPHRYS
 */
public class UserViewController implements Initializable {
    
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
    private TableColumn<UserDTO, String> colEmail;
    @FXML
    private TableColumn<UserDTO, String> colEncrypt;
    @FXML
    private TableColumn<UserDTO, String> colIdUser;
    @FXML
    private TableColumn<UserDTO, String> colLastName;
    @FXML
    private TableColumn<UserDTO, String> colName;
    @FXML
    private TableColumn<UserDTO, String> colRol;
    @FXML
    private TableColumn<UserDTO, String> colStatus;
    @FXML
    private TableColumn<UserDTO, String> colUser;
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
    @FXML
    private TableView<UserDTO> tblUsers;
    
    /**
     * Instancia de la factoría de vistas ({@link ViewFactory}) para instanciar escenas y componentes FXML.
     */
    private ViewFactory viewFacto = new ViewFactory();
    private UserService userService = new UserService();
    private ObservableList<UserDTO> observableListUsers;
    private ApplicationStatus userViewStatus = ApplicationStatus.NONE;
    private AlertInformation alertInfo = new AlertInformation();
    
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
    
    @FXML
    private void onCancel(ActionEvent event) {

    }

    @FXML
    private void onCreate(ActionEvent event) {
        
    }

    @FXML
    private void onDelete(ActionEvent event) {

    }
    
    private void loadTableUsers(){
        tblUsers.setItems(observableListUsers);
        colIdUser.setCellValueFactory(
                new PropertyValueFactory<UserDTO, String>("idUser")
        );
        colName.setCellValueFactory(
                new PropertyValueFactory<UserDTO, String>("name")
        );
        colLastName.setCellValueFactory(
                new PropertyValueFactory<UserDTO, String>("lastName")
        );
        colUser.setCellValueFactory(
                new PropertyValueFactory<UserDTO, String>("user")
        );
        colEmail.setCellValueFactory(
                new PropertyValueFactory<UserDTO, String>("email")
        );
        colRol.setCellValueFactory(
                new PropertyValueFactory<UserDTO, String>("rolName")
        );
        colStatus.setCellValueFactory(
                new PropertyValueFactory<UserDTO, String>("userStatus")
        );
        colEncrypt.setCellValueFactory(
                new PropertyValueFactory<UserDTO, String>("typeEncryptName")
        );
    }

    @FXML
    private void onRead(ActionEvent event) {
        if( userViewStatus  ==ApplicationStatus.NONE){
            UserStatus userStatus = userService.readUsers();
            switch (userStatus) {
                case READ_SUCCESS->{
                    observableListUsers = FXCollections.observableArrayList(userService.getUsersList());
                    loadTableUsers();
                }
            }
        }            
    }

    @FXML
    private void onSearch(ActionEvent event) {

    }

    @FXML
    private void onUpdate(ActionEvent event) {

    }

}
