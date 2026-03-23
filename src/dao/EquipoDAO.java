package dao;

import db.ConexionOracle;
import model.Equipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO implements GenericDAO<Equipo> {

    private Connection conn;

    public EquipoDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(Equipo e) {
        String sql = "INSERT INTO EQUIPO (nombre, ranking_fifa, id_confederacion, valor_mercado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getRankingFifa());
            ps.setInt(3, e.getIdConfederacion());
            ps.setDouble(4, e.getValorMercado());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[EquipoDAO] Error crear: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public Equipo obtenerPorId(int id) {
        String sql = "SELECT * FROM EQUIPO WHERE id_equipo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException ex) {
            System.err.println("[EquipoDAO] Error obtenerPorId: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<Equipo> obtenerTodos() {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM EQUIPO ORDER BY nombre";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException ex) {
            System.err.println("[EquipoDAO] Error obtenerTodos: " + ex.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Equipo e) {
        String sql = "UPDATE EQUIPO SET nombre=?, ranking_fifa=?, id_confederacion=?, valor_mercado=? WHERE id_equipo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getRankingFifa());
            ps.setInt(3, e.getIdConfederacion());
            ps.setDouble(4, e.getValorMercado());
            ps.setInt(5, e.getIdEquipo());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[EquipoDAO] Error actualizar: " + ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM EQUIPO WHERE id_equipo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("[EquipoDAO] Error eliminar: " + ex.getMessage());
            return false;
        }
    }

    private Equipo mapear(ResultSet rs) throws SQLException {
        Equipo e = new Equipo();
        e.setIdEquipo(rs.getInt("id_equipo"));
        e.setNombre(rs.getString("nombre"));
        e.setRankingFifa(rs.getInt("ranking_fifa"));
        e.setIdConfederacion(rs.getInt("id_confederacion"));
        e.setValorMercado(rs.getDouble("valor_mercado"));
        return e;
    }

    // ===========================================
    // REPORTES ESPECIALES
    // ===========================================

    /**
     * Reporte 3: Equipo mas caro de un Pais Anfitrion.
     * Un pais anfitrion puede tener multiples estadios y ciudades.
     */
    public List<Object[]> equipoMasCaroPorPaisAnfitrion() {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT pa.nombre AS pais, e.nombre AS equipo, e.valor_mercado " +
                     "FROM PAISANFITRION pa " +
                     "CROSS JOIN EQUIPO e " +
                     "WHERE e.valor_mercado = (SELECT MAX(valor_mercado) FROM EQUIPO " +
                     "                         WHERE id_confederacion = (" +
                     "                             SELECT id_confederacion FROM CONFEDERACION " +
                     "                             WHERE nombre = CASE pa.nombre " +
                     "                               WHEN 'Mexico' THEN 'CONCACAF' " +
                     "                               WHEN 'Estados Unidos' THEN 'CONCACAF' " +
                     "                               WHEN 'Canada' THEN 'CONCACAF' " +
                     "                               ELSE 'FIFA' END ))";
                     
        // NOTA: Como el PDF pide "equipo mas caro de un pais anfitrion" y en nuestro 
        // ER los paises anfitriones no tienen directamente equipos (solo ciudades y estadios),
        // interpretamos que es el equipo mas caro del continente/confederacion del pais anfitrion.
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            // Este es un query conceptual para cumplir la relacion pedida si no hay liga local
            // Lo adaptaremos para que no falle.
            while (rs.next()) {
                result.add(new Object[]{rs.getString("pais"), rs.getString("equipo"), rs.getDouble("valor_mercado")});
            }
        } catch (SQLException ex) {
            System.err.println("[EquipoDAO] Error equipoMasCaroPorPais: " + ex.getMessage());
        }
        return result;
    }
}
