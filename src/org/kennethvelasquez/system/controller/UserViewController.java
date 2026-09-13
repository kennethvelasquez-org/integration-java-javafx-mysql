/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import org.kennethvelasquez.system.model.Rol;
import org.kennethvelasquez.system.model.UserAccountStatus;
import org.kennethvelasquez.system.model.dto.UserDTO;
import org.kennethvelasquez.system.service.RolService;
import org.kennethvelasquez.system.service.RolStatus;
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
    private ComboBox<Rol> cmbRol;
    @FXML
    private ComboBox<UserAccountStatus> cmbStatus;
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
    private PasswordField pwdConfirmPassword;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtIdUser;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtName;
    @FXML
    private PasswordField pwdPassword;
    @FXML
    private TextField txtUser;
    @FXML
    private TableView<UserDTO> tblUsers;
    
    /**
     * Instancia de la factoría de vistas ({@link ViewFactory}) para instanciar escenas y componentes FXML.
     */
    private ViewFactory viewFacto = new ViewFactory();
    /**
     * Instancia del servicio de Usuarios para los CRUD
     */
    private UserService userService = new UserService();
    /**
     * Instancia del servicio de Rol.
     */
    private RolService rolService = new RolService();
    
    private ObservableList<UserDTO> observableListUsers;
    private ObservableList<Rol> observableListRoles;
    private ObservableList<UserAccountStatus> observableListUserAccountStatus;
    private List<CheckBox> listOptionsSecurity=new ArrayList<>();
    
    private ApplicationStatus userViewStatus = ApplicationStatus.NONE;
    private AlertInformation alertInfo = new AlertInformation();
    private Integer optionSecurity=0;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadComboBox();
        listOptionsSecurity.add(cbUnprotected);
        listOptionsSecurity.add(cbMD5);
        listOptionsSecurity.add(cbBCrypt);
        
        // Callback Reactivo del Modelo de Selección en la tabla
        // Metodo equivalente al 
        tblUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldUser, userSelect) -> {
            // El tercer parámetro 'userSelect' ya es el nuevo usuario seleccionado
            if (userViewStatus == ApplicationStatus.NONE && userSelect != null) {
                viewUser(userSelect);
                showBasicFields();
            }
        });
    }    
    
    private void loadComboBox(){
        RolStatus rolStatus= rolService.readRoles();
        
        switch (rolStatus) {
            case READ_SUCCESS->{
                observableListRoles = FXCollections.observableArrayList(rolService.getRolesList());
                cmbRol.setItems(observableListRoles);
            }
            case EMPTY_LIST->{
                alertInfo.viewAlert("LISTAR ROLES", "No hay roles para listar", 
                    "No existen roles actualmente",
                    "WARN");
            }
            case ERROR_READ_ROLES->{
                alertInfo.viewAlert("ERROR LISTAR ROLES", "Error al listar roles", 
                    "Ocurrió un error al momento de listar los roles.\n"+rolService.getMessageError(),
                    "ERR");
            }
        }
        UserAccountStatus [] listAccStatus = UserAccountStatus.values();
        observableListUserAccountStatus = FXCollections.observableArrayList(listAccStatus);
        cmbStatus.setItems(observableListUserAccountStatus);
    }
    
    /**
    * Desmarca todos los checkboxes de seguridad y deja marcado únicamente
    * el que se envía como parámetro. Si se envía null, desmarca todos.
    */
   private void selectOnlyOneSecurity(CheckBox checkToSelect) {
       for (CheckBox cb : listOptionsSecurity) {
           if (checkToSelect != null && cb.equals(checkToSelect)) {
               cb.setSelected(true);
           } else {
               cb.setSelected(false);
           }
       }
   }
    
    @FXML
    private void onSelectSecurity(ActionEvent event) {
        if (userViewStatus == ApplicationStatus.SAVE || userViewStatus == ApplicationStatus.UPDATE) {
            CheckBox checkSelect = (CheckBox) event.getSource();
            if (checkSelect.isSelected()) {
                // Deja solo este activo y apaga los demás
                selectOnlyOneSecurity(checkSelect);
                optionSecurity = switch (getNameCheckBox(event)) {
                    case "cbUnprotected" -> 1;
                    case "cbMD5" -> 2;
                    case "cbBCrypt" -> 3;
                    default -> 0;
                };
            } else {
                // El usuario hizo clic sobre el que ya estaba marcado para desmarcarlo
                optionSecurity = 0;
            }
        }
    }
    
    private String getNameCheckBox(ActionEvent event){
        String checkBoxSelect = event.getTarget().toString();
        int indexStartId = checkBoxSelect.indexOf("=");
        int indexEndId = checkBoxSelect.indexOf(", ");
        String idCheckBox = checkBoxSelect.substring(indexStartId+1, indexEndId);
        return idCheckBox;
        
        /*
            // Alternativa directa y nativa:
            private String getNameCheckBoxAlternativa(ActionEvent event) {
                CheckBox checkSelect = (CheckBox) event.getSource();
                return checkSelect.getId(); // Devuelve directamente "cbUnprotected", "cbMD5", etc.
            }
        */
    }
    

    
    @FXML
    private void onDashboard(MouseEvent event){
        viewFacto.dashboardView();
    }
    
    @FXML
    private void onCancel(ActionEvent event) {
        userViewStatus = ApplicationStatus.NONE;
        hideAllFields();
        clearAllFields();
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
                case ERROR_READ_USERS->{
                    alertInfo.viewAlert("ERROR LISTAR USUARIOS", "Error al listar usuarios", 
                    "Ocurrió un error al momento de listar datos de los usuarios.\n"+userService.getMessageError(),
                    "ERR");
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

    @FXML
    void onSelectUser(MouseEvent event) {
        selectUser();
    }
    
    private void selectUser(){
        /* OPCION RUSTICA PARA VISUALIZAR LOS DATOS DE UN USUARIO EN LA TABLA
        if( userViewStatus == ApplicationStatus.NONE ){
            UserDTO userSelect = tblUsers.getSelectionModel().getSelectedItem();
            if( userSelect != null ){
                viewUser(userSelect);
                showBasicFields();
            }
        } */
        
    }
    
    private void viewUser(UserDTO userSelected){
        txtIdUser.setText(userSelected.getIdUser());
        txtName.setText(userSelected.getName());
        txtLastName.setText(userSelected.getLastName());
        txtUser.setText(userSelected.getUser());
        txtEmail.setText(userSelected.getEmail());
        cmbStatus.getSelectionModel().select(UserAccountStatus.fromBoolean(userSelected.getStatus()));
        cmbRol.setValue(rolService.searchRolByName(userSelected.getRolName()));
        selectCheckBoxSecurityById(userSelected.getTypeEncrypt());
    }
    
    private void selectCheckBoxSecurityById(int typeEncrypt) {
        switch (typeEncrypt) {
            case 1 -> selectOnlyOneSecurity(cbUnprotected);
            case 2 -> selectOnlyOneSecurity(cbMD5);
            case 3 -> selectOnlyOneSecurity(cbBCrypt);
            default -> selectOnlyOneSecurity(null); // Si es 0 o desconocido, apaga todos
        }
    }
    
    /**
     * MODO SOLO LECTURA (Al hacer clic en la tabla):
     * Muestra todos los campos nítidos, pero bloquea la edición y el teclado.
     */
    private void showBasicFields() {
        // 1. TextFields: Habilitados visualmente pero NO editables (no se puede escribir)
        txtIdUser.setDisable(false);
        txtIdUser.setEditable(false);
        
        txtName.setDisable(false);
        txtName.setEditable(false);
        
        txtLastName.setDisable(false);
        txtLastName.setEditable(false);
        
        txtEmail.setDisable(false);
        txtEmail.setEditable(false);
        
        txtUser.setDisable(false);
        txtUser.setEditable(false);

        // 2. Contraseñas: Bloqueadas en modo consulta
        pwdPassword.setDisable(true);
        pwdConfirmPassword.setDisable(true);

        // 3. ComboBoxes: Visibles pero transparentes al clic y sin foco TAB
        cmbRol.setDisable(false);
        cmbRol.setMouseTransparent(true);
        cmbRol.setFocusTraversable(false); // false = NO recibe foco con TAB

        cmbStatus.setDisable(false);
        cmbStatus.setMouseTransparent(true);
        cmbStatus.setFocusTraversable(false);

        // 4. CheckBoxes de Seguridad: Visibles pero no clickeables
        setSecurityCheckboxesReadOnly(true);
    }

    /**
     * MODO EDICIÓN / AGREGAR (Al pulsar 'Nuevo' o 'Editar'):
     * Habilita la escritura en campos, contraseñas y selección en combos/checks.
     */
    private void showAllFields() {
        // 1. TextFields: Habilitados y editables (excepto ID que es autogenerado)
        txtIdUser.setDisable(false);
        txtIdUser.setEditable(false); // El ID nunca debe ser editable manualmente
        
        txtName.setDisable(false);
        txtName.setEditable(true);
        
        txtLastName.setDisable(false);
        txtLastName.setEditable(true);
        
        txtEmail.setDisable(false);
        txtEmail.setEditable(true);
        
        txtUser.setDisable(false);
        txtUser.setEditable(true);

        // 2. Contraseñas: Habilitadas para escribir
        pwdPassword.setDisable(false);
        pwdConfirmPassword.setDisable(false);

        // 3. ComboBoxes: Habilitados para elegir y accesibles con TAB
        cmbRol.setDisable(false);
        cmbRol.setMouseTransparent(false);
        cmbRol.setFocusTraversable(true); // true = SÍ puede navegar con TAB

        cmbStatus.setDisable(false);
        cmbStatus.setMouseTransparent(false);
        cmbStatus.setFocusTraversable(true);

        // 4. CheckBoxes de Seguridad: Interactivos
        setSecurityCheckboxesReadOnly(false);
    }

    /**
     * MODO INACTIVO / BLOQUEO TOTAL (Estado inicial o Cancelar):
     */
    private void hideAllFields() {
        txtIdUser.setDisable(true);
        txtName.setDisable(true);
        txtLastName.setDisable(true);
        txtEmail.setDisable(true);
        txtUser.setDisable(true);
        
        pwdPassword.setDisable(true);
        pwdConfirmPassword.setDisable(true);
        
        cmbRol.setDisable(true);
        cmbStatus.setDisable(true);

        cbUnprotected.setDisable(true);
        cbMD5.setDisable(true);
        cbBCrypt.setDisable(true);
    }

    /**
     * LIMPIEZA DE FORMULARIO
     */
    private void clearAllFields() {
        txtIdUser.clear();
        txtName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtUser.clear();
        
        pwdPassword.clear();
        pwdConfirmPassword.clear();
        
        cmbRol.setValue(null);
        cmbStatus.setValue(null);
        /*Metodo paso a paso para limpiar 3 checkbox 
        cbUnprotected.setSelected(false);
        cbMD5.setSelected(false);
        cbBCrypt.setSelected(false);*/
        
        // Apaga los 3 checkboxes en 1 sola línea
        selectOnlyOneSecurity(null); 
        
        tblUsers.getSelectionModel().clearSelection();
        optionSecurity = 0;
    }

    /**
     * Helper para no repetir código en los 3 checkboxes de seguridad
     */
    private void setSecurityCheckboxesReadOnly(boolean readOnly) {
        cbUnprotected.setDisable(false);
        cbMD5.setDisable(false);
        cbBCrypt.setDisable(false);

        cbUnprotected.setMouseTransparent(readOnly);
        cbUnprotected.setFocusTraversable(!readOnly);

        cbMD5.setMouseTransparent(readOnly);
        cbMD5.setFocusTraversable(!readOnly);

        cbBCrypt.setMouseTransparent(readOnly);
        cbBCrypt.setFocusTraversable(!readOnly);
    }
}
