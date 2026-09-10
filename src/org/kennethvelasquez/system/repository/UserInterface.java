/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.repository;

import org.kennethvelasquez.system.model.User;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
/**
 * Contrato de operaciones para la persistencia y acceso a datos de usuarios.
 * <p>
 * Define las operaciones CRUD y consultas que deben ser implementadas por los
 * repositorios de datos (como {@link UserRepository}), desacoplando la capa de
 * negocio de la implementación tecnológica concreta.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see UserRepository
 * @see org.kennethvelasquez.system.model.User
 */
public interface UserInterface {

    /**
     * Persiste un usuario con su contraseña sin encriptar.
     *
     * @param user Entidad con la información del usuario a registrar.
     * @throws SQLException Si ocurre un error de base de datos.
     * @throws SQLIntegrityConstraintViolationException Si se violan restricciones de unicidad o clave foránea.
     */
    void createUnprotected (User user) throws SQLException, SQLIntegrityConstraintViolationException;

    /**
     * Persiste un usuario delegando el hash MD5 al motor de base de datos.
     *
     * @param user Entidad con la información del usuario a registrar.
     * @throws SQLException Si ocurre un error de base de datos.
     * @throws SQLIntegrityConstraintViolationException Si se violan restricciones de unicidad o clave foránea.
     */
    void createMD5 (User user) throws SQLException, SQLIntegrityConstraintViolationException;

    /**
     * Persiste un usuario cuya contraseña ya fue cifrada con BCrypt.
     *
     * @param user Entidad con el hash BCrypt de la contraseña.
     * @throws SQLException Si ocurre un error de base de datos.
     * @throws SQLIntegrityConstraintViolationException Si se violan restricciones de unicidad o clave foránea.
     */
    void createBCrypt (User user) throws SQLException, SQLIntegrityConstraintViolationException;

    /**
     * Verifica la existencia de un usuario mediante su nombre de usuario o correo.
     *
     * @param user  Nombre de usuario a verificar.
     * @param email Correo electrónico a verificar.
     * @return {@code true} si existe; {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error de base de datos.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de integridad.
     */
    Boolean existsByEmailOrUser(String user,String email) throws SQLException, SQLIntegrityConstraintViolationException;
}
