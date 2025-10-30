/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ingesoft.controllers.clases;

import com.mycompany.ingesoft.models.Empresa;
import com.mycompany.ingesoft.models.Recurso;
import com.mycompany.ingesoft.models.Sucursal;

/**
 *
 * @author natha
 */
public class singleton {
    private static singleton instancia; 
    
    private int id_empresa; 
    
    
    private int id_sucursal; 


    private String nombreSucursal; 
    private Recurso recurso;
    private int Id_recurso;

    public int getId_recurso() {
        return Id_recurso;
    }

    public void setId_recurso(int Id_recurso) {
        this.Id_recurso = Id_recurso;
    }


    
    
    private boolean editado; 
    private String nombreEmpresa; 
    private Empresa empresa; 
    private Sucursal sucursal; 

    
    
   
    
    private singleton(){}; 
    
    public static singleton getInstancia(){
        if(instancia == null){
            instancia = new singleton();
        }
        return instancia; 
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public boolean isEditado() {
        return editado;
    }

    public void setEditado(boolean editado) {
        this.editado = editado;

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

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }
    
    public void reset() {
    this.id_empresa = 0;
    this.id_sucursal = 0;
    this.editado = false;
    this.nombreEmpresa = null;
    this.empresa = null;
    this.sucursal = null;
    this.recurso = null;
}

    
    
    
    
}
