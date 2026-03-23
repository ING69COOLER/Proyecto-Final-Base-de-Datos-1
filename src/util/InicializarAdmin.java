package util;

import dao.UsuarioDAO;
import model.Usuario;
import security.SeguridadService;

/**
 * Utilidad para inicializar el usuario Admin en la base de datos.
 * Ejecutar esta clase UNA VEZ para crear el admin inicial.
 * 
 * Credenciales iniciales: admin / Admin123!
 */
public class InicializarAdmin {

    public static void main(String[] args) {
        System.out.println("=== Inicializando usuario Admin ===");

        UsuarioDAO dao = new UsuarioDAO();

        // Verificar si ya existe
        Usuario existente = dao.obtenerPorNombre("admin");
        if (existente != null) {
            System.out.println("El usuario 'admin' ya existe. Actualizando contraseña...");
            String nuevoHash = SeguridadService.hashPassword("Admin123!");
            existente.setContrasenaHash(nuevoHash);
            dao.actualizar(existente);
            System.out.println("Contraseña actualizada: Admin123!");
        } else {
            Usuario admin = new Usuario();
            admin.setNombreUsuario("admin");
            admin.setContrasenaHash(SeguridadService.hashPassword("Admin123!"));
            admin.setTipoUsuario("Admin");
            boolean ok = dao.crear(admin);
            System.out.println(ok ? "Admin creado exitosamente." : "ERROR al crear admin.");
        }

        System.out.println("Usuario: admin");
        System.out.println("Contraseña: Admin123!");
        System.out.println("=== Fin inicialización ===");
        db.ConexionOracle.getInstance().closeConnection();
    }
}
