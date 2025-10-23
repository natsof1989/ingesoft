package com.mycompany.ingesoft.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {
    private String base; 
    private String host; 
    private String usuario; 
    private String password; 
    
    private Connection con; 

    public Connection getCon() {
        try {
            String url= "jdbc:mysql://"+host+"/"+base;//direccion de BD
            con = DriverManager.getConnection(url, this.usuario, this.password);
            System.out.println("Conectado");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No Conectado");
        }
        return con;
        
    }
    
    
   public Conexion() {
        this.base = "ingesoft";
        this.host = "127.0.0.1:3306";
        this.usuario = "root";
        this.password = "";
    }


    public Conexion(String base, String host, String usuario, String password) {
        this.base = base;
        this.host = host;
        this.usuario = usuario;
        this.password = password;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
