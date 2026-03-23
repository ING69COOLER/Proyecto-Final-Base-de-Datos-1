package model;

public class Ciudad {
    private int idCiudad;
    private String nombre;
    private int idPaisAnfitrion;
    
    // Extra para el UI
    private String nombrePais;

    public Ciudad() {}

    public Ciudad(int idCiudad, String nombre, int idPaisAnfitrion, String nombrePais) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.idPaisAnfitrion = idPaisAnfitrion;
        this.nombrePais = nombrePais;
    }

    public int getIdCiudad() { return idCiudad; }
    public void setIdCiudad(int id) { this.idCiudad = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getIdPaisAnfitrion() { return idPaisAnfitrion; }
    public void setIdPaisAnfitrion(int idPaisAnfitrion) { this.idPaisAnfitrion = idPaisAnfitrion; }
    public String getNombrePais() { return nombrePais; }
    public void setNombrePais(String nombrePais) { this.nombrePais = nombrePais; }
    
    // Para que el JComboBox lo muestre bonito: "Guadalajara (Mexico)"
    @Override
    public String toString() {
        if (nombrePais != null && !nombrePais.isEmpty()) {
            return nombre + " (" + nombrePais + ")";
        }
        return nombre;
    }
}
