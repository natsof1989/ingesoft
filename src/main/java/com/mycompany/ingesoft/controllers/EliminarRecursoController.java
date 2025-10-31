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
import com.mycompany.ingesoft.models.TipoRecurso;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class EliminarRecursoController implements Initializable {

    @FXML
    private Text txt_tituloConfirmacion;
    
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_aceptar;
    @FXML
    private TableView<Recurso> tabla_tipoRecurso;
    @FXML
    private TableColumn<Recurso, String> col_empresa;
    @FXML
    private TableColumn<Recurso, String> col_sucursal;
    @FXML
    private TableColumn<Recurso, Integer> col_id;
    @FXML
    private TableColumn<Recurso, String> col_recurso;

    ObservableList<Recurso> registros = FXCollections.observableArrayList(); 
    private Conexion conexion = new Conexion(); 
    private ClaseDAO dao = new ClaseDAO(conexion.getCon()); 
    private ModalListener listener;
    
    private TipoRecurso tipo;
    
    public void setListener(ModalListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipo = singleton.getInstancia().getTipo();
        
        if (tipo == null) {
            ControladorUtils.mostrarAlertaError("Error", "No se ha seleccionado un tipo de recurso");
            return;
        }
        
        col_id.setCellValueFactory(new PropertyValueFactory<>("idRecurso"));
        col_recurso.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        col_empresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        col_sucursal.setCellValueFactory(new PropertyValueFactory<>("nombreSucursal"));
        
        tabla_tipoRecurso.setItems(registros);
        
        String nombre = tipo.getDescripcion(); 
        txt_tituloConfirmacion.setText("¿Desea eliminar el tipo de recurso "+ nombre + "?");
        
        mostrarDatos();
    }    
    
    private void mostrarDatos(){
        int id = tipo.getIdTipoRecurso(); 
        System.out.println("[v0] Loading recursos for tipo ID: " + id);
        try { 
            registros.setAll(dao.obtenerRecursosPorTipo(id));
            System.out.println("[v0] Loaded " + registros.size() + " recursos");
        } catch (SQLException ex) {
            ControladorUtils.mostrarAlertaError("Error", "Hubo un error al cargar los recursos: " + ex.getMessage());
            Logger.getLogger(EliminarRecursoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void cancelar(ActionEvent event) {
        ControladorUtils.cerrarModal((Node) event.getSource());
    }

    @FXML
    private void eliminarTipoRecurso(ActionEvent event) throws SQLException {
        if(ControladorUtils.mostrarConfirmacion("Confirmar operación", "¿Está seguro de que desea eliminar el tipo de recurso: "
                + tipo.getDescripcion() + "? Tenga en cuenta que se eliminarán todos los recursos "
                + "relacionados a esta categoría.")){
            
            if(dao.eliminarTipoRecurso(tipo.getIdTipoRecurso())){
                ControladorUtils.mostrarAlertaChill("Aviso", "El tipo de recurso: " + tipo.getDescripcion() + " fue eliminado con éxito al igual que todos los recursos "
                        + "de esa categoría");
                
                if (listener != null) {
                    listener.onDataChanged();
                }
                ControladorUtils.cerrarModal((Node) event.getSource());
            } else {
                ControladorUtils.mostrarAlertaError("Error en la operación", "No se pudo eliminar el tipo de recurso " + tipo.getDescripcion());
            }
        }
    }
}
