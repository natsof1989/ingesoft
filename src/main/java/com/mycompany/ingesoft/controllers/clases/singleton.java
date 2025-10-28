/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ingesoft.controllers.clases;

import com.mycompany.ingesoft.models.Recurso;

/**
 *
 * @author natha
 */
public class singleton {
    private static singleton instancia; 
    
    private int id_empresa; 
    private String nombreEmpresa; 
    
    private int id_sucursal; 
    private String nombreSucursal; 
    private Recurso recurso; 
    
    private int idRecurso; 
    
    
    
    
    private String fxmlAnterior; 
    
    private singleton(){}; 
    
    public static singleton getInstancia(){
        if(instancia == null){
            instancia = new singleton();
        }
        return instancia; 
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }
    

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public String getFxmlAnterior() {
        return fxmlAnterior;
    }

    public singleton(String fxmlAnterior) {
        this.fxmlAnterior = fxmlAnterior;
    }

    public void setFxmlAnterior(String fxmlAnterior) {
        this.fxmlAnterior = fxmlAnterior;
    }
    
    

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }
    
    

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
    
    
    
    
}