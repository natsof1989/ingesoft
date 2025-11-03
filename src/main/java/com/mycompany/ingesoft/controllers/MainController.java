/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.ingesoft.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class MainController implements Initializable {

    @FXML private FlowPane flowPane;
    @FXML private ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("🔍 Inicializando MainController...");
        
        configurarFlowPaneResponsive();
        cargarDatos();
    }
    
    private void configurarFlowPaneResponsive() {
        if (flowPane == null) {
            System.err.println("❌ flowPane es null");
            return;
        }
        
        flowPane.setHgap(15);
        flowPane.setVgap(15);
        flowPane.setPadding(new Insets(20));
        
        if (scrollPane != null) {
            scrollPane.viewportBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                double anchoDisponible = newBounds.getWidth();
                flowPane.setPrefWrapLength(anchoDisponible);
                
            });
        }
    }
    
    private void cargarDatos() {
        List<String> datos = List.of("Empresa 1", "Empresa 2", "Empresa 3", "Empresa 4", 
                                   "Empresa 5", "Empresa 6", "Empresa 7", "Empresa 8");
        
        for (String texto : datos) {
            try {
                Node item = crearItem(texto);
                if (item != null) {
                    flowPane.getChildren().add(item);
                    System.out.println("✅ Item añadido: " + texto);
                }
            } catch (Exception e) {
                System.err.println("❌ Error con item: " + texto);
                e.printStackTrace();
            }
        }
        
        System.out.println("🎯 Total items cargados: " + flowPane.getChildren().size());
    }
    
    private Node crearItem(String texto) throws IOException {
        // Ruta CORRECTA para item.fxml en resources/fxml/
        URL fxmlUrl = getClass().getResource("/com/mycompany/ingesoft/fxml/item.fxml");
        
        if (fxmlUrl == null) {
            System.err.println("❌ ERROR: item.fxml no encontrado en: /com/mycompany/ingesoft/fxml/item.fxml");
            return crearItemFallback(texto);
        }
        
        
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Node nodo = loader.load();
        
        ItemController controller = loader.getController();
        if (controller != null) {
            controller.setData(texto);
        } else {
            System.err.println("❌ Controller es null para: " + texto);
        }
        
        return nodo;
    }
    
    private Node crearItemFallback(String texto) {
        // Item de emergencia por si hay problemas
        VBox item = new VBox();
        item.setPrefSize(180, 100);
        item.setStyle("-fx-background-color: #3c3f41; -fx-background-radius: 10; -fx-padding: 15;");
        item.setAlignment(Pos.CENTER);
        
        Label label = new Label(texto);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        
        item.getChildren().add(label);
        return item;
    }
}