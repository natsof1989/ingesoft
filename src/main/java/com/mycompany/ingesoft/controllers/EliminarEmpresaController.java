/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.controllers.clases.singleton;
import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.models.Sucursal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
    
}
