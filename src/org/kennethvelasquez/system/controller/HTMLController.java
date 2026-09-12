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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kennethvelasquez.system.utils.AlertInformation;

/**
 * Controlador de la vista de demostración del editor HTML ({@code HTMLView.fxml}).
 * <p>
 * Pertenece a la <b>Capa de Controladores (Presentation/Controller Layer)</b>.
 * Esta clase ejemplifica la navegación interna modular mediante subvistas incrustadas en el
 * panel central del Dashboard ({@code AnchorPane apContentMain}), utilizando el control
 * especializado {@link HTMLEditor} para la edición de texto enriquecido y generación de código HTML.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see DashboardController
 * @see org.kennethvelasquez.system.utils.ViewFactory
 * @see AlertInformation
 */
public class HTMLController implements Initializable {

    /**
     * Contenedor principal de la vista del editor HTML.
     */
    @FXML private Pane paneHtmlView;

    /**
     * Control visual interactivo para redacción y formateo de texto enriquecido (WYSIWYG).
     */
    @FXML private HTMLEditor htmlEditor;

    /**
     * Botón para restaurar la plantilla HTML inicial por defecto.
     */
    @FXML private Button btnReset;

    /**
     * Botón para visualizar el código fuente HTML generado por el editor.
     */
    @FXML private Button btnGetHtml;

    /**
     * Botón para simular el guardado del contenido redactado.
     */
    @FXML private Button btnSave;

    /**
     * Contenido HTML pedagógico inicial predeterminado.
     */
    private static final String DEFAULT_HTML_CONTENT = 
            "<body style='font-family: sans-serif; color: #333333;'>"
            + "<h2 style='color: #0077b6;'>Demostración de HTMLEditor en JavaFX</h2>"
            + "<p>Esta subvista fue cargada dinámicamente utilizando <b>ViewFactory.loadComponent(\"html-view\")</b>.</p>"
            + "<p>Puedes utilizar la barra de herramientas superior para dar formato a tu texto:</p>"
            + "<ul>"
            + "  <li><b>Negrita</b>, <i>cursiva</i> y <u>subrayado</u>.</li>"
            + "  <li>Listas numeradas y viñetas.</li>"
            + "  <li>Alineación de párrafos y cambios de color tipográfico.</li>"
            + "</ul>"
            + "<p style='color: #666666;'><i>Edita este texto y pulsa 'Ver Código HTML' o 'Guardar'.</i></p>"
            + "</body>";

    /**
     * Inicializa el controlador tras la carga del FXML.
     * Carga el contenido inicial en el editor HTML.
     *
     * @param url Ubicación utilizada para resolver rutas relativas para el objeto raíz, o {@code null} si no se conoce.
     * @param rb Recursos utilizados para localizar el objeto raíz, o {@code null} si no está localizado.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDefaultContent();
    }

    /**
     * Carga el contenido HTML demostrativo inicial en el editor.
     */
    private void loadDefaultContent() {
        if (htmlEditor != null) {
            htmlEditor.setHtmlText(DEFAULT_HTML_CONTENT);
        }
    }

    /**
     * Manejador del botón "Restablecer".
     * Restaura el contenido predeterminado del editor descartando las modificaciones realizadas.
     *
     * @param event Evento de acción del botón.
     */
    @FXML
    private void onReset(ActionEvent event) {
        loadDefaultContent();
    }

    /**
     * Manejador del botón "Ver Código HTML".
     * Extrae el código fuente HTML generado por {@link HTMLEditor#getHtmlText()}
     * y lo presenta al usuario en un escenario extra, en este caso hacemos una excepcion
     * de la nomenclatura de bloque que seguiamos: atributos> constructor > metodos.
     *
     * @param event Evento de acción del botón.
     */
    @FXML
    private void onGetHtml(ActionEvent event) {
        String generatedHtml = htmlEditor.getHtmlText();
        
        Stage stageExtra = new Stage();
        stageExtra.initModality(Modality.WINDOW_MODAL);
        stageExtra.initStyle(StageStyle.UNDECORATED);
        
        Pane paneContentHTML = new Pane();
        paneContentHTML.setPrefHeight(650);
        paneContentHTML.setPrefWidth(1280);
        paneContentHTML.setBackground(new Background(
            new BackgroundFill(Paint.valueOf("#fff6"), CornerRadii.EMPTY, Insets.EMPTY)
        ));
        
        TextArea txtAreaHTML = new TextArea();
        txtAreaHTML.setPrefWidth(1250);
        txtAreaHTML.setPrefHeight(585);
        txtAreaHTML.setLayoutX(15);
        txtAreaHTML.setLayoutY(45);
        txtAreaHTML.setWrapText(true);
        txtAreaHTML.setText(generatedHtml);
        
        Button btnExit = new Button("CERRAR CODE HTML");
        
        btnExit.setLayoutX(15);
        btnExit.setLayoutY(15);
        
        btnExit.setOnAction((t) -> {
            stageExtra.close();
        });
        paneContentHTML.getChildren().addAll(btnExit,txtAreaHTML);
        
        Scene sceneExtra = new Scene(paneContentHTML, 1280, 650);
        stageExtra.setScene(sceneExtra);
        stageExtra.sizeToScene();
        stageExtra.centerOnScreen();
        stageExtra.showAndWait();
    }

    /**
     * Manejador del botón "Guardar".
     * Simula la persistencia del contenido HTML generado y notifica al usuario.
     *
     * @param event Evento de acción del botón.
     */
    @FXML
    private void onSave(ActionEvent event) {
        String htmlContent = htmlEditor.getHtmlText();

        AlertInformation alert = new AlertInformation("CONTENIDO GUARDADO", "Operación Exitosa",
                "El documento HTML ha sido procesado exitosamente.", "INFO");
        alert.viewAlert();
    }
}
