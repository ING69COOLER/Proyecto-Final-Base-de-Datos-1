package dao;

import db.ConexionOracle;
import model.Jugador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase Data Access Object (DAO) de la entidad "Jugador".
 * Sirve como el puente VINCULANTE exclusivo entre tu código Java y la Base de 
 * Datos Oracle (Patrón de Arquitectura Empresarial).
 * Su responsabilidad es enviar y ejecutar código SQL de forma segura
 * encapsulando (ocultando) esas consultas frente a las interfaces gráficas.
 */
public class JugadorDAO implements GenericDAO<Jugador> {

    // Variable encargada de retener el túnel de conexión abierto con Oracle
    private Connection conn;

    public JugadorDAO() {
        // Se obtiene una conexión única protegida por el Patrón Singleton
        this.conn = ConexionOracle.getInstance().getConnection();
    }

    /**
     * Inserta la información en la Base de Datos.
     * @param j Objeto Jugador proveniente del formulario relleno.
     * @return boolean `true` si la fila fue añadida correctamente, `false` en caso de chocar contra reglas DB.
     */
    @Override
    public boolean crear(Jugador j) {
        // USO DE PREPARED-STATEMENT: Se envían variables como (?) y nunca concatenando a mano (+'variables')
        // Esto salva la aplicación de sufrir del mortífero ataque tecnológico "Inyección SQL".
        String sql = "INSERT INTO JUGADOR (nombre, apellido, fecha_nacimiento, posicion, " +
                     "peso, estatura, valor_mercado, id_equipo) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, j.getNombre());
            ps.setString(2, j.getApellido());
            ps.setDate(3, new java.sql.Date(j.getFechaNacimiento().getTime()));
            ps.setString(4, j.getPosicion());
            ps.setDouble(5, j.getPeso());
            ps.setDouble(6, j.getEstatura());
            ps.setDouble(7, j.getValorMercado());
            ps.setInt(8, j.getIdEquipo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Jugador obtenerPorId(int id) {
        String sql = "SELECT * FROM JUGADOR WHERE id_jugador = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Trae absolutamnete todas las filas del servidor SQL a la memoria RAM de Java 
     * en forma de Lista de Arrays de clase abstracta `Jugador`.
     */
    @Override
    public List<Jugador> obtenerTodos() {
        List<Jugador> lista = new ArrayList<>(); // Lista dinámica instanciada
        String sql = "SELECT * FROM JUGADOR ORDER BY apellido, nombre";
        
        // Emplea bloques `try-with-resources` integrados en Java 7 para asegurar
        // que la fuga de memoria NUNCA suceda porque fuerza el cierre del Statement (st.close).
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Convertir cada registro de la tabla SQL devuelta a un objeto del POJO
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Jugador j) {
        String sql = "UPDATE JUGADOR SET nombre=?, apellido=?, fecha_nacimiento=?, posicion=?, " +
                     "peso=?, estatura=?, valor_mercado=?, id_equipo=? WHERE id_jugador=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, j.getNombre());
            ps.setString(2, j.getApellido());
            ps.setDate(3, new java.sql.Date(j.getFechaNacimiento().getTime()));
            ps.setString(4, j.getPosicion());
            ps.setDouble(5, j.getPeso());
            ps.setDouble(6, j.getEstatura());
            ps.setDouble(7, j.getValorMercado());
            ps.setInt(8, j.getIdEquipo());
            ps.setInt(9, j.getIdJugador());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM JUGADOR WHERE id_jugador = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error eliminar: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    // CONSULTAS ESPECIALES (REPORTES)
    // ============================================================

    /**
     * Reporte 1: Jugador mas caro por confederacion.
     * Retorna Object[] {nombre_confederacion, nombre_completo_jugador, valor_mercado}
     */
    public List<Object[]> jugadorMasCaroPorConfederacion() {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT c.nombre AS confederacion, j.nombre || ' ' || j.apellido AS jugador, j.valor_mercado " +
                     "FROM JUGADOR j " +
                     "JOIN EQUIPO e ON j.id_equipo = e.id_equipo " +
                     "JOIN CONFEDERACION c ON e.id_confederacion = c.id_confederacion " +
                     "WHERE j.valor_mercado = ( " +
                     "    SELECT MAX(j2.valor_mercado) FROM JUGADOR j2 " +
                     "    JOIN EQUIPO e2 ON j2.id_equipo = e2.id_equipo " +
                     "    WHERE e2.id_confederacion = e.id_confederacion " +
                     ") " +
                     "ORDER BY c.nombre";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Object[]{rs.getString("confederacion"), rs.getString("jugador"), rs.getDouble("valor_mercado")});
            }
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error jugadorMasCaroPorConf: " + e.getMessage());
        }
        return result;
    }

    /**
     * Reporte 4: Jugadores menores de 20 anos de un equipo.
     */
    public List<Jugador> jugadoresMenoresDe20(int idEquipo) {
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT * FROM JUGADOR WHERE id_equipo = ? " +
                     "AND MONTHS_BETWEEN(SYSDATE, fecha_nacimiento) / 12 < 20 " +
                     "ORDER BY fecha_nacimiento DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error jugadoresMenoresDe20: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Reporte 6: Filtrar jugadores por rango de peso, estatura y equipo.
     */
    public List<Jugador> filtrarPorFisico(double pesoMin, double pesoMax,
                                           double estaturaMin, double estaturaMax, int idEquipo) {
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT * FROM JUGADOR WHERE id_equipo = ? " +
                     "AND peso BETWEEN ? AND ? AND estatura BETWEEN ? AND ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setDouble(2, pesoMin);
            ps.setDouble(3, pesoMax);
            ps.setDouble(4, estaturaMin);
            ps.setDouble(5, estaturaMax);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error filtrarPorFisico: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Reporte 7: Valor total de mercado de jugadores de equipos de una confederacion.
     */
    public double valorTotalPorConfederacion(int idConfederacion) {
        String sql = "SELECT NVL(SUM(j.valor_mercado), 0) AS total " +
                     "FROM JUGADOR j JOIN EQUIPO e ON j.id_equipo = e.id_equipo " +
                     "WHERE e.id_confederacion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idConfederacion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.err.println("[JugadorDAO] Error valorTotalPorConf: " + e.getMessage());
        }
        return 0;
    }

    private Jugador mapear(ResultSet rs) throws SQLException {
        return new Jugador(
            rs.getInt("id_jugador"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getDate("fecha_nacimiento"),
            rs.getString("posicion"),
            rs.getDouble("peso"),
            rs.getDouble("estatura"),
            rs.getDouble("valor_mercado"),
            rs.getInt("id_equipo")
        );
    }
}
