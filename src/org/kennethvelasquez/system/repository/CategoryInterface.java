/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.kennethvelasquez.system.repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import org.kennethvelasquez.system.model.Category;

/**
 * Contrato de operaciones para la persistencia y consulta de categorías.
 * <p>
 * Define los métodos implementados por {@link CategoryRepository}, desacoplando
 * la capa de servicio del acceso directo a la base de datos MySQL.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see CategoryRepository
 * @see org.kennethvelasquez.system.model.Category
 */
public interface CategoryInterface {

    /**
     * Consulta y retorna el catálogo completo de categorías registradas en la base de datos.
     * <p>
     * Consume el procedimiento almacenado {@code sp_read_category()}.
     * </p>
     *
     * @return Lista de entidades {@link Category} encontradas.
     * @throws SQLException Si ocurre un error durante la consulta.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de restricciones.
     */
    List<Category> read() throws SQLException, SQLIntegrityConstraintViolationException;
}
