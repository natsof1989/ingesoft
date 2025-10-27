/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ingesoft.controllers.clases;

import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author natha
 */
public class ControladorUtils {
    public static void mostrarWarning(String titulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText("Aviso");  // Esto elimina el "Message" por defecto
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
     public static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Personalizar botones (opcional)
        ButtonType buttonTypeYes = new ButtonType("Sí", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Mostrar diálogo y esperar respuesta
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }
     
    
    public static void mostrarAlertaChill(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText("Aviso");  // Esto elimina el "Message" por defecto
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    public static void mostrarAlertaError(String titulo, String mensaje) {
        new Alert(Alert.AlertType.ERROR, mensaje).showAndWait();
    }
     /**
     * Cierra cualquier ventana/modal que tenga el nodo raíz.
     * @param node cualquier nodo dentro del modal (puede ser un botón, layout, etc.)
     */
    public static void cerrarModal(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    

    
}
