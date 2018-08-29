package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static SessionWork toSessionWork(JSONObject jsonObject){
        SessionWork sessionWork = new SessionWork();
        try {
            sessionWork.setId(jsonObject.getString("id"));
            sessionWork.setShiftId(jsonObject.getString("shiftId"));
            sessionWork.setWeekId(jsonObject.getString("weekId"));
            sessionWork.setCoachId(jsonObject.getString("coachId"));
            sessionWork.setAthleteId(jsonObject.getString("athleteId"));
            sessionWork.setAttendance(jsonObject.getBoolean("attendance"));
            JSONObject shiftJsonObject = jsonObject.getJSONObject("shift");
            sessionWork.setShiftName(shiftJsonObject.getString("shiftName"));

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
