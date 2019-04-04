package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 7/09/2018.
 */

public class QuizQuestions {
    private String id;
    private String quizId;
    private String questionTitle;
    private String answer;

    public static QuizQuestions toQuizQuestions(JSONObject jsonObject){
        QuizQuestions quizQuestions = new QuizQuestions();

        try {
            quizQuestions.setAnswer(jsonObject.getString("answer"));
            JSONObject quizQuestionJSON = jsonObject.getJSONObject("quizQuestion");

            quizQuestions.setId(quizQuestionJSON.getString("id"));
            quizQuestions.setQuizId(quizQuestionJSON.getString("quizId"));
            quizQuestions.setQuestionTitle(quizQuestionJSON.getString("questionTitle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return quizQuestions;
    }

    public static QuizQuestions toOnlyQuizQuestions(JSONObject jsonObject){
        QuizQuestions quizQuestions = new QuizQuestions();

        try {
            quizQuestions.setId(jsonObject.getString("id"));
            quizQuestions.setQuizId(jsonObject.getString("quizId"));
            quizQuestions.setQuestionTitle(jsonObject.getString("questionTitle"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return quizQuestions;
    }

    public QuizQuestions() {
    }

    public QuizQuestions(String id, String quizId, String questionTitle,String answer) {
        this.id = id;
        this.quizId = quizId;
        this.questionTitle = questionTitle;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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
}
