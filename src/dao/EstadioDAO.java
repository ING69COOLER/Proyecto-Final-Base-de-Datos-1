package dao;

import db.ConexionOracle;
import model.Estadio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadioDAO implements GenericDAO<Estadio> {
    private Connection conn;

    public EstadioDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(Estadio e) {
        String sql = "INSERT INTO ESTADIO (nombre, capacidad, id_ciudad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getCapacidad());
            ps.setInt(3, e.getIdCiudad());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { return false; }
    }

    @Override
    public Estadio obtenerPorId(int id) {
        String sql = "SELECT * FROM ESTADIO WHERE id_estadio = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Estadio(
                rs.getInt("id_estadio"), rs.getString("nombre"), 
                rs.getInt("capacidad"), rs.getInt("id_ciudad")
            );
        } catch (SQLException ex) {}
        return null;
    }

    @Override
    public List<Estadio> obtenerTodos() {
        List<Estadio> lista = new ArrayList<>();
        String sql = "SELECT * FROM ESTADIO ORDER BY nombre";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Estadio(
                    rs.getInt("id_estadio"), rs.getString("nombre"), 
                    rs.getInt("capacidad"), rs.getInt("id_ciudad")
                ));
            }
        } catch (SQLException ex) {}
        return lista;
    }

    @Override
    public boolean actualizar(Estadio e) {
        String sql = "UPDATE ESTADIO SET nombre=?, capacidad=?, id_ciudad=? WHERE id_estadio=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getCapacidad());
            ps.setInt(3, e.getIdCiudad());
            ps.setInt(4, e.getIdEstadio());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM ESTADIO WHERE id_estadio = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { return false; }
    }
}
