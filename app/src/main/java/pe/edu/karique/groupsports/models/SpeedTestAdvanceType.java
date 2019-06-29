package pe.edu.karique.groupsports.models;

public class SpeedTestAdvanceType {
    private String id;
    private String categoria;

    public SpeedTestAdvanceType(String id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public SpeedTestAdvanceType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
