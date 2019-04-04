package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.edu.karique.groupsports.R;

/**
 * Created by karique on 2/09/2018.
 */

public class Mood {
    private String id;
    private String colorOfMoodId;
    private Date   dayOfMood;
    private String athleteId;
    private String coachId;
    private String descripcion;
    private boolean selected = false;

    public int getCardDrawableResource(){
        return colorOfMoodId.equals("1") ? R.drawable.ic_card_choosed_red :
                colorOfMoodId.equals("2") ? R.drawable.ic_card_choosed_yellow :
                colorOfMoodId.equals("3") ? R.drawable.ic_card_choosed_blue :
                colorOfMoodId.equals("4") ? R.drawable.ic_card_choosed_black :
                                            R.drawable.ic_card_choosed_green;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static Mood toMood(JSONObject moodJsonObject){
        Mood mood = new Mood();
        try {
            mood.setId(moodJsonObject.getString("id"));
            mood.setColorOfMoodId(moodJsonObject.getString("colorOfMoodId"));
            mood.setAthleteId(moodJsonObject.getString("athleteId"));
            mood.setCoachId(moodJsonObject.getString("coachId"));

            String dayOfMood = moodJsonObject.getString("dayOfMood");
            Date dayOfMoodDate = null;
            try {
                dayOfMoodDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dayOfMood);
                mood.setDayOfMood(dayOfMoodDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            JSONObject colorOfMoodJSONJsonObject = moodJsonObject.getJSONObject("colorOfMood");
            if (colorOfMoodJSONJsonObject != null)
                mood.setDescripcion(colorOfMoodJSONJsonObject.getString("descripcion"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mood;
    }

    public Mood() {
    }

    public Mood(String id, String colorOfMoodId, Date dayOfMood, String athleteId, String coachId, String descripcion) {
        this.id = id;
        this.colorOfMoodId = colorOfMoodId;
        this.dayOfMood = dayOfMood;
        this.athleteId = athleteId;
        this.coachId = coachId;
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColorOfMoodId() {
        return colorOfMoodId;
    }

    public void setColorOfMoodId(String colorOfMoodId) {
        this.colorOfMoodId = colorOfMoodId;
    }

    public Date getDayOfMood() {
        return dayOfMood;
    }

    public void setDayOfMood(Date dayOfMood) {
        this.dayOfMood = dayOfMood;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
