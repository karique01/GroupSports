package pe.edu.upc.groupsports.models;

import android.content.Intent;

/**
 * Created by karique on 3/05/2018.
 */

public class AthleteCategory {
    private String categoryName;
    private String edadFin;
    private String categoryApi;
    private int pictureId;
    private boolean selected;

    public AthleteCategory() {
    }

    public AthleteCategory(String categoryName, String edadFin, int pictureId,String categoryApi) {
        this.categoryName = categoryName;
        this.edadFin = edadFin;
        this.pictureId = pictureId;
        this.categoryApi = categoryApi;
        selected = false;
    }

    public String getCategoryApi() {
        return categoryApi;
    }

    public void setCategoryApi(String categoryApi) {
        this.categoryApi = categoryApi;
    }
    public String getUnderAge() {return String.valueOf(Integer.parseInt(edadFin) + 1);}

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
