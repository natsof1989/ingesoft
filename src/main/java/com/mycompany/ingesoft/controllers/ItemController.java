package com.mycompany.ingesoft.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ItemController {
    
    @FXML private Label lblTitulo;
    @FXML private Text txtIp;
    @FXML private Text txtUsuario;
    @FXML private Text txtContrasena;
    @FXML private Text txtAnydesk;
    @FXML private Text txtUsuarioSesion;
    @FXML private Text txtContrasenaSesion;
    
    public void setData(String nombreEmpresa) {
        lblTitulo.setText(nombreEmpresa);
        txtIp.setText("192.168.1." + (int)(Math.random() * 255));
        txtUsuario.setText("admin_" + nombreEmpresa.toLowerCase().replace(" ", ""));
        txtContrasena.setText("••••••");
        txtAnydesk.setText(generarNumeroAnydesk());
        txtUsuarioSesion.setText("user_" + nombreEmpresa.toLowerCase().replace(" ", ""));
        txtContrasenaSesion.setText("••••••••");
    }
    
    private String generarNumeroAnydesk() {
        return String.format("%03d %03d %03d", 
            (int)(Math.random() * 1000),
            (int)(Math.random() * 1000),
            (int)(Math.random() * 1000));
    }

}