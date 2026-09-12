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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.kennethvelasquez.system.model.User;
import org.kennethvelasquez.system.utils.ViewFactory;

/**
 * Controlador del panel principal de la aplicación ({@code DashboardView.fxml}).
 * <p>
 * Pertenece a la <b>Capa de Controladores (Presentation/Controller Layer)</b>.
 * Esta clase actúa como el panel de mando (Dashboard) central de la aplicación una vez
 * que el usuario ha iniciado sesión correctamente, cumpliendo con las siguientes responsabilidades:
 * <ul>
 *   <li><b>Bienvenida personalizada:</b> Obtiene la información del usuario autenticado en la sesión
 *       global a través de {@link AuthenticationController#getUserLogued()} y despliega su nombre completo
 *       en la barra inferior.</li>
 *   <li><b>Barra de navegación dinámica:</b> Administra las opciones superiores (Perfil, Usuarios, Productos, Cerrar Sesión),
 *       marcando visualmente el botón seleccionado mediante la clase CSS {@code active} y desmarcando los demás.</li>
 *   <li><b>Incrustación dinámica de subvistas (SPA - Single Page Application):</b> Gestiona el panel contenedor
 *       {@link #apContentMain}, permitiendo cargar y sustituir componentes o vistas secundarias (por ejemplo,
 *       {@code MyProfileView.fxml}) sin necesidad de recargar ni cambiar toda la ventana principal.</li>
 *   <li><b>Integración con la factoría de vistas:</b> Utiliza {@link ViewFactory} para la instanciación eficiente
 *       y centralizada de las subvistas en memoria.</li>
 * </ul>
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see AuthenticationController
 * @see ViewFactory
 * @see MyProfileController
 */
public class DashboardController implements Initializable {

    /**
     * Etiqueta situada en la barra inferior que presenta el nombre completo del usuario en sesión.
     */
    @FXML private Label lblFullName;

    /**
     * Contenedor raíz de la vista del Dashboard, estructurado en regiones (Top, Center, Bottom).
     */
    @FXML private BorderPane bpDashboardView;

    /**
     * Botón de navegación para finalizar la sesión del usuario actual y retornar a la vista de login.
     */
    @FXML private Button btnLogout;

    /**
     * Botón de navegación superior que permite abrir la vista del perfil de usuario ("Mi Perfil").
     */
    @FXML private Button btnMyPerfil;

    /**
     * Botón de navegación superior para acceder al módulo de gestión del catálogo de productos.
     */
    @FXML private Button btnProducts;

    /**
     * Botón de navegación superior para acceder al módulo de administración de usuarios.
     */
    @FXML private Button btnUsers;

    /**
     * Contenedor horizontal superior que agrupa y alinea los botones de navegación del Dashboard.
     */
    @FXML private HBox hboxOption;

    /**
     * Panel central ({@link AnchorPane}) destinado a hospedar dinámicamente las vistas secundarias incrustadas.
     */
    @FXML private AnchorPane apContentMain;

    /**
     * Instancia de la factoría de vistas ({@link ViewFactory}) para instanciar escenas y componentes FXML.
     */
    private ViewFactory viewFacto = new ViewFactory();

    /**
     * Inicializa el controlador tras la carga del FXML asociado.
     * <p>
     * Recupera el objeto {@link User} del usuario autenticado desde {@link AuthenticationController#getUserLogued()},
     * concatena sus nombres y apellidos, y actualiza el texto de {@link #lblFullName} para dar la bienvenida.
     * </p>
     *
     * @param url Ubicación utilizada para resolver rutas relativas para el objeto raíz, o {@code null} si no se conoce.
     * @param rb Recursos utilizados para localizar el objeto raíz, o {@code null} si no está localizado.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User userLogued = AuthenticationController.getUserLogued();
        if (userLogued != null) {
            String fullName = userLogued.getName() + " " + userLogued.getLastName();
            lblFullName.setText(fullName);
        }
    }

    /**
     * Localiza el índice posicional de un botón dentro del contenedor {@link #hboxOption}.
     * <p>
     * Itera sobre los nodos hijos de {@link #hboxOption}, comprobando si el nodo es una instancia
     * de {@link Button} y si su identificador coincide con {@code idButton}.
     * </p>
     *
     * @param idButton Identificador (ID) del botón cuyo índice se desea encontrar.
     * @return El índice entero en base cero correspondiente a la posición del botón, o {@code null} si no se encuentra.
     */
    private Integer findIndexOptionPressed(String idButton) {
        for (Node children : hboxOption.getChildren()) {
            if (children instanceof Button && children.getId() != null && children.getId().equals(idButton)) {
                return hboxOption.getChildren().indexOf(children);
            }
        }
        return null;
    }

    /**
     * Aplica la clase de estilo CSS {@code active} al botón seleccionado y la remueve de los demás.
     * <p>
     * Recorre todos los botones hijos de {@link #hboxOption}. Si el botón coincide con el índice
     * obtenido a través de {@link #findIndexOptionPressed(String)}, se le añade la clase {@code active}
     * (si no la tenía ya). A todos los demás botones se les remueve dicha clase, garantizando que solo
     * una opción se encuentre visualmente activa a la vez.
     * </p>
     *
     * @param idButton Identificador (ID) del botón sobre el cual se desea activar el estilo visual.
     */
    public void applyStyleOptionPressed(String idButton) {
        Integer indexOptionPressed = findIndexOptionPressed(idButton);
        if (indexOptionPressed == null) {
            return;
        }

        for (int index = 0; index < hboxOption.getChildren().size(); index++) {
            Node node = hboxOption.getChildren().get(index);
            if (node instanceof Button btnOption) {
                if (indexOptionPressed != index) 
                    btnOption.getStyleClass().remove("active");
                 else {
                    if (!btnOption.getStyleClass().contains("active")) 
                        btnOption.getStyleClass().add("active");
                    else
                        btnOption.getStyleClass().remove("active");
                }
            }
        }
    }

    /**
     * Manejador del evento de acción al presionar cualquiera de los botones de opciones superiores.
     * <p>
     * Obtiene la referencia al botón que detonó el evento ({@code event.getTarget()}) y delega
     * en {@link #applyStyleOptionPressed(String)} pasando el ID correspondiente para actualizar el estado visual.
     * </p>
     *
     * @param event Evento de acción disparado por el botón presionado.
     */
    @FXML
    private void onSelectOption(ActionEvent event) {
        Button buttonPressed = (Button) event.getTarget();
        applyStyleOptionPressed(buttonPressed.getId());
    }

    /**
     * Inserta o reemplaza una subvista dentro del panel central {@link #apContentMain}.
     * <p>
     * <b>Mecanismo de conmutación de contenido:</b>
     * <ol>
     *   <li>Si el parámetro {@code component} es {@code null}, la operación se descarta de forma segura.</li>
     *   <li>Si el panel central está vacío ({@code apContentMain.getChildren().isEmpty()}), agrega el nuevo componente directamente.</li>
     *   <li>Si ya existe un componente hospedado y su ID difiere del nuevo componente, se remueve el componente anterior
     *       y se inserta el nuevo, garantizando la alternancia fluida de subvistas.</li>
     * </ol>
     * </p>
     *
     * @param component Componente raíz (nodo {@link Parent}) de la vista que se desea desplegar en el área central.
     */
    private void insertContentAnchorPante(Parent component) {
        if (component == null) {
            return;
        }

        if (apContentMain.getChildren().isEmpty()) {
            apContentMain.getChildren().add(component);
        } else {
            Node firstChild = apContentMain.getChildren().get(0);     
            apContentMain.getChildren().remove(0);
            if (firstChild.getId() == null || !firstChild.getId().equals(component.getId())) {
                apContentMain.getChildren().add(component);
            }
        }
    }

    /**
     * Manejador del evento de clic para la opción "Mi Perfil" ({@link #btnMyPerfil}).
     * <p>
     * Solicita a la factoría {@link ViewFactory#loadComponent(String)} la carga del componente
     * correspondiente a la clave {@code "my-profile"} (vista {@code MyProfileView.fxml}) y la incrusta
     * en el panel principal llamando a {@link #insertContentAnchorPante(Parent)}.
     * </p>
     *
     * @param event Evento del ratón disparado al hacer clic sobre el botón "Mi Perfil".
     */
    @FXML
    private void onMyProfile(MouseEvent event) {
        Parent profileComponent = viewFacto.loadComponent("my-profile");
        insertContentAnchorPante(profileComponent);
        
    }

    /**
     * Método de compatibilidad hacia atrás para invocaciones desde FXML que apunten a {@code onMyPerfil}.
     * <p>
     * Redirige el flujo directamente hacia {@link #onMyProfile(MouseEvent)}.
     * </p>
     *
     * @param event Evento del ratón capturado.
     */
    @FXML
    private void onMyPerfil(MouseEvent event) {
        onMyProfile(event);
    }
}
