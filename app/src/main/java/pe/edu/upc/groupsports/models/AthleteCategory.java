package pe.edu.upc.groupsports.models;

/**
 * Created by karique on 3/05/2018.
 */

public class AthleteCategory {
    private String categoryName;
    private String edadInicio;
    private String edadFin;
    private int pictureId;

    public AthleteCategory() {
    }

    public AthleteCategory(String categoryName, String edadInicio, String edadFin, int pictureId) {
        this.categoryName = categoryName;
        this.edadInicio = edadInicio;
        this.edadFin = edadFin;
        this.pictureId = pictureId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getEdadInicio() {
        return edadInicio;
    }

    public void setEdadInicio(String edadInicio) {
        this.edadInicio = edadInicio;
    }

    public String getEdadFin() {
        return edadFin;
    }

    public void setEdadFin(String edadFin) {
        this.edadFin = edadFin;
    }


    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

}
