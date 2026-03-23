package dao;

import db.ConexionOracle;
import model.Ciudad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CiudadDAO implements GenericDAO<Ciudad> {
    private Connection conn;

    public CiudadDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(Ciudad c) {
        String sql = "INSERT INTO CIUDAD (nombre, id_pais_anfitrion) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getIdPaisAnfitrion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public Ciudad obtenerPorId(int id) {
        String sql = "SELECT c.id_ciudad, c.nombre, c.id_pais_anfitrion, p.nombre AS pais " +
                     "FROM CIUDAD c JOIN PAISANFITRION p ON c.id_pais_anfitrion = p.id_pais_anfitrion " +
                     "WHERE c.id_ciudad = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Ciudad(
                rs.getInt("id_ciudad"), rs.getString("nombre"), 
                rs.getInt("id_pais_anfitrion"), rs.getString("pais")
            );
        } catch (SQLException e) {}
        return null;
    }

    @Override
    public List<Ciudad> obtenerTodos() {
        List<Ciudad> lista = new ArrayList<>();
        String sql = "SELECT c.id_ciudad, c.nombre, c.id_pais_anfitrion, p.nombre AS pais " +
                     "FROM CIUDAD c JOIN PAISANFITRION p ON c.id_pais_anfitrion = p.id_pais_anfitrion " +
                     "ORDER BY p.nombre, c.nombre";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Ciudad(
                    rs.getInt("id_ciudad"), rs.getString("nombre"), 
                    rs.getInt("id_pais_anfitrion"), rs.getString("pais")
                ));
            }
        } catch (SQLException e) {}
        return lista;
    }

    @Override
    public boolean actualizar(Ciudad c) {
        String sql = "UPDATE CIUDAD SET nombre=?, id_pais_anfitrion=? WHERE id_ciudad=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getIdPaisAnfitrion());
            ps.setInt(3, c.getIdCiudad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM CIUDAD WHERE id_ciudad = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}
