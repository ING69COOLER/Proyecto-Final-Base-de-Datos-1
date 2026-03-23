package model;

import java.util.Date;

public class DirectorTecnico {
    private int idDt;
    private String nombre;
    private String nacionalidad;
    private Date fechaNacimiento;
    private int idEquipo;

    public DirectorTecnico() {}

    public DirectorTecnico(int idDt, String nombre, String nacionalidad, Date fechaNacimiento, int idEquipo) {
        this.idDt = idDt;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.fechaNacimiento = fechaNacimiento;
        this.idEquipo = idEquipo;
    }

    public int getIdDt() { return idDt; }
    public void setIdDt(int id) { this.idDt = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fecha) { this.fechaNacimiento = fecha; }
    
    public int getIdEquipo() { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }
    
    @Override
    public String toString() { return nombre; }
}
