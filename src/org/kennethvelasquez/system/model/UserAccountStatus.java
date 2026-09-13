/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.model;
/**
 * Representa el estado de una cuenta de usuario.
 * Mapea la representación en UI (texto), modelo (boolean) y persistencia (código numérico).
 */
public enum UserAccountStatus {
    ACTIVO(1, true, "ACTIVO"),
    INACTIVO(2, false, "INACTIVO");
    private final int dbValue;        // Lo que se guarda en MySQL (1 o 2)
    private final boolean active;      // Lo que se evalúa en lógica de Java
    private final String displayName;  // Lo que ve el usuario en el ComboBox
    UserAccountStatus(int dbValue, boolean active, String displayName) {
        this.dbValue = dbValue;
        this.active = active;
        this.displayName = displayName;
    }
    public int getDbValue() {
        return dbValue;
    }
    public boolean isActive() {
        return active;
    }
    public String getDisplayName() {
        return displayName;
    }
    /**
     * Convierte el número de la BD (1 o 2) al enum correspondiente.
     * @param dbValue
     * @return 
     */
    public static UserAccountStatus fromDb(Integer dbValue) {
        if (dbValue == null) return INACTIVO;
        return (dbValue == 1) ? ACTIVO : INACTIVO;
    }
    /**
     * Convierte un boolean (true o false) al enum correspondiente.
     * @param active
     * @return 
     */
    public static UserAccountStatus fromBoolean(Boolean active) {
        if (active == null) return INACTIVO;
        return active ? ACTIVO : INACTIVO;
    }
    /**
     * Sobrescribir toString() hace que JavaFX muestre automáticamente
     * "ACTIVO" o "INACTIVO" en el ComboBox sin necesidad de celdas personalizadas.
     */
    @Override
    public String toString() {
        return displayName;
    }
}