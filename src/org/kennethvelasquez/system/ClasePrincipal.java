/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.kennethvelasquez.system;

import javafx.application.Application;
import javafx.stage.Stage;
import org.kennethvelasquez.system.utils.SceneManager;
import org.kennethvelasquez.system.utils.ViewFactory;
/**
 *
 * @author STEPHRYS
 */
public class ClasePrincipal extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stageRoot){
        SceneManager.getInstanceSceneManager().setStagePrincipal(stageRoot);
        ViewFactory viewFacto = new ViewFactory();
        viewFacto.loginView();
    }
}
