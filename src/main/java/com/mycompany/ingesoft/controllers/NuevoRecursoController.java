package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.models.TipoRecurso;
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

public class NuevoRecursoController implements Initializable {

    @FXML
    private TextField txt_nuevoRecurso;
    @FXML
    private Button btn_cancelar;
    @FXML
    private Button btn_guardar;

    private ClaseDAO dao;
    private Conexion conexion;

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
        String nombreRecurso = txt_nuevoRecurso.getText().trim();

        if (nombreRecurso.isEmpty()) {
            mostrarAlerta("Error", "El nombre del recurso no puede estar vacío", Alert.AlertType.ERROR);
            return;
        }

        try {
            TipoRecurso nuevoTipo = new TipoRecurso();
            nuevoTipo.setDescripcion(nombreRecurso);

            boolean guardado = dao.insertarTipoRecurso(nuevoTipo);

            if (guardado) {
                mostrarAlerta("Éxito", "Tipo de recurso guardado correctamente", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) btn_guardar.getScene().getWindow();
                stage.close();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el tipo de recurso", Alert.AlertType.ERROR);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NuevoRecursoController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlerta("Error", "Error al guardar el tipo de recurso: " + ex.getMessage(), Alert.AlertType.ERROR);
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
