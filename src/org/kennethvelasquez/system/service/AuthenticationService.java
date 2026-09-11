/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

import java.sql.SQLException;
import org.kennethvelasquez.system.controller.AuthenticationController;
import org.kennethvelasquez.system.model.User;
import org.kennethvelasquez.system.repository.UserRepository;
import org.kennethvelasquez.system.utils.ToolBCrypt;

/**
 * Servicio encargado de gestionar la lógica de autenticación e inicio de sesión de los usuarios.
 * <p>
 * Pertenece a la <b>Capa de Negocio (Service Layer)</b>. Su función es:
 * <ul>
 *   <li>Consultar la existencia del usuario mediante su nombre de usuario o correo.</li>
 *   <li>Identificar el tipo de cifrado asociado a la cuenta (texto plano, MD5 o BCrypt).</li>
 *   <li>Delegar la verificación de credenciales al repositorio o a la utilidad criptográfica {@link ToolBCrypt}.</li>
 *   <li>Almacenar al usuario autenticado en la sesión global ({@link AuthenticationController}) tras un inicio exitoso.</li>
 *   <li>Retornar estados estructurados ({@link AuthenticationStatus}) para notificar el resultado a la vista.</li>
 * </ul>
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see AuthenticationStatus
 * @see AuthenticationController
 * @see UserRepository
 * @see ToolBCrypt
 */
public class AuthenticationService {
    
    /**
     * Repositorio utilizado para consultar datos de usuarios y validar contraseñas almacenadas.
     */
    private UserRepository userRepo = new UserRepository();

    /**
     * Almacena el mensaje de error técnico producido durante la consulta o conexión.
     */
    private String messageError;
    
    /**
     * Constructor por defecto del servicio de autenticación.
     */
    public AuthenticationService(){
        
    }
    
    /**
     * Autentica las credenciales de un usuario en el sistema.
     * <p>
     * El flujo de inicio de sesión se realiza en las siguientes etapas:
     * <ol>
     *   <li>Busca el registro del usuario por su correo o nombre de usuario en la base de datos.</li>
     *   <li>Si no se encuentra, finaliza retornando {@link AuthenticationStatus#ERROR_USER_NOT_FOUND}.</li>
     *   <li>Si existe, evalúa el atributo {@code typeEncrypt} del usuario:
     *       <ul>
     *         <li><b>Caso 1 (Sin protección):</b> Consulta a la base de datos mediante {@code loginUnprotected}.</li>
     *         <li><b>Caso 2 (MD5):</b> Valida la coincidencia del hash MD5 mediante {@code loginMD5}.</li>
     *         <li><b>Caso 3 (BCrypt):</b> Compara la contraseña en texto plano contra el hash almacenado
     *             utilizando {@link ToolBCrypt#validatePassword(String, String)}.</li>
     *       </ul>
     *   </li>
     *   <li>Si las credenciales no coinciden, retorna {@link AuthenticationStatus#ERROR_CREDENTIALS}.</li>
     *   <li>Si la autenticación es exitosa, registra la sesión en {@link AuthenticationController#setUserLogued(User)}
     *       y retorna {@link AuthenticationStatus#LOGIN_SUCCESS}.</li>
     * </ol>
     * </p>
     *
     * @param userData Correo electrónico o nombre de usuario ingresado en el formulario de acceso.
     * @param password Contraseña en texto plano provista por el usuario.
     * @return Un valor de {@link AuthenticationStatus} que describe el resultado:
     *         <ul>
     *           <li>{@link AuthenticationStatus#LOGIN_SUCCESS} si las credenciales son válidas.</li>
     *           <li>{@link AuthenticationStatus#ERROR_USER_NOT_FOUND} si el usuario o correo no existe.</li>
     *           <li>{@link AuthenticationStatus#ERROR_CREDENTIALS} si la contraseña no coincide.</li>
     *           <li>{@link AuthenticationStatus#ERROR_USER_SEARCH} si falló la búsqueda inicial en la base de datos.</li>
     *           <li>{@link AuthenticationStatus#ERROR_LOGIN} si ocurrió un error inesperado al procesar el inicio de sesión.</li>
     *         </ul>
     */
    public AuthenticationStatus userLogin(String userData, String password){
        User searchUser;
        try {
            searchUser = userRepo.searchByEmailOrUser(userData);
            if( searchUser ==null )
                return AuthenticationStatus.ERROR_USER_NOT_FOUND;
        } catch (SQLException e) {
            messageError = e.getMessage();
            return AuthenticationStatus.ERROR_USER_SEARCH;
        }
        try {
            User userLogued = switch(searchUser.getTypeEncrypt()){
                case 1->userRepo.loginUnprotected(userData, password);
                    
                case 2->userRepo.loginMD5(userData, password);
                    
                case 3->{
                    ToolBCrypt encript = new ToolBCrypt();
                    String savedPassword = searchUser.getPassword();
                    boolean passwordValid = encript.validatePassword(password, savedPassword);
                    if( passwordValid == true )
                        yield searchUser;
                    else 
                        yield null;
                }
                default-> null;
            };
            if( userLogued == null)
                return AuthenticationStatus.ERROR_CREDENTIALS;
            
            AuthenticationController.setUserLogued(userLogued);
            return AuthenticationStatus.LOGIN_SUCCESS;
        } catch (SQLException e) {
            messageError = e.getMessage();
            return AuthenticationStatus.ERROR_LOGIN;
        } catch (Exception e) {
            messageError = e.getMessage();
            return AuthenticationStatus.ERROR_LOGIN;
        }
    }

    /**
     * Obtiene el mensaje del último error técnico capturado.
     *
     * @return Mensaje de error técnico.
     */
    public String getMessageError() {
        return messageError;
    }

    /**
     * Establece el mensaje de error técnico.
     *
     * @param messageError Mensaje de error a registrar.
     */
    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
    
}
