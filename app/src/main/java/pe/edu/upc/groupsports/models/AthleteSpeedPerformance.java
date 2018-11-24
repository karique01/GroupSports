package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 24/09/2018.
 */

public class AthleteSpeedPerformance {
    private String athleteId;
    private String athleteName;
    private String athletePictureUrl;
    private String username;
    private int averageHours;
    private int averageMinutes;
    private int averageSeconds;
    private int averageMilliseconds;
    private int bestMarkHour;
    private int bestMarkMinute;
    private int bestMarkSecond;
    private int bestMarkMillisecond;
    private int age;

    public static AthleteSpeedPerformance toAthleteSpeedPerformance(JSONObject jsonObject){
        AthleteSpeedPerformance athleteSpeedPerformance = new AthleteSpeedPerformance();
        try {
            athleteSpeedPerformance.setAthleteId(jsonObject.getString("athleteId"));
            athleteSpeedPerformance.setAthleteName(jsonObject.getString("athleteName"));
            athleteSpeedPerformance.setAthletePictureUrl(jsonObject.getString("athletePictureUrl"));
            athleteSpeedPerformance.setUsername(jsonObject.getString("username"));
            athleteSpeedPerformance.setAverageHours(jsonObject.getInt("averageHours"));
            athleteSpeedPerformance.setAverageMinutes(jsonObject.getInt("averageMinutes"));
            athleteSpeedPerformance.setAverageSeconds(jsonObject.getInt("averageSeconds"));
            athleteSpeedPerformance.setAverageMilliseconds(jsonObject.getInt("averageMilliseconds"));
            athleteSpeedPerformance.setBestMarkHour(jsonObject.getInt("bestMarkHour"));
            athleteSpeedPerformance.setBestMarkMinute(jsonObject.getInt("bestMarkMinute"));
            athleteSpeedPerformance.setBestMarkSecond(jsonObject.getInt("bestMarkSecond"));
            athleteSpeedPerformance.setBestMarkMillisecond(jsonObject.getInt("bestMarkMillisecond"));
            athleteSpeedPerformance.setAge(jsonObject.getInt("age"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return athleteSpeedPerformance;
    }

    public float getAverageResultFloat() {
        return (averageHours*3600) +
                (averageMinutes*60) +
                averageSeconds +
                (averageMilliseconds < 100 ? (averageMilliseconds*1.0f/100) : (averageMilliseconds*1.0f/1000));
    }

    public float getBestMArkResultFloat() {
        return (bestMarkHour*3600) +
                (bestMarkMinute*60) +
                bestMarkSecond +
                (bestMarkMillisecond < 100 ? (bestMarkMillisecond*1.0f/100) : (bestMarkMillisecond*1.0f/1000));
    }

    public AthleteSpeedPerformance() {
    }

    public AthleteSpeedPerformance(String athleteId, String athleteName, String athletePictureUrl, String username, int averageHours, int averageMinutes, int averageSeconds, int averageMilliseconds, int bestMarkHour, int bestMarkMinute, int bestMarkSecond, int bestMarkMillisecond, int age) {
        this.athleteId = athleteId;
        this.athleteName = athleteName;
        this.athletePictureUrl = athletePictureUrl;
        this.username = username;
        this.averageHours = averageHours;
        this.averageMinutes = averageMinutes;
        this.averageSeconds = averageSeconds;
        this.averageMilliseconds = averageMilliseconds;
        this.bestMarkHour = bestMarkHour;
        this.bestMarkMinute = bestMarkMinute;
        this.bestMarkSecond = bestMarkSecond;
        this.bestMarkMillisecond = bestMarkMillisecond;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getAverageHours() {
        return averageHours;
    }

    public void setAverageHours(int averageHours) {
        this.averageHours = averageHours;
    }

    public int getAverageMinutes() {
        return averageMinutes;
    }

    public void setAverageMinutes(int averageMinutes) {
        this.averageMinutes = averageMinutes;
    }

    public int getAverageSeconds() {
        return averageSeconds;
    }

    public void setAverageSeconds(int averageSeconds) {
        this.averageSeconds = averageSeconds;
    }

    public int getAverageMilliseconds() {
        return averageMilliseconds;
    }

    public void setAverageMilliseconds(int averageMilliseconds) {
        this.averageMilliseconds = averageMilliseconds;
    }

    public int getBestMarkHour() {
        return bestMarkHour;
    }

    public void setBestMarkHour(int bestMarkHour) {
        this.bestMarkHour = bestMarkHour;
    }

    public int getBestMarkMinute() {
        return bestMarkMinute;
    }

    public void setBestMarkMinute(int bestMarkMinute) {
        this.bestMarkMinute = bestMarkMinute;
    }

    public int getBestMarkSecond() {
        return bestMarkSecond;
    }

    public void setBestMarkSecond(int bestMarkSecond) {
        this.bestMarkSecond = bestMarkSecond;
    }

    public int getBestMarkMillisecond() {
        return bestMarkMillisecond;
    }

    public void setBestMarkMillisecond(int bestMarkMillisecond) {
        this.bestMarkMillisecond = bestMarkMillisecond;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
