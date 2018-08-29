package pe.edu.upc.groupsports.models;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by karique on 25/08/2018.
 */

public class Week {
    private String id;
    private String weekTypeId;
    private Date   startDate;
    private Date   endDate;
    private String mesocycleId;
    private String activity;
    private String description;

    public Bundle toBundle(){
        Bundle weekBundle = new Bundle();
        weekBundle.putString("id",id);
        weekBundle.putString("weekTypeId",weekTypeId);
        weekBundle.putSerializable("startDate",startDate);
        weekBundle.putSerializable("endDate",endDate);
        weekBundle.putString("mesocycleId",mesocycleId);
        weekBundle.putString("activity",activity);
        weekBundle.putString("description",description);
        return weekBundle;
    }

    public static Week from(Bundle bundle){
        Week week = new Week();
        week.setId(bundle.getString("id"));
        week.setWeekTypeId(bundle.getString("weekTypeId"));
        week.setStartDate((Date)bundle.getSerializable("startDate"));
        week.setEndDate((Date)bundle.getSerializable("endDate"));
        week.setMesocycleId(bundle.getString("mesocycleId"));
        week.setActivity(bundle.getString("activity"));
        week.setDescription(bundle.getString("description"));
        return week;
    }

    public static Week toWeek(JSONObject jsonObject){
        Week week = new Week();
        try {
            week.setId(jsonObject.getString("id"));
            week.setWeekTypeId(jsonObject.getString("weekTypeId"));

            String startDateString = jsonObject.getString("startDate");
            String endDateString = jsonObject.getString("endDate");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);
                week.setStartDate(startDate);
                week.setEndDate(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            week.setMesocycleId(jsonObject.getString("mesocycleId"));
            week.setActivity(jsonObject.getString("activity"));

            JSONObject weekTypeJsonObject = jsonObject.getJSONObject("weekType");
            week.setDescription(weekTypeJsonObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return week;
    }

    public Week() {
    }

    public Week(String id, String weekTypeId, Date startDate, Date endDate, String mesocycleId, String activity, String description) {
        this.id = id;
        this.weekTypeId = weekTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mesocycleId = mesocycleId;
        this.activity = activity;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeekTypeId() {
        return weekTypeId;
    }

    public void setWeekTypeId(String weekTypeId) {
        this.weekTypeId = weekTypeId;
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

    public String getMesocycleId() {
        return mesocycleId;
    }

    public void setMesocycleId(String mesocycleId) {
        this.mesocycleId = mesocycleId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}























