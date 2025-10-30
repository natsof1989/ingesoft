package com.mycompany.ingesoft.models;

public class Recurso {
    private int idRecurso;
    private int idEmpresa;
    private Integer idSucursal; // nullable
    private int idTipoRecurso;
    private String titulo;
    private String usuario;
    private String contrasena;
    private String ip;
    private String nota;
    private boolean inicioSesion;
    private String anydesk;
    
    // 🔥 CAMPOS NUEVOS PARA DATOS DE SESIÓN
    private String usuarioSesion;
    private String passwordSesion;

    // Campos de texto para mostrar nombres de joins
    private String nombreEmpresa;
    private String nombreSucursal;
    private String nombreTipo;

    public Recurso() {
    }

    public Recurso(int idRecurso, int idEmpresa, Integer idSucursal, int idTipoRecurso, String titulo, String usuario, String contrasena, String ip, String nota, boolean inicioSesion, String anydesk, String nombreEmpresa, String nombreSucursal, String nombreTipo) {
        this.idRecurso = idRecurso;
        this.idEmpresa = idEmpresa;
        this.idSucursal = idSucursal;
        this.idTipoRecurso = idTipoRecurso;
        this.titulo = titulo;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.ip = ip;
        this.nota = nota;
        this.inicioSesion = inicioSesion;
        this.anydesk = anydesk;
        this.nombreEmpresa = nombreEmpresa;
        this.nombreSucursal = nombreSucursal;
        this.nombreTipo = nombreTipo;
    }

    // 🔥 MANTENEMOS ESTOS MÉTODOS POR SI LOS USAS EN OTRA PARTE
    public String getUser() {
        return usuario; // Retornamos el mismo campo
    }

    public void setUser(String user) {
        this.usuario = user; // Asignamos al mismo campo
    }

    public String getPassword() {
        return contrasena; // Retornamos el mismo campo
    }

    public void setPassword(String password) {
        this.contrasena = password; // Asignamos al mismo campo
    }
    
    // Getters y Setters principales
    public int getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(int idRecurso) {
        this.idRecurso = idRecurso;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public int getIdTipoRecurso() {
        return idTipoRecurso;
    }

    public void setIdTipoRecurso(int idTipoRecurso) {
        this.idTipoRecurso = idTipoRecurso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public boolean isInicioSesion() {
        return inicioSesion;
    }

    public void setInicioSesion(boolean inicioSesion) {
        this.inicioSesion = inicioSesion;
    }

    public String getAnydesk() {
        return anydesk;
    }

    public void setAnydesk(String anydesk) {
        this.anydesk = anydesk;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    // 🔥 NUEVOS GETTERS Y SETTERS PARA DATOS DE SESIÓN
    public String getUsuarioSesion() {
        return usuarioSesion;
    }

    public void setUsuarioSesion(String usuarioSesion) {
        this.usuarioSesion = usuarioSesion;
    }

    public String getPasswordSesion() {
        return passwordSesion;
    }

    public void setPasswordSesion(String passwordSesion) {
        this.passwordSesion = passwordSesion;
    }

    @Override
    public String toString() {
        return titulo + " (" + nombreTipo + ")";
    }
}