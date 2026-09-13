/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.model.dto;

import org.kennethvelasquez.system.model.User;

/**
 * Objeto de transferencia de datos (DTO) para la visualización en el TableView
 * de usuarios. Mapea directamente la vista de base de datos view_read_users.
 */

public class UserDTO extends User {
    // No declaramos name, email, etc., porque ya los hereda de User
    
    private String rolName;     // Se llama rolName para NO chocar con Integer rol de User
    private String userStatus; // Nuevo campo que no estaba en el User original
    private String typeEncryptName;
    private String idRolStr;
    private String typeEncryptStr;
    private String statusStr;
    
    public UserDTO() {
        super();
    }

    public UserDTO(String idUser, String name, String lastName, String email, String user,
            Integer rol, String rolName,  Integer typeEncrypt, Boolean status) {
        super(idUser, name, lastName, email, user, rol, typeEncrypt, status);
        this.rolName = rolName;
    }
    
    public UserDTO(String idUser, String name, String lastName, String email, String user,
            String idRolStr, String rolName,  String typeEncryptStr, String statusStr) {
        super();
        // Uso el setter heredado del padre (User)
        setIdUser(idUser);
        setName(name);
        setLastName(lastName);
        setEmail(email);
        setUser(user);
        this.idRolStr = idRolStr;
        this.typeEncryptStr = typeEncryptStr;
        this.statusStr = statusStr;
        this.rolName = rolName;
    }
    
    public String getRolName() {
        return rolName;
    }
    public void setRolName(String rolName) {
        this.rolName = rolName;
    }

    /**
     * Retorna la representación visual del estado del usuario para el TableView.
     * @return Valor String del estado de la cuenta
     */
    public String getUserStatus() {
        return (this.getStatus()) ? "ACTIVO" : "INACTIVO";
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * Retorna el nombre amigable del algoritmo de cifrado para el TableView.
     * Seguro contra valores nulos.
     * @return Valor String del cifrado de la contraseña
     */
    public String getTypeEncryptName() {
        if (this.getTypeEncrypt() == null) {
            return "Sin definir";
        }
        
        return switch (this.getTypeEncrypt()) {
            case 1 -> "DESPROTEGIDO";
            case 2 -> "MD5";
            case 3 -> "BCRYPT";
            default -> "Desconocido";
        };
    }

    public void setTypeEncryptName(String typeEncryptName) {
        this.typeEncryptName = typeEncryptName;
    }

    public String getIdRolStr() {
        return idRolStr;
    }

    public void setIdRolStr(String idRolStr) {
        this.idRolStr = idRolStr;
    }

    public String getTypeEncryptStr() {
        return typeEncryptStr;
    }

    public void setTypeEncryptStr(String typeEncryptStr) {
        this.typeEncryptStr = typeEncryptStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
    
}