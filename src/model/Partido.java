package model;

import java.sql.Timestamp;

public class Partido {
    private int idPartido;
    private int idEqLocal;
    private int idEqVisit;
    private Timestamp fechaHora;
    private int idEstadio;
    private int idGrupo;

    public Partido() {}
    public Partido(int id, int local, int visit, Timestamp fecha, int estadio, int grupo) {
        this.idPartido = id; this.idEqLocal = local; this.idEqVisit = visit;
        this.fechaHora = fecha; this.idEstadio = estadio; this.idGrupo = grupo;
    }

    public int getIdPartido()                  { return idPartido; }
    public void setIdPartido(int id)           { this.idPartido = id; }
    public int getIdEqLocal()                  { return idEqLocal; }
    public void setIdEqLocal(int id)           { this.idEqLocal = id; }
    public int getIdEqVisit()                  { return idEqVisit; }
    public void setIdEqVisit(int id)           { this.idEqVisit = id; }
    public Timestamp getFechaHora()            { return fechaHora; }
    public void setFechaHora(Timestamp ts)     { this.fechaHora = ts; }
    public int getIdEstadio()                  { return idEstadio; }
    public void setIdEstadio(int id)           { this.idEstadio = id; }
    public int getIdGrupo()                    { return idGrupo; }
    public void setIdGrupo(int id)             { this.idGrupo = id; }
}
