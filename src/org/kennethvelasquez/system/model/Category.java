/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.model;

import java.util.Objects;

/**
 * Entidad de dominio que representa la tabla Category en la base de datos.
 * <p>
 * Mapea los atributos requeridos para la clasificación de productos:
 * id_category, name_category y description.
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 */
public class Category {

    private Integer idCategory;
    private String nameCategory;
    private String description;

    public Category() {
    }

    /**
     * Constructor para la creación de una nueva categoría (sin ID autoincremental).
     *
     * @param nameCategory Nombre de la categoría (máximo 100 caracteres).
     * @param description  Descripción detallada (máximo 200 caracteres).
     */
    public Category(String nameCategory, String description) {
        this.nameCategory = nameCategory;
        this.description = description;
    }

    /**
     * Constructor completo con identificador único de base de datos.
     *
     * @param idCategory   Identificador único autoincremental.
     * @param nameCategory Nombre de la categoría.
     * @param description  Descripción detallada.
     */
    public Category(Integer idCategory, String nameCategory, String description) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.description = description;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retorna el nombre de la categoría para su representación directa en componentes JavaFX (como ComboBox).
     *
     * @return Nombre de la categoría o cadena vacía si es nulo.
     */
    @Override
    public String toString() {
        return idCategory + " | "+nameCategory;
    }
}
