package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 28/08/2018.
 */

public class SpeedTest {
    private String id;
    private String meters;
    private String sessionId;
    private String coachId;
    private String athleteId;
    private String hours;
    private String minutes;
    private String seconds;
    private String milliseconds;
    private Date date;
    private boolean available;

    public static SpeedTest toSpeedTest(JSONObject jsonObject){
        SpeedTest speedTest = new SpeedTest();

        try {
            speedTest.setId(jsonObject.getString("id"));
            speedTest.setMeters(jsonObject.getString("meters"));
            speedTest.setSessionId(jsonObject.getString("sessionId"));
            speedTest.setCoachId(jsonObject.getString("coachId"));
            speedTest.setAthleteId(jsonObject.getString("athleteId"));
            speedTest.setDate(Funciones.getDateFromString(jsonObject.getString("date")));
            speedTest.setAvailable(jsonObject.getBoolean("available"));
            speedTest.setHours(jsonObject.getString("hours"));
            speedTest.setMinutes(jsonObject.getString("minutes"));
            speedTest.setSeconds(jsonObject.getString("seconds"));
            speedTest.setMilliseconds(jsonObject.getString("milliseconds"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return speedTest;
    }

    public SpeedTest() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getResultFloat() {
        float hour = Float.valueOf(hours);
        float min = Float.valueOf(minutes);
        float sec = Float.valueOf(seconds);
        float millisec = Float.valueOf(milliseconds);
        return (hour*3600) + (min*60) + sec + (millisec < 100 ? millisec/100 : millisec/1000);
    }

    public int getDayOfYear(){
        return Funciones.getDayOfYearFromDate(getDate()) + 1;
    }

    public String getResult(){
        return String.valueOf(getResultFloat());
    }

    public String getHours() {
        return hours;
    }

    public int getHoursInt() {
        return Integer.valueOf(hours);
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public int getMinutesInt() {
        return Integer.valueOf(minutes);
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSeconds() {
        return seconds;
    }

    public int getSecondsInt() {
        return Integer.valueOf(seconds);
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getMilliseconds() {
        return milliseconds;
    }

    public int getMillisecondsInt() {
        return Integer.valueOf(milliseconds);
    }

    public void setMilliseconds(String milliseconds) {
        this.milliseconds = milliseconds;
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
