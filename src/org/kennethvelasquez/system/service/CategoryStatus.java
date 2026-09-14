/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

/**
 * Enumeración que define los posibles estados resultantes tras operaciones
 * de negocio y lectura sobre la entidad {@link org.kennethvelasquez.system.model.Category}.
 * <p>
 * Facilita la comunicación entre {@link CategoryService} y los controladores de la vista
 * como {@link org.kennethvelasquez.system.controller.ProductViewController}, permitiendo
 * disparar alertas específicas según el desenlace de cada acción.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see CategoryService
 * @see org.kennethvelasquez.system.model.Category
 */
public enum CategoryStatus {
    /**
     * La consulta se ejecutó exitosamente y se recuperó la lista de categorías.
     */
    READ_SUCCESS,
    /**
     * La consulta se ejecutó con éxito pero no existen registros en la tabla Category.
     */
    EMPTY_LIST,
    /**
     * Ocurrió un fallo técnico o error de SQL al intentar consultar la lista de categorías.
     */
    ERROR_READ_CATEGORIES
}
