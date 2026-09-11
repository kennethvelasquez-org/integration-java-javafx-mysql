package org.kennethvelasquez.system.service;

/**
 * Enumeración que define los posibles estados resultantes tras el proceso
 * de creación y registro de un usuario en el sistema.
 * <p>
 * Comunica el desenlace de la operación desde {@link UserService} hacia
 * {@link org.kennethvelasquez.system.controller.RegisterController}, permitiendo
 * desplegar alertas específicas según el tipo de respuesta.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see UserService
 */
public enum UserStatus {

    /**
     * El usuario se registró y persistió exitosamente en la base de datos.
     */
    USER_CREATED,

    /**
     * El identificador del tipo de encriptación seleccionado no corresponde a ninguna opción válida.
     */
    INCORRECT_ENCRYPT_TYPE,

    /**
     * Ocurrió un error al intentar verificar la disponibilidad del correo o usuario en la base de datos.
     */
    ERROR_USER_SEARCH,

    /**
     * Ocurrió un error inesperado al momento de ejecutar la inserción del nuevo usuario.
     */
    ERROR_USER_CREATE,

    /**
     * El correo electrónico o nombre de usuario ingresado ya se encuentra registrado por otra cuenta.
     */
    USER_EXISTS
}