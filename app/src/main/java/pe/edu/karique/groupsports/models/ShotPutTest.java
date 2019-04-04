package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 14/09/2018.
 */

public class ShotPutTest {
    private String id;
    private String result;
    private String ballWeight;
    private String shotPutTypeId;
    private String sessionId;
    private String coachId;
    private String athleteId;
    private Date date;
    private String type;

    public static ShotPutTest toShotPutTest(JSONObject jsonObject){
        ShotPutTest shotPutTest = new ShotPutTest();

        try {
            shotPutTest.setId(jsonObject.getString("id"));
            shotPutTest.setResult(jsonObject.getString("result"));
            shotPutTest.setBallWeight(jsonObject.getString("ballWeight"));
            shotPutTest.setShotPutTypeId(jsonObject.getString("shotPutTypeId"));
            shotPutTest.setCoachId(jsonObject.getString("coachId"));
            shotPutTest.setAthleteId(jsonObject.getString("athleteId"));
            shotPutTest.setDate(Funciones.getDateFromString(jsonObject.getString("date")));

            JSONObject typeJsonObject = jsonObject.getJSONObject("shotPutType");
            shotPutTest.setType(typeJsonObject.getString("type"));

            shotPutTest.setSessionId(jsonObject.getString("sessionId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return shotPutTest;
    }

    public ShotPutTest() {
    }

    public ShotPutTest(String id, String result, String ballWeight, String shotPutTypeId, String sessionId, String coachId, String athleteId, Date date, String type) {
        this.id = id;
        this.result = result;
        this.ballWeight = ballWeight;
        this.shotPutTypeId = shotPutTypeId;
        this.sessionId = sessionId;
        this.coachId = coachId;
        this.athleteId = athleteId;
        this.date = date;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBallWeight() {
        return ballWeight;
    }

    public void setBallWeight(String ballWeight) {
        this.ballWeight = ballWeight;
    }

    public String getShotPutTypeId() {
        return shotPutTypeId;
    }

    public void setShotPutTypeId(String shotPutTypeId) {
        this.shotPutTypeId = shotPutTypeId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
