/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.kennethvelasquez.system.model.User;
import org.kennethvelasquez.system.utils.AlertInformation;

/**
 * Controlador de la vista de perfil de usuario ({@code MyProfileView.fxml}).
 * <p>
 * Pertenece a la <b>Capa de Controladores (Presentation/Controller Layer)</b> de la aplicación.
 * Esta clase es responsable de:
 * <ul>
 *   <li>Presentar la información del usuario autenticado actualmente en la sesión global.</li>
 *   <li>Gestionar la alternancia entre dos modos de visualización:
 *     <ul>
 *       <li><b>Modo Solo Lectura ({@code gridMyInfo}):</b> Muestra los datos del perfil mediante etiquetas {@link Label}.</li>
 *       <li><b>Modo Edición ({@code gridEditMyInfo}):</b> Despliega campos de texto {@link TextField}, habilitando únicamente
 *           los campos permitidos (nombres y apellidos) y bloqueando los de solo lectura (ID, usuario, correo y rol).</li>
 *     </ul>
 *   </li>
 *   <li>Controlar la visibilidad y el comportamiento dinámico de los botones de acción:
 *     <ul>
 *       <li>El botón "Cancelar" permanece oculto en modo lectura y visible únicamente en modo edición.</li>
 *       <li>El botón principal conmuta su texto ("Editar" &harr; "Guardar") y su estilo CSS ({@code btn-primary} &harr; {@code btn-secondary}).</li>
 *     </ul>
 *   </li>
 *   <li>Garantizar que el contenedor activo siempre ocupe el ancho completo disponible en {@code hboxInfo},
 *       sincronizando las propiedades {@code visible} y {@code managed} de JavaFX con {@link Priority#ALWAYS}.</li>
 *   <li>Validar y aplicar las actualizaciones de los datos del usuario en la sesión activa.</li>
 * </ul>
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see AuthenticationController
 * @see User
 * @see AlertInformation
 */
public class MyProfileController implements Initializable {

    /**
     * Contenedor principal de la vista de perfil.
     */
    @FXML private Pane paneMyProfile;

    /**
     * Contenedor horizontal que aloja los GridPanes de información y edición, permitiendo su expansión dinámica.
     */
    @FXML private HBox hboxInfo;

    /**
     * Contenedor en cuadrícula utilizado para mostrar los datos del usuario en modo solo lectura.
     */
    @FXML private GridPane gridMyInfo;

    /**
     * Contenedor en cuadrícula utilizado para editar los datos permitidos del usuario mediante campos de texto.
     */
    @FXML private GridPane gridEditMyInfo;

    /**
     * Etiqueta que muestra el identificador único del usuario en modo solo lectura.
     */
    @FXML private Label lblInfoIdUser;

    /**
     * Etiqueta que muestra los nombres del usuario en modo solo lectura.
     */
    @FXML private Label lblInfoName;

    /**
     * Etiqueta que muestra los apellidos del usuario en modo solo lectura.
     */
    @FXML private Label lblInfoLastName;

    /**
     * Etiqueta que muestra el nombre de usuario (alias o username) en modo solo lectura.
     */
    @FXML private Label lblInfoUser;

    /**
     * Etiqueta que muestra la dirección de correo electrónico en modo solo lectura.
     */
    @FXML private Label lblInfoEmail;

    /**
     * Etiqueta que muestra el rol o nivel de privilegios asignado al usuario en modo solo lectura.
     */
    @FXML private Label lblInfoRol;

    /**
     * Campo de texto no editable que muestra el ID del usuario en el formulario de edición.
     */
    @FXML private TextField txtEditIdUser;

    /**
     * Campo de texto editable para modificar los nombres del usuario.
     */
    @FXML private TextField txtEditName;

    /**
     * Campo de texto editable para modificar los apellidos del usuario.
     */
    @FXML private TextField txtEditLastName;

    /**
     * Campo de texto no editable que muestra el nombre de usuario en el formulario de edición.
     */
    @FXML private TextField txtEditUser;

    /**
     * Campo de texto no editable que muestra el correo electrónico registrado en el formulario de edición.
     */
    @FXML private TextField txtEditEmail;

    /**
     * Campo de texto no editable que muestra el rol asignado al usuario en el formulario de edición.
     */
    @FXML private TextField txtEditRol;

    /**
     * Botón para cancelar la edición actual, descartar modificaciones y volver al modo solo lectura.
     */
    @FXML private Button btnCancel;

    /**
     * Botón multifunción que conmuta entre iniciar la edición ("Editar") y confirmar los cambios ingresados ("Guardar").
     */
    @FXML private Button btnEdit;

    /**
     * Bandera de estado que indica si la interfaz se encuentra actualmente en modo edición ({@code true}) o solo lectura ({@code false}).
     */
    private boolean isEditing = false;

    /**
     * Referencia a la instancia de {@link User} correspondiente al usuario que ha iniciado sesión en el sistema.
     */
    private User userLogued;

    /**
     * Inicializa el controlador tras la carga del archivo FXML.
     * <p>
     * Configura las reglas de dimensionamiento horizontal para que ambos {@link GridPane}
     * se expandan al ancho completo dentro de {@link #hboxInfo} utilizando {@link Priority#ALWAYS}
     * y {@link Double#MAX_VALUE}. Asimismo, obtiene la instancia del usuario autenticado desde
     * {@link AuthenticationController#getUserLogued()}, carga sus datos en la interfaz y establece
     * el estado inicial en modo solo lectura.
     * </p>
     *
     * @param url Ubicación utilizada para resolver rutas relativas para el objeto raíz, o {@code null} si no se conoce.
     * @param rb Recursos utilizados para localizar el objeto raíz, o {@code null} si no está localizado.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Asegurar que ambos GridPanes siempre ocupen el ancho máximo disponible en hboxInfo
        HBox.setHgrow(gridMyInfo, Priority.ALWAYS);
        HBox.setHgrow(gridEditMyInfo, Priority.ALWAYS);
        gridMyInfo.setMaxWidth(Double.MAX_VALUE);
        gridEditMyInfo.setMaxWidth(Double.MAX_VALUE);

        // Obtener usuario autenticado de la sesión global
        userLogued = AuthenticationController.getUserLogued();
        loadUserData();

        // Estado inicial por defecto:
        // - gridMyInfo visible
        // - gridEditMyInfo oculto
        // - btnCancel oculto
        // - btnEdit visible con texto "Editar" y estilo btn-primary
        setEditMode(false);
    }

    /**
     * Carga y sincroniza la información del usuario autenticado en los componentes de la vista.
     * <p>
     * Asigna los valores del objeto {@link #userLogued} tanto a las etiquetas de texto del
     * modo solo lectura ({@link #gridMyInfo}) como a los campos de entrada del modo edición
     * ({@link #gridEditMyInfo}). En caso de que algún atributo sea nulo, se muestra un guion {@code "-"}
     * o una cadena vacía para mantener la consistencia visual.
     * </p>
     */
    private void loadUserData() {
        if (userLogued != null) {
            // Llenar datos en el grid de solo lectura
            lblInfoIdUser.setText(userLogued.getIdUser() != null ? userLogued.getIdUser() : "-");
            lblInfoName.setText(userLogued.getName() != null ? userLogued.getName() : "-");
            lblInfoLastName.setText(userLogued.getLastName() != null ? userLogued.getLastName() : "-");
            lblInfoUser.setText(userLogued.getUser() != null ? userLogued.getUser() : "-");
            lblInfoEmail.setText(userLogued.getEmail() != null ? userLogued.getEmail() : "-");
            lblInfoRol.setText(userLogued.getRol() != null ? String.valueOf(userLogued.getRol()) : "-");

            // Llenar datos en el formulario de edición
            txtEditIdUser.setText(userLogued.getIdUser() != null ? userLogued.getIdUser() : "");
            txtEditName.setText(userLogued.getName() != null ? userLogued.getName() : "");
            txtEditLastName.setText(userLogued.getLastName() != null ? userLogued.getLastName() : "");
            txtEditUser.setText(userLogued.getUser() != null ? userLogued.getUser() : "");
            txtEditEmail.setText(userLogued.getEmail() != null ? userLogued.getEmail() : "");
            txtEditRol.setText(userLogued.getRol() != null ? String.valueOf(userLogued.getRol()) : "");
        }
    }

    /**
     * Conmuta la interfaz de usuario entre el modo de solo lectura y el modo de edición.
     * <p>
     * <b>Manejo del Ancho y Layout en JavaFX:</b><br>
     * Para lograr que el contenedor activo ocupe el 100% del ancho del {@link #hboxInfo},
     * no basta con alternar {@code setVisible()}. Se debe alternar simultáneamente la propiedad
     * {@code setManaged()}:
     * <ul>
     *   <li>Al establecer {@code setManaged(false)} en el contenedor inactivo, el motor de layout
     *       de JavaFX ignora completamente dicho nodo y no le reserva espacio horizontal.</li>
     *   <li>El contenedor activo con {@code setManaged(true)} y {@link Priority#ALWAYS} toma
     *       inmediatamente la totalidad del ancho disponible en el contenedor padre.</li>
     * </ul>
     * </p>
     * <p>
     * Adicionalmente, este método:
     * <ul>
     *   <li>Oculta o muestra el botón "Cancelar" ({@link #btnCancel}).</li>
     *   <li>Actualiza el texto del botón principal ({@link #btnEdit}) entre "Editar" y "Guardar".</li>
     *   <li>Modifica dinámicamente las clases de estilo CSS del botón principal entre {@code btn-primary} y {@code btn-secondary}.</li>
     * </ul>
     * </p>
     *
     * @param editMode {@code true} para activar el modo de edición; {@code false} para regresar al modo de solo lectura.
     */
    private void setEditMode(boolean editMode) {
        this.isEditing = editMode;

        // 1. GridPanes: solo el grid activo es visible y gestionado por el layout
        gridMyInfo.setVisible(!editMode);
        gridMyInfo.setManaged(!editMode);

        gridEditMyInfo.setVisible(editMode);
        gridEditMyInfo.setManaged(editMode);

        // 2. Botón Cancelar: oculto en lectura, visible solo en edición
        btnCancel.setVisible(editMode);
        btnCancel.setManaged(editMode);

        // 3. Botón de acción: cambia texto y clases CSS (btn-primary <-> btn-secondary)
        if (editMode) {
            btnEdit.setText("Guardar");
            btnEdit.getStyleClass().remove("btn-primary");
            if (!btnEdit.getStyleClass().contains("btn-secondary")) {
                btnEdit.getStyleClass().add("btn-secondary");
            }
        } else {
            btnEdit.setText("Editar");
            btnEdit.getStyleClass().remove("btn-secondary");
            if (!btnEdit.getStyleClass().contains("btn-primary")) {
                btnEdit.getStyleClass().add("btn-primary");
            }
        }
    }

    /**
     * Gestiona el evento de clic en el botón principal de la vista ({@link #btnEdit}).
     * <p>
     * Evalúa el estado actual de la interfaz ({@link #isEditing}):
     * <ul>
     *   <li>Si está en <b>modo solo lectura</b>: cambia la vista al modo edición mediante {@code setEditMode(true)}.</li>
     *   <li>Si está en <b>modo edición</b>: procede a validar y persistir los cambios mediante {@link #saveChanges()}.</li>
     * </ul>
     * </p>
     *
     * @param event Evento de acción generado al interactuar con el botón.
     */
    @FXML
    private void onActionEdit(ActionEvent event) {
        if (!isEditing) {
            setEditMode(true);
        } else {
            saveChanges();
        }
    }

    /**
     * Gestiona el evento de clic en el botón Cancelar ({@link #btnCancel}).
     * <p>
     * Descarta cualquier cambio no guardado en los campos de edición recargando los datos
     * originales del usuario mediante {@link #loadUserData()}, y restablece la interfaz
     * al modo de solo lectura llamando a {@code setEditMode(false)}.
     * </p>
     *
     * @param event Evento de acción generado al pulsar el botón Cancelar.
     */
    @FXML
    private void onCancel(ActionEvent event) {
        loadUserData();
        setEditMode(false);
    }

    /**
     * Valida y aplica las modificaciones realizadas sobre los campos editables del perfil.
     * <p>
     * Ejecuta el siguiente procedimiento:
     * <ol>
     *   <li>Extrae y limpia ({@code trim}) el texto ingresado en los campos de nombres y apellidos.</li>
     *   <li>Verifica que los campos obligatorios no estén vacíos. Si alguno está vacío, detiene el flujo
     *       y muestra una alerta de advertencia mediante {@link AlertInformation}.</li>
     *   <li>Actualiza las propiedades {@code name} y {@code lastName} en el objeto {@link #userLogued} de la sesión actual.</li>
     *   <li>Refresca las etiquetas de la vista de lectura con los nuevos valores invocando {@link #loadUserData()}.</li>
     *   <li>Regresa la vista al modo de solo lectura con {@code setEditMode(false)}.</li>
     *   <li>Despliega una alerta informativa notificando que los cambios se guardaron con éxito.</li>
     * </ol>
     * </p>
     */
    private void saveChanges() {
        String newName = txtEditName.getText().trim();
        String newLastName = txtEditLastName.getText().trim();

        if (newName.isEmpty() || newLastName.isEmpty()) {
            AlertInformation alert = new AlertInformation("CAMPOS VACÍOS", "Error al actualizar",
                    "El nombre y el apellido no pueden quedar vacíos.", "WARN");
            alert.viewAlert();
            return;
        }

        // Actualizar datos del usuario actual
        if (userLogued != null) {
            userLogued.setName(newName);
            userLogued.setLastName(newLastName);
        }

        // Refrescar vistas y regresar a modo lectura
        loadUserData();
        setEditMode(false);

        AlertInformation alert = new AlertInformation("PERFIL ACTUALIZADO", "Cambios guardados",
                "Tu información ha sido actualizada exitosamente.", "INFO");
        alert.viewAlert();
    }
}
