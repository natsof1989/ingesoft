package com.mycompany.ingesoft.models;

/**
 * Modelo para la tabla recurso
 * Contiene información de accesos y credenciales
 */
public class Recurso {
    private Integer idRecurso;
    private Integer idEmpresa;
    private Integer idSucursal;
    private Integer idTipoRecurso;
    private String titulo;
    private String usuario;        // Para otros tipos de datos (no login)
    private String contrasena;     // Para otros tipos de datos (no login)
    private String ip;
    private String nota;
    private Boolean inicioSesion;
    private String anydesk;
    private String user;           // Usuario para inicio de sesión
    private String password;       // Contraseña para inicio de sesión
    
    // Campos adicionales para mostrar nombres
    private String nombreEmpresa;
    private String nombreSucursal;
    private String nombreTipoRecurso;

    public Recurso() {
        this.inicioSesion = false;
    }

   
    
    

    // Getters y Setters
    public Integer getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Integer idRecurso) {
        this.idRecurso = idRecurso;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public Integer getIdTipoRecurso() {
        return idTipoRecurso;
    }

    public void setIdTipoRecurso(Integer idTipoRecurso) {
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

    public Boolean isInicioSesion() {
        return inicioSesion;
    }

    public void setInicioSesion(Boolean inicioSesion) {
        this.inicioSesion = inicioSesion;
    }

    public String getAnydesk() {
        return anydesk;
    }

    public void setAnydesk(String anydesk) {
        this.anydesk = anydesk;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getNombreTipoRecurso() {
        return nombreTipoRecurso;
    }

    public void setNombreTipoRecurso(String nombreTipoRecurso) {
        this.nombreTipoRecurso = nombreTipoRecurso;
    }

    @Override
    public String toString() {
        return "Recurso{" +
                "idRecurso=" + idRecurso +
                ", titulo='" + titulo + '\'' +
                ", empresa='" + nombreEmpresa + '\'' +
                ", sucursal='" + nombreSucursal + '\'' +
                ", inicioSesion=" + inicioSesion +
                '}';
    }
}