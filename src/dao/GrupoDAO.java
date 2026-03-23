package dao;

import db.ConexionOracle;
import model.Grupo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrupoDAO implements GenericDAO<Grupo> {
    private Connection conn;

    public GrupoDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(Grupo g) {
        String sql = "INSERT INTO GRUPO (nombre_grupo) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getNombre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public Grupo obtenerPorId(int id) {
        String sql = "SELECT * FROM GRUPO WHERE id_grupo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Grupo(rs.getInt("id_grupo"), rs.getString("nombre_grupo"));
        } catch (SQLException e) {}
        return null;
    }

    @Override
    public List<Grupo> obtenerTodos() {
        List<Grupo> lista = new ArrayList<>();
        String sql = "SELECT * FROM GRUPO ORDER BY nombre_grupo";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(new Grupo(rs.getInt("id_grupo"), rs.getString("nombre_grupo")));
        } catch (SQLException e) {}
        return lista;
    }

    @Override
    public boolean actualizar(Grupo g) {
        String sql = "UPDATE GRUPO SET nombre_grupo=? WHERE id_grupo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getNombre());
            ps.setInt(2, g.getIdGrupo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM GRUPO WHERE id_grupo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}
