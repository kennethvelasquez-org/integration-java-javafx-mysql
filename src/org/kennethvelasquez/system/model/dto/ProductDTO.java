/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.model.dto;

import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kennethvelasquez.system.model.Product;
import org.kennethvelasquez.system.utils.ImageTool;

/**
 * Objeto de transferencia de datos (DTO) para la visualización de productos en el TableView.
 * Mapea directamente la vista de base de datos {@code view_read_product}.
 * <p>
 * Hereda todos los atributos base de {@link Product} y añade el nombre legible
 * de la categoría ({@code categoryName}), además de métodos auxiliares para renderizar
 * imágenes y enlazar columnas en JavaFX.
 * </p>
 *
 * @author STEPHRYS
 */
public class ProductDTO extends Product {

    private String categoryName;

    public ProductDTO() {
        super();
    }

    /**
     * Constructor que mapea exactamente los campos devueltos por {@code view_read_product} y {@code sp_read_products}.
     *
     * @param idProduct    ID único del producto
     * @param name         Nombre del producto
     * @param description  Descripción del producto
     * @param price        Precio del producto
     * @param imgProducto  Imagen en bytes (mediumblob)
     * @param idCategory   ID numérico de la categoría
     * @param categoryName Nombre de la categoría asociada
     */
    public ProductDTO(Integer idProduct, String name, String description, Double price,
                      byte[] imgProducto, Integer idCategory, String categoryName) {
        super(idProduct, name, description, price, imgProducto, idCategory);
        this.categoryName = categoryName;
    }

    /**
     * Constructor completo incluyendo el usuario creador (para auditoría o vistas extendidas).
     *
     * @param idProduct    ID único del producto
     * @param name         Nombre del producto
     * @param description  Descripción del producto
     * @param price        Precio del producto
     * @param imgProducto  Imagen en bytes (mediumblob)
     * @param idUser       UUID del usuario creador
     * @param idCategory   ID numérico de la categoría
     * @param categoryName Nombre de la categoría asociada
     */
    public ProductDTO(Integer idProduct, String name, String description, Double price,
                      byte[] imgProducto, String idUser, Integer idCategory, String categoryName) {
        super(idProduct, name, description, price, imgProducto, idUser, idCategory);
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Alias para compatibilidad en PropertyValueFactory("id").
     * @return El identificador del producto
     */
    public Integer getId() {
        return getIdProduct();
    }

    /**
     * Alias para compatibilidad en PropertyValueFactory("nameCategory").
     * @return Nombre de la categoría
     */
    public String getNameCategory() {
        return categoryName;
    }

    /**
     * Convierte el arreglo de bytes (mediumblob) en un objeto {@link Image} de JavaFX.
     * Útil si el TableView utiliza un CellFactory para renderizar imágenes.
     *
     * @return Objeto {@link Image} o {@code null} si no hay imagen almacenada
     */
    public Image getImage() {
        if (getImgProducto() != null && getImgProducto().length > 0) {
            return ImageTool.bytesToImage(getImgProduct());
        }
        return null;
    }

    /**
     * Retorna un componente {@link ImageView} listo para ser mostrado directamente en un TableView
     * mediante {@code PropertyValueFactory<ProductDTO, ImageView>("imageView")}.
     *
     * @return {@link ImageView} dimensionado a 50x50 píxeles, o {@code null} si no hay imagen
     */
    public ImageView getImageView() {
        Image img = getImage();
        if (img != null) {
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(145);
            imageView.setFitHeight(145);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            return imageView;
        }
        return null;
    }
}
