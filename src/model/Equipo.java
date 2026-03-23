package model;

public class Equipo {
    private int idEquipo;
    private String nombre;
    private int rankingFifa;
    private int idConfederacion;
    private double valorMercado;

    public Equipo() {}
    public Equipo(int id, String nombre, int ranking, int idConf, double valor) {
        this.idEquipo = id; this.nombre = nombre; this.rankingFifa = ranking;
        this.idConfederacion = idConf; this.valorMercado = valor;
    }

    public int getIdEquipo()                     { return idEquipo; }
    public void setIdEquipo(int id)              { this.idEquipo = id; }
    public String getNombre()                    { return nombre; }
    public void setNombre(String nombre)         { this.nombre = nombre; }
    public int getRankingFifa()                  { return rankingFifa; }
    public void setRankingFifa(int r)            { this.rankingFifa = r; }
    public int getIdConfederacion()              { return idConfederacion; }
    public void setIdConfederacion(int id)       { this.idConfederacion = id; }
    public double getValorMercado()              { return valorMercado; }
    public void setValorMercado(double v)        { this.valorMercado = v; }

    @Override public String toString()           { return nombre; }
}
