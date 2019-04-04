package pe.edu.karique.groupsports.models;

/**
 * Created by karique on 28/02/2018.
 */

public class CoachUpdate {
    private String tittle;
    private String description;

    public CoachUpdate() {
    }

    public CoachUpdate(String tittle, String description) {
        this.tittle = tittle;
        this.description = description;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
