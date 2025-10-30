package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.controllers.clases.ControladorUtils;
import com.mycompany.ingesoft.controllers.clases.singleton;
import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.interfaces.ModalListener;
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
    private Conexion conexion = new Conexion();
    private ClaseDAO dao; 
    private ModalListener listener;

    public void setListener(ModalListener listener){
        this.listener = listener;
    }

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        dao = new ClaseDAO(conexion.getCon());
        
         if(singleton.getInstancia().isEditado()){
            txt_nombreEmpresa.setText(singleton.getInstancia().getNombreEmpresa());
            btn_guardar.setText("Actualizar");
            
        }
    }    
    
    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) btn_cancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void guardar(ActionEvent event) throws SQLException {
        
       
        String nombreEmpresa = txt_nombreEmpresa.getText().trim();
        
        if (nombreEmpresa.isEmpty()) {
            mostrarAlerta("Error", "El nombre de la empresa no puede estar vacío", Alert.AlertType.ERROR);
            return;
        }
        if(singleton.getInstancia().isEditado()){
            try {
                String nameEmpresa = txt_nombreEmpresa.getText(); 
                int idEmpresa = singleton.getInstancia().getId_empresa(); 
                if(dao.actualizarEmpresa(nameEmpresa, idEmpresa)){
                    ControladorUtils.mostrarAlertaChill("Actualización éxitosa", "La actualización realizada");
                    Stage stage = (Stage) btn_guardar.getScene().getWindow();
                    stage.close();
                    singleton.getInstancia().setEditado(false);
                     if (listener != null) {
                            listener.onDataChanged();
                        }
                } else {
                    ControladorUtils.mostrarAlertaError("Error", "No se pudo realizar la actualización");
                }
            } catch(SQLException ex){
                ControladorUtils.mostrarAlertaError("Error SQL", ex.getMessage());
            }
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
