package dao;

import db.ConexionOracle;
import model.DirectorTecnico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectorTecnicoDAO implements GenericDAO<DirectorTecnico> {

    private Connection conn;

    public DirectorTecnicoDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(DirectorTecnico d) {
        String sql = "INSERT INTO DIRECTORTECNICO (nombre, nacionalidad, fecha_nacimiento, id_equipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getNacionalidad());
            ps.setDate(3, new java.sql.Date(d.getFechaNacimiento().getTime()));
            ps.setInt(4, d.getIdEquipo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DirectorTecnicoDAO] Error crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public DirectorTecnico obtenerPorId(int id) {
        String sql = "SELECT * FROM DIRECTORTECNICO WHERE id_dt = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[DirectorTecnicoDAO] Error obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<DirectorTecnico> obtenerTodos() {
        List<DirectorTecnico> lista = new ArrayList<>();
        String sql = "SELECT * FROM DIRECTORTECNICO ORDER BY nombre";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DirectorTecnicoDAO] Error obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(DirectorTecnico d) {
        String sql = "UPDATE DIRECTORTECNICO SET nombre=?, nacionalidad=?, fecha_nacimiento=?, id_equipo=? WHERE id_dt=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getNacionalidad());
            ps.setDate(3, new java.sql.Date(d.getFechaNacimiento().getTime()));
            ps.setInt(4, d.getIdEquipo());
            ps.setInt(5, d.getIdDt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DirectorTecnicoDAO] Error actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM DIRECTORTECNICO WHERE id_dt = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DirectorTecnicoDAO] Error eliminar: " + e.getMessage());
            return false;
        }
    }

    private DirectorTecnico mapear(ResultSet rs) throws SQLException {
        DirectorTecnico d = new DirectorTecnico();
        d.setIdDt(rs.getInt("id_dt"));
        d.setNombre(rs.getString("nombre"));
        d.setNacionalidad(rs.getString("nacionalidad"));
        d.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        d.setIdEquipo(rs.getInt("id_equipo"));
        return d;
    }
}
