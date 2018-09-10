package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 8/09/2018.
 */

public class AthleteQuiz {
    private String id;
    private String athleteId;
    private String quizId;
    private String answeredDate;
    private boolean available;

    public JSONObject toJSONObject(){
        JSONObject athleteQuizJsonObject = new JSONObject();
        try {
            athleteQuizJsonObject.put("id",id);
            athleteQuizJsonObject.put("athleteId",athleteId);
            athleteQuizJsonObject.put("quizId",quizId);
            athleteQuizJsonObject.put("answeredDate",answeredDate);
            athleteQuizJsonObject.put("available",available);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return athleteQuizJsonObject;
    }

    public AthleteQuiz() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getAnsweredDate() {
        return answeredDate;
    }

    public void setAnsweredDate(String answeredDate) {
        this.answeredDate = answeredDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
