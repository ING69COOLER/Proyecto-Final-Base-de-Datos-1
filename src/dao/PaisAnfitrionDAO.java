package dao;

import db.ConexionOracle;
import model.PaisAnfitrion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaisAnfitrionDAO implements GenericDAO<PaisAnfitrion> {
    private Connection conn;

    public PaisAnfitrionDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(PaisAnfitrion p) {
        String sql = "INSERT INTO PAISANFITRION (nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public PaisAnfitrion obtenerPorId(int id) {
        String sql = "SELECT * FROM PAISANFITRION WHERE id_pais_anfitrion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new PaisAnfitrion(rs.getInt("id_pais_anfitrion"), rs.getString("nombre"));
        } catch (SQLException e) {}
        return null;
    }

    @Override
    public List<PaisAnfitrion> obtenerTodos() {
        List<PaisAnfitrion> lista = new ArrayList<>();
        String sql = "SELECT * FROM PAISANFITRION ORDER BY nombre";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(new PaisAnfitrion(rs.getInt("id_pais_anfitrion"), rs.getString("nombre")));
        } catch (SQLException e) {}
        return lista;
    }

    @Override
    public boolean actualizar(PaisAnfitrion p) {
        String sql = "UPDATE PAISANFITRION SET nombre=? WHERE id_pais_anfitrion=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getIdPaisAnfitrion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM PAISANFITRION WHERE id_pais_anfitrion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}
