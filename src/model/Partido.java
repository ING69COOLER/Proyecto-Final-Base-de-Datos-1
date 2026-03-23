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

    public Partido(int idPartido, int idEqLocal, int idEqVisit, Timestamp fechaHora, int idEstadio, int idGrupo) {
        this.idPartido = idPartido;
        this.idEqLocal = idEqLocal;
        this.idEqVisit = idEqVisit;
        this.fechaHora = fechaHora;
        this.idEstadio = idEstadio;
        this.idGrupo = idGrupo;
    }

    public int getIdPartido() { return idPartido; }
    public void setIdPartido(int id) { this.idPartido = id; }
    
    public int getIdEqLocal() { return idEqLocal; }
    public void setIdEqLocal(int id) { this.idEqLocal = id; }
    
    public int getIdEqVisit() { return idEqVisit; }
    public void setIdEqVisit(int id) { this.idEqVisit = id; }
    
    public Timestamp getFechaHora() { return fechaHora; }
    public void setFechaHora(Timestamp ts) { this.fechaHora = ts; }
    
    public int getIdEstadio() { return idEstadio; }
    public void setIdEstadio(int id) { this.idEstadio = id; }
    
    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int id) { this.idGrupo = id; }
}
