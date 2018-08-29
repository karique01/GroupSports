package pe.edu.upc.groupsports.models;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 17/08/2018.
 */

public class TrainingPlan {
    String id;
    String name;
    Date   startDate;
    Date   endDate;
    String coachId;
    String athleteId;
    String athleteFullName;
    String athletePictureUrl;

    public TrainingPlan() {
    }

    public TrainingPlan(String id, String name, Date startDate, Date endDate, String coachId, String athleteId, String athleteFullName, String athletePictureUrl) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.coachId = coachId;
        this.athleteId = athleteId;
        this.athleteFullName = athleteFullName;
        this.athletePictureUrl = athletePictureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public String getAthleteFullName() {
        return athleteFullName;
    }

    public void setAthleteFullName(String athleteFullName) {
        this.athleteFullName = athleteFullName;
    }

    public String getAthletePictureUrl() {
        return athletePictureUrl;
    }

    public void setAthletePictureUrl(String athletePictureUrl) {
        this.athletePictureUrl = athletePictureUrl;
    }

    public static TrainingPlan toTrainingPlan(JSONObject jsonObject){
        TrainingPlan trainingPlan = new TrainingPlan();

        try {
            trainingPlan.setId(jsonObject.getString("id"));
            trainingPlan.setName(jsonObject.getString("name"));

            JSONObject atheleteJsonObject = jsonObject.getJSONObject("athelete");
            JSONObject userJsonObject = atheleteJsonObject.getJSONObject("user");

            trainingPlan.setAthleteFullName(
                    Funciones.capitalize(
                            userJsonObject.getString("firstName") + " " + userJsonObject.getString("lastName")
                    )
            );
            trainingPlan.setAthletePictureUrl(userJsonObject.getString("pictureUrl"));

            trainingPlan.setCoachId(jsonObject.getString("coachId"));
            trainingPlan.setAthleteId(jsonObject.getString("athleteId"));

            try {
                String startDateString = jsonObject.getString("startDate");
                String endDateString = jsonObject.getString("endDate");
                Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);
                trainingPlan.setStartDate(startDate);
                trainingPlan.setEndDate(endDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trainingPlan;
    }

    public Bundle toBundle(){
        Bundle tpBundle = new Bundle();
        tpBundle.putString("id",id);
        tpBundle.putString("name",name);
        tpBundle.putSerializable("startDate",startDate);
        tpBundle.putSerializable("endDate",endDate);
        tpBundle.putString("coachId",coachId);
        tpBundle.putString("athleteId",athleteId);
        tpBundle.putString("athleteFullName",athleteFullName);
        tpBundle.putString("athletePictureUrl",athletePictureUrl);
        return tpBundle;
    }

    public static TrainingPlan from(Bundle bundle){
        TrainingPlan trainingPlan = new TrainingPlan();
        trainingPlan.setId(bundle.getString("id"));
        trainingPlan.setName(bundle.getString("name"));
        trainingPlan.setStartDate((Date)bundle.getSerializable("startDate"));
        trainingPlan.setEndDate((Date)bundle.getSerializable("endDate"));
        trainingPlan.setCoachId(bundle.getString("coachId"));
        trainingPlan.setAthleteId(bundle.getString("athleteId"));
        trainingPlan.setAthleteFullName(bundle.getString("athleteFullName"));
        trainingPlan.setAthletePictureUrl(bundle.getString("athletePictureUrl"));
        return trainingPlan;
    }
}
