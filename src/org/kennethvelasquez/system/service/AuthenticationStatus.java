package org.kennethvelasquez.system.service;

/**
 * Enumeración que representa los distintos estados o resultados posibles
 * tras un intento de autenticación (inicio de sesión) en el sistema.
 * <p>
 * Facilita la comunicación entre la capa de negocio ({@link AuthenticationService})
 * y la capa de presentación (controladores JavaFX), evitando el uso de códigos numéricos
 * o booleanos ambiguos.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see AuthenticationService
 */
public enum AuthenticationStatus {

    /**
     * El usuario fue autenticado con éxito y sus credenciales coinciden plenamente.
     */
    LOGIN_SUCCESS,

    /**
     * No se encontró ningún registro en la base de datos asociado al usuario o correo provisto.
     */
    ERROR_USER_NOT_FOUND,

    /**
     * Ocurrió un error técnico al intentar buscar y verificar la existencia del usuario en la base de datos.
     */
    ERROR_USER_SEARCH,

    /**
     * El usuario fue encontrado pero la contraseña ingresada no coincide con el hash o texto almacenado.
     */
    ERROR_CREDENTIALS,

    /**
     * Ocurrió un fallo general o inesperado durante la ejecución del proceso de inicio de sesión.
     */
    ERROR_LOGIN
}


