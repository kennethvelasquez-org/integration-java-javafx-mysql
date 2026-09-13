/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import org.kennethvelasquez.system.model.User;
import org.kennethvelasquez.system.model.dto.UserDTO;
import org.kennethvelasquez.system.repository.UserRepository;
import org.kennethvelasquez.system.utils.ToolBCrypt;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con los usuarios.
 * <p>
 * Pertenece a la <b>Capa de Negocio (Service Layer)</b>. Su función principal es:
 * <ul>
 *   <li>Validar reglas de negocio antes de persistir la información (por ejemplo, evitar usuarios duplicados).</li>
 *   <li>Determinar el flujo de creación según el mecanismo de encriptación seleccionado.</li>
 *   <li>Capturar y transformar excepciones técnicas de persistencia (como {@link SQLException})
 *       en estados comprensibles por la interfaz de usuario ({@link UserStatus}).</li>
 * </ul>
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see org.kennethvelasquez.system.repository.UserRepository
 * @see org.kennethvelasquez.system.service.UserStatus
 * @see org.kennethvelasquez.system.model.User
 */
public class UserService {
    
    /**
     * Repositorio para la interacción directa con la base de datos en operaciones de usuario.
     */
    private UserRepository userRepo = new UserRepository();
    
    private List<UserDTO> usersList;
    
    /**
     * Almacena el mensaje de error técnico producido durante las operaciones con la base de datos.
     * <p>
     * Se utiliza para propagar la causa del fallo hacia las capas superiores (controladores/vistas).
     * </p>
     */
    private String messageError;
    
    private final Integer rolStandard = 101; 
    
    /**
     * Constructor por defecto del servicio de usuarios.
     */
    public UserService(){
        
    }
    
    /**
     * Orquesta el proceso de validación y creación de un nuevo usuario en el sistema.
     * <p>
     * El flujo de ejecución es el siguiente:
     * <ol>
     *   <li>Verifica si ya existe un usuario registrado con el mismo nombre de usuario o correo electrónico.</li>
     *   <li>Si el usuario ya existe, interrumpe el flujo retornando {@link UserStatus#USER_EXISTS}.</li>
     *   <li>Si no existe, instancia un nuevo objeto {@link User} con rol por defecto (101).</li>
     *   <li>Deriva la inserción al método correspondiente del repositorio según el tipo de encriptación:
     *       <ul>
     *         <li><b>1:</b> Inserción sin encriptación (texto plano).</li>
     *         <li><b>2:</b> Inserción con hash MD5 generado a nivel de base de datos.</li>
     *         <li><b>3:</b> Inserción para encriptación con BCrypt.</li>
     *       </ul>
     *   </li>
     *   <li>Si ocurre un error en la base de datos, captura la excepción y retorna el estado de error apropiado.</li>
     * </ol>
     * </p>
     *
     * @param name         Nombre(s) del usuario.
     * @param lastName     Apellido(s) del usuario.
     * @param email        Dirección de correo electrónico (debe ser única en la base de datos).
     * @param user         Nombre de usuario único para credenciales de acceso.
     * @param password     Contraseña del usuario en texto plano ingresada en la vista.
     * @param typeEncrypt  Identificador del algoritmo de encriptación (1 = Sin protección, 2 = MD5, 3 = BCrypt).
     * @return Un valor de {@link UserStatus} que indica el resultado de la operación:
     *         <ul>
     *           <li>{@link UserStatus#USER_CREATED} si se registró con éxito.</li>
     *           <li>{@link UserStatus#USER_EXISTS} si el correo o usuario ya estaban registrados.</li>
     *           <li>{@link UserStatus#INCORRECT_ENCRYPT_TYPE} si el tipo de encriptación no es válido.</li>
     *           <li>{@link UserStatus#ERROR_USER_SEARCH} si falló la verificación de duplicados.</li>
     *           <li>{@link UserStatus#ERROR_USER_CREATE} si ocurrió un fallo inesperado al persistir los datos.</li>
     *         </ul>
     */
    public UserStatus createUser(String name, String lastName, String email,
            String user, String password,  Integer typeEncrypt){
        try {
            boolean searchUser = userRepo.existsByEmailOrUser(user,email);
            if( searchUser == true)
                return UserStatus.USER_EXISTS;
        } catch (SQLException e) {
            messageError = e.getMessage();
            return UserStatus.ERROR_USER_SEARCH;
        }
        
        try {
            User newUser = new User(name, lastName, email, user, password, rolStandard, typeEncrypt);
            switch (typeEncrypt) {
                case 1 -> userRepo.createUnprotected(newUser);
                case 2 -> userRepo.createMD5(newUser);
                case 3 -> {
                    ToolBCrypt encrypt = new ToolBCrypt();
                    newUser.setPassword(encrypt.encryptToString(password));
                    userRepo.createBCrypt(newUser);
                }
                default -> {
                    return UserStatus.INCORRECT_ENCRYPT_TYPE;
                }
            }
            return UserStatus.USER_CREATED;
        } catch (SQLException e) {
            messageError = e.getMessage();
            return UserStatus.ERROR_USER_CREATE;
        } catch (Exception e){
            messageError = e.getMessage();
            return UserStatus.ERROR_USER_CREATE;
        }
    }
    
    /**
     * Consulta el catálogo de usuarios para poblar la vista del TableView.
     *
     * @return El estado de la operación ({@link UserStatus#READ_SUCCESS}, 
     *         {@link UserStatus#EMPTY_LIST} o {@link UserStatus#ERROR_READ_USERS}).
     */
    public UserStatus readUsers() {
        try {
            this.usersList = userRepo.read();
            
            if (this.usersList == null || this.usersList.isEmpty()) 
                return UserStatus.EMPTY_LIST;
            
            return  UserStatus.READ_SUCCESS;
        } catch (SQLException  e) {
            this.messageError = e.getMessage();
            return UserStatus.ERROR_READ_USERS;
        } catch (Exception  e) {
            this.messageError = e.getMessage();
            return UserStatus.ERROR_READ_USERS;
        }
    }
    
    /**
     * Desactiva lógicamente a un usuario verificando previamente su existencia en el sistema.
     *
     * @param idUser Identificador único (UUID) del usuario a desactivar.
     * @param user   Nombre de usuario para verificación previa.
     * @param email  Correo electrónico para verificación previa.
     * @return {@link UserStatus#USER_DELETED} si se desactivó con éxito;
     *         {@link UserStatus#USER_NOT_FOUND} si no se localizó la cuenta;
     *         {@link UserStatus#ERROR_USER_SEARCH} si falló la comprobación previa;
     *         {@link UserStatus#ERROR_USER_DELETE} ante parámetros inválidos o fallo al eliminar.
     */
    public UserStatus deleteUser(String idUser, String user, String email) {
        if (idUser == null || idUser.trim().isEmpty()) {
            this.messageError = "El ID de usuario no es válido.";
            return UserStatus.ERROR_USER_DELETE;
        }
        
         try {
            boolean searchUser = userRepo.existsByEmailOrUser(user,email);
            if( searchUser != true)
                return UserStatus.USER_NOT_FOUND;
        } catch (SQLException e) {
            messageError = e.getMessage();
            return UserStatus.ERROR_USER_SEARCH;
        }
         
        try {
            userRepo.delete(idUser.trim());
            return UserStatus.USER_DELETED;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return UserStatus.ERROR_USER_DELETE;
        }
    }
    
    public UserStatus updateUser(String idUser, String name, String lastName, String email,
                                 String user, String password, Integer idRol, 
                                 Integer typeEncrypt, Boolean userStatus){        
        try {
            User newUser = new User(idUser,name, lastName, email, user, password, idRol, typeEncrypt,userStatus);
            switch (typeEncrypt) {
                case 1, 2 -> userRepo.update(newUser);
                case 3 -> {
                    ToolBCrypt encrypt = new ToolBCrypt();
                    newUser.setPassword(encrypt.encryptToString(password));
                    userRepo.update(newUser);
                }
                default -> {
                    return UserStatus.INCORRECT_ENCRYPT_TYPE;
                }
            }
            return UserStatus.USER_UPDATED;
        } catch (SQLIntegrityConstraintViolationException e) {
            messageError = e.getMessage();
            return UserStatus.USER_EXISTS;
        } catch (SQLException e) {
            messageError = e.getMessage();
            return UserStatus.ERROR_USER_UPDATE;
        } catch (Exception e){
            messageError = e.getMessage();
            return UserStatus.ERROR_USER_UPDATE;
        }
    }
    
    public List<UserDTO> getUsersList() {
        return usersList;
    }

    /**
     * Obtiene el mensaje de error técnico almacenado.
     * 
     * @return El mensaje de error actual.
     */
    public String getMessageError() {
        return messageError;
    }

    /**
     * Establece el mensaje de error técnico.
     * 
     * @param messageError El mensaje de error a establecer.
     */
    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
}
