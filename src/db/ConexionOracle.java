package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton para la conexion a Oracle.
 * Configura las credenciales de tu base de datos Oracle aqui.
 */
public class ConexionOracle {

    // =====================================================
    // CONFIGURACION ORACLE 21c EXPRESS EDITION (XE)
    // Pluggable Database: XEPDB1
    // =====================================================
    private static final String HOST     = "localhost";
    private static final String PORT     = "1521";
    // Oracle 21c XE usa Service Name "XEPDB1" (Pluggable DB)
    // NO usar el formato antiguo @host:port:SID
    private static final String URL      = "jdbc:oracle:thin:@//" + HOST + ":" + PORT + "/XEPDB1";
    private static final String USER     = "MUNDIAL2026";  // Crea este usuario en XEPDB1
    private static final String PASSWORD = "Mundial2026";  // Cambia por tu contraseña

    private static ConexionOracle instance;
    private Connection connection;

    private ConexionOracle() {
        try {
            // Oracle 21c XE: el driver se registra automáticamente con ojdbc11.jar
            // Pero lo cargamos explícitamente por compatibilidad
            Class.forName("oracle.jdbc.OracleDriver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            this.connection.setAutoCommit(true);
            System.out.println("[DB] Conexión a Oracle 21c XE (XEPDB1) establecida correctamente.");
            System.out.println("[DB] URL: " + URL);
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] ERROR: ojdbc11.jar no encontrado en el classpath.");
            System.err.println("[DB] Descarga desde: https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html");
            throw new RuntimeException("Driver JDBC no encontrado", e);
        } catch (SQLException e) {
            System.err.println("[DB] ERROR al conectar a Oracle 21c XE:");
            System.err.println("[DB]   URL   : " + URL);
            System.err.println("[DB]   USER  : " + USER);
            System.err.println("[DB]   Causa : " + e.getMessage());
            System.err.println("[DB] Verifica:");
            System.err.println("[DB]   1. Servicio OracleServiceXE corriendo (services.msc)");
            System.err.println("[DB]   2. Puerto 1521 libre");
            System.err.println("[DB]   3. Usuario MUNDIAL2026 creado en XEPDB1");
            throw new RuntimeException("No se pudo conectar a Oracle 21c XE", e);
        }
    }

    /**
     * Obtiene la instancia Singleton de la conexion.
     */
    public static synchronized ConexionOracle getInstance() {
        if (instance == null || !instance.isConnected()) {
            instance = new ConexionOracle();
        }
        return instance;
    }

    /**
     * Retorna el objeto Connection activo.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Verifica si la conexion esta activa.
     */
    private boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Cierra la conexion con la base de datos.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error al cerrar conexión: " + e.getMessage());
        }
    }

    /**
     * Prueba la conexion imprimiendo un mensaje.
     * @return true si la conexion es exitosa.
     */
    public boolean testConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("[DB] Conexión activa: " + URL);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("[DB] Fallo de conexion: " + e.getMessage());
        }
        return false;
    }
}
