package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 28/08/2018.
 */

public class SpeedTest {
    private String id;
    private String result;
    private String meters;
    private String sessionId;
    private String coachId;
    private String athleteId;
    private boolean available;

    public static SpeedTest toSpeedTest(JSONObject jsonObject){
        SpeedTest speedTest = new SpeedTest();

        try {

            speedTest.setId(jsonObject.getString("id"));
            speedTest.setResult(jsonObject.getString("result"));
            speedTest.setMeters(jsonObject.getString("meters"));
            speedTest.setSessionId(jsonObject.getString("sessionId"));
            speedTest.setCoachId(jsonObject.getString("coachId"));
            speedTest.setAthleteId(jsonObject.getString("athleteId"));
            speedTest.setAvailable(jsonObject.getBoolean("available"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return speedTest;
    }

    public SpeedTest() {
    }

    public SpeedTest(String id, String result, String meters, String sessionId, String coachId, String athleteId, boolean available) {
        this.id = id;
        this.result = result;
        this.meters = meters;
        this.sessionId = sessionId;
        this.coachId = coachId;
        this.athleteId = athleteId;
        this.available = available;
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

    public String getMeters() {
        return meters;
    }

    public void setMeters(String meters) {
        this.meters = meters;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
