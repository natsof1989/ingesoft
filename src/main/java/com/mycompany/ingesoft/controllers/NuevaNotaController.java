package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.models.Empresa;
import com.mycompany.ingesoft.models.Sucursal;
import com.mycompany.ingesoft.models.TipoRecurso;
import com.mycompany.ingesoft.models.Recurso;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana Nueva Nota / Recurso
 * Maneja la creación de registros en la tabla recursos
 * con campos opcionales y tipos de recurso variables.
 * 
 * @author natha
 */
public class NuevaNotaController implements Initializable {

    @FXML private TextField txt_titulo;
    @FXML private ComboBox<Empresa> cmb_empresas;
    @FXML private ComboBox<Sucursal> cmb_sucursales;
    @FXML private ComboBox<TipoRecurso> cmb_tipoRecurso;
    @FXML private TextField txt_usuario;
    @FXML private TextField txt_password;
    @FXML private TextField txt_IP;       // Debe coincidir con el fx:id del FXML
    @FXML private TextField txt_anydesk;
    @FXML private TextArea txt_nota;
    @FXML private Button btn_cancelar;
    @FXML private Button btn_guardar;

    private ClaseDAO dao;
    private Conexion conexion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
        dao = new ClaseDAO(conexion.getCon());
        cargarEmpresas();
        cargarTiposDeRecurso();

        // Cuando se selecciona una empresa, carga sus sucursales
        cmb_empresas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarSucursales(newVal.getIdEmpresa());
            } else {
                cmb_sucursales.getItems().clear();
            }
        });
    }

    private void cargarEmpresas() {
        try {
            List<Empresa> empresas = dao.obtenerEmpresas();
            cmb_empresas.setItems(FXCollections.observableArrayList(empresas));
            cmb_empresas.getSelectionModel().clearSelection();
            cmb_sucursales.getItems().clear();
        } catch (SQLException ex) {
            Logger.getLogger(NuevaNotaController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlerta("Error", "No se pudieron cargar las empresas: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarSucursales(int idEmpresa) {
        try {
            List<Sucursal> sucursales = dao.obtenerSucursalesPorEmpresa(idEmpresa);
            cmb_sucursales.setItems(FXCollections.observableArrayList(sucursales));
            cmb_sucursales.getSelectionModel().clearSelection();
        } catch (SQLException ex) {
            Logger.getLogger(NuevaNotaController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlerta("Error", "No se pudieron cargar las sucursales: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cargarTiposDeRecurso() {
        try {
            List<TipoRecurso> tipos = dao.obtenerTiposDeRecurso();
            cmb_tipoRecurso.setItems(FXCollections.observableArrayList(tipos));
            cmb_tipoRecurso.getSelectionModel().clearSelection();
        } catch (SQLException ex) {
            Logger.getLogger(NuevaNotaController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlerta("Error", "No se pudieron cargar los tipos de recurso: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) btn_cancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void guardar(ActionEvent event) {
        String titulo = valorONulo(txt_titulo.getText());
        String usuario = valorONulo(txt_usuario.getText());
        String password = valorONulo(txt_password.getText());
        String ip = valorONulo(txt_IP.getText());
        String anydesk = valorONulo(txt_anydesk.getText());
        String nota = valorONulo(txt_nota.getText());

        Empresa empresaSeleccionada = cmb_empresas.getSelectionModel().getSelectedItem();
        Sucursal sucursalSeleccionada = cmb_sucursales.getSelectionModel().getSelectedItem();
        TipoRecurso tipoSeleccionado = cmb_tipoRecurso.getSelectionModel().getSelectedItem();

        if (empresaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una empresa", Alert.AlertType.ERROR);
            return;
        }
        if (titulo == null) {
            mostrarAlerta("Error", "El título no puede estar vacío", Alert.AlertType.ERROR);
            return;
        }
        if (tipoSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un tipo de recurso", Alert.AlertType.ERROR);
            return;
        }

        try {
            Recurso recurso = new Recurso();
            recurso.setTitulo(titulo);
            recurso.setUsuario(usuario);
            recurso.setContrasena(password);
            recurso.setIp(ip);
            recurso.setNota(nota);
            recurso.setAnydesk(anydesk);
            recurso.setIdEmpresa(empresaSeleccionada.getIdEmpresa());
            recurso.setIdTipoRecurso(tipoSeleccionado.getIdTipoRecurso());
            recurso.setInicioSesion(false);

            // Sucursal opcional
            if (sucursalSeleccionada != null) {
                recurso.setIdSucursal(sucursalSeleccionada.getIdSucursal());
            } else {
                recurso.setIdSucursal(null);
            }

            boolean guardado = dao.insertarRecurso(recurso);

            if (guardado) {
                mostrarAlerta("Éxito", "Recurso guardado correctamente", Alert.AlertType.INFORMATION);
                Stage stage = (Stage) btn_guardar.getScene().getWindow();
                stage.close();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el recurso", Alert.AlertType.ERROR);
            }
        } catch (SQLException ex) {
            Logger.getLogger(NuevaNotaController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlerta("Error", "Error al guardar el recurso: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private String valorONulo(String texto) {
        return (texto == null || texto.trim().isEmpty()) ? null : texto.trim();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
