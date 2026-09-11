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
    
}
