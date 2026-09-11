/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author STEPHRYS
 */
public class SceneManager {
    private static SceneManager instanceSceneManager;
    private Stage stagePrincipal;
    private SceneManager(){}
    
    public static SceneManager getInstanceSceneManager(){
        if( instanceSceneManager == null)
            instanceSceneManager = new SceneManager();
        return instanceSceneManager;
    }
    
    public void changeScene(Scene scene){
        try {
            stagePrincipal.setScene(scene);
            stagePrincipal.sizeToScene();
            stagePrincipal.show();
        } catch (NullPointerException objectNull) {
            AlertInformation alertInfo = new AlertInformation();
            System.out.println("Error Change Scene");
            objectNull.printStackTrace();
            alertInfo.viewAlert("ERRO CAMBIO DE ESCENA", 
                    "ERROR AL REALIZAR EL CAMBIO DE ESCENAR EN EL ESCENARIO", 
                    "ERRO AL CAMBIAR ESCENA "+objectNull.getMessage(),
                    "ERR");
        }
    }

    public Stage getStagePrincipal() {
        return stagePrincipal;
    }

    public void setStagePrincipal(Stage stagePrincipal) {
        this.stagePrincipal = stagePrincipal;
    }
    
    
    
}
