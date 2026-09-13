/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.repository;

import java.util.List;
import org.kennethvelasquez.system.model.Rol;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
/**
 * Contrato de operaciones CRUD para la persistencia y acceso a datos de roles.
 * <p>
 * Define las operaciones que deben ser implementadas por {@link RolRepository},
 * desacoplando la capa de lógica de negocio del acceso directo a la base de datos.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see RolRepository
 * @see org.kennethvelasquez.system.model.Rol
 */
public interface RolInterface {
    /**
     * Registra un nuevo rol en la base de datos.
     *
     * @param rol Entidad con la información del rol a registrar.
     * @throws SQLException Si ocurre un error de ejecución en la base de datos.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de restricción de integridad.
     */
    void create(Rol rol) throws SQLException, SQLIntegrityConstraintViolationException;
    /**
     * Obtiene la lista de todos los roles registrados.
     * <p>
     * Consume el procedimiento almacenado {@code sp_read_rol()}.
     * </p>
     *
     * @return Lista de entidades {@link Rol}.
     * @throws SQLException Si ocurre un error durante la consulta.
     * @throws SQLIntegrityConstraintViolationException Si ocurre un error de integridad.
     */
    List<Rol> read() throws SQLException, SQLIntegrityConstraintViolationException;
    /**
     * Busca un rol específico a partir de su identificador único.
     * <p>
     * Consume el procedimiento almacenado {@code sp_search_rol(?)}.
     * </p>
     *
     * @param idRol Identificador primario del rol.
     * @return Instancia de {@link Rol} si existe; {@code null} en caso contrario.
     * @throws SQLException Si ocurre un error durante la consulta.
     * @throws SQLIntegrityConstraintViolationException Si ocurre un error de integridad.
     */
    Rol search(int idRol) throws SQLException, SQLIntegrityConstraintViolationException;
    /**
     * Actualiza la información de un rol existente.
     *
     * @param rol Entidad con los datos modificados.
     * @throws SQLException Si ocurre un error durante la actualización.
     * @throws SQLIntegrityConstraintViolationException Si se violan restricciones en la base de datos.
     */
    void update(Rol rol) throws SQLException, SQLIntegrityConstraintViolationException;
    /**
     * Elimina un rol de la base de datos según su identificador.
     *
     * @param idRol Identificador del rol a eliminar.
     * @throws SQLException Si ocurre un error al eliminar.
     * @throws SQLIntegrityConstraintViolationException Si el rol está asociado a registros dependientes (FK).
     */
    void delete(int idRol) throws SQLException, SQLIntegrityConstraintViolationException;
}