package dao;

import db.ConexionOracle;
import model.Partido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidoDAO implements GenericDAO<Partido> {

    private Connection conn;

    public PartidoDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    @Override
    public boolean crear(Partido p) {
        String sql = "INSERT INTO PARTIDO (id_eq_local, id_eq_visit, fecha_hora, id_estadio, id_grupo) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdEqLocal());
            ps.setInt(2, p.getIdEqVisit());
            ps.setTimestamp(3, p.getFechaHora());
            ps.setInt(4, p.getIdEstadio());
            ps.setInt(5, p.getIdGrupo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PartidoDAO] Error crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Partido obtenerPorId(int id) {
        String sql = "SELECT * FROM PARTIDO WHERE id_partido = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {}
        return null;
    }

    @Override
    public List<Partido> obtenerTodos() {
        List<Partido> lista = new ArrayList<>();
        String sql = "SELECT * FROM PARTIDO ORDER BY fecha_hora";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean actualizar(Partido p) {
        String sql = "UPDATE PARTIDO SET id_eq_local=?, id_eq_visit=?, fecha_hora=?, id_estadio=?, id_grupo=? WHERE id_partido=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdEqLocal());
            ps.setInt(2, p.getIdEqVisit());
            ps.setTimestamp(3, p.getFechaHora());
            ps.setInt(4, p.getIdEstadio());
            ps.setInt(5, p.getIdGrupo());
            ps.setInt(6, p.getIdPartido());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM PARTIDO WHERE id_partido = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public List<Object[]> obtenerPorEstadio(int idEstadio) {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT p.id_partido, el.nombre AS local, ev.nombre AS visitante, " +
                     "p.fecha_hora, es.nombre AS estadio " +
                     "FROM PARTIDO p " +
                     "JOIN EQUIPO el ON p.id_eq_local = el.id_equipo " +
                     "JOIN EQUIPO ev ON p.id_eq_visit = ev.id_equipo " +
                     "JOIN ESTADIO es ON p.id_estadio = es.id_estadio " +
                     "WHERE p.id_estadio = ? ORDER BY p.fecha_hora";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstadio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Object[]{
                    rs.getInt("id_partido"), rs.getString("local"), rs.getString("visitante"),
                    rs.getTimestamp("fecha_hora"), rs.getString("estadio")
                });
            }
        } catch (SQLException e) {}
        return result;
    }

    public List<Partido> filtrarPorEstadio(int idEstadio) {
        List<Partido> lista = new ArrayList<>();
        String sql = "SELECT * FROM PARTIDO WHERE id_estadio = ? ORDER BY fecha_hora";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEstadio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public List<Object[]> partidosPorEquipoPorPais() {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT e.nombre AS equipo, pa.nombre AS pais, COUNT(*) AS num_partidos " +
                     "FROM PARTIDO p " +
                     "JOIN EQUIPO e ON (p.id_eq_local = e.id_equipo OR p.id_eq_visit = e.id_equipo) " +
                     "JOIN ESTADIO es ON p.id_estadio = es.id_estadio " +
                     "JOIN CIUDAD c ON es.id_ciudad = c.id_ciudad " +
                     "JOIN PAISANFITRION pa ON c.id_pais_anfitrion = pa.id_pais_anfitrion " +
                     "WHERE e.id_equipo NOT IN ( " +
                     "    SELECT eq.id_equipo FROM EQUIPO eq " +
                     "    JOIN CONFEDERACION conf ON eq.id_confederacion = conf.id_confederacion " +
                     "    WHERE pa.nombre = 'Mexico' AND conf.nombre = 'CONCACAF' " +
                     ") " +
                     "GROUP BY e.nombre, pa.nombre ORDER BY e.nombre, pa.nombre";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Object[]{rs.getString("equipo"), rs.getString("pais"), rs.getInt("num_partidos")});
            }
        } catch (SQLException e) {}
        return result;
    }

    private Partido mapear(ResultSet rs) throws SQLException {
        return new Partido(
            rs.getInt("id_partido"),
            rs.getInt("id_eq_local"),
            rs.getInt("id_eq_visit"),
            rs.getTimestamp("fecha_hora"),
            rs.getInt("id_estadio"),
            rs.getInt("id_grupo")
        );
    }
}
