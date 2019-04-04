package pe.edu.karique.groupsports.models;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by karique on 24/08/2018.
 */

public class Mesocycle {

    private String id;
    private String trainingPlanId;
    private String numberOfIntenseWeeks;
    private String numberOfRestWeeks;
    private Date startDate;
    private Date endDate;
    private String mesocycleTypeId;
    private String mesocycleName;

    public Mesocycle() {
    }

    public Mesocycle(String id, String trainingPlanId, String numberOfIntenseWeeks, String numberOfRestWeeks, Date startDate, Date endDate, String mesocycleTypeId, String mesocycleName) {
        this.id = id;
        this.trainingPlanId = trainingPlanId;
        this.numberOfIntenseWeeks = numberOfIntenseWeeks;
        this.numberOfRestWeeks = numberOfRestWeeks;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mesocycleTypeId = mesocycleTypeId;
        this.mesocycleName = mesocycleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrainingPlanId() {
        return trainingPlanId;
    }

    public void setTrainingPlanId(String trainingPlanId) {
        this.trainingPlanId = trainingPlanId;
    }

    public String getNumberOfIntenseWeeks() {
        return numberOfIntenseWeeks;
    }

    public void setNumberOfIntenseWeeks(String numberOfIntenseWeeks) {
        this.numberOfIntenseWeeks = numberOfIntenseWeeks;
    }

    public String getNumberOfRestWeeks() {
        return numberOfRestWeeks;
    }

    public void setNumberOfRestWeeks(String numberOfRestWeeks) {
        this.numberOfRestWeeks = numberOfRestWeeks;
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

    public String getMesocycleTypeId() {
        return mesocycleTypeId;
    }

    public void setMesocycleTypeId(String mesocycleTypeId) {
        this.mesocycleTypeId = mesocycleTypeId;
    }

    public String getMesocycleName() {
        return mesocycleName;
    }

    public void setMesocycleName(String mesocycleName) {
        this.mesocycleName = mesocycleName;
    }

    public Bundle toBundle(){
        Bundle mesocycleBundle = new Bundle();
        mesocycleBundle.putString("id",id);
        mesocycleBundle.putString("trainingPlanId",trainingPlanId);
        mesocycleBundle.putString("numberOfIntenseWeeks",numberOfIntenseWeeks);
        mesocycleBundle.putString("numberOfRestWeeks",numberOfRestWeeks);
        mesocycleBundle.putString("mesocycleTypeId",mesocycleTypeId);
        mesocycleBundle.putString("mesocycleName",mesocycleName);
        mesocycleBundle.putSerializable("startDate",startDate);
        mesocycleBundle.putSerializable("endDate",endDate);
        return mesocycleBundle;
    }

    public static Mesocycle from(Bundle bundle){
        Mesocycle mesocycle = new Mesocycle();
        mesocycle.setId(bundle.getString("id"));
        mesocycle.setTrainingPlanId(bundle.getString("trainingPlanId"));
        mesocycle.setNumberOfIntenseWeeks(bundle.getString("numberOfIntenseWeeks"));
        mesocycle.setNumberOfRestWeeks(bundle.getString("numberOfRestWeeks"));
        mesocycle.setMesocycleTypeId(bundle.getString("mesocycleTypeId"));
        mesocycle.setMesocycleName(bundle.getString("mesocycleName"));
        mesocycle.setStartDate((Date)bundle.getSerializable("startDate"));
        mesocycle.setEndDate((Date)bundle.getSerializable("endDate"));
        return mesocycle;
    }

    public static Mesocycle toMesocycle(JSONObject jsonObject){
        Mesocycle mesocycle = new Mesocycle();
        try {
            mesocycle.setId(jsonObject.getString("id"));
            mesocycle.setTrainingPlanId(jsonObject.getString("trainingPlanId"));
            mesocycle.setNumberOfIntenseWeeks(jsonObject.getString("numberOfIntenseWeeks"));
            mesocycle.setNumberOfRestWeeks(jsonObject.getString("numberOfRestWeeks"));
            mesocycle.setMesocycleTypeId(jsonObject.getString("mesocycleTypeId"));
            JSONObject jsonObjectMCType = jsonObject.getJSONObject("mesocycleType");
            mesocycle.setMesocycleName(jsonObjectMCType.getString("mesocycleName"));

            try {
                String startDateString = jsonObject.getString("startDate");
                String endDateString = jsonObject.getString("endDate");
                Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);
                mesocycle.setStartDate(startDate);
                mesocycle.setEndDate(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mesocycle;
    }

}
