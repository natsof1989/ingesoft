package com.mycompany.ingesoft.dao;

import com.mycompany.ingesoft.models.Empresa;
import com.mycompany.ingesoft.models.Sucursal;
import com.mycompany.ingesoft.models.TipoRecurso;
import com.mycompany.ingesoft.models.Recurso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClaseDAO {
    private final Connection conexion;

    public ClaseDAO(Connection conexion) {
        this.conexion = conexion;
    }

    // ================== EMPRESAS ==================
    public List<Empresa> obtenerEmpresas() throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT * FROM empresa ORDER BY descripcion";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Empresa emp = new Empresa();
                emp.setIdEmpresa(rs.getInt("id_empresa"));
                emp.setDescripcion(rs.getString("descripcion"));
                empresas.add(emp);
            }
        }
        return empresas;
    }
    
    public boolean eliminarEmpresa(int idEmpresa) throws SQLException {
        String sql = "DELETE FROM empresa WHERE id_empresa = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    
    public boolean eliminarTipoRecurso(int idTipoRecurso) throws SQLException {
        String sql = "DELETE FROM tipo_recurso WHERE id_tipo_recurso = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idTipoRecurso);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    public boolean eliminarSucursal(int idSucursal) throws SQLException {
        String sql = "DELETE FROM sucursales WHERE id_sucursal = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; 
        }
    }
    
    public boolean eliminarRecurso(int idRecurso) throws SQLException {
        String sql = "DELETE FROM recurso WHERE id_recurso = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idRecurso);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; 
        }
    }

    public List<Sucursal> obtenerSucursalesValidacion(int idEmpresa) throws SQLException {
        List<Sucursal> sucursales = new ArrayList<>();
        String sql = "SELECT id_sucursal, descripcion, direccion FROM sucursales WHERE id_empresa=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Sucursal sucur = new Sucursal();
                    sucur.setIdSucursal(rs.getInt("id_sucursal"));
                    sucur.setDescripcion(rs.getString("descripcion"));
                    sucur.setDireccion(rs.getString("direccion")); 
                    sucursales.add(sucur);
                }
            }
        }
        return sucursales;
    }
    
    public List<Sucursal> obtenerSucursalesObjetosIds() throws SQLException {
        List<Sucursal> sucursales = new ArrayList<>();
        String sql = """
            SELECT 
                s.id_sucursal AS id_sucursal,
                s.id_empresa AS id_empresa,
                s.descripcion AS sucursal_desc,
                s.direccion AS direccion,
                s.telefono as telefono,
                e.descripcion AS empresa_desc
            FROM sucursales s
            INNER JOIN empresa e ON s.id_empresa = e.id_empresa
            ORDER BY e.descripcion, s.descripcion
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Sucursal sucur = new Sucursal();
                sucur.setDescripcion(rs.getString("sucursal_desc"));
                sucur.setDireccion(rs.getString("direccion"));
                sucur.setNombreEmpresa(rs.getString("empresa_desc"));
                sucur.setIdSucursal(rs.getInt("id_sucursal"));
                sucur.setIdEmpresa(rs.getInt("id_empresa"));
                sucur.setTelefono(rs.getString("telefono"));
                sucursales.add(sucur);
            }
        }
        return sucursales;
    }

    public boolean insertarEmpresa(Empresa empresa) throws SQLException {
        String sql = "INSERT INTO empresa (descripcion) VALUES (?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, empresa.getDescripcion());
            return ps.executeUpdate() > 0;
        }
    }

    // ================== SUCURSALES ==================
    public List<Sucursal> obtenerSucursalesPorEmpresa(int idEmpresa) throws SQLException {
        List<Sucursal> sucursales = new ArrayList<>();
        String sql = "SELECT * FROM sucursales WHERE id_empresa = ? ORDER BY descripcion";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Sucursal suc = new Sucursal();
                    suc.setIdSucursal(rs.getInt("id_sucursal"));
                    suc.setIdEmpresa(rs.getInt("id_empresa"));
                    suc.setDescripcion(rs.getString("descripcion"));
                    suc.setDireccion(rs.getString("direccion"));
                    suc.setTelefono(rs.getString("telefono"));
                    sucursales.add(suc);
                }
            }
        }
        return sucursales;
    }

    public boolean insertarSucursal(Sucursal sucursal) throws SQLException {
        String sql = "INSERT INTO sucursales (id_empresa, descripcion, direccion, telefono) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, sucursal.getIdEmpresa());
            ps.setString(2, sucursal.getDescripcion());
            ps.setString(3, sucursal.getDireccion());
            ps.setString(4, sucursal.getTelefono());
            return ps.executeUpdate() > 0;
        }
    }

    // ================== TIPOS DE RECURSO ==================
    public List<TipoRecurso> obtenerTiposDeRecurso() throws SQLException {
        List<TipoRecurso> tipos = new ArrayList<>();
        String sql = "SELECT * FROM tipo_recurso ORDER BY descripcion";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoRecurso tipo = new TipoRecurso();
                tipo.setIdTipoRecurso(rs.getInt("id_tipo_recurso"));
                tipo.setDescripcion(rs.getString("descripcion"));
                tipos.add(tipo);
            }
        }
        return tipos;
    }

    public boolean insertarTipoRecurso(TipoRecurso tipo) throws SQLException {
        String sql = "INSERT INTO tipo_recurso (descripcion) VALUES (?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, tipo.getDescripcion());
            return ps.executeUpdate() > 0;
        }
    }

    // ================== RECURSOS ==================
    public List<Recurso> obtenerRecursos() throws SQLException {
        List<Recurso> recursos = new ArrayList<>();
        String sql = """
            SELECT r.*, e.descripcion AS empresa_desc, 
                   s.descripcion AS sucursal_desc, 
                   t.descripcion AS tipo_desc
            FROM recurso r
            JOIN empresa e ON r.id_empresa = e.id_empresa
            LEFT JOIN sucursales s ON r.id_sucursal = s.id_sucursal
            JOIN tipo_recurso t ON r.id_tipo_recurso = t.id_tipo_recurso
            ORDER BY e.descripcion, s.descripcion
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                recursos.add(mapRecurso(rs));
            }
        }
        return recursos;
    }

    public boolean insertarRecurso(Recurso recurso) throws SQLException {
        String sql = "INSERT INTO recurso (titulo, usuario, contrasena, ip, nota, id_empresa, id_sucursal, id_tipo_recurso, inicio_sesion, anydesk) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            // Campos obligatorios
            ps.setString(1, recurso.getTitulo());

            // Campos opcionales
            if (esVacio(recurso.getUsuario())) {
                ps.setNull(2, java.sql.Types.VARCHAR);
            } else {
                ps.setString(2, recurso.getUsuario());
            }

            if (esVacio(recurso.getContrasena())) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, recurso.getContrasena());
            }

            if (esVacio(recurso.getIp())) {
                ps.setNull(4, java.sql.Types.VARCHAR);
            } else {
                ps.setString(4, recurso.getIp());
            }

            if (esVacio(recurso.getNota())) {
                ps.setNull(5, java.sql.Types.VARCHAR);
            } else {
                ps.setString(5, recurso.getNota());
            }

            // Empresa (obligatoria)
            ps.setInt(6, recurso.getIdEmpresa());

            // Sucursal opcional
            if (recurso.getIdSucursal() == null) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, recurso.getIdSucursal());
            }

            // Tipo recurso obligatorio
            ps.setInt(8, recurso.getIdTipoRecurso());

            // inicio_sesion: booleano
            ps.setBoolean(9, recurso.isInicioSesion());

            // anydesk (puede ser nulo si no se usa)
            if (esVacio(recurso.getAnydesk())) {
                ps.setNull(10, java.sql.Types.VARCHAR);
            } else {
                ps.setString(10, recurso.getAnydesk());
            }

            return ps.executeUpdate() > 0;
        }
    }

    // Método auxiliar
    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    // ================== NUEVOS MÉTODOS (para el controller) ==================

    public List<Recurso> obtenerRecursosPorSucursal(int idSucursal) throws SQLException {
        String sql = """
            SELECT r.*, e.descripcion AS empresa_desc, s.descripcion AS sucursal_desc, t.descripcion AS tipo_desc
            FROM recurso r
            JOIN empresa e ON r.id_empresa = e.id_empresa
            JOIN sucursales s ON r.id_sucursal = s.id_sucursal
            JOIN tipo_recurso t ON r.id_tipo_recurso = t.id_tipo_recurso
            WHERE r.id_sucursal = ?
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                List<Recurso> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapRecurso(rs));
                return lista;
            }
        }
    }

    public List<Recurso> obtenerRecursosPorEmpresa(int idEmpresa) throws SQLException {
        String sql = """
            SELECT r.*, e.descripcion AS empresa_desc, s.descripcion AS sucursal_desc, t.descripcion AS tipo_desc
            FROM recurso r
            JOIN empresa e ON r.id_empresa = e.id_empresa
            LEFT JOIN sucursales s ON r.id_sucursal = s.id_sucursal
            JOIN tipo_recurso t ON r.id_tipo_recurso = t.id_tipo_recurso
            WHERE r.id_empresa = ?
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                List<Recurso> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapRecurso(rs));
                return lista;
            }
        }
    }

    public List<Recurso> extraerNotasTodas() throws SQLException {
        return obtenerRecursos(); // simplemente reutiliza el método principal
    }

    public List<Recurso> buscarRecursos(String filtro) throws SQLException {
        String sql = """
            SELECT r.*, e.descripcion AS empresa_desc, s.descripcion AS sucursal_desc, t.descripcion AS tipo_desc
            FROM recurso r
            JOIN empresa e ON r.id_empresa = e.id_empresa
            LEFT JOIN sucursales s ON r.id_sucursal = s.id_sucursal
            JOIN tipo_recurso t ON r.id_tipo_recurso = t.id_tipo_recurso
            WHERE r.titulo LIKE ? OR r.usuario LIKE ? OR e.descripcion LIKE ? OR s.descripcion LIKE ?
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            String f = "%" + filtro + "%";
            ps.setString(1, f);
            ps.setString(2, f);
            ps.setString(3, f);
            ps.setString(4, f);
            try (ResultSet rs = ps.executeQuery()) {
                List<Recurso> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapRecurso(rs));
                return lista;
            }
        }
    }
    
    public boolean actualizarEmpresa(String nombreEmpresa, int idEmpresa) throws SQLException {
        String sql = "UPDATE empresa SET descripcion = ? WHERE id_empresa = ?"; 
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, nombreEmpresa);
            ps.setInt(2, idEmpresa);
            int filasAfectadas = ps.executeUpdate(); 
            return filasAfectadas > 0; 
        }
    }
    public boolean actualizarTipoRecurso(String tipoRecurso, int idTipoRecurso) throws SQLException {
        String sql = "UPDATE tipo_recurso set descripcion=? where id_tipo_recurso=?"; 
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, tipoRecurso);
            ps.setInt(2, idTipoRecurso);
            int filasAfectadas = ps.executeUpdate(); 
            return filasAfectadas > 0; 
        }
    }
    
    public boolean actualizarSucursal(Sucursal sucursal) throws SQLException {
        String sql = "UPDATE sucursales SET descripcion=?, id_empresa=?, direccion=?, telefono=? WHERE id_sucursal=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, sucursal.getDescripcion());
            ps.setInt(2, sucursal.getIdEmpresa());
            ps.setString(3, sucursal.getDireccion());
            ps.setString(4, sucursal.getTelefono());
            ps.setInt(5, sucursal.getIdSucursal());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }

    // ================== MÉTODO AUXILIAR ==================
    private Recurso mapRecurso(ResultSet rs) throws SQLException {
        Recurso r = new Recurso();
        r.setIdRecurso(rs.getInt("id_recurso"));
        r.setIdEmpresa(rs.getInt("id_empresa"));
        r.setIdSucursal((Integer) rs.getObject("id_sucursal"));
        r.setIdTipoRecurso(rs.getInt("id_tipo_recurso"));
        r.setTitulo(rs.getString("titulo"));
        r.setUsuario(rs.getString("usuario"));
        r.setContrasena(rs.getString("contrasena"));
        r.setIp(rs.getString("ip"));
        r.setNota(rs.getString("nota"));
        r.setInicioSesion(rs.getBoolean("inicio_sesion"));
        r.setAnydesk(rs.getString("anydesk"));
        r.setNombreEmpresa(rs.getString("empresa_desc"));
        r.setNombreSucursal(rs.getString("sucursal_desc"));
        r.setNombreTipoRecurso(rs.getString("tipo_desc"));
        r.setUsuarioSesion(null);
        r.setPasswordSesion(null);
    
        return r;
    }

    // ================== TIPOS DE RECURSO POR SUCURSAL ==================
    public List<TipoRecurso> obtenerTiposRecursoPorSucursal(int idSucursal) throws SQLException {
        List<TipoRecurso> tipos = new ArrayList<>();
        String sql = """
            SELECT DISTINCT tr.id_tipo_recurso, tr.descripcion
            FROM recurso r
            JOIN tipo_recurso tr ON r.id_tipo_recurso = tr.id_tipo_recurso
            WHERE r.id_sucursal = ?
            ORDER BY tr.descripcion
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoRecurso tipo = new TipoRecurso();
                    tipo.setIdTipoRecurso(rs.getInt("id_tipo_recurso"));
                    tipo.setDescripcion(rs.getString("descripcion"));
                    tipos.add(tipo);
                }
            }
        }
        return tipos;
    }

    public boolean actualizarRecurso(Recurso recurso){
        String sql = """
            UPDATE recurso 
            SET titulo = ?, ip = ?, anydesk = ?, nota = ?, usuario = ?, contrasena = ?, 
                id_empresa = ?, id_sucursal = ?, id_tipo_recurso = ?, 
                usuario_sesion = ?, password_sesion = ?
            WHERE id_recurso = ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, recurso.getTitulo());
            ps.setString(2, recurso.getIp());
            ps.setString(3, recurso.getAnydesk());
            ps.setString(4, recurso.getNota());
            ps.setString(5, recurso.getUsuario());
            ps.setString(6, recurso.getContrasena());
            ps.setInt(7, recurso.getIdEmpresa());

            if (recurso.getIdSucursal() != null) {
                ps.setInt(8, recurso.getIdSucursal());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            ps.setInt(9, recurso.getIdTipoRecurso());
            ps.setString(10, recurso.getUsuarioSesion());
            ps.setString(11, recurso.getPasswordSesion());
            ps.setInt(12, recurso.getIdRecurso());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ClaseDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        }
    }
    
    public List<Recurso> obtenerRecursosPorTipo(int idTipoRecurso) throws SQLException {
        String sql = """
            SELECT r.*, e.descripcion AS empresa_desc, s.descripcion AS sucursal_desc, t.descripcion AS tipo_desc
            FROM recurso r
            JOIN empresa e ON r.id_empresa = e.id_empresa
            LEFT JOIN sucursales s ON r.id_sucursal = s.id_sucursal
            JOIN tipo_recurso t ON r.id_tipo_recurso = t.id_tipo_recurso
            WHERE r.id_tipo_recurso = ?
        """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idTipoRecurso);
            try (ResultSet rs = ps.executeQuery()) {
                List<Recurso> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapRecurso(rs));
                return lista;
            }
        }
    }
    
    public List<Recurso> obtenerRecursosValidacion(int idSucursal) throws SQLException {
        List<Recurso> lista = new ArrayList<>();
        String sql = "SELECT id_recurso, titulo FROM recurso WHERE id_sucursal=?";
        try(PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setInt(1, idSucursal);
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Recurso recurso = new Recurso();
                    recurso.setIdRecurso(rs.getInt("id_recurso"));
                    recurso.setTitulo(rs.getString("titulo"));
                    lista.add(recurso);
                }
            }
        }
        return lista; 
    }

    public Recurso obtenerRecursoPorId(int idRecurso) throws SQLException {
        String sql = """
            SELECT r.*, 
                   e.descripcion AS empresa_desc, 
                   s.descripcion AS sucursal_desc, 
                   t.descripcion AS tipo_desc
            FROM recurso r
            JOIN empresa e ON r.id_empresa = e.id_empresa
            LEFT JOIN sucursales s ON r.id_sucursal = s.id_sucursal
            JOIN tipo_recurso t ON r.id_tipo_recurso = t.id_tipo_recurso
            WHERE r.id_recurso = ?
        """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idRecurso);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRecurso(rs); // mapea a un solo objeto
                } else {
                    return null; // no se encontró
                }
            }
        }
    }
}