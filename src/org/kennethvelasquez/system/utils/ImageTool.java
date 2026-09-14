package org.kennethvelasquez.system.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

/**
 * Clase utilitaria para la gestión, conversión y validación de imágenes en JavaFX.
 * <p>
 * Centraliza la lógica de conversión entre archivos locales ({@link File}),
 * representaciones binarias en arreglos de bytes ({@code byte[]}) para persistencia en MySQL,
 * y componentes gráficos {@link Image}.
 * </p>
 *
 * @author STEPHRYS
 */
public class ImageTool {

    /**
     * Límite de tamaño máximo permitido para imágenes: 10 MB (en bytes).
     */
    public static final long MAX_IMAGE_SIZE_BYTES = 10L * 1024L * 1024L;

    /**
     * Extensiones de imagen soportadas de forma nativa por JavaFX.
     */
    public static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(
            ".png", ".jpg", ".jpeg", ".gif", ".bmp"
    );

    /**
     * Constructor privado para prevenir instanciación de clase utilitaria.
     */
    private ImageTool() {
    }

    /**
     * Convierte un archivo de imagen local en un arreglo de bytes ({@code byte[]})
     * apto para almacenamiento en campos BLOB / MEDIUMBLOB de MySQL.
     *
     * @param file Archivo a leer
     * @return Arreglo de bytes con el contenido del archivo
     * @throws IOException Si ocurre un error al leer el archivo en disco
     */
    public static byte[] fileToBytes(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Convierte un arreglo de bytes ({@code byte[]}) en un objeto {@link Image} de JavaFX.
     *
     * @param bytes Arreglo de bytes de la imagen
     * @return Objeto {@link Image} renderizable en JavaFX, o {@code null} si los bytes son inválidos o nulos
     */
    public static Image bytesToImage(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            return new Image(bis);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Valida si el archivo posee una extensión de imagen soportada nativamente por JavaFX.
     *
     * @param file Archivo a validar
     * @return {@code true} si la extensión es válida (.png, .jpg, .jpeg, .gif, .bmp), {@code false} en caso contrario
     */
    public static boolean isValidImageExtension(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        String fileName = file.getName().toLowerCase();
        for (String extension : SUPPORTED_EXTENSIONS) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida que el archivo no supere el tamaño máximo especificado en bytes.
     *
     * @param file         Archivo a evaluar
     * @param maxSizeBytes Límite en bytes
     * @return {@code true} si el tamaño está dentro del límite, {@code false} si lo supera o no existe
     */
    public static boolean isValidImageSize(File file, long maxSizeBytes) {
        if (file == null || !file.exists()) {
            return false;
        }
        return file.length() <= maxSizeBytes;
    }

    /**
     * Valida que el archivo no supere el límite predeterminado de 10 MB.
     *
     * @param file Archivo a evaluar
     * @return {@code true} si no excede los 10 MB
     */
    public static boolean isValidImageSize(File file) {
        return isValidImageSize(file, MAX_IMAGE_SIZE_BYTES);
    }

    /**
     * Proporciona un filtro configurado para {@link FileChooser} con las extensiones
     * de imagen permitidas por JavaFX.
     *
     * @return {@link FileChooser.ExtensionFilter} para diálogos de selección de archivos
     */
    public static FileChooser.ExtensionFilter getImageExtensionFilter() {
        return new FileChooser.ExtensionFilter(
                "Imágenes soportadas (*.png, *.jpg, *.jpeg, *.gif, *.bmp)",
                "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp",
                "*.PNG", "*.JPG", "*.JPEG", "*.GIF", "*.BMP"
        );
    }
}
