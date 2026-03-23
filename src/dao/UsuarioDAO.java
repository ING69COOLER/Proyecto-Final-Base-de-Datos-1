package dao;

import db.ConexionOracle;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements GenericDAO<Usuario> {

    private Connection conn;

    public UsuarioDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(Usuario u) {
        String sql = "INSERT INTO USUARIO (nombre_usuario, contrasena_hash, tipo_usuario) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombreUsuario());
            ps.setString(2, u.getContrasenaHash());
            ps.setString(3, u.getTipoUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM USUARIO WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO ORDER BY id_usuario";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE USUARIO SET nombre_usuario=?, contrasena_hash=?, tipo_usuario=? WHERE id_usuario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombreUsuario());
            ps.setString(2, u.getContrasenaHash());
            ps.setString(3, u.getTipoUsuario());
            ps.setInt(4, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM USUARIO WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error eliminar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Autentica un usuario por nombre y verifica el hash de la contraseña.
     * El hash debe verificarse con SeguridadService.verificarPassword().
     */
    public Usuario obtenerPorNombre(String nombreUsuario) {
        String sql = "SELECT * FROM USUARIO WHERE nombre_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error obtenerPorNombre: " + e.getMessage());
        }
        return null;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre_usuario"),
            rs.getString("contrasena_hash"),
            rs.getString("tipo_usuario"),
            rs.getDate("fecha_creacion")
        );
    }
}
