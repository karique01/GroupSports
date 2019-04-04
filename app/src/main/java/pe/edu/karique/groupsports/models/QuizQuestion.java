package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 8/09/2018.
 */

public class QuizQuestion {
    private String id;
    private String quizId;
    private String questionTitle;
    private boolean available;

    public JSONObject toJSONObject(){
        JSONObject quizQuestionJsonObject = new JSONObject();
        try {
            quizQuestionJsonObject.put("id",id);
            quizQuestionJsonObject.put("quizId",quizId);
            quizQuestionJsonObject.put("questionTitle",questionTitle);
            quizQuestionJsonObject.put("available",available);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return quizQuestionJsonObject;
    }

    public QuizQuestion() {
        questionTitle = "";
        available = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
