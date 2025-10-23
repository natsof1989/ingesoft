package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.models.Empresa;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author natha
 */
public class NuevaEmpresaController implements Initializable {

    @FXML
    private TextField txt_nombreEmpresa;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_guardar;
    
    private ClaseDAO dao;
    private Conexion conexion;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
        dao = new ClaseDAO(conexion.getCon());
    }    
    
    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) btn_cancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void guardar(ActionEvent event) {
        String nombreEmpresa = txt_nombreEmpresa.getText().trim();
        
        if (nombreEmpresa.isEmpty()) {
            mostrarAlerta("Error", "El nombre de la empresa no puede estar vacío", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            Empresa nuevaEmpresa = new Empresa();
            nuevaEmpresa.setDescripcion(nombreEmpresa);
            
            boolean guardado = dao.insertarEmpresa(nuevaEmpresa);
            
            if (guardado) {
                mostrarAlerta("Éxito", "Empresa guardada correctamente", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) btn_guardar.getScene().getWindow();
                stage.close();
            } else {
                mostrarAlerta("Error", "No se pudo guardar la empresa", Alert.AlertType.ERROR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NuevaEmpresaController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlerta("Error", "Error al guardar la empresa: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
