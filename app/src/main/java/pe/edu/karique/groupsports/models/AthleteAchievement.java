package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 24/09/2018.
 */

public class AthleteAchievement {
    private String id;
    private String place;
    private String description;
    private String athleteId;
    private String athleteAchievementTypeId;
    private String resultTime;
    private String resultDistance;

    public static AthleteAchievement toAthleteAchievement(JSONObject jsonObject){
        AthleteAchievement athleteAchievement = new AthleteAchievement();
        try {
            athleteAchievement.setId(jsonObject.getString("id"));
            athleteAchievement.setPlace(jsonObject.getString("place"));
            athleteAchievement.setDescription(jsonObject.getString("description"));
            athleteAchievement.setAthleteId(jsonObject.getString("athleteId"));
            athleteAchievement.setAthleteAchievementTypeId(jsonObject.getString("athleteAchievementTypeId"));

            if (!jsonObject.isNull("resultTime"))
                athleteAchievement.setResultTime(jsonObject.getString("resultTime"));
            if (!jsonObject.isNull("resultDistance"))
                athleteAchievement.setResultDistance(jsonObject.getString("resultDistance"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return athleteAchievement;
    }

    public AthleteAchievement() {
    }

    public AthleteAchievement(String id, String place, String description, String athleteId, String athleteAchievementTypeId, String resultTime, String resultDistance) {
        this.id = id;
        this.place = place;
        this.description = description;
        this.athleteId = athleteId;
        this.athleteAchievementTypeId = athleteAchievementTypeId;
        this.resultTime = resultTime;
        this.resultDistance = resultDistance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getAthleteAchievementTypeId() {
        return athleteAchievementTypeId;
    }

    public void setAthleteAchievementTypeId(String athleteAchievementTypeId) {
        this.athleteAchievementTypeId = athleteAchievementTypeId;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getResultDistance() {
        return resultDistance;
    }

    public void setResultDistance(String resultDistance) {
        this.resultDistance = resultDistance;
    }
}
