package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 17/09/2018.
 */

public class JumpTest {
    private String id;
    private String distanceResult;
    private String jumpTestTypeId;
    private String description;
    private String coachId;
    private String athleteId;
    private Date   date;

    public static JumpTest toJumpTest(JSONObject jsonObject){
        JumpTest jumpTest = new JumpTest();

        try {
            jumpTest.setId(jsonObject.getString("id"));
            jumpTest.setDistanceResult(jsonObject.getString("distanceResult"));
            jumpTest.setJumpTestTypeId(jsonObject.getString("jumpTestTypeId"));

            JSONObject jumpTestTypeJsonObject = jsonObject.getJSONObject("jumpTestType");
            jumpTest.setDescription(jumpTestTypeJsonObject.getString("description"));

            jumpTest.setCoachId(jsonObject.getString("coachId"));
            jumpTest.setAthleteId(jsonObject.getString("athleteId"));
            jumpTest.setDate(Funciones.getDateFromString(jsonObject.getString("date")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jumpTest;
    }

    public JumpTest() {
    }

    public JumpTest(String id, String distanceResult, String jumpTestTypeId, String description, String coachId, String athleteId, Date date) {
        this.id = id;
        this.distanceResult = distanceResult;
        this.jumpTestTypeId = jumpTestTypeId;
        this.description = description;
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

    public String getDistanceResult() {
        return distanceResult;
    }

    public void setDistanceResult(String distanceResult) {
        this.distanceResult = distanceResult;
    }

    public String getJumpTestTypeId() {
        return jumpTestTypeId;
    }

    public void setJumpTestTypeId(String jumpTestTypeId) {
        this.jumpTestTypeId = jumpTestTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
