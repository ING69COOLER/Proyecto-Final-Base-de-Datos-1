package dao;

import db.ConexionOracle;
import model.Bitacora;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BitacoraDAO implements GenericDAO<Bitacora> {

    private Connection conn;

    public BitacoraDAO() {
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    /**
     * Registra el ingreso de un usuario y retorna el id del registro creado.
     */
    public int registrarIngreso(int idUsuario) {
        String sql = "INSERT INTO BITACORA (id_usuario, fecha_hora_ingreso) VALUES (?, ?) RETURNING id_registro INTO ?";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, idUsuario);
            cs.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();
            return cs.getInt(3);
        } catch (SQLException e) {
            // Fallback sin RETURNING
            System.err.println("[BitacoraDAO] Usando método alternativo para ingreso.");
            String ins = "INSERT INTO BITACORA (id_usuario, fecha_hora_ingreso) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(ins)) {
                ps.setInt(1, idUsuario);
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                ps.executeUpdate();
                // Obtener último ID
                String selId = "SELECT MAX(id_registro) FROM BITACORA WHERE id_usuario = ?";
                PreparedStatement ps2 = conn.prepareStatement(selId);
                ps2.setInt(1, idUsuario);
                ResultSet rs = ps2.executeQuery();
                if (rs.next()) return rs.getInt(1);
            } catch (SQLException ex) {
                System.err.println("[BitacoraDAO] Error registrarIngreso: " + ex.getMessage());
            }
        }
        return -1;
    }

    /**
     * Actualiza la fecha de salida del registro de bitácora.
     */
    public boolean registrarSalida(int idRegistro) {
        String sql = "UPDATE BITACORA SET fecha_hora_salida = ? WHERE id_registro = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setInt(2, idRegistro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BitacoraDAO] Error registrarSalida: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reporte de bitácora filtrado por rango de fechas.
     */
    public List<Bitacora> obtenerPorRango(java.util.Date inicio, java.util.Date fin) {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT * FROM BITACORA WHERE fecha_hora_ingreso BETWEEN ? AND ? ORDER BY fecha_hora_ingreso DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(inicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fin.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[BitacoraDAO] Error obtenerPorRango: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Reporte de Auditoría Integrado (Trae Nombre de Usuario).
     */
    public List<Object[]> obtenerAuditoria(java.util.Date inicio, java.util.Date fin) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT b.id_registro, u.nombre_usuario, b.fecha_hora_ingreso, b.fecha_hora_salida " +
                     "FROM BITACORA b JOIN USUARIO u ON b.id_usuario = u.id_usuario " +
                     "WHERE b.fecha_hora_ingreso BETWEEN ? AND ? ORDER BY b.fecha_hora_ingreso DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(inicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fin.getTime()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_registro"),
                    rs.getString("nombre_usuario"),
                    rs.getTimestamp("fecha_hora_ingreso"),
                    rs.getTimestamp("fecha_hora_salida")
                });
            }
        } catch (SQLException e) {
            System.err.println("[BitacoraDAO] Error obtenerAuditoria: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean crear(Bitacora b) {
        String sql = "INSERT INTO BITACORA (id_usuario, fecha_hora_ingreso, fecha_hora_salida) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, b.getIdUsuario());
            ps.setTimestamp(2, b.getFechaHoraIngreso());
            ps.setTimestamp(3, b.getFechaHoraSalida());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BitacoraDAO] Error crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Bitacora obtenerPorId(int id) {
        String sql = "SELECT * FROM BITACORA WHERE id_registro = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[BitacoraDAO] Error obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Bitacora> obtenerTodos() {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT * FROM BITACORA ORDER BY fecha_hora_ingreso DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[BitacoraDAO] Error obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Bitacora b) {
        String sql = "UPDATE BITACORA SET fecha_hora_salida=? WHERE id_registro=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, b.getFechaHoraSalida());
            ps.setInt(2, b.getIdRegistro());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BitacoraDAO] Error actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM BITACORA WHERE id_registro = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[BitacoraDAO] Error eliminar: " + e.getMessage());
            return false;
        }
    }

    private Bitacora mapear(ResultSet rs) throws SQLException {
        return new Bitacora(
            rs.getInt("id_registro"),
            rs.getInt("id_usuario"),
            rs.getTimestamp("fecha_hora_ingreso"),
            rs.getTimestamp("fecha_hora_salida")
        );
    }
}
