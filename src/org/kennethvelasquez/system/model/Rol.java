/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.model;

/**
 * Entidad de dominio que representa la tabla Rol.
 */
public class Rol {

    private Integer idRol;
    private String name;
    private String description;

    public Rol() {
    }

    public Rol(Integer idRol, String name, String description) {
        this.idRol = idRol;
        this.name = name;
        this.description = description;
    }

    public Rol(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sobrescritura para que el ComboBox de JavaFX muestre el nombre y el del rol.
     */
    @Override
    public String toString() {
        return idRol + " | "+name;
    }
}
