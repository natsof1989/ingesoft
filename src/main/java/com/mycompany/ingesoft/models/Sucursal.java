package com.mycompany.ingesoft.models;

public class Sucursal {
    private int idSucursal;
    private int idEmpresa;
    private String descripcion;
    private String direccion;
    private String telefono;
    private String nombreEmpresa; 

    // Getters y Setters
    public int getIdSucursal() {
        return idSucursal;
    }

    public Sucursal() {
    }

    public Sucursal(int idSucursal, String descripcion, String direccion, String nombreEmpresa) {
        this.idSucursal = idSucursal;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.nombreEmpresa = nombreEmpresa; 
    }

    public Sucursal(String descripcion, String direccion, String nombreEmpresa) {
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.nombreEmpresa = nombreEmpresa;
    }
    
    

    public Sucursal(int idSucursal, int idEmpresa, String descripcion, String direccion) {
        this.idSucursal = idSucursal;
        this.idEmpresa = idEmpresa;
        this.descripcion = descripcion;
        this.direccion = direccion;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    
    
    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
