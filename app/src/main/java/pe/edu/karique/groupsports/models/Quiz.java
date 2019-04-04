package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 8/09/2018.
 */

public class Quiz {
    private String id;
    private String coachId;
    private String quizTitle;
    private String date;
    private boolean available;

    public JSONObject toJSONObject(){
        JSONObject quizJsonObject = new JSONObject();
        try {
            quizJsonObject.put("id",id);
            quizJsonObject.put("coachId",coachId);
            quizJsonObject.put("quizTitle",quizTitle);
            quizJsonObject.put("date",date);
            quizJsonObject.put("available",available);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return quizJsonObject;
    }

    public Quiz() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
