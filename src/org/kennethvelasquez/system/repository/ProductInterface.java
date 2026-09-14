/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.kennethvelasquez.system.repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import org.kennethvelasquez.system.model.Product;
import org.kennethvelasquez.system.model.dto.ProductDTO;

/**
 * Contrato de operaciones para la persistencia y acceso a datos de productos.
 * <p>
 * Define las operaciones CRUD que deben ser implementadas por los repositorios de datos
 * (como {@link ProductRepository}), desacoplando la capa de servicio y lógica de negocio
 * de la implementación tecnológica concreta en MySQL.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see ProductRepository
 * @see org.kennethvelasquez.system.model.Product
 * @see org.kennethvelasquez.system.model.dto.ProductDTO
 */
public interface ProductInterface {

    /**
     * Registra y persiste un nuevo producto en la base de datos.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_create_product}, el cual almacena
     * el nombre, descripción, precio, bytes de la imagen y la categoría foránea asociada.
     * </p>
     *
     * @param product Entidad {@link Product} con los datos del nuevo producto a registrar.
     * @throws SQLException Si ocurre un error de comunicación, sintaxis o fallo en MySQL.
     * @throws SQLIntegrityConstraintViolationException Si se violan restricciones de integridad
     *                                                  (clave foránea de categoría inválida o checks de longitud/precio).
     */
    void create(Product product) throws SQLException, SQLIntegrityConstraintViolationException;

    /**
     * Consulta y retorna el catálogo completo de productos disponibles en el sistema.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_read_products}, el cual consulta la vista
     * {@code view_read_product} realizando el {@code INNER JOIN} con la tabla de categorías.
     * </p>
     *
     * @param idUser ID del usuario que hace la accion
     * @return Lista de objetos {@link ProductDTO} con la información de cada producto.
     * @throws SQLException Si ocurre un error durante la consulta a la base de datos.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de restricciones.
     */
    List<ProductDTO> read(String idUser) throws SQLException, SQLIntegrityConstraintViolationException;

    /**
     * Busca un producto específico a partir de su identificador numérico único.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_search_product}, retornando la información
     * detallada junto con el nombre de su categoría asociada.
     * </p>
     *
     * @param product Objeto de producto del cual se buscara
     * @return Objeto {@link ProductDTO} si se encuentra el registro; {@code null} en caso contrario.
     * @throws SQLException Si ocurre un error durante la ejecución de la consulta.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de integridad.
     */
    ProductDTO search(Product product) throws SQLException, SQLIntegrityConstraintViolationException;

    /**
     * Actualiza la información general, precio, imagen y/o categoría de un producto existente.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_update_product}. Si la imagen provista en
     * {@code product.getImgProducto()} es nula, el procedimiento conservará la imagen previa.
     * </p>
     *
     * @param product Entidad {@link Product} con los datos actualizados a persistir.
     * @throws SQLException Si ocurre un error durante la actualización en MySQL.
     * @throws SQLIntegrityConstraintViolationException Si los nuevos datos violan restricciones
     *                                                  de longitud, precio mayor a cero o clave foránea.
     */
    void update(Product product) throws SQLException, SQLIntegrityConstraintViolationException;

    /**
     * Elimina físicamente un producto de la base de datos según su identificador primario.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_delete_product}.
     * </p>
     *
     * @param product Objeto de producto del cual se eliminara
     * @throws SQLException Si ocurre un error de ejecución en la base de datos.
     * @throws SQLIntegrityConstraintViolationException Si el producto está asociado a registros dependientes.
     */
    void delete(Product product) throws SQLException, SQLIntegrityConstraintViolationException;
}
