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
    USER_EXISTS,
    
    // ESTADOS DE LECTURA Y CONSULTA
    /**
     * La consulta se ejecutó exitosamente y se recuperó la lista de usuarios.
     */
    READ_SUCCESS,
    /**
     * No existe el usuario que se estaba buscando en la base de datos.
     */
    USER_NOT_FOUND,
    /**
     * Ocurrió un fallo técnico o error de SQL al intentar consultar la lista de usuarios.
     */
    ERROR_READ_USERS,
    /**
     * La consulta se ejecutó con éxito pero no existen registros en la base de datos.
     */
    EMPTY_LIST,
    
    // ESTADOS DE ACTUALIZACIÓN / ELIMINACIÓN
    /** La información del usuario fue actualizada con éxito. */
    USER_UPDATED,
    /** Ocurrió un error al intentar actualizar el usuario. */
    ERROR_USER_UPDATE,
    /** El usuario fue desactivado o eliminado exitosamente. */
    USER_DELETED,
    /** Ocurrió un error al intentar eliminar o desactivar al usuario. */
    ERROR_USER_DELETE
}