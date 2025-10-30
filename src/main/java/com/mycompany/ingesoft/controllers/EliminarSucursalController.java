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
import com.mycompany.ingesoft.models.Recurso;
import com.mycompany.ingesoft.models.Sucursal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class EliminarSucursalController implements Initializable {

    @FXML
    private Label txt_nameEmpresa;
    @FXML
    private Text txt_tituloConfirmacion;
    @FXML
    private TableView<Recurso> tabla_recurso;
    @FXML
    private TableColumn<Recurso, Integer> col_id;
    @FXML
    private TableColumn<Recurso, String> col_recurso;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_aceptar;
    
    ObservableList<Recurso> registros = FXCollections.observableArrayList(); 
    private Sucursal sucursal = singleton.getInstancia().getSucursal(); 
    private Conexion conexion = new Conexion(); 
    private ClaseDAO dao = new ClaseDAO(conexion.getCon()); 
    private ModalListener listener;
    public void setListener(ModalListener listener) {
        this.listener = listener;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        col_id.setCellValueFactory(new PropertyValueFactory<>("idRecurso"));
        col_recurso.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        
        tabla_recurso.setItems(registros);
        
        txt_tituloConfirmacion.setText("¿Desea eliminar la sucursal "+sucursal.getDescripcion()+"?");
        txt_nameEmpresa.setText(sucursal.getNombreEmpresa());
        
        try {
            mostrarDatos();
        } catch (SQLException ex) {
            Logger.getLogger(EliminarSucursalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void mostrarDatos() throws SQLException{
        int id = sucursal.getIdSucursal(); 
        registros.setAll(dao.obtenerRecursosValidacion(id)); 
    }
    
    @FXML
    private void cancelar(ActionEvent event) {
        ControladorUtils.cerrarModal((Node) event.getSource());
    }

    @FXML
    private void eliminarSucursal(ActionEvent event) throws SQLException {
        
        
        
        ControladorUtils.mostrarWarning("Aviso", "Al eliminar una sucursal se eliminan todos los recursos "
                + "relacionados a esa sucursal (notas). Esta acción no se puede deshacer.");

         
        if(ControladorUtils.mostrarConfirmacion("Confirmar eliminacion", "¿Está seguro de que desea eliminar la sucursal " + sucursal.getDescripcion() + "?")){
            if(dao.eliminarSucursal(sucursal.getIdSucursal())){
                ControladorUtils.mostrarAlertaChill("Aviso", "La sucursal " + sucursal.getDescripcion() +" fue eliminada con éxito "
                        + "al igual que toda la información relacionada a ella.");

                // 🔹 Avisar al padre para que recargue la lista
                if(listener != null) {
                    listener.onDataChanged();
                }

                // 🔹 Cerrar el modal
                ControladorUtils.cerrarModal((Node) event.getSource());

            } else{
                ControladorUtils.mostrarAlertaError("Error al intentar eliminar la sucursal", "Ocurrió un error al intentar eliminar la sucursal.");
            }
        }
        
    }

}
