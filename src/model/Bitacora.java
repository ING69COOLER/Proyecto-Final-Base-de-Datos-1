package model;

import java.sql.Timestamp;

public class Bitacora {
    private int idRegistro;
    private int idUsuario;
    private Timestamp fechaHoraIngreso;
    private Timestamp fechaHoraSalida;

    public Bitacora() {}

    public Bitacora(int idRegistro, int idUsuario, Timestamp ingreso, Timestamp salida) {
        this.idRegistro = idRegistro;
        this.idUsuario = idUsuario;
        this.fechaHoraIngreso = ingreso;
        this.fechaHoraSalida = salida;
    }

    public int getIdRegistro()                       { return idRegistro; }
    public void setIdRegistro(int id)                { this.idRegistro = id; }
    public int getIdUsuario()                        { return idUsuario; }
    public void setIdUsuario(int id)                 { this.idUsuario = id; }
    public Timestamp getFechaHoraIngreso()           { return fechaHoraIngreso; }
    public void setFechaHoraIngreso(Timestamp ts)    { this.fechaHoraIngreso = ts; }
    public Timestamp getFechaHoraSalida()            { return fechaHoraSalida; }
    public void setFechaHoraSalida(Timestamp ts)     { this.fechaHoraSalida = ts; }
}
