package model;

public class Confederacion {
    private int idConfederacion;
    private String nombre;

    public Confederacion() {}
    public Confederacion(int id, String nombre) { this.idConfederacion = id; this.nombre = nombre; }

    public int getIdConfederacion()              { return idConfederacion; }
    public void setIdConfederacion(int id)       { this.idConfederacion = id; }
    public String getNombre()                    { return nombre; }
    public void setNombre(String nombre)         { this.nombre = nombre; }

    @Override public String toString()           { return nombre; }
}
