package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 23/09/2018.
 */

public class AthleteFodaItem {
    private String id;
    private String fodaItemValue;
    private String fodaItemTypeId;
    private String athleteId;
    private Date date;

    public static AthleteFodaItem toAthleteFodaItem(JSONObject jsonObject){
        AthleteFodaItem athleteFodaItem = new AthleteFodaItem();
        try {
            athleteFodaItem.setId(jsonObject.getString("id"));
            athleteFodaItem.setFodaItemValue(jsonObject.getString("fodaItemValue"));
            athleteFodaItem.setFodaItemTypeId(jsonObject.getString("fodaItemTypeId"));
            athleteFodaItem.setAthleteId(jsonObject.getString("athleteId"));
            athleteFodaItem.setDate(Funciones.getDateFromString(jsonObject.getString("date")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return athleteFodaItem;
    }

    public AthleteFodaItem() {
    }

    public AthleteFodaItem(String id, String fodaItemValue, String fodaItemTypeId, String athleteId, Date date) {
        this.id = id;
        this.fodaItemValue = fodaItemValue;
        this.fodaItemTypeId = fodaItemTypeId;
        this.athleteId = athleteId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFodaItemValue() {
        return fodaItemValue;
    }

    public void setFodaItemValue(String fodaItemValue) {
        this.fodaItemValue = fodaItemValue;
    }

    public String getFodaItemTypeId() {
        return fodaItemTypeId;
    }

    public void setFodaItemTypeId(String fodaItemTypeId) {
        this.fodaItemTypeId = fodaItemTypeId;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
