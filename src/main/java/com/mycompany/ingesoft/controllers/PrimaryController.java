package com.mycompany.ingesoft.controllers;

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
import javafx.geometry.Insets;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conexion = new Conexion();
        dao = new ClaseDAO(conexion.getCon());

        cargarEmpresas();
        cargarTiposRecurso();
        cargarTodosLosRecursos();

        // Búsqueda en vivo
        txt_buscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                extraerDatos(null);
            } else {
                buscarRecursos(newValue);
            }
        });

        
        cmb_tipoRecurso.setOnAction(e -> extraerDatos(null));
        
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
            newColumns = 1;
        } else if (width < 900) {
            newColumns = 2;
        } else if (width < 1200) {
            newColumns = 3;
        } else {
            newColumns = 4;
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
        }
    }


    private void cargarTodosLosRecursos() {
        try {
            List<Recurso> lista = dao.obtenerRecursos();
            mostrarRecursos(lista);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
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

        // Título principal
        Label lblTitulo = new Label(recurso.getTitulo());
        lblTitulo.getStyleClass().add("card-title");
        lblTitulo.setWrapText(true);
        card.getChildren().add(lblTitulo);

        // Empresa y Sucursal
        HBox empresaSucursalBox = new HBox(8);
        empresaSucursalBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
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

        // Contenido dinámico
        VBox contentBox = new VBox(8);

        // IP (solo si hay datos)
        if (recurso.getIp() != null && !recurso.getIp().trim().isEmpty()) {
            HBox ipBox = new HBox(8);
            ipBox.getStyleClass().add("info-row");
            ipBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            Label lblIpIcon = new Label("🌐");
            lblIpIcon.getStyleClass().add("info-icon");
            
            Label lblIp = new Label(recurso.getIp());
            lblIp.getStyleClass().add("card-info");
            HBox.setHgrow(lblIp, javafx.scene.layout.Priority.ALWAYS);
            
            Button btnCopiarIp = new Button();
            btnCopiarIp.getStyleClass().add("copy-icon");
            btnCopiarIp.setGraphic(createCopyIcon());
            btnCopiarIp.setOnAction(e -> copiarAlPortapapeles(recurso.getIp()));
            btnCopiarIp.setTooltip(new javafx.scene.control.Tooltip("Copiar IP"));
            
            ipBox.getChildren().addAll(lblIpIcon, lblIp, btnCopiarIp);
            contentBox.getChildren().add(ipBox);
        }

        // AnyDesk (solo si hay datos)
        if (recurso.getAnydesk() != null && !recurso.getAnydesk().trim().isEmpty()) {
            HBox anydeskBox = new HBox(8);
            anydeskBox.getStyleClass().add("info-row");
            anydeskBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            Label lblAnydeskIcon = new Label("🖥️");
            lblAnydeskIcon.getStyleClass().add("info-icon");
            
            Label lblAnydesk = new Label(recurso.getAnydesk());
            lblAnydesk.getStyleClass().add("card-info");
            HBox.setHgrow(lblAnydesk, javafx.scene.layout.Priority.ALWAYS);
            
            Button btnCopiarAnydesk = new Button();
            btnCopiarAnydesk.getStyleClass().add("copy-icon");
            btnCopiarAnydesk.setGraphic(createCopyIcon());
            btnCopiarAnydesk.setOnAction(e -> copiarAlPortapapeles(recurso.getAnydesk()));
            btnCopiarAnydesk.setTooltip(new javafx.scene.control.Tooltip("Copiar AnyDesk"));
            
            anydeskBox.getChildren().addAll(lblAnydeskIcon, lblAnydesk, btnCopiarAnydesk);
            contentBox.getChildren().add(anydeskBox);
        }

        // Nota (solo si hay datos)
        if (recurso.getNota() != null && !recurso.getNota().trim().isEmpty()) {
            VBox noteBox = new VBox(4);
            noteBox.getStyleClass().add("note-box");
            
            Label lblNoteIcon = new Label("📝");
            lblNoteIcon.getStyleClass().add("info-icon");
            
            Label lblNota = new Label(recurso.getNota());
            lblNota.getStyleClass().add("card-note");
            lblNota.setWrapText(true);
            lblNota.setMaxHeight(60);
            
            noteBox.getChildren().addAll(lblNoteIcon, lblNota);
            contentBox.getChildren().add(noteBox);
        }

        card.getChildren().add(contentBox);

        // Separador para credenciales (solo si hay inicio de sesión)
        if (recurso.isInicioSesion() && 
                
            recurso.getUsuario() != null && !recurso.getUsuario().trim().isEmpty()) {
            
            // Sección de credenciales colapsable
            VBox credSection = new VBox(0);
            credSection.getStyleClass().add("credentials-section");
            
            // Botón para desplegar/colapsar credenciales
            Button btnToggleCred = new Button("🔐 Ver Credenciales de Acceso");
            btnToggleCred.getStyleClass().add("toggle-credentials-button");
            btnToggleCred.setMaxWidth(Double.MAX_VALUE);
            btnToggleCred.setOnAction(e -> toggleCredentials(credSection, btnToggleCred, recurso));
            
            // Contenedor de credenciales (inicialmente oculto)
            VBox credBox = new VBox(10);
            credBox.getStyleClass().add("credentials-container");
            credBox.setManaged(false);
            credBox.setVisible(false);
            
            // Usuario
            HBox usuarioBox = new HBox(10);
            usuarioBox.getStyleClass().add("credential-row");
            usuarioBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            VBox userLabelBox = new VBox(2);
            Label lblUserLabel = new Label("USUARIO");
            lblUserLabel.getStyleClass().add("credential-label");
            
            Label lblUserIcon = new Label("👤");
            lblUserIcon.getStyleClass().add("credential-icon");
            userLabelBox.getChildren().addAll(lblUserLabel, lblUserIcon);
            
            Label lblUsuario = new Label(recurso.getUsuario());
            lblUsuario.getStyleClass().add("credential-value");
            HBox.setHgrow(lblUsuario, javafx.scene.layout.Priority.ALWAYS);
            
            Button btnCopiarUsuario = new Button();
            btnCopiarUsuario.getStyleClass().add("copy-button");
            btnCopiarUsuario.setGraphic(createCopyIcon());
            btnCopiarUsuario.setOnAction(e -> copiarAlPortapapeles(recurso.getUsuario()));
            btnCopiarUsuario.setTooltip(new javafx.scene.control.Tooltip("Copiar usuario"));
            
            usuarioBox.getChildren().addAll(userLabelBox, lblUsuario, btnCopiarUsuario);
            credBox.getChildren().add(usuarioBox);

            // Contraseña (solo si hay datos)
            if (recurso.getContrasena() != null && !recurso.getContrasena().trim().isEmpty()) {
                HBox passwordBox = new HBox(10);
                passwordBox.getStyleClass().add("credential-row");
                passwordBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                
                VBox passLabelBox = new VBox(2);
                Label lblPassLabel = new Label("CONTRASEÑA");
                lblPassLabel.getStyleClass().add("credential-label");
                
                Label lblPassIcon = new Label("🔒");
                lblPassIcon.getStyleClass().add("credential-icon");
                passLabelBox.getChildren().addAll(lblPassLabel, lblPassIcon);
                
                Label lblPassword = new Label("••••••••");
                lblPassword.getStyleClass().add("credential-password");
                HBox.setHgrow(lblPassword, javafx.scene.layout.Priority.ALWAYS);
                
                Button btnCopiarPassword = new Button();
                btnCopiarPassword.getStyleClass().add("copy-button");
                btnCopiarPassword.setGraphic(createCopyIcon());
                btnCopiarPassword.setOnAction(e -> copiarAlPortapapeles(recurso.getContrasena()));
                btnCopiarPassword.setTooltip(new javafx.scene.control.Tooltip("Copiar contraseña"));
                
                passwordBox.getChildren().addAll(passLabelBox, lblPassword, btnCopiarPassword);
                credBox.getChildren().add(passwordBox);
            }
            
            // Botón de acción (inicialmente oculto)
            Button btnLogin = new Button("🚀 Iniciar Sesión");
            btnLogin.getStyleClass().add("login-button-credential");
            btnLogin.setOnAction(e -> iniciarSesion(recurso));
            btnLogin.setMaxWidth(Double.MAX_VALUE);
            btnLogin.setManaged(false);
            btnLogin.setVisible(false);
            
            credSection.getChildren().addAll(btnToggleCred, credBox, btnLogin);
            card.getChildren().add(credSection);
        }

        return card;
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
            } catch (SQLException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            cmb_sucursal.setDisable(true);
            cmb_sucursal.getItems().clear();
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
            } else if (empSel != null && empSel.getIdEmpresa() != -1) {
                lista = dao.obtenerRecursosPorEmpresa(empSel.getIdEmpresa());
            } else {
                lista = dao.obtenerRecursos();
            }

            mostrarRecursos(lista);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscarRecursos(String texto) {
        try {
            List<Recurso> lista = dao.buscarRecursos(texto);
            mostrarRecursos(lista);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
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
    }
}