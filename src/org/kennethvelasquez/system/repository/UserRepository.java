/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.repository;

import org.kennethvelasquez.system.model.User;

import org.kennethvelasquez.system.config.ConexionDB;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
/**
 * Repositorio encargado de la persistencia y acceso a datos de los usuarios.
 * <p>
 * Pertenece a la <b>Capa de Acceso a Datos (Data Access Layer / Repository Pattern)</b>.
 * Su función principal es interactuar directamente con la base de datos MySQL mediante
 * JDBC y la invocación de procedimientos almacenados (Stored Procedures), aislando por
 * completo los detalles de persistencia y consultas SQL de la capa de servicio ({@link org.kennethvelasquez.system.service.UserService}).
 * </p>
 *
 * <p>
 * Responsabilidades clave:
 * <ul>
 *   <li>Ejecutar los procedimientos almacenados de inserción y consulta en MySQL.</li>
 *   <li>Mapear los atributos de la entidad de dominio {@link User} a los parámetros correspondientes de JDBC.</li>
 *   <li>Asegurar la liberación adecuada de recursos de base de datos (mediante {@code try-with-resources}).</li>
 * </ul>
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see org.kennethvelasquez.system.repository.UserInterface
 * @see org.kennethvelasquez.system.service.UserService
 * @see org.kennethvelasquez.system.config.ConexionDB
 * @see org.kennethvelasquez.system.model.User
 */
public class UserRepository implements UserInterface{
    
    /**
     * Instancia singleton que administra la conexión activa a la base de datos MySQL.
     */
    private ConexionDB conexionDB = ConexionDB.getInstanceConexionDB();

    /**
     * Inserta un nuevo usuario en la base de datos almacenando la contraseña en texto plano (sin encriptación).
     * <p>
     * Invoca el procedimiento almacenado {@code sp_create_user_unprotected}, el cual se encarga
     * de generar el identificador UUID y guardar los datos tal cual fueron provistos.
     * </p>
     *
     * @param user Entidad {@link User} que contiene la información del usuario a registrar.
     * @throws SQLException Si ocurre un error de comunicación o fallo en el motor de base de datos.
     * @throws SQLIntegrityConstraintViolationException Si se viola una restricción de unicidad (correo o usuario duplicado) o llave foránea.
     */
    @Override
    public void createUnprotected(User user) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure="{call sp_create_user_unprotected(?,?,?,?,?,?,?)}";
        try (CallableStatement callSP = conexionDB.getConnection()
                .prepareCall(storedProcedure)) {
            callSP.setString(1, user.getName());
            callSP.setString(2, user.getLastName());
            callSP.setString(3, user.getUser());
            callSP.setString(4, user.getEmail());
            callSP.setString(5, user.getPassword());
            callSP.setInt(6, user.getRol());
            callSP.setInt(7, user.getTypeEncrypt());
            callSP.execute();
        }
    }

    /**
     * Inserta un nuevo usuario delegando el cálculo del hash MD5 directamente al motor de base de datos.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_create_user_hashed}, donde MySQL aplica la función
     * nativa {@code md5()} sobre la contraseña recibida antes de persistirla.
     * </p>
     *
     * @param user Entidad {@link User} que contiene los datos del usuario.
     * @throws SQLException Si ocurre un error de comunicación o fallo en el motor de base de datos.
     * @throws SQLIntegrityConstraintViolationException Si se viola una restricción de unicidad o clave foránea.
     */
    @Override
    public void createMD5(User user) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure="{call sp_create_user_hashed(?,?,?,?,?,?,?)}";
        try (CallableStatement callSP = conexionDB.getConnection()
                .prepareCall(storedProcedure)) {
            callSP.setString(1, user.getName());
            callSP.setString(2, user.getLastName());
            callSP.setString(3, user.getUser());
            callSP.setString(4, user.getEmail());
            callSP.setString(5, user.getPassword());
            callSP.setInt(6, user.getRol());
            callSP.setInt(7, user.getTypeEncrypt());
            callSP.execute();
        }
    }

    /**
     * Inserta un nuevo usuario cuya contraseña ha sido procesada previamente con el algoritmo BCrypt.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_create_user_bcrypt}. Dado que MySQL no implementa
     * BCrypt de forma nativa, el valor de {@code user.getPassword()} debe ser el hash generado en la aplicación.
     * </p>
     *
     * @param user Entidad {@link User} con el hash de contraseña ya generado y listo para almacenar.
     * @throws SQLException Si ocurre un error de comunicación o fallo en el motor de base de datos.
     * @throws SQLIntegrityConstraintViolationException Si se viola una restricción de unicidad o clave foránea.
     */
    @Override
    public void createBCrypt(User user) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure="{call sp_create_user_bcrypt(?,?,?,?,?,?,?)}";
        
        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, user.getName());
            callSP.setString(2, user.getLastName());
            callSP.setString(3, user.getUser());
            callSP.setString(4, user.getEmail());
            callSP.setString(5, user.getPassword());
            callSP.setInt(6, user.getRol());
            callSP.setInt(7, user.getTypeEncrypt());
            callSP.execute();
        }
    }

    /**
     * Comprueba si ya existe un usuario registrado con el nombre de usuario o dirección de correo indicados.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_read_user_by_email_or_user} y evalúa la presencia
     * de al menos un registro en el resultado para determinar la existencia del usuario.
     * </p>
     *
     * @param nameUser Nombre de usuario o identificador de acceso a buscar.
     * @param email    Dirección de correo electrónico a buscar.
     * @return {@code true} si se encontró al menos un registro coincidente con el usuario o correo;
     *         {@code false} en caso contrario.
     * @throws SQLException Si ocurre un error durante la ejecución de la consulta.
     * @throws SQLIntegrityConstraintViolationException Si ocurre un error de violación de restricciones.
     */
    @Override
    public Boolean existsByEmailOrUser(String nameUser, String email) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure="{call sp_read_user_by_email_or_user(?,?)}";

        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, nameUser);
            callSP.setString(2, email);
            ResultSet result = callSP.executeQuery();
            if (result.next()) {
                return true;
            }
        }
        return false;
    }
    
}
