/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.utils;

/**
 *
 * @author STEPHRYS
 */
public class Validations {
    public Validations(){
        
    }
    
    public Boolean isEqualsText(String textOriginal, String textCompare){
        return textOriginal.equals(textCompare);
    }
    
    public Boolean isEmptyText(String text){
        
        boolean isEmpty = false;
        
        if( text.isEmpty() || text.isBlank())
            isEmpty = true;
        
        return isEmpty;
    }
    public Boolean isValidLengthText(String text, int lengthMax){
        return  text.length() <= lengthMax;
    }
    
    public Boolean isValidEmail(String email){
        int dotCount=0, arrobeCount=0;
        //VALIDA LA EXISTENCIA DE PUNTOS CONSECUTIVOS
        for( int index=0; index< email.length(); index++ ){
            if( email.charAt( index ) == '.' )
                dotCount++;
            if( dotCount>1 )
                return false;
        }
        //VALIDA LA EXISTENCIA DE SOLO UN UNICO ARROBA
        for( int index=0; index< email.length(); index++ ){
            if( email.charAt( index ) == '@' )
                arrobeCount++;
        }
        return arrobeCount == 1;
    }
    
    /**
     * Valida si un texto representa una estructura decimal numérica válida.
     *
     * @param text Cadena de texto a evaluar.
     * @return true si es convertible a Double; false si contiene letras o formato erróneo.
     */
    public Boolean isValidDecimal(String text) {
        if (text == null || isEmptyText(text)) {
            return false;
        }
        try {
            Double.valueOf(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida si un texto representa un número decimal positivo mayor a cero (ej. precios).
     *
     * @param text Cadena de texto a evaluar.
     * @return true si es un número decimal y su valor es estrictamente mayor a 0.0; false en caso contrario.
     */
    public Boolean isValidPositiveDecimal(String text) {
        if (text == null || isEmptyText(text)) {
            return false;
        }
        try {
            double value = Double.parseDouble(text.trim());
            return value > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida si un texto representa una estructura de número entero válida.
     *
     * @param text Cadena de texto a evaluar.
     * @return true si es convertible a Integer; false si contiene letras, decimales o formato erróneo.
     */
    public Boolean isValidInteger(String text) {
        if (text == null || isEmptyText(text)) {
            return false;
        }
        try {
            Integer.valueOf(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida si un texto representa un número entero positivo mayor a cero (ej. IDs, stock, cantidades).
     *
     * @param text Cadena de texto a evaluar.
     * @return true si es un número entero y su valor es estrictamente mayor a 0; false en caso contrario.
     */
    public Boolean isValidPositiveInteger(String text) {
        if (text == null || isEmptyText(text)) {
            return false;
        }
        try {
            int value = Integer.parseInt(text.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

