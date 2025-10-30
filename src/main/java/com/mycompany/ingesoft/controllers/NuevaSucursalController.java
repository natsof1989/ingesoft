package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.controllers.clases.ControladorUtils;
import com.mycompany.ingesoft.controllers.clases.singleton;
import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.interfaces.ModalListener;
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
 * FXML Controller class - Nueva Sucursal (modo depuración)
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

    private ModalListener listener;

    public void setListener(ModalListener listener) {
        this.listener = listener;
        System.out.println("[DEBUG] Listener asignado: " + (listener != null));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("\n=== [DEBUG] Inicializando NuevaSucursalController ===");

        try {
            conexion = new Conexion();
            dao = new ClaseDAO(conexion.getCon());
            
            // CORRECCIÓN: Primero cargar las empresas
            cargarEmpresas();
            
            // CORRECCIÓN: Después de cargar empresas, verificar si estamos en modo edición
            if (singleton.getInstancia().isEditado()) {
                System.out.println("[DEBUG] Modo edición detectado, inicializando campos...");
                
                // Obtener la sucursal a editar
                Sucursal sucursal = singleton.getInstancia().getSucursal();
                Empresa empresa = singleton.getInstancia().getEmpresa();
                
                if (sucursal != null) {
                    System.out.println("[DEBUG] Sucursal a editar: " + sucursal.getDescripcion());
                    
                    // CORRECCIÓN: Establecer la empresa primero
                    if (empresa != null) {
                        // Buscar la empresa en el ComboBox y seleccionarla
                        cmb_empresas.getItems().forEach(emp -> {
                            if (emp.getIdEmpresa() == empresa.getIdEmpresa()) {
                                cmb_empresas.getSelectionModel().select(emp);
                                System.out.println("[DEBUG] Empresa seleccionada: " + emp.getDescripcion());
                            }
                        });
                    }
                    
                    // CORRECCIÓN: Inicializar los campos con los datos de la sucursal
                    txt_nombreSucursal.setText(sucursal.getDescripcion() != null ? sucursal.getDescripcion() : "");
                    txt_direccion.setText(sucursal.getDireccion() != null ? sucursal.getDireccion() : "");
                    txt_telefono.setText(sucursal.getTelefono() != null ? sucursal.getTelefono() : "");
                    
                    // Cambiar texto del botón
                    btn_guardar.setText("Actualizar");
                    
                    System.out.println("[DEBUG] Campos inicializados correctamente");
                } else {
                    System.err.println("[ERROR] La sucursal en el singleton es nula");
                }
            } else {
                System.out.println("[DEBUG] Modo creación de nueva sucursal");
                btn_guardar.setText("Guardar");
            }

        } catch (Exception ex) {
            System.err.println("[ERROR] Fallo en initialize(): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void cargarEmpresas() {
        System.out.println("[DEBUG] Cargando lista de empresas...");
        try {
            List<Empresa> empresas = dao.obtenerEmpresas();
            System.out.println("[DEBUG] Total de empresas obtenidas: " + (empresas != null ? empresas.size() : 0));

            if (empresas == null) {
                System.err.println("[ADVERTENCIA] dao.obtenerEmpresas() devolvió null.");
                mostrarAlerta("Error", "No se pudieron cargar las empresas (lista nula).", Alert.AlertType.ERROR);
                return;
            }

            cmb_empresas.setItems(FXCollections.observableArrayList(empresas));

            if (!empresas.isEmpty()) {
                // CORRECCIÓN: No seleccionar automáticamente si estamos en modo edición
                if (!singleton.getInstancia().isEditado()) {
                    cmb_empresas.getSelectionModel().selectFirst();
                    System.out.println("[DEBUG] Primera empresa seleccionada por defecto.");
                }
            }

        } catch (SQLException ex) {
            System.err.println("[ERROR SQL] Error al cargar empresas: " + ex.getMessage());
            ex.printStackTrace();
            mostrarAlerta("Error", "No se pudieron cargar las empresas: " + ex.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception ex) {
            System.err.println("[ERROR] Excepción inesperada en cargarEmpresas(): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        System.out.println("[DEBUG] Acción cancelar() invocada.");
        
        // CORRECCIÓN: Limpiar el singleton al cancelar
        singleton.getInstancia().reset();
        
        try {
            Stage stage = (Stage) btn_cancelar.getScene().getWindow();
            stage.close();
            System.out.println("[DEBUG] Ventana cerrada correctamente.");
        } catch (Exception ex) {
            System.err.println("[ERROR] No se pudo cerrar la ventana: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void guardar(ActionEvent event) {
        System.out.println("\n=== [DEBUG] Acción guardar() ejecutada ===");

        String nombreSucursal = txt_nombreSucursal.getText() == null ? "" : txt_nombreSucursal.getText().trim();
        String direccion = txt_direccion.getText() == null ? "" : txt_direccion.getText().trim();
        String telefono = txt_telefono.getText() == null ? "" : txt_telefono.getText().trim();
        Empresa empresaSeleccionada = cmb_empresas.getSelectionModel().getSelectedItem();

        System.out.println("[DEBUG] Datos capturados -> Nombre: " + nombreSucursal + ", Dirección: " + direccion + ", Teléfono: " + telefono);
        System.out.println("[DEBUG] Empresa seleccionada: " + empresaSeleccionada);

        if (nombreSucursal.isEmpty()) {
            mostrarAlerta("Error", "El nombre de la sucursal no puede estar vacío", Alert.AlertType.ERROR);
            System.out.println("[WARN] Campo nombre vacío, operación cancelada.");
            return;
        }

        if (empresaSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una empresa", Alert.AlertType.ERROR);
            System.out.println("[WARN] Empresa no seleccionada, operación cancelada.");
            return;
        }

        try {
            if (singleton.getInstancia().isEditado()) {
                System.out.println("[DEBUG] Actualizando sucursal existente...");

                // CORRECCIÓN: Obtener la sucursal actual del singleton
                Sucursal sucursal = singleton.getInstancia().getSucursal();
                
                if (sucursal != null) {
                    sucursal.setDescripcion(nombreSucursal);
                    sucursal.setDireccion(direccion);
                    sucursal.setTelefono(telefono);
                    sucursal.setIdEmpresa(empresaSeleccionada.getIdEmpresa());

                    boolean actualizado = dao.actualizarSucursal(sucursal);
                    System.out.println("[DEBUG] Resultado actualización: " + actualizado);

                    if (actualizado) {
                        ControladorUtils.mostrarAlertaChill("Operación exitosa", "Sucursal actualizada con éxito");
                    } else {
                        mostrarAlerta("Error", "No se pudo actualizar la sucursal", Alert.AlertType.ERROR);
                    }
                } else {
                    mostrarAlerta("Error", "No se encontró la sucursal a editar", Alert.AlertType.ERROR);
                    System.err.println("[ERROR] La sucursal en el singleton es nula durante la actualización");
                }

            } else {
                System.out.println("[DEBUG] Insertando nueva sucursal...");

                Sucursal nuevaSucursal = new Sucursal();
                nuevaSucursal.setDescripcion(nombreSucursal);
                nuevaSucursal.setDireccion(direccion);
                nuevaSucursal.setTelefono(telefono);
                nuevaSucursal.setIdEmpresa(empresaSeleccionada.getIdEmpresa());

                boolean insertado = dao.insertarSucursal(nuevaSucursal);
                System.out.println("[DEBUG] Resultado inserción: " + insertado);

                if (insertado) {
                    mostrarAlerta("Éxito", "Sucursal guardada correctamente", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo guardar la sucursal", Alert.AlertType.ERROR);
                }
            }

            // CORRECCIÓN: Limpiar el singleton después de la operación
            singleton.getInstancia().reset();

            Stage stage = (Stage) btn_guardar.getScene().getWindow();
            stage.close();
            System.out.println("[DEBUG] Ventana cerrada tras guardar.");

            if (listener != null) {
                System.out.println("[DEBUG] Notificando listener de cambios...");
                listener.onDataChanged();
            } else {
                System.out.println("[DEBUG] No hay listener asignado, omitiendo notificación.");
            }

        } catch (SQLException ex) {
            System.err.println("[ERROR SQL] Error al guardar la sucursal: " + ex.getMessage());
            ex.printStackTrace();
            mostrarAlerta("Error", "Error al guardar la sucursal: " + ex.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception ex) {
            System.err.println("[ERROR] Excepción inesperada en guardar(): " + ex.getMessage());
            ex.printStackTrace();
            mostrarAlerta("Error", "Excepción inesperada: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        System.out.println("[DEBUG] Mostrando alerta: [" + titulo + "] " + mensaje);
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}