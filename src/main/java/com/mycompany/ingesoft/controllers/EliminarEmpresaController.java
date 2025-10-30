/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.controllers.clases.ControladorUtils;
import com.mycompany.ingesoft.controllers.clases.singleton;
import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.interfaces.ModalListener;
import com.mycompany.ingesoft.models.Sucursal;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
/**
 * FXML Controller class
 *
 * @author natha
 */
public class EliminarEmpresaController implements Initializable {


    @FXML
    private Text txt_tituloConfirmacion;
    @FXML
    private TableView<Sucursal> tabla_sucursal;
    @FXML
    private TableColumn<Sucursal, Integer> col_id;
    @FXML
    private TableColumn<Sucursal, String> col_sucursal;
    @FXML
    private TableColumn<Sucursal, String> col_direccion;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_aceptar;
    /**
     * Initializes the controller class.
     */
    ObservableList<Sucursal> registros = FXCollections.observableArrayList(); 
    private Conexion conexion = new Conexion(); 
    private ClaseDAO dao = new ClaseDAO(conexion.getCon()); 
    private ModalListener listener;
    @FXML
    private Label txt_nameEmpresa;
    

    public void setListener(ModalListener listener) {
        this.listener = listener;
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //col_idCaso.setCellValueFactory(new PropertyValueFactory<>("id_caso"));
        
        col_id.setCellValueFactory(new PropertyValueFactory<>("idSucursal"));
        col_sucursal.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        col_direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        
        String nombre = singleton.getInstancia().getNombreEmpresa(); 
        txt_tituloConfirmacion.setText("¿Desea eliminar la empresa "+nombre+"?");
        try {
            mostrarDatos();
        } catch (SQLException ex) {
            Logger.getLogger(EliminarEmpresaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tabla_sucursal.setItems(registros);
        // TODO
    }    
    
    private void mostrarDatos() throws SQLException{
        registros.setAll(dao.obtenerSucursalesValidacion(singleton.getInstancia().getId_empresa())); 
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ControladorUtils.cerrarModal((Node) event.getSource());
    }
     
    private void eliminarEmpresa(ActionEvent event) throws SQLException {
        ControladorUtils.mostrarWarning("Aviso", "Al eliminar una empresa se eliminan también todos los datos "
                + "relacionados a esa empresa (sucursales y recursos). Esta acción no se puede deshacer. ");

        int companyId = singleton.getInstancia().getId_empresa(); 
        String companyName = singleton.getInstancia().getNombreEmpresa(); 
        if(ControladorUtils.mostrarConfirmacion("Confirmar eliminacion", "¿Está seguro de que desea eliminar la empresa " + companyName + "?")){
            if(dao.eliminarEmpresa(companyId)){
                ControladorUtils.mostrarAlertaChill("Aviso", "La empresa " + companyName +" fue eliminada con éxito "
                        + "al igual que toda la información relacionada a ella.");

                // 🔹 Avisar al padre para que recargue la lista
                if(listener != null) {
                    listener.onDataChanged();
                }

                // 🔹 Cerrar el modal
                ControladorUtils.cerrarModal((Node) event.getSource());

            } else{
                ControladorUtils.mostrarAlertaError("Error al intentar eliminar la empresa", "Ocurrió un error al intentar eliminar la empresa.");
            }
        }
    }

    public void mostrarWarning(String titulo, String mensaje){
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

}
