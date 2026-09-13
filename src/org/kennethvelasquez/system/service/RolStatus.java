/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

/**
 * Enumeración que define los posibles estados resultantes tras operaciones
 * de negocio y CRUD sobre la entidad {@link org.kennethvelasquez.system.model.Rol}.
 * <p>
 * Facilita la comunicación entre {@link RolService} y los controladores de la vista,
 * permitiendo disparar alertas específicas ({@link org.kennethvelasquez.system.utils.AlertInformation})
 * según el desenlace de cada acción.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see RolService
 * @see org.kennethvelasquez.system.model.Rol
 */
public enum RolStatus {
    // ==========================================
    // 1. ESTADOS DE CREACIÓN Y VALIDACIÓN
    // ==========================================
    /**
     * El rol se registró y persistió exitosamente en la base de datos.
     */
    ROL_CREATED,
    /**
     * El nombre del rol ingresado ya se encuentra registrado.
     */
    ROL_EXISTS,
    /**
     * Ocurrió un error inesperado al momento de ejecutar la inserción del rol.
     */
    ERROR_ROL_CREATE,
    // ==========================================
    // 2. ESTADOS DE LECTURA Y CONSULTA
    // ==========================================
    /**
     * La consulta se ejecutó exitosamente y se recuperó la lista de roles.
     */
    READ_SUCCESS,
    /**
     * La consulta se ejecutó con éxito pero no existen registros en la tabla Rol.
     */
    EMPTY_LIST,
    /**
     * Ocurrió un fallo técnico o error de SQL al intentar consultar la lista de roles.
     */
    ERROR_READ_ROLES,
    /**
     * El rol buscado por su identificador fue localizado satisfactoriamente.
     */
    ROL_FOUND,
    /**
     * No se encontró ningún rol con el identificador provisto.
     */
    ROL_NOT_FOUND,
    /**
     * Ocurrió un error de SQL al momento de buscar un rol individual.
     */
    ERROR_SEARCH_ROL,
    // ==========================================
    // 3. ESTADOS DE ACTUALIZACIÓN
    // ==========================================
    /**
     * La información del rol fue actualizada con éxito en la base de datos.
     */
    ROL_UPDATED,
    /**
     * Ocurrió un error técnico al intentar actualizar el rol.
     */
    ERROR_ROL_UPDATE,
    // ==========================================
    // 4. ESTADOS DE ELIMINACIÓN
    // ==========================================
    /**
     * El rol fue eliminado exitosamente del sistema.
     */
    ROL_DELETED,
    /**
     * No se puede eliminar el rol porque está vinculado a uno o más usuarios existentes
     * (Violación de restricción de clave foránea / FK).
     */
    ROL_HAS_DEPENDENCIES,
    /**
     * Ocurrió un fallo técnico al intentar eliminar el rol.
     */
    ERROR_ROL_DELETE
}
