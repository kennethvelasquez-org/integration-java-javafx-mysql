/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.controller;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.kennethvelasquez.system.utils.ViewFactory;

/**
 *
 * @author STEPHRYS
 */
public class DashboardController implements Initializable{
    
    @FXML private Label lblFullName;
    @FXML private BorderPane bpDashboardView;
    @FXML private Button btnLogout;
    @FXML private Button btnMyPerfil;
    @FXML private Button btnProducts;
    @FXML private Button btnUsers;
    @FXML private HBox hboxOption;    
    private ViewFactory viewFacto = new ViewFactory();

    public void initialize(URL url, ResourceBundle rb) {
        String fullName = AuthenticationController.getUserLogued().getName()+" "
                + AuthenticationController.getUserLogued().getLastName();
        lblFullName.setText(fullName);
    }

    private Integer findIndexOptionPressed(String idButton){
        for(Node children: hboxOption.getChildren()){
            if( children instanceof Button 
                    && children.getId().equals(idButton))
                return hboxOption.getChildren().indexOf(children);
        }
        return null;
    }
    
    public void applyStyleOptionPressed(String idButton){
        int indexOptionPressed = findIndexOptionPressed(idButton);
        for(int index=0;index<hboxOption.getChildren().size();index++){
            Button btnOption = (Button) hboxOption.getChildren().get(index);
            if( indexOptionPressed != index){
                btnOption.getStyleClass().remove("active");
            }else{
                if( !btnOption.getStyleClass().contains("active"))
                    btnOption.getStyleClass().add("active");
            }
        }
    }
    
    @FXML
    private void onSelectOption(ActionEvent event){
        /*CALCULAR EN EL TEXTO DEL BUTTON EL ID ↓*/
        //String objectSelected = event.getTarget().toString();
//        int indexStart =objectSelected.indexOf("=")+1;
//        int indexEnd =objectSelected.indexOf(", ");
        Button buttonPressed = (Button) event.getTarget();//OBTENER EL OBTEJO QUE SE ACCIONÓ
        applyStyleOptionPressed(buttonPressed.getId());
    }
    
    @FXML
    private void onMyPerfil(MouseEvent event){
        
    }
}
