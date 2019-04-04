package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 17/09/2018.
 */

public class StrengthTest {
    private String id;
    private String maxRepetitionWeightValue;
    private String description;
    private String strengthTestTypeId;
    private String coachId;
    private String athleteId;
    private Date date;

    public static StrengthTest toStrengthTest(JSONObject jsonObject){
        StrengthTest strengthTest = new StrengthTest();
        try {
            strengthTest.setId(jsonObject.getString("id"));
            strengthTest.setMaxRepetitionWeightValue(jsonObject.getString("maxRepetitionWeightValue"));

            JSONObject strengthTestTypeJsonObject = jsonObject.getJSONObject("strengthTestType");
            strengthTest.setDescription(strengthTestTypeJsonObject.getString("description"));

            strengthTest.setStrengthTestTypeId(jsonObject.getString("strengthTestTypeId"));
            strengthTest.setCoachId(jsonObject.getString("coachId"));
            strengthTest.setAthleteId(jsonObject.getString("athleteId"));
            strengthTest.setDate(Funciones.getDateFromString(jsonObject.getString("date")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return strengthTest;
    }

    public StrengthTest() {
    }

    public StrengthTest(String id, String maxRepetitionWeightValue, String description, String strengthTestTypeId, String coachId, String athleteId, Date date) {
        this.id = id;
        this.maxRepetitionWeightValue = maxRepetitionWeightValue;
        this.description = description;
        this.strengthTestTypeId = strengthTestTypeId;
        this.coachId = coachId;
        this.athleteId = athleteId;
        this.date = date;
    }

    public int getDayOfYear(){
        return Funciones.getDayOfYearFromDate(getDate()) + 1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaxRepetitionWeightValue() {
        return maxRepetitionWeightValue;
    }

    public void setMaxRepetitionWeightValue(String maxRepetitionWeightValue) {
        this.maxRepetitionWeightValue = maxRepetitionWeightValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStrengthTestTypeId() {
        return strengthTestTypeId;
    }

    public void setStrengthTestTypeId(String strengthTestTypeId) {
        this.strengthTestTypeId = strengthTestTypeId;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
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
