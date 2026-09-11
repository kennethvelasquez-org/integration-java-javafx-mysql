
package org.kennethvelasquez.system.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.Locale;

/**
 * 
 * CLASE GENERADA CON IA https://chat.qwen.ai/s/6f405266-ab15-45ff-85d1-22a1a6e6462c?fev=0.2.91
 * 
 * Clase utilitaria para construir y mostrar alertas de JavaFX.
 * <p>
 * Esta clase permite crear alertas a partir de los atributos internos de la
 * instancia o a partir de parámetros enviados directamente a los métodos
 * {@code viewAlert}.
 * </p>
 *
 * <p>
 * También permite asociar una imagen de 45px x 45px, conservando la proporción
 * original, siempre que la imagen sea un recurso válido del proyecto.
 * </p>
 *  * @author STEPHRYS
 */
public class AlertInformation {

    /**
     * Tamaño máximo deseado para la imagen mostrada en la alerta.
     */
    private static final int IMAGE_SIZE = 45;

    /**
     * Mensaje mostrado cuando no se puede cargar la imagen indicada.
     */
    private static final String IMAGE_LOAD_ERROR_MESSAGE = "- Error carga de imagen";

    /**
     * Cabecera o encabezado del mensaje de la alerta.
     */
    private String messageHead;

    /**
     * Título de la alerta.
     */
    private String title;

    /**
     * Mensaje principal o cuerpo de la alerta.
     */
    private String message;

    /**
     * Tipo de alerta representado como texto.
     * <p>
     * Ejemplos: "INFO", "INFORMATION", "WARNING", "WARN", "ERROR", etc.
     * </p>
     */
    private String type;

    /**
     * Constructor vacío.
     * <p>
     * Crea una instancia de {@code AlertInformation} sin inicializar atributos.
     * </p>
     */
    public AlertInformation() {
        // Constructor vacío intencional.
    }

    /**
     * Constructor con todos los atributos de la alerta.
     *
     * @param messageHead Cabecera o encabezado de la alerta.
     * @param title       Título de la alerta.
     * @param message     Mensaje principal de la alerta.
     * @param type        Tipo de alerta en formato texto.
     */
    public AlertInformation(String messageHead, String title, String message, String type) {
        this.messageHead = messageHead;
        this.title = title;
        this.message = message;
        this.type = type;
    }

    /**
     * Obtiene la cabecera o encabezado de la alerta.
     *
     * @return Cabecera de la alerta.
     */
    public String getMessageHead() {
        return messageHead;
    }

    /**
     * Establece la cabecera o encabezado de la alerta.
     *
     * @param messageHead Cabecera de la alerta.
     */
    public void setMessageHead(String messageHead) {
        this.messageHead = messageHead;
    }

    /**
     * Obtiene el título de la alerta.
     *
     * @return Título de la alerta.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Establece el título de la alerta.
     *
     * @param title Título de la alerta.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene el mensaje principal o cuerpo de la alerta.
     *
     * @return Mensaje principal de la alerta.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje principal o cuerpo de la alerta.
     *
     * @param message Mensaje principal de la alerta.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene el tipo de alerta en formato texto.
     *
     * @return Tipo de alerta en formato texto.
     */
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo de alerta en formato texto.
     *
     * @param type Tipo de alerta en formato texto.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Muestra una alerta utilizando los atributos actuales de la instancia.
     * <p>
     * Este método construye la alerta con los valores almacenados en:
     * </p>
     * <ul>
     * <li>{@link #messageHead}</li>
     * <li>{@link #title}</li>
     * <li>{@link #message}</li>
     * <li>{@link #type}</li>
     * </ul>
     *
     * <p>
     * La alerta se muestra mediante {@code showAndWait()}, por lo que la
     * ejecución queda bloqueada hasta que el usuario cierre la alerta.
     * </p>
     *
     * <p>
     * Este método debe ejecutarse en el hilo de aplicación de JavaFX.
     * </p>
     */
    public void viewAlert() {
        Alert alert = buildAlert(this.messageHead, this.title, this.message, this.type);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta utilizando los parámetros recibidos.
     * <p>
     * Si los atributos internos de la instancia están vacíos o nulos, este
     * método los actualizará con los valores recibidos. Si ya tienen valor,
     * no serán sobrescritos.
     * </p>
     *
     * <p>
     * La alerta mostrada se construye con los valores enviados como parámetros.
     * </p>
     *
     * @param messageHead Cabecera o encabezado de la alerta.
     * @param title       Título de la alerta.
     * @param message     Mensaje principal de la alerta.
     * @param type        Tipo de alerta en formato texto.
     */
    public void viewAlert(String messageHead, String title, String message, String type) {
        updateAttributesIfBlank(messageHead, title, message, type);

        Alert alert = buildAlert(messageHead, title, message, type);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta utilizando los parámetros recibidos y una imagen
     * cargada desde un recurso del proyecto.
     * <p>
     * Si los atributos internos de la instancia están vacíos o nulos, este
     * método los actualizará con los valores recibidos. Si ya tienen valor,
     * no serán sobrescritos.
     * </p>
     *
     * <p>
     * La imagen indicada en {@code imageURL} se mostrará con un tamaño máximo
     * de 45px x 45px, conservando la proporción original.
     * </p>
     *
     * <p>
     * Si la imagen no puede cargarse, la alerta se mostrará sin imagen y la
     * cabecera y el cuerpo contendrán el mensaje:
     * </p>
     *
     * <pre>
     * - Error carga de imagen
     * </pre>
     *
     * @param messageHead Cabecera o encabezado de la alerta.
     * @param title       Título de la alerta.
     * @param message     Mensaje principal de la alerta.
     * @param type        Tipo de alerta en formato texto.
     * @param imageURL    Ruta del recurso del proyecto correspondiente a la imagen.
     */
    public void viewAlert(String messageHead,
                          String title,
                          String message,
                          String type,
                          String imageURL) {

        updateAttributesIfBlank(messageHead, title, message, type);

        Alert alert = buildAlert(messageHead, title, message, type);
        applyImage(alert, imageURL);

        alert.showAndWait();
    }

    /**
     * Convierte un tipo de alerta en formato texto a su equivalente
     * {@link AlertType}.
     * <p>
     * Este método acepta mayúsculas, minúsculas y espacios en blanco antes o
     * después del valor.
     * </p>
     *
     * <p>
     * Valores aceptados:
     * </p>
     *
     * <ul>
     * <li>{@code "INFO"} o {@code "INFORMATION"} retorna {@link AlertType#INFORMATION}</li>
     * <li>{@code "WARN"} o {@code "WARNING"} retorna {@link AlertType#WARNING}</li>
     * <li>{@code "ERR"} o {@code "ERROR"} retorna {@link AlertType#ERROR}</li>
     * <li>{@code "CONFIRM"} o {@code "CONFIRMATION"} retorna {@link AlertType#CONFIRMATION}</li>
     * <li>{@code "NONE"} retorna {@link AlertType#NONE}</li>
     * </ul>
     *
     * <p>
     * Si el valor recibido es nulo, vacío o no corresponde a ninguno de los
     * tipos contemplados, se retorna {@link AlertType#INFORMATION}.
     * </p>
     *
     * @param type Tipo de alerta en formato texto.
     * @return Valor {@link AlertType} correspondiente.
     */
    public AlertType getAlertType(String type) {
        if (isBlank(type)) {
            return AlertType.INFORMATION;
        }

        String normalizedType = type.trim().toUpperCase(Locale.ROOT);

        return switch (normalizedType) {
            case "WARN", "WARNING" -> AlertType.WARNING;
            case "INFO", "INFORMATION" -> AlertType.INFORMATION;
            case "ERR", "ERROR" -> AlertType.ERROR;
            case "CONFIRM", "CONFIRMATION" -> AlertType.CONFIRMATION;
            case "NONE" -> AlertType.NONE;
            default -> AlertType.INFORMATION;
        };
    }

    /**
     * Construye una instancia de {@link Alert} sin mostrarla.
     *
     * @param messageHead Cabecera o encabezado de la alerta.
     * @param title       Título de la alerta.
     * @param message     Mensaje principal de la alerta.
     * @param type        Tipo de alerta en formato texto.
     * @return Objeto {@link Alert} configurado.
     */
    private Alert buildAlert(String messageHead, String title, String message, String type) {
        Alert alert = new Alert(getAlertType(type));

        alert.setTitle(title);
        alert.setHeaderText(messageHead);
        alert.setContentText(message);

        return alert;
    }

    /**
     * Actualiza los atributos internos de la instancia únicamente cuando
     * estos se encuentran vacíos o nulos.
     * <p>
     * Si un atributo ya posee un valor no vacío, no será modificado.
     * </p>
     *
     * @param messageHead Cabecera o encabezado recibido.
     * @param title       Título recibido.
     * @param message     Mensaje recibido.
     * @param type        Tipo de alerta recibido.
     */
    private void updateAttributesIfBlank(String messageHead,
                                         String title,
                                         String message,
                                         String type) {

        if (isBlank(this.messageHead) && !isBlank(messageHead)) {
            this.messageHead = messageHead;
        }

        if (isBlank(this.title) && !isBlank(title)) {
            this.title = title;
        }

        if (isBlank(this.message) && !isBlank(message)) {
            this.message = message;
        }

        if (isBlank(this.type) && !isBlank(type)) {
            this.type = type;
        }
    }

    /**
     * Intenta cargar una imagen desde un recurso del proyecto y agregarla a la
     * alerta.
     * <p>
     * La imagen se mostrará con un tamaño máximo de 45px x 45px, conservando
     * la proporción original.
     * </p>
     *
     * <p>
     * Si la imagen no puede cargarse, se elimina el gráfico de la alerta y se
     * muestra un mensaje de error en la cabecera y en el cuerpo.
     * </p>
     *
     * @param alert     Alerta a la que se agregará la imagen.
     * @param imageURL  Ruta del recurso del proyecto correspondiente a la imagen.
     */
    private void applyImage(Alert alert, String imageURL) {
        if (isBlank(imageURL)) {
            applyImageLoadError(alert);
            return;
        }

        InputStream inputStream = resolveImageResource(imageURL);

        if (inputStream == null) {
            applyImageLoadError(alert);
            return;
        }

        try (InputStream stream = inputStream) {
            Image image = new Image(stream );

            if (image.isError() || image.getException() != null) {
                applyImageLoadError(alert);
                return;
            }

            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setFitHeight(IMAGE_SIZE);
            imageView.setPreserveRatio(true);

            alert.getDialogPane().setGraphic(imageView);

        } catch (Exception e) {
            applyImageLoadError(alert);
        }
    }

    /**
     * Intenta resolver un recurso del proyecto como {@link InputStream}.
     * <p>
     * Primero intenta cargar el recurso tal como viene en {@code imageURL}.
     * Si no lo encuentra y la ruta no comienza con {@code "/"}, intenta
     * cargarla agregando la barra inicial.
     * </p>
     *
     * @param imageURL Ruta del recurso del proyecto.
     * @return {@link InputStream} del recurso o {@code null} si no se encuentra.
     */
    private InputStream resolveImageResource(String imageURL) {
        InputStream stream = getClass().getResourceAsStream(imageURL);

        if (stream == null && !imageURL.startsWith("/")) {
            stream = getClass().getResourceAsStream("/" + imageURL);
        }

        return stream;
    }

    /**
     * Aplica el estado de error de carga de imagen a la alerta.
     * <p>
     * Se elimina el gráfico y se coloca el mensaje de error tanto en la
     * cabecera como en el cuerpo de la alerta.
     * </p>
     *
     * @param alert Alerta a la que se aplicará el error de carga de imagen.
     */
    private void applyImageLoadError(Alert alert) {
        alert.setHeaderText(IMAGE_LOAD_ERROR_MESSAGE);
        alert.setContentText(IMAGE_LOAD_ERROR_MESSAGE);
        alert.getDialogPane().setGraphic(null);
    }

    /**
     * Determina si una cadena es nula, vacía o contiene únicamente espacios en
     * blanco.
     *
     * @param value Cadena a evaluar.
     * @return {@code true} si la cadena es nula, vacía o solo contiene espacios.
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}