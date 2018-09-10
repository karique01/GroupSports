package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 10/09/2018.
 */

public class QuizQuestionByAthlete {
    private String id;
    private String quizQuestionId;
    private String answer;
    private String athleteQuizId;
    private String questionTitle;
    private boolean available = true;

    public JSONObject toJSONJsonObject (){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("quizQuestionId",quizQuestionId);
            jsonObject.put("answer",answer);
            jsonObject.put("athleteQuizId",athleteQuizId);
            jsonObject.put("available",available);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public QuizQuestionByAthlete() {
    }

    public QuizQuestionByAthlete(String id, String quizQuestionId, String answer, String athleteQuizId, boolean available) {
        this.id = id;
        this.quizQuestionId = quizQuestionId;
        this.answer = answer;
        this.athleteQuizId = athleteQuizId;
        this.available = available;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizQuestionId() {
        return quizQuestionId;
    }

    public void setQuizQuestionId(String quizQuestionId) {
        this.quizQuestionId = quizQuestionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAthleteQuizId() {
        return athleteQuizId;
    }

    public void setAthleteQuizId(String athleteQuizId) {
        this.athleteQuizId = athleteQuizId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
