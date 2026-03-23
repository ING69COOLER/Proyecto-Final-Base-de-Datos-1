package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import model.Usuario;

/**
 * Servicio de seguridad para hash de contraseñas y control de permisos.
 * 
 * NOTA: Se implementa SHA-256 con salt para no necesitar dependencias externas.
 * Si se permite BCrypt como librería externa, reemplaza hashPassword/verificarPassword.
 */
public class SeguridadService {

    /**
     * Genera un salt aleatorio codificado en Base64.
     */
    public static String generarSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashea una contraseña usando SHA-256 con salt.
     * Formato almacenado: "salt$hash"
     */
    public static String hashPassword(String passwordPlano) {
        try {
            String salt = generarSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedBytes = md.digest(passwordPlano.getBytes());
            String hash = Base64.getEncoder().encodeToString(hashedBytes);
            return salt + "$" + hash;  // Guardamos salt + hash
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error en hash de contraseña", e);
        }
    }

    /**
     * Verifica si una contraseña en texto plano coincide con el hash almacenado.
     */
    public static boolean verificarPassword(String passwordPlano, String hashAlmacenado) {
        try {
            if (hashAlmacenado == null || !hashAlmacenado.contains("$")) return false;
            String[] partes = hashAlmacenado.split("\\$");
            if (partes.length != 2) return false;
            String salt = partes[0];
            String hashOriginal = partes[1];
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedBytes = md.digest(passwordPlano.getBytes());
            String hashNuevo = Base64.getEncoder().encodeToString(hashedBytes);
            return hashNuevo.equals(hashOriginal);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error verificando contraseña", e);
        }
    }

    /**
     * Verifica si el usuario tiene permiso para operaciones CRUD (Admin o Tradicional).
     */
    public static boolean tienePermisoCRUD(Usuario usuario) {
        if (usuario == null) return false;
        return "Admin".equals(usuario.getTipoUsuario()) ||
               "Tradicional".equals(usuario.getTipoUsuario());
    }

    /**
     * Verifica si el usuario es Administrador.
     */
    public static boolean esAdmin(Usuario usuario) {
        if (usuario == null) return false;
        return "Admin".equals(usuario.getTipoUsuario());
    }

    /**
     * Los usuarios Esporadicos solo pueden leer.
     */
    public static boolean soloLectura(Usuario usuario) {
        if (usuario == null) return true;
        return "Esporadico".equals(usuario.getTipoUsuario());
    }
}
