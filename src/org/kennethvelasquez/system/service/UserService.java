/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

import java.sql.SQLException;
import org.kennethvelasquez.system.model.User;
import org.kennethvelasquez.system.repository.UserRepository;

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
                    newUser.setPassword(password);
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
