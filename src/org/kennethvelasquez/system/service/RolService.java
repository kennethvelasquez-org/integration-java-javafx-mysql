/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.service;

import java.util.ArrayList;
import java.util.List;
import org.kennethvelasquez.system.model.Rol;
import org.kennethvelasquez.system.repository.RolRepository;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;

/**
 *
 * @author STEPHRYS
 */
public class RolService {
    /**
     * Repositorio desacoplado mediante interfaz para acceso a base de datos.
     */
    private RolRepository rolRepo = new RolRepository();
    /**
     * Mensaje técnico de error para auditoría o propagación a la vista.
     */
    private String messageError;
    /**
     * Caché de roles en memoria tras una operación de lectura.
     */
    private List<Rol> rolesList = new ArrayList<>();
    /**
     * Variable para almacenar el rol obtenido en búsquedas individuales.
     */
    private Rol currentRol;
    public RolService() {
    }
    
    /**
     * Valida y crea un nuevo rol en la base de datos.
     *
     * @param name        Nombre del rol (máximo 70 caracteres).
     * @param description Descripción del rol (máximo 100 caracteres).
     * @return Estado resultante de la operación en {@link RolStatus}.
     */
    public RolStatus createRol(String name, String description) {
        try {
            Rol newRol = new Rol(name.trim(), description.trim());
            rolRepo.create(newRol);
            return RolStatus.ROL_CREATED;
        } catch (SQLIntegrityConstraintViolationException e) {
            this.messageError = e.getMessage();
            return RolStatus.ROL_EXISTS;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return RolStatus.ERROR_ROL_CREATE;
        }
    }
    
    /**
     * Obtiene todos los roles registrados para suministrar a ComboBox o TableView.
     *
     * @return {@link RolStatus#READ_SUCCESS} si se encontraron registros,
     *         {@link RolStatus#EMPTY_LIST} si la tabla está vacía,
     *         o {@link RolStatus#ERROR_READ_ROLES} ante un fallo de SQL.
     */
    public RolStatus readRoles() {
        try {
            this.rolesList = rolRepo.read();
            if (this.rolesList == null || this.rolesList.isEmpty()) 
                return RolStatus.EMPTY_LIST;
            
            return RolStatus.READ_SUCCESS;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return RolStatus.ERROR_READ_ROLES;
        }
    }
    
    /**
     * Localiza un rol por su identificador único.
     *
     * @param idRol Identificador del rol.
     * @return {@link RolStatus#ROL_FOUND} si se localizó,
     *         o {@link RolStatus#ROL_NOT_FOUND} si no existe.
     */
    public RolStatus searchRol(int idRol) {
        try {
            this.currentRol = rolRepo.search(idRol);
            if (this.currentRol != null) 
                return RolStatus.ROL_FOUND;
            
            return RolStatus.ROL_NOT_FOUND;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return RolStatus.ERROR_SEARCH_ROL;
        }
    }
    
    /**
     * Valida y actualiza los datos de un rol existente.
     *
     * @param idRol       Identificador del rol a modificar.
     * @param name        Nuevo nombre del rol.
     * @param description Nueva descripción del rol.
     * @return Estado resultante de la actualización.
     */
    public RolStatus updateRol(int idRol, String name, String description) {
        try {
            Rol rol = new Rol(idRol, name.trim(), description.trim());
            rolRepo.update(rol);
            return RolStatus.ROL_UPDATED;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return RolStatus.ERROR_ROL_UPDATE;
        }
    }
   
    /**
     * Elimina un rol del sistema tras verificar que no tenga usuarios dependientes.
     *
     * @param idRol Identificador único del rol.
     * @return {@link RolStatus#ROL_DELETED} si se eliminó,
     *         {@link RolStatus#ROL_HAS_DEPENDENCIES} si tiene usuarios vinculados (FK),
     *         o {@link RolStatus#ERROR_ROL_DELETE} si ocurrió otro error SQL.
     */
    public RolStatus deleteRol(int idRol) {
        if (idRol <= 0) {
            return RolStatus.ROL_NOT_FOUND;
        }
        try {
            rolRepo.delete(idRol);
            return RolStatus.ROL_DELETED;
        } catch (SQLIntegrityConstraintViolationException e) {
            // Se dispara automáticamente cuando un usuario en MySQL tiene este id_rol asignado
            this.messageError = e.getMessage();
            return RolStatus.ROL_HAS_DEPENDENCIES;
        } catch (SQLException e) {
            this.messageError = e.getMessage();
            return RolStatus.ERROR_ROL_DELETE;
        }
    }
    
    public Rol searchRolByName(String name){
        for (Rol rol : rolesList) {
            if( rol.getName().equals(name))
                return rol;
        }
        return null;
    }
    public Integer searchIndexRolByName(String name){
        for (Rol rol : rolesList) {
            if( rol.getName().equals(name))
                return rolesList.indexOf(rol);
        }
        return null;
    }
    
    public List<Rol> getRolesList() {
        return rolesList;
    }
    public Rol getCurrentRol() {
        return currentRol;
    }
    public String getMessageError() {
        return messageError;
    }
}
