/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import org.kennethvelasquez.system.model.Product;
import org.kennethvelasquez.system.model.dto.ProductDTO;
import org.kennethvelasquez.system.repository.ProductRepository;
import org.kennethvelasquez.system.utils.ImageTool;

/**
 * Servicio encargado de gestionar la lógica de negocio, validaciones y persistencia
 * de la entidad {@link Product}.
 * <p>
 * Pertenece a la <b>Capa de Negocio (Service Layer)</b>. Su propósito es:
 * <ul>
 *   <li>Validar reglas de formato, tamaño y tipos de imágenes antes de su procesamiento.</li>
 *   <li>Verificar restricciones de longitud de texto y valores de precio antes de persistir.</li>
 *   <li>Capturar y traducir excepciones de base de datos ({@link SQLException}) en estados
 *       amigables tipados mediante {@link ProductStatus}.</li>
 * </ul>
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see org.kennethvelasquez.system.repository.ProductRepository
 * @see org.kennethvelasquez.system.service.ProductStatus
 * @see org.kennethvelasquez.system.model.Product
 * @see org.kennethvelasquez.system.model.dto.ProductDTO
 */
public class ProductService {

    /**
     * Repositorio para la interacción con la base de datos MySQL.
     */
    private ProductRepository productRepo = new ProductRepository();

    /**
     * Caché de productos en memoria resultante de la última lectura.
     */
    private List<ProductDTO> productsList = new ArrayList<>();

    /**
     * Producto obtenido tras una operación de búsqueda individual.
     */
    private ProductDTO currentProduct;

    /**
     * Almacenamiento temporal de los bytes de una imagen validada y procesada.
     */
    private byte[] temporalImageBytes;

    /**
     * Detalle del mensaje de error técnico producido por la base de datos o I/O.
     */
    private String messageError;

    public ProductService() {
    }

    /**
     * Valida y procesa un archivo de imagen seleccionado por el usuario.
     * <p>
     * Verifica la existencia, las extensiones nativas permitidas por JavaFX y que el
     * peso no sobrepase los 10 MB. Si es válido, convierte el archivo a bytes y los almacena
     * temporalmente en el servicio para su posterior consulta con {@link #getTemporalImageBytes()}.
     * </p>
     *
     * @param file Archivo físico seleccionado desde la interfaz gráfica.
     * @return {@link ProductStatus#IMAGE_SUCCESS} si la imagen es válida y fue leída;
     *         {@link ProductStatus#IMAGE_INVALID_FORMAT} si el formato no está permitido;
     *         {@link ProductStatus#IMAGE_SIZE_EXCEEDED} si supera los 10 MB;
     *         {@link ProductStatus#IMAGE_READ_ERROR} si el archivo no existe o falló la lectura.
     */
    public ProductStatus processProductImage(File file) {
        if (file == null || !file.exists()) {
            return ProductStatus.IMAGE_READ_ERROR;
        }

        if (!ImageTool.isValidImageExtension(file)) {
            return ProductStatus.IMAGE_INVALID_FORMAT;
        }

        if (!ImageTool.isValidImageSize(file)) {
            return ProductStatus.IMAGE_SIZE_EXCEEDED;
        }

        try {
            this.temporalImageBytes = ImageTool.fileToBytes(file);
            return ProductStatus.IMAGE_SUCCESS;
        } catch (IOException e) {
            this.messageError = e.getMessage();
            return ProductStatus.IMAGE_READ_ERROR;
        }
    }

    /**
     * Libera la memoria ocupada por los bytes de imagen temporal almacenados en el servicio.
     */
    public void clearTemporalImage() {
        this.temporalImageBytes = null;
    }

    
    /**
     * Valida las reglas de formato y negocio de los campos del producto.
     *
     * @param name        Nombre del producto (no vacío, máx 100 caracteres).
     * @param description Descripción (no vacía, máx 200 caracteres).
     * @param price       Precio del producto (mayor a cero).
     * @param idCategory  ID de categoría (no nulo, mayor a cero).
     * @return {@link ProductStatus} con el error específico, o {@code null} si los datos son válidos.
     */
    private ProductStatus validateProductData(String name, String description, Double price, Integer idCategory) {
        if (name == null || name.trim().isEmpty()
                || description == null || description.trim().isEmpty()
                || price == null) {
            return ProductStatus.INVALID_DATA;
        }

        if (name.trim().length() > 100) {
            return ProductStatus.INVALID_NAME_LENGTH;
        }

        if (description.trim().length() > 200) {
            return ProductStatus.INVALID_DESCRIPTION_LENGTH;
        }

        if (price <= 0.0) {
            return ProductStatus.INVALID_PRICE;
        }

        if (idCategory == null || idCategory <= 0) {
            return ProductStatus.CATEGORY_REQUIRED;
        }

        return null;
    }

    /**
     * Valida y crea un nuevo producto en la base de datos con referencia al usuario creador.
     *
     * @param name        Nombre del producto.
     * @param description Descripción detallada.
     * @param price       Precio unitario.
     * @param imgProducto Arreglo de bytes con la imagen (puede ser null).
     * @param idCategory  ID de la categoría a la que pertenece.
     * @param idUser      UUID del usuario que ejecuta la acción.
     * @return Estado resultante de la operación en {@link ProductStatus}.
     */
    public ProductStatus createProduct(String name, String description, Double price, byte[] imgProducto, Integer idCategory, String idUser) {
        ProductStatus validationStatus = validateProductData(name, description, price, idCategory);
        if (validationStatus != null) {
            return validationStatus;
        }

        try {
            Product product = new Product(name.trim(), description.trim(), price, imgProducto, idUser, idCategory);
            productRepo.create(product);
            clearTemporalImage();
            return ProductStatus.PRODUCT_CREATED;
        } catch (SQLIntegrityConstraintViolationException e) {
            this.messageError = e.getMessage();
            return ProductStatus.CATEGORY_REQUIRED;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return ProductStatus.ERROR_PRODUCT_CREATE;
        }
    }

    /**
     * Consulta y recupera la lista completa de productos registrados registrando la auditoría del usuario.
     *
     * @param idUser UUID del usuario que ejecuta la lectura.
     * @return {@link ProductStatus#READ_SUCCESS} si se encontraron registros,
     *         {@link ProductStatus#EMPTY_LIST} si la tabla no tiene datos,
     *         o {@link ProductStatus#ERROR_READ_PRODUCTS} ante un error de base de datos.
     */
    public ProductStatus readProducts(String idUser) {
        try {
            this.productsList = productRepo.read(idUser);
            if (this.productsList == null || this.productsList.isEmpty()) {
                return ProductStatus.EMPTY_LIST;
            }
            return ProductStatus.READ_SUCCESS;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return ProductStatus.ERROR_READ_PRODUCTS;
        }
    }

    /**
     * Busca un producto por su identificador primario registrando la auditoría del usuario.
     *
     * @param idProduct Identificador único del producto.
     * @param idUser    UUID del usuario que ejecuta la búsqueda.
     * @return {@link ProductStatus#PRODUCT_FOUND} si se localizó,
     *         {@link ProductStatus#PRODUCT_NOT_FOUND} si no existe,
     *         o {@link ProductStatus#ERROR_SEARCH_PRODUCT} si ocurrió un error SQL.
     */
    public ProductStatus searchProduct(int idProduct, String idUser) {
        if (idProduct <= 0) {
            return ProductStatus.PRODUCT_NOT_FOUND;
        }

        try {
            Product searchParam = new Product(idProduct, idUser);
            this.currentProduct = productRepo.search(searchParam);
            if (this.currentProduct != null) {
                productsList.clear();
                productsList.add(currentProduct);
                return ProductStatus.PRODUCT_FOUND;
            }
            return ProductStatus.PRODUCT_NOT_FOUND;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return ProductStatus.ERROR_SEARCH_PRODUCT;
        }
    }

    /**
     * Valida y actualiza los datos de un producto existente con referencia al usuario que modifica.
     * <p>
     * Si {@code imgProducto} se envía como {@code null}, la base de datos preservará la imagen actual.
     * </p>
     *
     * @param idProduct   Identificador del producto a actualizar.
     * @param name        Nombre actualizado.
     * @param description Descripción actualizada.
     * @param price       Nuevo precio.
     * @param imgProducto Nueva imagen en bytes (o null para mantener la actual).
     * @param idCategory  ID de categoría asociada.
     * @param idUser      UUID del usuario que ejecuta la actualización.
     * @return Estado resultante de la actualización.
     */
    public ProductStatus updateProduct(Integer idProduct, String name, String description,
            Double price, byte[] imgProducto, Integer idCategory, String idUser) {
        if (idProduct == null || idProduct <= 0) {
            return ProductStatus.PRODUCT_NOT_FOUND;
        }

        ProductStatus validationStatus = validateProductData(name, description, price, idCategory);
        if (validationStatus != null) {
            return validationStatus;
        }

        try {
            Product product = new Product(idProduct, name.trim(), description.trim(), price, imgProducto, idUser, idCategory);
            productRepo.update(product);
            clearTemporalImage();
            return ProductStatus.PRODUCT_UPDATED;
        } catch (SQLIntegrityConstraintViolationException e) {
            this.messageError = e.getMessage();
            return ProductStatus.CATEGORY_REQUIRED;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return ProductStatus.ERROR_PRODUCT_UPDATE;
        }
    }

    /**
     * Elimina físicamente un producto del sistema registrando la auditoría del usuario que elimina.
     *
     * @param idProduct Identificador del producto a eliminar.
     * @param idUser    UUID del usuario que ejecuta la eliminación.
     * @return {@link ProductStatus#PRODUCT_DELETED} si se eliminó correctamente,
     *         {@link ProductStatus#PRODUCT_HAS_DEPENDENCIES} si tiene registros relacionados (FK),
     *         o {@link ProductStatus#ERROR_PRODUCT_DELETE} si ocurrió otro error SQL.
     */
    public ProductStatus deleteProduct(int idProduct, String idUser) {
        Product actionParam = new Product(idProduct, idUser);
        try {
            ProductDTO searchProduct = productRepo.search(actionParam);
            if (searchProduct == null) {
                return ProductStatus.PRODUCT_NOT_FOUND;
            }
        } catch (SQLException e) {
            messageError = e.getMessage();
            return ProductStatus.ERROR_SEARCH_PRODUCT;
        }

        try {
            productRepo.delete(actionParam);
            return ProductStatus.PRODUCT_DELETED;
        } catch (SQLIntegrityConstraintViolationException e) {
            this.messageError = e.getMessage();
            return ProductStatus.PRODUCT_HAS_DEPENDENCIES;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return ProductStatus.ERROR_PRODUCT_DELETE;
        }
    }

    // =========================================================================
    // 3. GETTERS
    // =========================================================================

    public List<ProductDTO> getProductsList() {
        return productsList;
    }

    public ProductDTO getCurrentProduct() {
        return currentProduct;
    }

    public byte[] getTemporalImageBytes() {
        return temporalImageBytes;
    }

    public String getMessageError() {
        return messageError;
    }
}
