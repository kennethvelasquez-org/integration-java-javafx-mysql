/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.kennethvelasquez.system.ClasePrincipal;
import javafx.fxml.JavaFXBuilderFactory;

/**
 *
 * @author STEPHRYS
 */
public class ViewFactory {
    private final String PATH_VIEWS = "/org/kennethvelasquez/system/view/";
    public ViewFactory(){
        
    }
    
    public Scene loadFileFXML(String nameFile, int width, int height){
        String pathFileFXML = PATH_VIEWS + nameFile;
        try {
            //Cargador de FXML
            FXMLLoader loaderFXML = new FXMLLoader();
            //Lector de ruta del FXML
            URL pathFile = ClasePrincipal.class.getResource(pathFileFXML);
            loaderFXML.setLocation(pathFile);
            loaderFXML.setBuilderFactory(new JavaFXBuilderFactory());
            
            return new Scene(loaderFXML.load(), width,height);
        } catch (IOException ioException) {
            throw new UncheckedIOException("Error al cargar/leer el archivo FXML", ioException);
        }
    }
    
    public void loadScene(String nameScene){
        Scene scene;
        try {
            switch (nameScene) {
                case "login"-> {
                    scene = loadFileFXML("LoginView.fxml", 350, 425);
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("INICIO DE SESIÓN");
                }
                case "register"-> {
                    scene = loadFileFXML("RegisterView.fxml",635,580);
                    SceneManager.getInstanceSceneManager().getStagePrincipal().setTitle("CREAR CUENTA");
                }

                default-> scene = loadFileFXML("NotFoundView.fxml", 350, 350);
            }
            SceneManager.getInstanceSceneManager().changeScene(scene);
        } catch (NullPointerException nullPointer) {
            AlertInformation alertInfo = new AlertInformation("ERROR CAMBIO DE VENTANA",
                    "Error al cambiar Ventana",
                    "Se genero un error al cambiar de ventana "+nullPointer.getMessage(), 

                    "ERR");
            System.out.println("Error Load Scene");
            nullPointer.printStackTrace();
            alertInfo.viewAlert();
        }
    }
    
    public void loginView(){
        loadScene("login");
    }
    
    public void registerView(){
        loadScene("register");
    }
    
}
