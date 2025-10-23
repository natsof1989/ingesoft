package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.models.Empresa;
import com.mycompany.ingesoft.models.Sucursal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class - Nueva Sucursal
 */
public class NuevaSucursalController implements Initializable {

    @FXML
    private ComboBox<Empresa> cmb_empresas;
    @FXML
    private TextField txt_nombreSucursal;
    @FXML
    private TextField txt_direccion;
    @FXML
    private TextField txt_telefono;
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
        cargarEmpresas();
    }

    private void cargarEmpresas() {
        try {
            List<Empresa> empresas = dao.obtenerEmpresas(); // método actualizado
            cmb_empresas.setItems(FXCollections.observableArrayList(empresas));

            if (!empresas.isEmpty()) {
                cmb_empresas.getSelectionModel().selectFirst();
            }
        } catch (SQLException ex) {
            Logger.getLogger(NuevaSucursalController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlerta("Error", "No se pudieron cargar las empresas: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) btn_cancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void guardar(ActionEvent event) {
        String nombreSucursal = txt_nombreSucursal.getText().trim();
        String direccion = txt_direccion.getText().trim();
        String telefono = txt_telefono.getText().trim();
        Empresa empresaSeleccionada = cmb_empresas.getSelectionModel().getSelectedItem();

        // Validaciones
        if (nombreSucursal.isEmpty()) {
            mostrarAlerta("Error", "El nombre de la sucursal no puede estar vacío", Alert.AlertType.ERROR);
            return;
        }

        if (empresaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una empresa", Alert.AlertType.ERROR);
            return;
        }

        try {
            Sucursal nuevaSucursal = new Sucursal();
            nuevaSucursal.setDescripcion(nombreSucursal); // atributo actualizado
            nuevaSucursal.setDireccion(direccion);
            nuevaSucursal.setTelefono(telefono);
            nuevaSucursal.setIdEmpresa(empresaSeleccionada.getIdEmpresa());

            boolean guardado = dao.insertarSucursal(nuevaSucursal);

            if (guardado) {
                mostrarAlerta("Éxito", "Sucursal guardada correctamente", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) btn_guardar.getScene().getWindow();
                stage.close();
            } else {
                mostrarAlerta("Error", "No se pudo guardar la sucursal", Alert.AlertType.ERROR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NuevaSucursalController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlerta("Error", "Error al guardar la sucursal: " + ex.getMessage(), Alert.AlertType.ERROR);
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
