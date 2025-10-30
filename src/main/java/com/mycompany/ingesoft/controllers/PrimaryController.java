package com.mycompany.ingesoft.controllers;

import com.mycompany.ingesoft.controllers.clases.singleton;
import com.mycompany.ingesoft.dao.ClaseDAO;
import com.mycompany.ingesoft.dao.Conexion;
import com.mycompany.ingesoft.models.Empresa;
import com.mycompany.ingesoft.models.Recurso;
import com.mycompany.ingesoft.models.Sucursal;
import com.mycompany.ingesoft.models.TipoRecurso;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Separator;

/**
 * Controlador principal - Gestor de Recursos
 */
public class PrimaryController implements Initializable {

    @FXML
    private TextField txt_buscar;
    @FXML
    private ComboBox<Empresa> cmb_empresas;
    @FXML
    private ComboBox<Sucursal> cmb_sucursal;
    @FXML
    private ComboBox<TipoRecurso> cmb_tipoRecurso;
    @FXML
    private GridPane contenedor_gridpane;
    @FXML
    private MenuItem btn_nuevaEmpresa;
    @FXML
    private MenuItem btn_nuevaSucursal;
    @FXML
    private MenuItem btn_gestionarEmpresas;
    @FXML
    private MenuItem btn_gestionarSucursales;
    @FXML
    private MenuItem btn_nuevoRecurso;
    @FXML
    private MenuItem btn_nuevaNota;

    private ClaseDAO dao;
    private Conexion conexion;
    private int currentColumns = 4;
    
    private int idEmpresaSelected = 0; 
    private int idSucursalSelected = 0; 
    private int idTipoRecursoSelected = 0; 
    @FXML
    private MenuItem btn_gestionarRecursos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
        dao = new ClaseDAO(conexion.getCon());

        // Cargar solo empresas al inicio
        cargarEmpresas();
        cargarTiposRecurso();
        cargarTodosLosRecursos();

        // Deshabilitar combos dependientes al inicio
        cmb_sucursal.setDisable(true);
        cmb_tipoRecurso.setDisable(true);

        // Búsqueda en vivo
        txt_buscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                extraerDatos(null);
            } else {
                buscarRecursos(newValue);
            }
        });

        // Listeners en cascada
        cmb_empresas.setOnAction(this::habilitarSucursales);   // Empresa → habilita sucursal
        cmb_sucursal.setOnAction(this::extraerDatos); // Sucursal → habilita tipo recurso
        cmb_tipoRecurso.setOnAction(e -> extraerDatos(null));
        
        // Ajuste dinámico de columnas en el grid
        contenedor_gridpane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                adjustGridColumns(newValue.doubleValue());
            }
        });
    }

    private void adjustGridColumns(double width) {
        int newColumns;
        if (width < 600) {
            newColumns = 2;
        } else if (width < 900) {
            newColumns = 3;
        } else if (width < 1200) {
            newColumns = 4;
        } else {
            newColumns = 5;
        }
        
        if (newColumns != currentColumns) {
            currentColumns = newColumns;
            List<VBox> cards = new ArrayList<>();
            contenedor_gridpane.getChildren().forEach(node -> {
                if (node instanceof VBox) {
                    cards.add((VBox) node);
                }
            });
            contenedor_gridpane.getChildren().clear();
            layoutCards(cards);
        }
    }

    private void layoutCards(List<VBox> cards) {
        contenedor_gridpane.getChildren().clear();
        
        for (int i = 0; i < cards.size(); i++) {
            int row = i / currentColumns;
            int col = i % currentColumns;
            contenedor_gridpane.add(cards.get(i), col, row);
        }
    }

    private void cargarEmpresas() {
        try {
            List<Empresa> empresas = dao.obtenerEmpresas();
            Empresa todas = new Empresa();
            todas.setIdEmpresa(-1);
            todas.setDescripcion("Todas las empresas");
            empresas.add(0, todas);

            cmb_empresas.setItems(FXCollections.observableArrayList(empresas));
            cmb_empresas.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error de carga", "No se pudieron cargar las empresas: " + ex.getMessage());
        }
    }

   private void cargarTiposRecurso() {
        try {
            List<TipoRecurso> tipos = dao.obtenerTiposDeRecurso();
            if (tipos == null) {
                tipos = new ArrayList<>();
            }

            TipoRecurso todos = new TipoRecurso();
            todos.setIdTipoRecurso(-1);
            todos.setDescripcion("Todos los tipos de recurso");
            tipos.add(0, todos);

            cmb_tipoRecurso.setItems(FXCollections.observableArrayList(tipos));
            cmb_tipoRecurso.getSelectionModel().selectFirst();

        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error de carga", "No se pudieron cargar los tipos de recurso: " + ex.getMessage());
        }
    }
    
    private void cargarTodosLosRecursos() {
        try {
            List<Recurso> lista = dao.obtenerRecursos();
            mostrarRecursos(lista);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error de carga", "No se pudieron cargar los recursos: " + ex.getMessage());
        }
    }
    
    private void mostrarRecursos(List<Recurso> lista) {
        contenedor_gridpane.getChildren().clear();

        List<VBox> cards = new ArrayList<>();
        for (Recurso recurso : lista) {
            VBox card = crearTarjetaRecurso(recurso);
            cards.add(card);
        }
        layoutCards(cards);
    }
    
    private VBox crearTarjetaRecurso(Recurso recurso) {
        VBox card = new VBox(10);
        card.getStyleClass().add("resource-card");
        card.setMaxWidth(Double.MAX_VALUE);
        card.setMinWidth(280);
        GridPane.setFillWidth(card, true);

        // Header con título y menú
        HBox headerBox = new HBox();
        headerBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Título principal
        Label lblTitulo = new Label(recurso.getTitulo());
        lblTitulo.getStyleClass().add("card-title");
        lblTitulo.setWrapText(true);
        lblTitulo.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        HBox.setHgrow(lblTitulo, javafx.scene.layout.Priority.ALWAYS);

        MenuButton menuAcciones = new MenuButton("⋮");
        menuAcciones.getStyleClass().add("menu-button-card");
        menuAcciones.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        HBox contenedor = new HBox(menuAcciones);
        contenedor.setAlignment(Pos.CENTER_LEFT);

        MenuItem itemEditar = new MenuItem("Editar");
        itemEditar.setOnAction(e -> editarRecurso(recurso));

        MenuItem itemEliminar = new MenuItem("Eliminar");
        itemEliminar.setOnAction(e -> eliminarRecurso(recurso));

        menuAcciones.getItems().addAll(itemEditar, itemEliminar);
        headerBox.getChildren().addAll(lblTitulo, menuAcciones);
        card.getChildren().add(headerBox);

        // Empresa y sucursal
        HBox empresaSucursalBox = new HBox(8);
        empresaSucursalBox.setAlignment(javafx.geometry.Pos.CENTER);

        Label lblEmpresa = new Label(recurso.getNombreEmpresa() != null ? recurso.getNombreEmpresa() : "Sin empresa");
        lblEmpresa.getStyleClass().add("card-empresa");

        if (recurso.getNombreSucursal() != null && !recurso.getNombreSucursal().trim().isEmpty()) {
            Label lblSeparador = new Label("•");
            lblSeparador.getStyleClass().add("card-separator-text");
            lblSeparador.setTextFill(javafx.scene.paint.Color.valueOf("#657786"));

            Label lblSucursal = new Label(recurso.getNombreSucursal());
            lblSucursal.getStyleClass().add("card-sucursal");

            empresaSucursalBox.getChildren().addAll(lblEmpresa, lblSeparador, lblSucursal);
        } else {
            empresaSucursalBox.getChildren().add(lblEmpresa);
        }
        card.getChildren().add(empresaSucursalBox);

        // Tipo de Recurso
        if (recurso.getIdTipoRecurso()!= null && !recurso.getNombreTipoRecurso().trim().isEmpty()) {
            Label lblTipoRecurso = new Label(recurso.getNombreTipoRecurso());
            lblTipoRecurso.getStyleClass().add("server-badge");
            card.getChildren().add(lblTipoRecurso);
        }

        // Separador
        Separator separator = new Separator();
        separator.getStyleClass().add("card-separator");
        card.getChildren().add(separator);

        // 🔹 Contenido dinámico restaurado
        VBox contentBox = new VBox(8);

        // IP
        if (recurso.getIp() != null && !recurso.getIp().trim().isEmpty()) {
            HBox ipBox = new HBox(8);
            ipBox.getStyleClass().add("info-row");
            ipBox.setAlignment(javafx.geometry.Pos.CENTER);

            Label lblIpIcon = new Label("🌐");
            Label lblIp = new Label(recurso.getIp());
            lblIp.getStyleClass().add("card-info");

            Button btnCopiarIp = new Button();
            btnCopiarIp.setGraphic(createCopyIcon());
            btnCopiarIp.setOnAction(e -> copiarAlPortapapeles(recurso.getIp()));

            ipBox.getChildren().addAll(lblIpIcon, lblIp, btnCopiarIp);
            contentBox.getChildren().add(ipBox);
        }

        // AnyDesk
        if (recurso.getAnydesk() != null && !recurso.getAnydesk().trim().isEmpty()) {
            HBox anydeskBox = new HBox(8);
            anydeskBox.getStyleClass().add("info-row");
            anydeskBox.setAlignment(javafx.geometry.Pos.CENTER);

            Label lblAnydeskIcon = new Label("🖥️");
            Label lblAnydesk = new Label(recurso.getAnydesk());
            lblAnydesk.getStyleClass().add("card-info");

            Button btnCopiarAnydesk = new Button();
            btnCopiarAnydesk.setGraphic(createCopyIcon());
            btnCopiarAnydesk.setOnAction(e -> copiarAlPortapapeles(recurso.getAnydesk()));

            anydeskBox.getChildren().addAll(lblAnydeskIcon, lblAnydesk, btnCopiarAnydesk);
            contentBox.getChildren().add(anydeskBox);
        }

        // Nota
        if (recurso.getNota() != null && !recurso.getNota().trim().isEmpty()) {
            VBox noteBox = new VBox(4);
            noteBox.getStyleClass().add("note-box");

            Label lblNoteIcon = new Label("📝");
            Label lblNota = new Label(recurso.getNota());
            lblNota.setWrapText(true);

            noteBox.getChildren().addAll(lblNoteIcon, lblNota);
            contentBox.getChildren().add(noteBox);
        }

        card.getChildren().add(contentBox);

        return card;
    }

    // Método mejorado para editar - AHORA USA EditTarjeta.fxml
    private void editarRecurso(Recurso recurso) {
        try {
            // Guardar datos del recurso en el singleton
            singleton.getInstancia().setId_recurso(recurso.getIdRecurso());
            singleton.getInstancia().setId_empresa(recurso.getIdEmpresa());
            singleton.getInstancia().setNombreEmpresa(recurso.getNombreEmpresa());
            singleton.getInstancia().setId_sucursal(recurso.getIdSucursal());
            singleton.getInstancia().setNombreSucursal(recurso.getNombreSucursal());
            
            // Abrir la ventana de nuevaNota (que ahora también sirve para editar)
            abrirVentanaEdicion("EditarTarjeta", "Editar Recurso");
            
        } catch (Exception ex) {
            mostrarAlertaError("Error al preparar edición", 
                              "No se pudo preparar la edición del recurso: " + ex.getMessage());
        }
    }

    // Método mejorado para eliminar
    private void eliminarRecurso(Recurso recurso) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Eliminar este recurso?");
        alert.setContentText("Recurso: " + recurso.getTitulo() + 
                            "\n\nEsta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    dao.eliminarRecurso(recurso.getIdRecurso());
                    mostrarAlertaInformacion("Éxito", 
                                           "Recurso eliminado correctamente");
                    // Recargar recursos manteniendo filtros actuales
                    recargarRecursosConFiltrosActuales();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                    mostrarAlertaError("Error de base de datos", 
                                     "No se pudo eliminar el recurso: " + ex.getMessage());
                }
            }
        });
    }

    // Método para abrir ventana de edición (ahora usa nuevaNota.fxml)
    private void abrirVentanaEdicion(String fxmlFileName, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/ingesoft/fxml/" + fxmlFileName + ".fxml"
            ));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Configurar para que se actualice al cerrar
            stage.setOnHidden(event -> {
                recargarRecursosConFiltrosActuales();
            });
            
            stage.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error", "No se pudo abrir la ventana de edición");
        }
    }

    // Recargar manteniendo los filtros actuales
    private void recargarRecursosConFiltrosActuales() {
        // Simplemente llamar a extraerDatos con null para mantener filtros
        extraerDatos(null);
    }

    // Métodos de utilidad para alertas
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

    private javafx.scene.shape.SVGPath createCopyIcon() {
        javafx.scene.shape.SVGPath copyIcon = new javafx.scene.shape.SVGPath();
        copyIcon.setContent("M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z");
        copyIcon.getStyleClass().add("copy-icon-svg");
        return copyIcon;
    }

    private void copiarAlPortapapeles(String texto) {
        if (texto != null && !texto.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(texto);
            clipboard.setContent(content);
        }
    }

    private void toggleCredentials(VBox credSection, Button toggleButton, Recurso recurso) {
        // Obtener los elementos hijos
        VBox credBox = (VBox) credSection.getChildren().get(1);
        Button loginButton = (Button) credSection.getChildren().get(2);
        
        if (credBox.isVisible()) {
            // Colapsar
            credBox.setVisible(false);
            credBox.setManaged(false);
            loginButton.setVisible(false);
            loginButton.setManaged(false);
            toggleButton.setText("🔐 Ver Credenciales de Acceso");
            toggleButton.getStyleClass().remove("toggle-credentials-button-active");
        } else {
            // Desplegar
            credBox.setVisible(true);
            credBox.setManaged(true);
            loginButton.setVisible(true);
            loginButton.setManaged(true);
            toggleButton.setText("🔒 Ocultar Credenciales de Acceso");
            toggleButton.getStyleClass().add("toggle-credentials-button-active");
        }
    }

    private void iniciarSesion(Recurso recurso) {
        System.out.println("Inicio de sesión en recurso: " + recurso.getTitulo());
    }

    @FXML
    private void habilitarSucursales(ActionEvent event) {
        Empresa empresaSeleccionada = cmb_empresas.getSelectionModel().getSelectedItem();

        if (empresaSeleccionada != null && empresaSeleccionada.getIdEmpresa() != -1) {
            try {
                List<Sucursal> sucursales = dao.obtenerSucursalesPorEmpresa(empresaSeleccionada.getIdEmpresa());
                Sucursal todas = new Sucursal();
                todas.setIdSucursal(-1);
                todas.setDescripcion("Todas las sucursales");
                sucursales.add(0, todas);

                cmb_sucursal.setItems(FXCollections.observableArrayList(sucursales));
                cmb_sucursal.getSelectionModel().selectFirst();
                cmb_sucursal.setDisable(false);

                // Resetear tipos
                cmb_tipoRecurso.getItems().clear();
                cmb_tipoRecurso.setDisable(true);

                // Mostrar recursos filtrados por empresa
                extraerDatos(null);

            } catch (SQLException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                mostrarAlertaError("Error de carga", "No se pudieron cargar las sucursales: " + ex.getMessage());
            }
        } else {
            cmb_sucursal.setDisable(true);
            cmb_sucursal.getItems().clear();
            cmb_tipoRecurso.setDisable(true);
            cmb_tipoRecurso.getItems().clear();
            extraerDatos(null);
        }
    }

    @FXML
    private void extraerDatos(ActionEvent event) {
        Empresa empSel = cmb_empresas.getSelectionModel().getSelectedItem();
        Sucursal sucSel = cmb_sucursal.getSelectionModel().getSelectedItem();
        TipoRecurso tipoSel = cmb_tipoRecurso.getSelectionModel().getSelectedItem();

        try {
            List<Recurso> lista;

            if (tipoSel != null && tipoSel.getIdTipoRecurso() != -1) {
                lista = dao.obtenerRecursosPorTipo(tipoSel.getIdTipoRecurso());
            } else if (sucSel != null && sucSel.getIdSucursal() != -1) {
                lista = dao.obtenerRecursosPorSucursal(sucSel.getIdSucursal());

                // 👉 Al elegir sucursal, cargar tipos de esa sucursal
                List<TipoRecurso> tipos = dao.obtenerTiposRecursoPorSucursal(sucSel.getIdSucursal());
                if (tipos == null) tipos = new ArrayList<>();
                TipoRecurso todos = new TipoRecurso();
                todos.setIdTipoRecurso(-1);
                todos.setDescripcion("Todos los tipos de recurso");
                tipos.add(0, todos);

                cmb_tipoRecurso.setItems(FXCollections.observableArrayList(tipos));
                cmb_tipoRecurso.getSelectionModel().selectFirst();
                cmb_tipoRecurso.setDisable(false);

            } else if (empSel != null && empSel.getIdEmpresa() != -1) {
                lista = dao.obtenerRecursosPorEmpresa(empSel.getIdEmpresa());
            } else {
                lista = dao.obtenerRecursos();
            }

            mostrarRecursos(lista);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error de carga", "No se pudieron cargar los recursos: " + ex.getMessage());
        }
    }

    private void buscarRecursos(String texto) {
        try {
            List<Recurso> lista = dao.buscarRecursos(texto);
            mostrarRecursos(lista);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error de búsqueda", "No se pudieron buscar los recursos: " + ex.getMessage());
        }
    }

    @FXML
    private void nuevaEmpresa(ActionEvent event) {
        abrirVentana("nuevaEmpresa", "Nueva Empresa");
    }

    @FXML
    private void nuevaSucursal(ActionEvent event) {
        abrirVentana("nuevaSucursal", "Nueva Sucursal");
    }

    @FXML
    private void nuevoRecurso(ActionEvent event) {
        abrirVentana("nuevoRecurso", "Nuevo Recurso");
    }

    private void abrirVentana(String fxmlFileName, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/ingesoft/fxml/" + fxmlFileName + ".fxml"
            ));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarTodosLosRecursos();
            cargarEmpresas();
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            mostrarAlertaError("Error", "No se pudo abrir la ventana: " + ex.getMessage());
        }
    }

    @FXML
    private void nuevaNota(ActionEvent event) {
        abrirVentana("nuevaNota", "Nueva Nota");
    }

    @FXML
    private void gestionarEmpresas(ActionEvent event) {
        abrirVentana("gestionarEmpresa", "Gestión de empresas");
    }

    @FXML
    private void gestionarSucursales(ActionEvent event) {
        abrirVentana("gestionarSucursal", "Gestión de sucursales");
    }

    @FXML
    private void tipoRecurso(ActionEvent event) {

        extraerDatos(event);

        
        

    }
}