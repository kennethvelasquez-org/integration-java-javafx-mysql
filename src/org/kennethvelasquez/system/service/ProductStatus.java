/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.kennethvelasquez.system.service;

/**
 * Enumeración que define los posibles estados y resultados de las operaciones de negocio,
 * persistencia CRUD y procesamiento de imágenes sobre la entidad {@link org.kennethvelasquez.system.model.Product}.
 * <p>
 * Comunica los desenlaces desde la capa de servicio hacia los controladores de la vista,
 * permitiendo disparar alertas informativas, de advertencia o de error desacopladas de la lógica interna.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see org.kennethvelasquez.system.model.Product
 */
public enum ProductStatus {

    // ==========================================
    // 1. ESTADOS DE CREACIÓN Y VALIDACIÓN DE DATOS
    // ==========================================
    /**
     * El producto se registró y persistió exitosamente en la base de datos.
     */
    PRODUCT_CREATED,

    /**
     * Uno o más campos requeridos del producto se encuentran vacíos o nulos.
     */
    INVALID_DATA,

    /**
     * El nombre del producto excede los 100 caracteres permitidos.
     */
    INVALID_NAME_LENGTH,

    /**
     * La descripción del producto excede los 200 caracteres permitidos.
     */
    INVALID_DESCRIPTION_LENGTH,

    /**
     * El precio provisto es inválido (menor o igual a cero).
     */
    INVALID_PRICE,

    /**
     * No se seleccionó una categoría válida para el producto (clave foránea requerida).
     */
    CATEGORY_REQUIRED,

    /**
     * Ocurrió un error inesperado al momento de ejecutar la inserción del producto en la base de datos.
     */
    ERROR_PRODUCT_CREATE,

    // ==========================================
    // 2. ESTADOS DE LECTURA Y CONSULTA
    // ==========================================
    /**
     * La consulta se ejecutó exitosamente y se recuperó la lista de productos.
     */
    READ_SUCCESS,

    /**
     * La consulta se ejecutó con éxito pero no existen productos registrados en la base de datos.
     */
    EMPTY_LIST,

    /**
     * Ocurrió un fallo técnico o error de SQL al intentar consultar la lista de productos.
     */
    ERROR_READ_PRODUCTS,

    /**
     * El producto buscado por su identificador fue localizado satisfactoriamente.
     */
    PRODUCT_FOUND,

    /**
     * No se encontró ningún producto con el identificador provisto.
     */
    PRODUCT_NOT_FOUND,

    /**
     * Ocurrió un fallo técnico al momento de buscar un producto individual.
     */
    ERROR_SEARCH_PRODUCT,

    // ==========================================
    // 3. ESTADOS DE ACTUALIZACIÓN
    // ==========================================
    /**
     * La información del producto fue actualizada con éxito en la base de datos.
     */
    PRODUCT_UPDATED,

    /**
     * Ocurrió un error técnico o fallo de base de datos al intentar actualizar el producto.
     */
    ERROR_PRODUCT_UPDATE,

    // ==========================================
    // 4. ESTADOS DE ELIMINACIÓN
    // ==========================================
    /**
     * El producto fue eliminado físicamente de la base de datos.
     */
    PRODUCT_DELETED,

    /**
     * No se puede eliminar el producto debido a restricciones de integridad referencial
     * (por ejemplo, si está asociado a ventas o detalles de facturación).
     */
    PRODUCT_HAS_DEPENDENCIES,

    /**
     * Ocurrió un fallo técnico al intentar eliminar el producto.
     */
    ERROR_PRODUCT_DELETE,

    // ==========================================
    // 5. ESTADOS DE VALIDACIÓN Y CARGA DE IMÁGENES
    // ==========================================
    /**
     * El archivo de imagen fue validado y convertido a bytes exitosamente.
     */
    IMAGE_SUCCESS,

    /**
     * El archivo seleccionado no posee una extensión de imagen soportada por JavaFX
     * (.png, .jpg, .jpeg, .gif, .bmp).
     */
    IMAGE_INVALID_FORMAT,

    /**
     * El archivo de imagen excede el límite de peso permitido (10 MB).
     */
    IMAGE_SIZE_EXCEEDED,

    /**
     * Ocurrió un error de lectura de archivo (I/O) al intentar procesar la imagen seleccionada.
     */
    IMAGE_READ_ERROR
}
