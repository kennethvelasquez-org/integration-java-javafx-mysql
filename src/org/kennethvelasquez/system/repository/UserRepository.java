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
import java.util.ArrayList;
import java.util.List;
import org.kennethvelasquez.system.model.dto.UserDTO;
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
    
    /**
     * Busca y obtiene la información completa de un usuario por su correo electrónico o nombre de usuario.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_read_user_by_email_or_user}. Mapea todas las columnas
     * de la consulta hacia una nueva instancia de {@link User}, incluyendo su ID generado y el tipo de encriptación.
     * </p>
     *
     * @param dataUser Correo electrónico o nombre de usuario a consultar.
     * @return Instancia de {@link User} con todos los atributos poblados si fue encontrado; {@code null} si no existe.
     * @throws SQLException Si ocurre un error de comunicación o ejecución SQL.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de integridad.
     */
    @Override
    public User searchByEmailOrUser(String dataUser) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure="{call sp_read_user_by_email_or_user(?,?)}";

        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, dataUser);
            callSP.setString(2, dataUser);
            ResultSet result = callSP.executeQuery();
            if (result.next()) {
                User findUser = new User();
                findUser.setName(result.getString(1));
                findUser.setLastName(result.getString(2));
                findUser.setEmail(result.getString(3));
                findUser.setUser(result.getString(4));
                findUser.setPassword(result.getString(5));
                findUser.setRol(result.getInt(6));
                findUser.setTypeEncrypt(result.getInt(7));
                findUser.setIdUser(result.getString(8));
                return findUser;
            }
        }
        return null;
    }
    
    /**
     * Valida las credenciales de inicio de sesión para cuentas sin protección criptográfica.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_login_user_unprotected}, el cual verifica en la base de datos
     * la coincidencia exacta de la contraseña en texto plano con el correo o usuario indicado.
     * </p>
     *
     * @param dataUser Correo electrónico o nombre de usuario provisto.
     * @param password Contraseña en texto plano a verificar.
     * @return Entidad {@link User} con los datos de sesión si la autenticación es correcta; {@code null} si es inválida.
     * @throws SQLException Si ocurre un error de ejecución en la base de datos.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de integridad.
     */
    @Override
    public User loginUnprotected(String dataUser, String password) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure="{call sp_login_user_unprotected(?,?)}";

        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, dataUser);
            callSP.setString(2, password);
            ResultSet result = callSP.executeQuery();
            if (result.next()) {
                User findUser = new User();
                findUser.setIdUser(result.getString(1));
                findUser.setName(result.getString(2));
                findUser.setLastName(result.getString(3));
                findUser.setEmail(result.getString(4));
                findUser.setUser(result.getString(5));
                findUser.setRol(result.getInt(6));
                return findUser;
            }
        }
        return null;
    }

    /**
     * Valida las credenciales de inicio de sesión para cuentas con contraseña hasheada con MD5.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_login_user_hashed}, delegando a MySQL la aplicación
     * de la función {@code md5()} sobre la contraseña recibida antes de compararla con la base de datos.
     * </p>
     *
     * @param dataUser Correo electrónico o nombre de usuario provisto.
     * @param password Contraseña en texto plano para cotejar con el hash MD5.
     * @return Entidad {@link User} con los datos de sesión si la autenticación es correcta; {@code null} si es inválida.
     * @throws SQLException Si ocurre un error de ejecución en la base de datos.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de integridad.
     */
    @Override
    public User loginMD5(String dataUser, String password) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure="{call sp_login_user_hashed(?,?)}";

        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, dataUser);
            callSP.setString(2, password);
            ResultSet result = callSP.executeQuery();
            if (result.next()) {
                User findUser = new User();
                findUser.setIdUser(result.getString(1));
                findUser.setName(result.getString(2));
                findUser.setLastName(result.getString(3));
                findUser.setEmail(result.getString(4));
                findUser.setUser(result.getString(5));
                findUser.setRol(result.getInt(7));
                return findUser;
            }
        }
        return null;
    }

        /**
     * Obtiene la lista completa de usuarios registrados en el sistema consultando la vista {@code view_read_users}.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_read_users}, el cual realiza un {@code INNER JOIN}
     * entre las tablas {@code User} y {@code Rol} para obtener la información detallada de cada usuario.
     * </p>
     *
     * @return Lista de tipo {@link List}&lt;{@link User}&gt; con todos los registros encontrados (o lista vacía si no hay registros).
     * @throws SQLException Si ocurre un error durante la consulta en la base de datos.
     * @throws SQLIntegrityConstraintViolationException Si ocurre una violación de restricciones de integridad.
     */
    @Override
    public List read() throws SQLException, SQLIntegrityConstraintViolationException {
        List<UserDTO> listUsers = new ArrayList<UserDTO>();
        String storedProcedure = "{call sp_read_users()}";
        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            ResultSet result = callSP.executeQuery();
            
            // Usamos while en lugar de if porque esperamos múltiples registros
            while (result.next()) {
                UserDTO user = new UserDTO();
                user.setIdUser(result.getString(1));       // Columna 1: u.id_user ("ID Usuario")
                user.setName(result.getString(2));         // Columna 2: u.name (Nombres)
                user.setLastName(result.getString(3));     // Columna 3: u.last_name (Apellidos)
                user.setEmail(result.getString(4));        // Columna 4: u.email (Correo)
                user.setUser(result.getString(5));         // Columna 5: u.user (Usuario)
                user.setTypeEncrypt(result.getInt(6));     // Columna 6: u.type_encrypt (Cifrado)
                user.setRolName(result.getString(7));      // Columna 7: r.name (Rol en texto)
                user.setStatus(result.getBoolean(8));   // Columna 8: u.user_status (Estado booleano)
                
                listUsers.add(user);
            }
        }
        return listUsers;
    }

    /**
    * {@inheritDoc}
    * <p>
    * Invoca el procedimiento almacenado {@code sp_soft_delete_user} para
    * modificar el atributo {@code user_status = false} del usuario en MySQL.
    * </p>
    */
    @Override
    public void delete(String idUser) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure = "{call sp_delete_user(?)}";
        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, idUser);
            callSP.execute();
        }
    }
    
    /**
     * Actualiza la información de un usuario existente en la base de datos.
     * <p>
     * Invoca el procedimiento almacenado {@code sp_update_user} enviando como parámetros
     * los atributos de la entidad {@link User}. El procedimiento almacenado se encarga de
     * localizar el registro por su {@code id_user} y actualizar sus nombres, apellidos,
     * correo, nombre de usuario, contraseña (si fue provista), rol, tipo de encriptación y estado.
     * </p>
     *
     * <p>
     * <b>Manejo de recursos:</b><br>
     * Utiliza la sentencia {@code try-with-resources} sobre {@link CallableStatement},
     * garantizando la liberación automática de los cursores y recursos de memoria en el
     * controlador JDBC al concluir la ejecución de la consulta.
     * </p>
     *
     * @param user Entidad {@link User} que encapsula los datos modificados que serán persistidos.
     * @throws SQLException Si ocurre un error de comunicación o fallo en la ejecución del procedimiento almacenado en MySQL.
     * @throws SQLIntegrityConstraintViolationException Si se produce una colisión por valor duplicado en columnas únicas 
     *                                                  ({@code email} o {@code user}) o una violación de llave foránea en {@code id_rol}.
     */
    @Override
    public void update(User user) throws SQLException, SQLIntegrityConstraintViolationException {
        String storedProcedure = "{call sp_update_user(?,?,?,?,?,?,?,?,?)}";
        try (CallableStatement callSP = conexionDB.getConnection().prepareCall(storedProcedure)) {
            callSP.setString(1, user.getIdUser());
            callSP.setString(2, user.getName());
            callSP.setString(3, user.getLastName());
            callSP.setString(4, user.getEmail());
            callSP.setString(5, user.getUser());
            callSP.setString(6, user.getPassword());
            callSP.setInt(7, user.getRol());
            callSP.setInt(8, user.getTypeEncrypt());
            callSP.setBoolean(9, user.getStatus());

            callSP.execute();
        }
    }
    
    
}
