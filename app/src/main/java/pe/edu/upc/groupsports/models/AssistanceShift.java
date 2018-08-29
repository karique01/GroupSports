package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.groupsports.util.Constantes;

/**
 * Created by karique on 21/08/2018.
 */

public class AssistanceShift {
    private String sessionId;
    private String shiftId;
    private String shiftName;
    private Double intensityPercentage;
    private boolean attendance;

    public AssistanceShift() {
    }

    public AssistanceShift(String sessionId, String shiftId, String shiftName, Double intensityPercentage, boolean attendance) {
        this.sessionId = sessionId;
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.intensityPercentage = intensityPercentage;
        this.attendance = attendance;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Double getIntensityPercentage() {
        return intensityPercentage;
    }

    public void setIntensityPercentage(Double intensityPercentage) {
        this.intensityPercentage = intensityPercentage;
    }

    public boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }

    public static String getShiftNameFrom(AssistanceShift assistanceShift){
        return assistanceShift.getShiftId().equals(Constantes.SHIFT_ID_MAÑANA_STR) ? Constantes.SHIFT_MAÑANA_STRING.substring(0,1) :
                assistanceShift.getShiftId().equals(Constantes.SHIFT_ID_TARDE_STR) ? Constantes.SHIFT_TARDE_STRING.substring(0,1) :
                        Constantes.SHIFT_NOCHE_STRING.substring(0,1);
    }

    public static AssistanceShift toAssistanceShift(JSONObject jsonObject){
        AssistanceShift assistanceShift = new AssistanceShift();

        try {
            assistanceShift.setSessionId(jsonObject.getString("sessionId"));
            assistanceShift.setShiftId(jsonObject.getString("shiftId"));
            assistanceShift.setShiftName(jsonObject.getString("shiftName"));
            assistanceShift.setIntensityPercentage(jsonObject.getDouble("intensityPercentage"));
            assistanceShift.setAttendance(jsonObject.getBoolean("attendance"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return assistanceShift;
    }

    public JSONObject toJSONAssistanceShift(){
        try {
            return new JSONObject()
                    .put("sessionId",sessionId)
                    .put("shiftId",shiftId)
                    .put("shiftName",shiftName)
                    .put("intensityPercentage",intensityPercentage)
                    .put("attendance",attendance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
