package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 24/09/2018.
 */

public class AthleteJumpPerformance {
    private String athleteId;
    private String athleteName;
    private String athletePictureUrl;
    private String username;
    private int age;
    private float averageSaltability;
    private float bestJumpMark;

    public static AthleteJumpPerformance toAthleteJumpPerformance(JSONObject jsonObject){
        AthleteJumpPerformance athleteJumpPerformance = new AthleteJumpPerformance();
        try {
            athleteJumpPerformance.setAthleteId(jsonObject.getString("athleteId"));
            athleteJumpPerformance.setAthleteName(jsonObject.getString("athleteName"));
            athleteJumpPerformance.setAthletePictureUrl(jsonObject.getString("athletePictureUrl"));
            athleteJumpPerformance.setUsername(jsonObject.getString("username"));
            athleteJumpPerformance.setAge(jsonObject.getInt("age"));
            athleteJumpPerformance.setAverageSaltability((float)jsonObject.getDouble("averageSaltability"));
            athleteJumpPerformance.setBestJumpMark((float)jsonObject.getDouble("bestJumpMark"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return athleteJumpPerformance;
    }

    public AthleteJumpPerformance() {
    }

    public AthleteJumpPerformance(String athleteId, String athleteName, String athletePictureUrl, String username, int age, float averageSaltability, float bestJumpMark) {
        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.athletePictureUrl = athletePictureUrl;
        this.username = username;
        this.age = age;
        this.averageSaltability = averageSaltability;
        this.bestJumpMark = bestJumpMark;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getAthletePictureUrl() {
        return athletePictureUrl;
    }

    public void setAthletePictureUrl(String athletePictureUrl) {
        this.athletePictureUrl = athletePictureUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getAverageSaltability() {
        return averageSaltability;
    }

    public void setAverageSaltability(float averageSaltability) {
        this.averageSaltability = averageSaltability;
    }

    public float getBestJumpMark() {
        return bestJumpMark;
    }

    public void setBestJumpMark(float bestJumpMark) {
        this.bestJumpMark = bestJumpMark;
    }
}
