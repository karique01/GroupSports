package pe.edu.upc.groupsports.models;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by karique on 26/08/2018.
 */

public class SessionWork {
    private String athleteId;
    private String id;
    private String weekId;
    private Date sessionDay;
    private String coachId;
    private String shiftName;
    private boolean attendance;
    private double intensityPercentage;
    private String shiftId;
    private String username;
    private String pictureUrl;
    private String trainingPlanName;
    private String mesocycleName;
    private List<BinnacleDetail> binnacleDetails;

//    public static SessionWork toSessionWork(JSONObject jsonObject){
//        SessionWork sessionWork = new SessionWork();
//        try {
//            sessionWork.setId(jsonObject.getString("id"));
//            sessionWork.setShiftId(jsonObject.getString("shiftId"));
//            sessionWork.setWeekId(jsonObject.getString("weekId"));
//            sessionWork.setCoachId(jsonObject.getString("coachId"));
//            sessionWork.setAthleteId(jsonObject.getString("athleteId"));
//            sessionWork.setAttendance(jsonObject.getBoolean("attendance"));
//            JSONObject shiftJsonObject = jsonObject.getJSONObject("shift");
//            sessionWork.setShiftName(shiftJsonObject.getString("shiftName"));
//
//            String sessionDayString = jsonObject.getString("sessionDay");
//            Date sessionDayy = null;
//            try {
//                sessionDayy = new SimpleDateFormat("yyyy-MM-dd").parse(sessionDayString);
//                sessionWork.setSessionDay(sessionDayy);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            sessionWork.setIntensityPercentage(jsonObject.getDouble("intensityPercentage"));
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return sessionWork;
//    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("athleteId",athleteId);
        bundle.putString("id",id);
        bundle.putString("weekId",weekId);
        bundle.putSerializable("sessionDay",sessionDay);
        bundle.putString("coachId",coachId);
        bundle.putString("shiftName",shiftName);
        bundle.putBoolean("attendance",attendance);
        bundle.putDouble("intensityPercentage",intensityPercentage);
        bundle.putString("shiftId",shiftId);
        bundle.putString("username",username);
        bundle.putString("pictureUrl",pictureUrl);
        bundle.putString("trainingPlanName",trainingPlanName);
        bundle.putString("mesocycleName",mesocycleName);
        return bundle;
    }

    public static SessionWork from(Bundle bundle){
        SessionWork sessionWork = new SessionWork();
        sessionWork.setAthleteId(bundle.getString("athleteId"));
        sessionWork.setId(bundle.getString("id"));
        sessionWork.setWeekId(bundle.getString("weekId"));
        sessionWork.setSessionDay((Date)bundle.getSerializable("sessionDay"));
        sessionWork.setCoachId(bundle.getString("coachId"));
        sessionWork.setShiftName(bundle.getString("shiftName"));
        sessionWork.setAttendance(bundle.getBoolean("attendance"));
        sessionWork.setIntensityPercentage(bundle.getDouble("intensityPercentage"));
        sessionWork.setShiftId(bundle.getString("shiftId"));
        sessionWork.setUsername(bundle.getString("username"));
        sessionWork.setPictureUrl(bundle.getString("pictureUrl"));
        sessionWork.setTrainingPlanName(bundle.getString("trainingPlanName"));
        sessionWork.setMesocycleName(bundle.getString("mesocycleName"));

        return sessionWork;
    }

    public static SessionWork toSessionWorkForBinnacle(JSONObject jsonObject){
        SessionWork sessionWork = new SessionWork();
        try {
            sessionWork.setId(jsonObject.getString("id"));
            sessionWork.setShiftId(jsonObject.getString("shiftId"));
            sessionWork.setWeekId(jsonObject.getString("weekId"));
            sessionWork.setCoachId(jsonObject.getString("coachId"));
            sessionWork.setAthleteId(jsonObject.getString("athleteId"));
            sessionWork.setAttendance(jsonObject.getBoolean("attendance"));
            sessionWork.setPictureUrl(jsonObject.getString("pictureURL"));
            sessionWork.setUsername(jsonObject.getString("username"));
            sessionWork.setTrainingPlanName(jsonObject.getString("trainingPlanName"));
            sessionWork.setMesocycleName(jsonObject.getString("mesocycleName"));

            JSONObject shiftJsonObject = jsonObject.getJSONObject("shift");
            sessionWork.setShiftName(shiftJsonObject.getString("shiftName"));

            //entradas de la bitacora
            JSONArray jsonArrayBinnacle = jsonObject.getJSONArray("binnacleDetail");
            List<BinnacleDetail> binnacleDetails = new ArrayList<>();

            for (int i = 0; i < jsonArrayBinnacle.length(); i++) {
                binnacleDetails.add(BinnacleDetail.toBinnacleDetail(jsonArrayBinnacle.getJSONObject(i)));
            }
            sessionWork.setBinnacleDetails(binnacleDetails);

            String sessionDayString = jsonObject.getString("sessionDay");
            Date sessionDayy = null;
            try {
                sessionDayy = new SimpleDateFormat("yyyy-MM-dd").parse(sessionDayString);
                sessionWork.setSessionDay(sessionDayy);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            sessionWork.setIntensityPercentage(jsonObject.getDouble("intensityPercentage"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sessionWork;
    }

    public List<BinnacleDetail> getBinnacleDetails() {
        return binnacleDetails;
    }

    public void setBinnacleDetails(List<BinnacleDetail> binnacleDetails) {
        this.binnacleDetails = binnacleDetails;
    }

    public String getTrainingPlanName() {
        return trainingPlanName;
    }

    public void setTrainingPlanName(String trainingPlanName) {
        this.trainingPlanName = trainingPlanName;
    }

    public String getMesocycleName() {
        return mesocycleName;
    }

    public void setMesocycleName(String mesocycleName) {
        this.mesocycleName = mesocycleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getAthleteId ()
    {
        return athleteId;
    }

    public void setAthleteId (String athleteId)
    {
        this.athleteId = athleteId;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getWeekId ()
    {
        return weekId;
    }

    public void setWeekId (String weekId)
    {
        this.weekId = weekId;
    }

    public Date getSessionDay ()
    {
        return sessionDay;
    }

    public void setSessionDay (Date sessionDay)
    {
        this.sessionDay = sessionDay;
    }

    public String getCoachId ()
    {
        return coachId;
    }

    public void setCoachId (String coachId)
    {
        this.coachId = coachId;
    }

    public String getShiftName ()
    {
        return shiftName;
    }

    public void setShiftName (String shiftName)
    {
        this.shiftName = shiftName;
    }

    public boolean getAttendance ()
    {
        return attendance;
    }

    public void setAttendance (boolean attendance)
    {
        this.attendance = attendance;
    }

    public double getIntensityPercentage ()
    {
        return intensityPercentage;
    }

    public void setIntensityPercentage (double intensityPercentage)
    {
        this.intensityPercentage = intensityPercentage;
    }

    public String getShiftId ()
    {
        return shiftId;
    }

    public void setShiftId (String shiftId)
    {
        this.shiftId = shiftId;
    }
}
