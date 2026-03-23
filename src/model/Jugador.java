package model;

import java.util.Date;

public class Jugador {
    private int idJugador;
    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private String posicion;
    private double peso;
    private double estatura;
    private double valorMercado;
    private int idEquipo;

    public Jugador() {}

    public Jugador(int id, String nombre, String apellido, Date fechaNac,
                   String posicion, double peso, double estatura, double valor, int idEquipo) {
        this.idJugador = id; this.nombre = nombre; this.apellido = apellido;
        this.fechaNacimiento = fechaNac; this.posicion = posicion;
        this.peso = peso; this.estatura = estatura;
        this.valorMercado = valor; this.idEquipo = idEquipo;
    }

    public int getIdJugador()                    { return idJugador; }
    public void setIdJugador(int id)             { this.idJugador = id; }
    public String getNombre()                    { return nombre; }
    public void setNombre(String nombre)         { this.nombre = nombre; }
    public String getApellido()                  { return apellido; }
    public void setApellido(String ap)           { this.apellido = ap; }
    public Date getFechaNacimiento()             { return fechaNacimiento; }
    public void setFechaNacimiento(Date f)       { this.fechaNacimiento = f; }
    public String getPosicion()                  { return posicion; }
    public void setPosicion(String p)            { this.posicion = p; }
    public double getPeso()                      { return peso; }
    public void setPeso(double p)                { this.peso = p; }
    public double getEstatura()                  { return estatura; }
    public void setEstatura(double e)            { this.estatura = e; }
    public double getValorMercado()              { return valorMercado; }
    public void setValorMercado(double v)        { this.valorMercado = v; }
    public int getIdEquipo()                     { return idEquipo; }
    public void setIdEquipo(int id)              { this.idEquipo = id; }

    public String getNombreCompleto()            { return nombre + " " + apellido; }

    /**
     * Calcula la edad del jugador basada en fecha de nacimiento.
     */
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalDate nac = fechaNacimiento.toInstant()
            .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        return (int) java.time.temporal.ChronoUnit.YEARS.between(nac, hoy);
    }

    @Override public String toString() { return getNombreCompleto(); }
}
