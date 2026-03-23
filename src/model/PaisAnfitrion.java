package model;

public class PaisAnfitrion {
    private int idPaisAnfitrion;
    private String nombre;

    public PaisAnfitrion() {}

    public PaisAnfitrion(int idPaisAnfitrion, String nombre) {
        this.idPaisAnfitrion = idPaisAnfitrion;
        this.nombre = nombre;
    }

    public int getIdPaisAnfitrion() { return idPaisAnfitrion; }
    public void setIdPaisAnfitrion(int id) { this.idPaisAnfitrion = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    @Override
    public String toString() { return nombre; }
}
