/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.model;

/**
 *
 * @author STEPHRYS
 */
public class User {
    private String name;
    private String lastName;
    private String email;
    private String user;
    private String password;
    private Integer rol;
    private Integer typeEncrypt;
    private String idUser;

    public User() {
    }

    public User(String name, String lastName, Integer rol) {
        this.name = name;
        this.lastName = lastName;
        this.rol = rol;
    }

    public User(String name, String lastName, String email, String user, 
            String password, Integer rol, Integer typeEncrypt) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.user = user;
        this.password = password;
        this.rol = rol;
        this.typeEncrypt = typeEncrypt;
    }
    public User(String name, String lastName, String email, String user, Integer rol) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.user = user;
        this.rol = rol;
    }
    public User(String name, String lastName, String email, String user, 
            String password, Integer rol, Integer typeEncrypt,String idUser) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.user = user;
        this.password = password;
        this.rol = rol;
        this.typeEncrypt = typeEncrypt;
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRol() {
        return rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    public Integer getTypeEncrypt() {
        return typeEncrypt;
    }

    public void setTypeEncrypt(Integer typeEncrypt) {
        this.typeEncrypt = typeEncrypt;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    
}
