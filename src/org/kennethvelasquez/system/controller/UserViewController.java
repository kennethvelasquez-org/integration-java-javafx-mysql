/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import org.kennethvelasquez.system.utils.Validations;
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
    @FXML
    private Label lblConfirmPassword;

    @FXML
    private Label lblPassword;
    
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
    private Validations validate = new Validations();
    private Integer optionSecurity=0;
    private UserDTO userSelect;
    private boolean isChangePassword=false;
    
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
        // Metodo equivalente al metodo onSelectUser
        // EJEMPLO DE ESTADO, como no tengo un estado view tengo que controlar los botones manualmente siempre en este metodo
        tblUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldUser, userSelect) -> {
            this.userSelect = userSelect;
            // El tercer parámetro 'userSelect' ya es el nuevo usuario seleccionado
            if (userViewStatus == ApplicationStatus.NONE && userSelect != null) {
                viewUser();
                showBasicFields();
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
                btnCancel.setDisable(false);
                btnSearch.setDisable(true);
                btnCreate.setDisable(true);
                btnRead.setDisable(true);
            }
        });
        
        pwdPassword.textProperty().addListener(onInputChangePwd);
        pwdConfirmPassword.textProperty().addListener(onInputChangePwd);
        pwdPassword.focusedProperty().addListener(onFocusChangedPwd);
        pwdConfirmPassword.focusedProperty().addListener(onFocusChangedPwd);
        
        controlOptionsCRUD();
    }    
    
    ChangeListener<Boolean>onFocusChangedPwd = (observable, oldValue, newValue)->{
        if( userViewStatus == ApplicationStatus.SAVE ){
            boolean reset = !validate.isEmptyText(pwdPassword.getText()) && !validate.isEmptyText(pwdConfirmPassword.getText());
            isChangePassword = reset;
            if(isChangePassword == false && userViewStatus==ApplicationStatus.SAVE){
                selectCheckBoxSecurityById(optionSecurity = userSelect.getTypeEncrypt());
            }
            setSecurityCheckboxesReadOnly(!isChangePassword);
        }
    };
    
    ChangeListener<String> onInputChangePwd = (observable, oldValue, newValue)->{
        boolean reset = !validate.isEmptyText(pwdPassword.getText()) && !validate.isEmptyText(pwdConfirmPassword.getText());
            isChangePassword = reset;
            if(isChangePassword == false && userViewStatus==ApplicationStatus.SAVE)
                selectCheckBoxSecurityById(optionSecurity = userSelect.getTypeEncrypt());
            setSecurityCheckboxesReadOnly(!isChangePassword);
    };
    
    private void controlOptionsCRUD(){
        switch (userViewStatus) {
            case NONE->{
                btnCreate.setDisable(false);
                btnUpdate.setDisable(true);
                btnRead.setDisable(false);
                btnDelete.setDisable(true);
                btnCancel.setDisable(true);
            }
            case CREATE->{
                btnCreate.setDisable(false);
                btnRead.setDisable(true);
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
                btnCancel.setDisable(false);
            }
            case SAVE->{
                btnCreate.setDisable(true);
                btnRead.setDisable(true);
                btnUpdate.setDisable(false);
                btnDelete.setDisable(true);
                btnCancel.setDisable(false);
            }
            case DELETE->{
                btnCreate.setDisable(true);
                btnRead.setDisable(true);
                btnUpdate.setDisable(true);
                btnDelete.setDisable(false);
                btnCancel.setDisable(false);
            }
        }
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
        if (userViewStatus == ApplicationStatus.CREATE || userViewStatus == ApplicationStatus.SAVE) {
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
        switch (userViewStatus) {
            case CREATE->{
                btnCreate.setText("AGREGAR");
            }
            case SAVE->{
                btnUpdate.setText("EDITAR");
                lblPassword.setText("Contraseña");
                lblConfirmPassword.setText("Confirmar Contraseña");
            }
        }
        userViewStatus = ApplicationStatus.NONE;
        hideAllFields();
        clearAllFields();
        controlOptionsCRUD();
    }
    
    @FXML
    private void onCreate(ActionEvent event) {
        
    }

    private void deleteUser(UserDTO userSelect){
        alertInfo.viewAlert("ELIMINAR USUARIO",
                        "¿Estás seguro de eliminar?",
                        "Has seleccionado un usuario para eliminar\n"
                        + "Al validar que el usuario es el correcto puedes continuar", 
                        "CONFIRM");
        if( alertInfo.isConfirmed() ){
            UserStatus userStatus = userService.deleteUser(userSelect.getIdUser(),
                                                    userSelect.getName(),
                                                    userSelect.getEmail());
            switch (userStatus) {
                case USER_DELETED -> {
                    alertInfo.viewAlert("ELIMINAR USUARIO",
                        "Se ha eliminado el usuario!!",
                        "El usuario que ha seleccionado se ha eliminado.\n"
                        + "Si quieres activar el usuario tienes que editar su estado", 
                        "INFO");
                    userService.readUsers();
                    loadTableUsers();   // Refresca el TableView con los datos actualizados
                }
                case USER_NOT_FOUND -> 
                    alertInfo.viewAlert(
                        "USUARIO NO ENCONTRADO",
                        "Registro inexistente",
                        "El usuario indicado no existe o ya fue eliminado del sistema.",
                        "WARN"
                    );
                case ERROR_USER_SEARCH -> 
                    alertInfo.viewAlert(
                        "ERROR DE COMPROBACIÓN",
                        "Error al verificar usuario",
                        "Ocurrió un error al intentar verificar la existencia del usuario:\n" 
                                + userService.getMessageError(),
                        "ERR"
                    );
                case ERROR_USER_DELETE -> 
                    alertInfo.viewAlert(
                        "ERROR AL ELIMINAR",
                        "Fallo en base de datos",
                        "Ocurrió un error inesperado al desactivar el usuario:\n" 
                                + userService.getMessageError(),
                        "ERR"
                    );
                default -> 
                    alertInfo.viewAlert(
                        "ERROR DESCONOCIDO",
                        "Estado no controlado",
                        "Respuesta no controlada del servicio de usuarios.",
                        "ERR"
                    );
            }
        }else{
            alertInfo.viewAlert("ELIMINAR USUARIO",
                "No se ha eliminado el usuario",
                "El usuario seleccionado no se ha eliminado",
                "INFO");
        }
        clearAllFields();   // Limpia los campos del formulario
        hideAllFields(); //Oculto todos los campos
        userViewStatus = ApplicationStatus.NONE;
        controlOptionsCRUD();
    }
    
    @FXML
    private void onDelete(ActionEvent event) {
        if( userViewStatus == ApplicationStatus.NONE ){
            userSelect = tblUsers.getSelectionModel().getSelectedItem();
            if( userSelect != null ){
                userViewStatus = ApplicationStatus.DELETE;
                controlOptionsCRUD();
                viewUser();
                showBasicFields();
                deleteUser(userSelect);
            }else{
                alertInfo.viewAlert("ELIMINAR USUARIO", "No se seleccionó usuario", 
                        "No ha elegido un usuario en la tabla.\nTiene que seleccionar un usuario para eliminar.",
                        "WARN");
            }
        }
    }
    
    private void loadTableUsers(){
        observableListUsers = FXCollections.observableArrayList(userService.getUsersList());
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
        if( userViewStatus  == ApplicationStatus.NONE){
            UserStatus userStatus = userService.readUsers();
            switch (userStatus) {
                case READ_SUCCESS->{
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
        userViewStatus = ApplicationStatus.SEARCH;
        clearAllFields();
        hideAllFields();
    }
    
    @FXML
    private void onUpdate(ActionEvent event) {
        switch(userViewStatus){
            case NONE->{
                if( userSelect != null ){
                    userViewStatus = ApplicationStatus.SAVE;
                    controlOptionsCRUD();
                    btnUpdate.setText("GUARDAR");
                    lblPassword.setText("Contraseña (opcional)");
                    lblConfirmPassword.setText("Confirmar Contraseña (opcional)");
                    showAllFields();
                    setSecurityCheckboxesReadOnly(!isChangePassword);
                }else{
                    alertInfo.viewAlert("EDITAR USUARIO", "No se seleccionó usuario", 
                            "No ha elegido un usuario en la tabla.\nTiene que seleccionar un usuario para editar.",
                            "WARN");
                }
            }
            case SAVE->{
                boolean fieldsValid = isValidAllFields();
                if( fieldsValid == true ){
                    String idUser = txtIdUser.getText().trim();
                    String name = txtName.getText().trim();
                    String lastName = txtLastName.getText().trim();
                    String email = txtEmail.getText().trim();
                    String user = txtUser.getText().trim();
                    String password = pwdPassword.getText().trim();
                    Rol rolSelect = cmbRol.getSelectionModel().getSelectedItem();
                    UserAccountStatus userAccStatus = cmbStatus.getSelectionModel().getSelectedItem();
                    btnUpdate.setText("EDITAR");
                    lblPassword.setText("Contraseña");
                    lblConfirmPassword.setText("Confirmar Contraseña");
                    
                    UserStatus userStatus = userService.updateUser(
                            idUser, name,  lastName, email,
                            user,password, rolSelect.getIdRol(),
                            optionSecurity, userAccStatus.isActive()
                    );
                    
                    switch (userStatus) {
                        case USER_UPDATED -> {
                            alertInfo.viewAlert(
                                "ACTUALIZACIÓN EXITOSA",
                                "Usuario actualizado",
                                "Los datos del usuario han sido modificados exitosamente.",
                                "INFO"
                            );
                            userService.readUsers();
                            loadTableUsers();
                        }
                        case USER_EXISTS -> 
                            alertInfo.viewAlert(
                                "CUENTA DUPLICADA",
                                "Usuario o correo ya registrado",
                                "El nombre de usuario o correo electrónico ya pertenece a otra cuenta registrada.",
                                "WARN"
                        );
                        case INCORRECT_ENCRYPT_TYPE -> 
                            alertInfo.viewAlert(
                                "ERROR DE ENCRIPTACIÓN",
                                "Seguridad no válida",
                                "Debe seleccionar una opción válida de encriptación para la nueva contraseña.",
                                "WARN"
                        );
                        case ERROR_USER_UPDATE -> 
                            alertInfo.viewAlert(
                                "ERROR AL ACTUALIZAR",
                                "Fallo en base de datos",
                                "Ocurrió un error inesperado al modificar la información del usuario:\n" 
                                        + userService.getMessageError(),
                                "ERR"
                        );
                        default -> 
                            alertInfo.viewAlert(
                                "ERROR DESCONOCIDO",
                                "Estado no controlado",
                                "Respuesta no controlada del servicio de usuarios.",
                                "ERR"
                            );
                    }
                    userViewStatus = ApplicationStatus.NONE;
                    controlOptionsCRUD();
                    hideAllFields();
                    clearAllFields();
                    setSecurityCheckboxesReadOnly(!isChangePassword);
                }
            }
        }
    }
    
    public boolean isValidAllFields(){
        String name = txtName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String user = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        String password = pwdPassword.getText().trim();
        String confirmPassword = pwdConfirmPassword.getText().trim();
        Rol rolSelect = cmbRol.getSelectionModel().getSelectedItem();
        UserAccountStatus userAccStatus = cmbStatus.getSelectionModel().getSelectedItem();
        
        //Validacion campos vacios
        if (validate.isEmptyText(name) || validate.isEmptyText(lastName) 
                || validate.isEmptyText(user) || validate.isEmptyText(email)) {
            alertInfo.viewAlert("CAMPOS VACIOS", "Error de campos vacios", 
                    "Ha dejado campos vacios en el formulario\nASEGURESE DE LLENAR TODOS LOS CAMPOS",
                    "WARN");
            return false;
        }  
        
        //Valdiar el correo electronico 
        if(validate.isValidEmail(email)==false){
            alertInfo.viewAlert("EMAIL INCORRECTO", "Error de formato de email", 
                    "El email ingresado no es correcto\n"
                    + "ASEGURESE DE INGRESAR UN EMAIL VALIDO\n"
                    + "Ejemplo: pepito@gmail.com",
                    "WARN");
            return false;
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
        
        if( (isChangePassword ==true && userViewStatus == ApplicationStatus.SAVE) ||
                userViewStatus == ApplicationStatus.CREATE){
            if (!validate.isValidLengthText(password, 70)) {
                msg = "El campo Contraseña no puede exceder los 70 caracteres.";
            }
            
            if( validate.isEqualsText(password, confirmPassword) ==false){
                alertInfo.viewAlert("ERROR DE CONTRASEÑAS", "Error de contraseñas desiguales", 
                        "Las contraseñas ingresadas no coinciden.",
                        "ERR");
                return false;
            }
            
            //validacion de elegir el tipo de serguridad en la clave
            if (optionSecurity==null || optionSecurity == 0) {
                alertInfo.viewAlert("SEGURIDAD DE CONTRASEÑA", "Error al elegir la seguridad de contraseña", 
                        "ELIJA UNA OPCION CON LA QUE SE ENCRIPTARÁ LA CONTRASEÑA",
                        "ERR");
                return false;
            }
        }
        if( !msg.trim().equals("")){
            alertInfo.viewAlert("ERROR DE TEXTO", "Error en la cantidad de letras", 
                    msg,
                    "ERR");
            return false;
        }
        
        if( rolSelect == null){
            alertInfo.viewAlert("ROL DE USUARIO", "No ha elegido rol", 
                        "Elija un Rol para el usuario",
                        "ERR");
                return false;
        }
        if( userAccStatus == null){
            alertInfo.viewAlert("ESTADO DE USUARIO", "No hay estado de usuario", 
                        "Elija un Estado para el usuario",
                        "ERR");
                return false;
        }
        
        return true;
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
    
    private void viewUser(){
        txtIdUser.setText(userSelect.getIdUser());
        txtName.setText(userSelect.getName());
        txtLastName.setText(userSelect.getLastName());
        txtUser.setText(userSelect.getUser());
        txtEmail.setText(userSelect.getEmail());
        cmbStatus.getSelectionModel().select(UserAccountStatus.fromBoolean(userSelect.getStatus()));
        cmbRol.setValue(rolService.searchRolByName(userSelect.getRolName()));
        optionSecurity =userSelect.getTypeEncrypt();
        selectCheckBoxSecurityById(optionSecurity);
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
        
        // 3. Contraseñas: Desbloqueadas
        pwdPassword.setEditable(true);
        pwdConfirmPassword.setEditable(true);

        // 4. ComboBoxes: Habilitados para elegir y accesibles con TAB
        cmbRol.setDisable(false);
        cmbRol.setMouseTransparent(false);
        cmbRol.setFocusTraversable(true); // true = SÍ puede navegar con TAB

        cmbStatus.setDisable(false);
        cmbStatus.setMouseTransparent(false);
        cmbStatus.setFocusTraversable(true);

        // 5. CheckBoxes de Seguridad: Interactivos
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
        
        pwdPassword.setEditable(false);
        pwdConfirmPassword.setEditable(false);
        
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
