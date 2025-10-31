package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.controllers.clases.singleton;
import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.models.Empresa;
import com.mycompany.ingesoft.models.Recurso;
import com.mycompany.ingesoft.models.Sucursal;
import com.mycompany.ingesoft.models.TipoRecurso;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditarTarjetaController implements Initializable {

    @FXML
    private TextField inputTitulo;
    @FXML
    private ComboBox<TipoRecurso> comboTipoRecurso;
    @FXML
    private ComboBox<Empresa> comboEmpresas;
    @FXML
    private ComboBox<Sucursal> comboSucursales;
    @FXML
    private TextField inputIP;
    @FXML
    private TextField inputAnyDesk;
    @FXML
    private TextField inputUsuario;
    @FXML
    private PasswordField inputPassword;
    @FXML
    private CheckBox chkInicioSesion;
    @FXML
    private VBox vboxCamposLogin;
    @FXML
    private TextField inputUsuarioSesion;
    @FXML
    private PasswordField inputPasswordSesion;
    @FXML
    private TextArea textAreaDescripcion;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;

    private ClaseDAO dao;
    private Conexion conexion;
    private Stage stage;
    private Recurso recursoActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
        dao = new ClaseDAO(conexion.getCon());
        
        cargarEmpresas();
        cargarTiposRecurso();
        
        comboEmpresas.setOnAction(this::cargarSucursalesPorEmpresa);
        
        cargarDatosRecurso();
        
        btnGuardar.setText("Guardar");
    }
    
    private void cargarDatosRecurso() {
        try {
            int idRecurso = singleton.getInstancia().getId_recurso();
            recursoActual = dao.obtenerRecursoPorId(idRecurso);
            
            if (recursoActual != null) {
                inputTitulo.setText(recursoActual.getTitulo());
                inputIP.setText(recursoActual.getIp());
                inputAnyDesk.setText(recursoActual.getAnydesk());
                textAreaDescripcion.setText(recursoActual.getNota());
                
                if (recursoActual.getUsuario() != null) {
                    inputUsuario.setText(recursoActual.getUsuario());
                }
                if (recursoActual.getPassword() != null) {
                    inputPassword.setText(recursoActual.getPassword());
                }
                
                if (recursoActual.getIdEmpresa() != -1) {
                    seleccionarEmpresaPorId(recursoActual.getIdEmpresa());
                    
                    if (recursoActual.getIdSucursal() != -1) {
                        javafx.application.Platform.runLater(() -> {
                            seleccionarSucursalPorId(recursoActual.getIdSucursal());
                        });
                    }
                }
                
                if (recursoActual.getIdTipoRecurso() != -1) {
                    seleccionarTipoRecursoPorId(recursoActual.getIdTipoRecurso());
                }
                
                if (recursoActual.getUsuarioSesion() != null || recursoActual.getPasswordSesion() != null) {
                    chkInicioSesion.setSelected(true);
                    vboxCamposLogin.setVisible(true);
                    vboxCamposLogin.setManaged(true);
                    
                    if (recursoActual.getUsuarioSesion() != null) {
                        inputUsuarioSesion.setText(recursoActual.getUsuarioSesion());
                    }
                    if (recursoActual.getPasswordSesion() != null) {
                        inputPasswordSesion.setText(recursoActual.getPasswordSesion());
                    }
                }
                
            } else {
                mostrarAlertaError("Error", "No se encontró el recurso para editar");
                cerrarVentana();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EditarTarjetaController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error", "No se pudo cargar el recurso: " + ex.getMessage());
            cerrarVentana();
        }
    }

    private void cargarEmpresas() {
        try {
            List<Empresa> empresas = dao.obtenerEmpresas();
            comboEmpresas.setItems(FXCollections.observableArrayList(empresas));
        } catch (SQLException ex) {
            Logger.getLogger(EditarTarjetaController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error", "No se pudieron cargar las empresas: " + ex.getMessage());
        }
    }

    private void cargarTiposRecurso() {
        try {
            List<TipoRecurso> tipos = dao.obtenerTiposDeRecurso();
            comboTipoRecurso.setItems(FXCollections.observableArrayList(tipos));
        } catch (SQLException ex) {
            Logger.getLogger(EditarTarjetaController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error", "No se pudieron cargar los tipos de recurso: " + ex.getMessage());
        }
    }

    private void cargarSucursalesPorEmpresa(ActionEvent event) {
        Empresa empresaSeleccionada = comboEmpresas.getSelectionModel().getSelectedItem();
        
        if (empresaSeleccionada != null) {
            try {
                List<Sucursal> sucursales = dao.obtenerSucursalesPorEmpresa(empresaSeleccionada.getIdEmpresa());
                comboSucursales.setItems(FXCollections.observableArrayList(sucursales));
            } catch (SQLException ex) {
                Logger.getLogger(EditarTarjetaController.class.getName()).log(Level.SEVERE, null, ex);
                mostrarAlertaError("Error", "No se pudieron cargar las sucursales: " + ex.getMessage());
            }
        }
    }

    private void seleccionarEmpresaPorId(int idEmpresa) {
        for (Empresa empresa : comboEmpresas.getItems()) {
            if (empresa.getIdEmpresa() == idEmpresa) {
                comboEmpresas.getSelectionModel().select(empresa);
                break;
            }
        }
    }

    private void seleccionarSucursalPorId(int idSucursal) {
        for (Sucursal sucursal : comboSucursales.getItems()) {
            if (sucursal.getIdSucursal() == idSucursal) {
                comboSucursales.getSelectionModel().select(sucursal);
                break;
            }
        }
    }

    private void seleccionarTipoRecursoPorId(int idTipoRecurso) {
        for (TipoRecurso tipo : comboTipoRecurso.getItems()) {
            if (tipo.getIdTipoRecurso() == idTipoRecurso) {
                comboTipoRecurso.getSelectionModel().select(tipo);
                break;
            }
        }
    }

    @FXML
    private void mostrarCamposLogin(ActionEvent event) {
        boolean mostrar = chkInicioSesion.isSelected();
        vboxCamposLogin.setVisible(mostrar);
        vboxCamposLogin.setManaged(mostrar);
    }

    @FXML
    private void guardar(ActionEvent event) throws SQLException {
        if (!validarCampos()) {
            return;
        }

        String titulo = inputTitulo.getText() != null ? inputTitulo.getText().trim() : "";
        String ip = inputIP.getText() != null ? inputIP.getText().trim() : "";
        String anydesk = inputAnyDesk.getText() != null ? inputAnyDesk.getText().trim() : "";
        String nota = textAreaDescripcion.getText() != null ? textAreaDescripcion.getText().trim() : "";
        String usuario = inputUsuario.getText() != null ? inputUsuario.getText().trim() : "";
        String password = inputPassword.getText() != null ? inputPassword.getText().trim() : "";

        recursoActual.setTitulo(titulo);
        recursoActual.setIp(ip);
        recursoActual.setAnydesk(anydesk);
        recursoActual.setNota(nota);
        recursoActual.setUsuario(usuario);
        recursoActual.setPassword(password);

        Empresa empSel = comboEmpresas.getSelectionModel().getSelectedItem();
        if (empSel != null) {
            recursoActual.setIdEmpresa(empSel.getIdEmpresa());
        }

        Sucursal sucSel = comboSucursales.getSelectionModel().getSelectedItem();
        if (sucSel != null) {
            recursoActual.setIdSucursal(sucSel.getIdSucursal());
        }

        TipoRecurso tipoSel = comboTipoRecurso.getSelectionModel().getSelectedItem();
        if (tipoSel != null) {
            recursoActual.setIdTipoRecurso(tipoSel.getIdTipoRecurso());
        }

        if (chkInicioSesion.isSelected()) {
            String usuarioSesion = inputUsuarioSesion.getText() != null ? inputUsuarioSesion.getText().trim() : "";
            String passwordSesion = inputPasswordSesion.getText() != null ? inputPasswordSesion.getText().trim() : "";

            recursoActual.setUsuarioSesion(usuarioSesion.isEmpty() ? null : usuarioSesion);
            recursoActual.setPasswordSesion(passwordSesion.isEmpty() ? null : passwordSesion);
        } else {
            recursoActual.setUsuarioSesion(null);
            recursoActual.setPasswordSesion(null);
        }

        boolean actualizado = dao.actualizarRecurso(recursoActual);
        if (actualizado) {
            mostrarAlertaInformacion("Éxito", "Recurso actualizado correctamente");
            cerrarVentana();
        } else {
            mostrarAlertaError("Error", "No se pudo actualizar el recurso");
        }
    }

    private boolean validarCampos() {
        if (inputTitulo.getText() == null || inputTitulo.getText().trim().isEmpty()) {
            mostrarAlertaError("Error de validación", "El título es obligatorio");
            return false;
        }

        if (comboTipoRecurso.getSelectionModel().getSelectedItem() == null) {
            mostrarAlertaError("Error de validación", "Debe seleccionar un tipo de recurso");
            return false;
        }

        if (comboEmpresas.getSelectionModel().getSelectedItem() == null) {
            mostrarAlertaError("Error de validación", "Debe seleccionar una empresa");
            return false;
        }

        return true;
    }

    @FXML
    private void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        if (stage != null) {
            stage.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAlertaInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
