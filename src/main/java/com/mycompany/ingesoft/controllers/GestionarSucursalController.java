/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.models.Sucursal;
import com.mycompany.ingesoft.controllers.clases.singleton;
import com.mycompany.ingesoft.interfaces.ModalListener;
import com.mycompany.ingesoft.models.Empresa;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class GestionarSucursalController implements Initializable {

    @FXML
    private VBox contenedorVbox;
    
    private Conexion conexion = new Conexion(); 
    private ClaseDAO dao = new ClaseDAO(conexion.getCon()); 
    private List<Sucursal> sucursales = new ArrayList(); 
    
    private ModalListener listener;

    public void setListener(ModalListener listener){
        this.listener = listener;
    }
    
    private void recargarSucursales() throws SQLException{
            sucursales.clear();
            sucursales.addAll(dao.obtenerSucursalesObjetosIds()); 
            cargarSucursales();
         
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sucursales.addAll(dao.obtenerSucursalesObjetosIds());
            cargarSucursales();
        } catch (SQLException ex) {
            Logger.getLogger(GestionarSucursalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void cargarSucursales() {
        
        contenedorVbox.getChildren().clear();
        contenedorVbox.setSpacing(10);
        
        for (Sucursal sucursal : sucursales) {
            HBox sucursalCard = crearTarjetaSucursal(sucursal);
            contenedorVbox.getChildren().add(sucursalCard);
        }
    }
    
    private HBox crearTarjetaSucursal(Sucursal sucursal) {
        // Contenedor principal de la tarjeta
        HBox cardContainer = new HBox();
        cardContainer.getStyleClass().add("card-container");
        cardContainer.setAlignment(Pos.CENTER_LEFT);
        cardContainer.setSpacing(15);
        cardContainer.setPadding(new Insets(15));
        cardContainer.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        // Contenedor para el nombre de la sucursal
        VBox nombreContainer = new VBox();
        nombreContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label nombreLabel = new Label(sucursal.getDescripcion());
        nombreLabel.getStyleClass().add("sucursal-nombre");
        nombreLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        // Si la sucursal tiene información de empresa, mostrarla
        if (sucursal.getNombreEmpresa() != null && !sucursal.getNombreEmpresa().trim().isEmpty()) {
            Label empresaLabel = new Label(sucursal.getNombreEmpresa());
            empresaLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-font-style: italic;");
            nombreContainer.getChildren().addAll(nombreLabel, empresaLabel);
        } else {
            nombreContainer.getChildren().add(nombreLabel);
        }
        
        // Espacio flexible
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Contenedor para los botones de acción
        HBox accionesContainer = new HBox();
        accionesContainer.setAlignment(Pos.CENTER_RIGHT);
        accionesContainer.setSpacing(10);
        
        // Botón de editar
        StackPane editarBtn = crearBotonIcono("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z", "#2196F3");
        editarBtn.setOnMouseClicked((MouseEvent event) -> {
            // CORRECCIÓN: Limpiar el singleton antes de usarlo
            singleton.getInstancia().reset();
            
            // Establecer que estamos en modo edición
            singleton.getInstancia().setEditado(true);
            singleton.getInstancia().setSucursal(sucursal);
            
            // Crear objeto empresa con los datos necesarios
            Empresa empresa = new Empresa(sucursal.getIdEmpresa(), sucursal.getNombreEmpresa()); 
            singleton.getInstancia().setEmpresa(empresa);
            
            // CORRECCIÓN: El listener debe recargar los datos después de cerrar la ventana de edición
            abrirVentana("nuevaSucursal", "Editar sucursal", () -> {
                try {
                    recargarSucursales();
                } catch (SQLException ex) {
                    Logger.getLogger(GestionarSucursalController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
        
        // Botón de eliminar
        StackPane eliminarBtn = crearBotonIcono("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z", "#F44336");
        eliminarBtn.setOnMouseClicked((MouseEvent event) -> {
            singleton.getInstancia().setSucursal(sucursal);
            singleton.getInstancia().setId_sucursal(sucursal.getIdSucursal());
            // Aquí puedes agregar la lógica para eliminar la sucursal
            abrirVentana("eliminarSucursal", "Eliminar una sucursal", ()->
            {
                try {
                    actualizarSucursales();
                } catch (SQLException ex) {
                    Logger.getLogger(GestionarSucursalController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
        
        accionesContainer.getChildren().addAll(editarBtn, eliminarBtn);
        
        // Agregar todos los elementos al contenedor principal
        cardContainer.getChildren().addAll(nombreContainer, spacer, accionesContainer);
        
        return cardContainer;
    }
    
    private void actualizarSucursales() throws SQLException{
        sucursales = dao.obtenerSucursalesObjetosIds();
        cargarSucursales();
    }
    
    private StackPane crearBotonIcono(String svgPath, String color) {
        StackPane buttonContainer = new StackPane();
        buttonContainer.setPrefSize(40, 40);
        buttonContainer.setStyle("-fx-background-color: " + color + "22; -fx-background-radius: 50;");
        buttonContainer.setAlignment(Pos.CENTER);
        
        
        SVGPath icon = new SVGPath();
        icon.setContent(svgPath);
        icon.setFill(Color.web(color));
        icon.setStyle("-fx-cursor: hand;");
        
        buttonContainer.getChildren().add(icon);
        
        // Efecto hover
        buttonContainer.setOnMouseEntered(event -> {
            buttonContainer.setStyle("-fx-background-color: " + color + "33; -fx-background-radius: 50;");
        });
        
        buttonContainer.setOnMouseExited(event -> {
            buttonContainer.setStyle("-fx-background-color: " + color + "22; -fx-background-radius: 50;");
        });
        
        return buttonContainer;
    }
    
     private void abrirVentana(String fxmlFileName, String titulo, ModalListener listener) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/ingesoft/fxml/" + fxmlFileName + ".fxml"
            ));
            Parent root = loader.load();

            Object controller = loader.getController();
                if (controller instanceof EliminarSucursalController) {
                    ((EliminarSucursalController) controller).setListener(listener);
                } else if (controller instanceof NuevaSucursalController) {
                    ((NuevaSucursalController) controller).setListener(listener);
                }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex) {
            Logger.getLogger(GestionarEmpresaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
