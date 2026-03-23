package model;

import java.util.Date;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String contrasenaHash;
    private String tipoUsuario;
    private Date fechaCreacion;

    public Usuario() {}

    public Usuario(int idUsuario, String nombreUsuario, String contrasenaHash,
                   String tipoUsuario, Date fechaCreacion) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.tipoUsuario = tipoUsuario;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdUsuario()            { return idUsuario; }
    public void setIdUsuario(int id)     { this.idUsuario = id; }

    public String getNombreUsuario()              { return nombreUsuario; }
    public void setNombreUsuario(String nombre)   { this.nombreUsuario = nombre; }

    public String getContrasenaHash()             { return contrasenaHash; }
    public void setContrasenaHash(String hash)    { this.contrasenaHash = hash; }

    public String getTipoUsuario()                { return tipoUsuario; }
    public void setTipoUsuario(String tipo)       { this.tipoUsuario = tipo; }

    public Date getFechaCreacion()                { return fechaCreacion; }
    public void setFechaCreacion(Date fecha)      { this.fechaCreacion = fecha; }

    @Override
    public String toString() {
        return nombreUsuario + " (" + tipoUsuario + ")";
    }
}
