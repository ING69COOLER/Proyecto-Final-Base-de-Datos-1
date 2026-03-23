package security;

import model.Usuario;

/**
 * Maneja la sesion actual del usuario logueado en la aplicacion.
 * Patron Singleton de estado de sesion.
 */
public class SesionActual {

    private static Usuario usuarioLogueado = null;
    private static int idRegistroBitacora = -1;

    private SesionActual() {} // No instanciable

    /**
     * Inicia la sesion para el usuario dado.
     */
    public static void iniciarSesion(Usuario usuario, int idRegistro) {
        usuarioLogueado = usuario;
        idRegistroBitacora = idRegistro;
        System.out.println("[Sesion] Sesion iniciada para: " + usuario.getNombreUsuario());
    }

    /**
     * Cierra la sesion actual.
     */
    public static void cerrarSesion() {
        System.out.println("[Sesion] Sesion cerrada para: " +
            (usuarioLogueado != null ? usuarioLogueado.getNombreUsuario() : "nadie"));
        usuarioLogueado = null;
        idRegistroBitacora = -1;
    }

    public static Usuario getUsuario()           { return usuarioLogueado; }
    public static int getIdRegistroBitacora()    { return idRegistroBitacora; }
    public static boolean hayUsuario()           { return usuarioLogueado != null; }

    public static boolean esAdmin() {
        return SeguridadService.esAdmin(usuarioLogueado);
    }

    public static boolean puedeEscribir() {
        return SeguridadService.tienePermisoCRUD(usuarioLogueado);
    }

    public static boolean soloPuedeLeer() {
        return SeguridadService.soloLectura(usuarioLogueado);
    }
}
