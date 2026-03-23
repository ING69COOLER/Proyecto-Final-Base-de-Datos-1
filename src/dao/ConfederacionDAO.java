package dao;

import db.ConexionOracle;
import model.Confederacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfederacionDAO implements GenericDAO<Confederacion> {

    private Connection conn;

    public ConfederacionDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(Confederacion c) {
        String sql = "INSERT INTO CONFEDERACION (nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ConfederacionDAO] Error crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Confederacion obtenerPorId(int id) {
        String sql = "SELECT * FROM CONFEDERACION WHERE id_confederacion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Confederacion(rs.getInt("id_confederacion"), rs.getString("nombre"));
        } catch (SQLException e) {
            System.err.println("[ConfederacionDAO] Error obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Confederacion> obtenerTodos() {
        List<Confederacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM CONFEDERACION ORDER BY nombre";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(new Confederacion(rs.getInt("id_confederacion"), rs.getString("nombre")));
        } catch (SQLException e) {
            System.err.println("[ConfederacionDAO] Error obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Confederacion c) {
        String sql = "UPDATE CONFEDERACION SET nombre=? WHERE id_confederacion=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getIdConfederacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ConfederacionDAO] Error actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM CONFEDERACION WHERE id_confederacion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ConfederacionDAO] Error eliminar: " + e.getMessage());
            return false;
        }
    }
}
