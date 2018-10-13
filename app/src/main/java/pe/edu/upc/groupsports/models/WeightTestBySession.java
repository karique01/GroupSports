package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 15/09/2018.
 */

public class WeightTestBySession {
    private String id;
    private String weightBefore;
    private String weightAfter;
    private String sessionId;
    private String shiftName;
    private Date   sessionDay;

    public static WeightTestBySession toWeightTestBySession(JSONObject weightJsonObject){
        WeightTestBySession weightTestBySession = new WeightTestBySession();
        try {
            weightTestBySession.setId(weightJsonObject.getString("id"));
            if (!weightJsonObject.isNull("weightBefore")) {
                weightTestBySession.setWeightBefore(weightJsonObject.getString("weightBefore"));
            }
            if (!weightJsonObject.isNull("weightAfter")) {
                weightTestBySession.setWeightAfter(weightJsonObject.getString("weightAfter"));
            }

            JSONObject sessionJsonObject = weightJsonObject.getJSONObject("session");
            weightTestBySession.setSessionId(sessionJsonObject.getString("id"));

            JSONObject shiftJsonObject = sessionJsonObject.getJSONObject("shift");
            weightTestBySession.setShiftName(shiftJsonObject.getString("shiftName"));
            weightTestBySession.setSessionDay(Funciones.getDateFromString(sessionJsonObject.getString("sessionDay")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weightTestBySession;
    }

    public WeightTestBySession() {
    }

    public WeightTestBySession(String id, String weightBefore, String weightAfter, String sessionId, String shiftName, Date sessionDay) {
        this.id = id;
        this.weightBefore = weightBefore;
        this.weightAfter = weightAfter;
        this.sessionId = sessionId;
        this.shiftName = shiftName;
        this.sessionDay = sessionDay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeightBefore() {
        return weightBefore == null ? "" : weightBefore;
    }

    public void setWeightBefore(String weightBefore) {
        this.weightBefore = weightBefore;
    }

    public String getWeightAfter() {
        return weightAfter == null ? "" : weightAfter;
    }

    public void setWeightAfter(String weightAfter) {
        this.weightAfter = weightAfter;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Date getSessionDay() {
        return sessionDay;
    }

    public void setSessionDay(Date sessionDay) {
        this.sessionDay = sessionDay;
    }
}
