package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 15/09/2018.
 */

public class AntropometricTest {
    private String id;
    private String size;
    private String weight;
    private String wingspan;
    private String bodyFatPercentage;
    private String leanBodyPercentage;
    private String athleteId;
    private String coachId;
    private Date date;

    public static AntropometricTest toAntropometricTest(JSONObject jsonObject){
        AntropometricTest antropometricTest = new AntropometricTest();

        try {
            antropometricTest.setId(jsonObject.getString("id"));
            antropometricTest.setSize(jsonObject.getString("size"));
            antropometricTest.setWeight(jsonObject.getString("weight"));
            antropometricTest.setWingspan(jsonObject.getString("wingspan"));
            antropometricTest.setBodyFatPercentage(jsonObject.getString("bodyFatPercentage"));
            antropometricTest.setLeanBodyPercentage(jsonObject.getString("leanBodyPercentage"));
            antropometricTest.setAthleteId(jsonObject.getString("athleteId"));
            antropometricTest.setCoachId(jsonObject.getString("coachId"));
            antropometricTest.setDate(Funciones.getDateFromString(jsonObject.getString("date")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return antropometricTest;
    }

    public AntropometricTest() {
    }

    public AntropometricTest(String id, String size, String weight, String wingspan, String bodyFatPercentage, String leanBodyPercentage, String athleteId, String coachId, Date date) {
        this.id = id;
        this.size = size;
        this.weight = weight;
        this.wingspan = wingspan;
        this.bodyFatPercentage = bodyFatPercentage;
        this.leanBodyPercentage = leanBodyPercentage;
        this.athleteId = athleteId;
        this.coachId = coachId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWingspan() {
        return wingspan;
    }

    public void setWingspan(String wingspan) {
        this.wingspan = wingspan;
    }

    public String getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(String bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public String getLeanBodyPercentage() {
        return leanBodyPercentage;
    }

    public void setLeanBodyPercentage(String leanBodyPercentage) {
        this.leanBodyPercentage = leanBodyPercentage;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
