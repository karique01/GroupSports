package pe.edu.upc.groupsports.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 8/09/2018.
 */

public class AthletesQuestion {
    private String id;
    private String athleteId;
    private String quizId;
    private Date answeredDate;
    private String userName;
    private String pictureUrl;
    private String pictureUrlCoach;
    private String quizTitle;
    private Date date;
    private List<QuizQuestions> quizQuestions;
    private boolean selected;

    public static AthletesQuestion toAthletesQuestionToAthlete(JSONObject jsonObject){
        AthletesQuestion athletesQuestion = new AthletesQuestion();
        try {
            athletesQuestion.setId(jsonObject.getString("id"));
            athletesQuestion.setAthleteId(jsonObject.getString("athleteId"));
            athletesQuestion.setQuizId(jsonObject.getString("quizId"));
            athletesQuestion.setUserName(jsonObject.getString("userName"));
            athletesQuestion.setPictureUrl(jsonObject.getString("pictureUrl"));
            athletesQuestion.setPictureUrl(jsonObject.getString("pictureUrl"));
            athletesQuestion.setPictureUrlCoach(jsonObject.getString("pictureUrlCoach"));
            athletesQuestion.setQuizTitle(jsonObject.getString("quizTitle"));
            athletesQuestion.setDate(Funciones.getDateFromString(jsonObject.getString("date")));

            athletesQuestion.quizQuestions = new ArrayList<>();
            JSONArray quizQuestionByAthleteJsonArray = jsonObject.getJSONArray("quizQuestionByAthlete");
            for (int i = 0; i < quizQuestionByAthleteJsonArray.length(); i++) {
                athletesQuestion.quizQuestions.add(QuizQuestions.toQuizQuestions(quizQuestionByAthleteJsonArray.getJSONObject(i)));
            }

            if (athletesQuestion.quizQuestions.size() > 0) {
                athletesQuestion.setAnsweredDate(Funciones.getDateFromString(jsonObject.getString("answeredDate")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return athletesQuestion;
    }

    public static AthletesQuestion toAthletesQuestion(JSONObject jsonObject, List<QuizQuestions> quizQuestions){
        AthletesQuestion athletesQuestion = new AthletesQuestion();
        try {
            athletesQuestion.setId(jsonObject.getString("id"));
            athletesQuestion.setAthleteId(jsonObject.getString("athleteId"));
            athletesQuestion.setQuizId(jsonObject.getString("quizId"));
            athletesQuestion.setUserName(jsonObject.getString("userName"));
            athletesQuestion.setPictureUrl(jsonObject.getString("pictureUrl"));
            athletesQuestion.setQuizTitle(jsonObject.getString("quizTitle"));
            athletesQuestion.setDate(Funciones.getDateFromString(jsonObject.getString("date")));

            athletesQuestion.quizQuestions = new ArrayList<>();
            JSONArray quizQuestionByAthleteJsonArray = jsonObject.getJSONArray("quizQuestionByAthlete");
            for (int i = 0; i < quizQuestionByAthleteJsonArray.length(); i++) {
                athletesQuestion.quizQuestions.add(QuizQuestions.toQuizQuestions(quizQuestionByAthleteJsonArray.getJSONObject(i)));
            }

            if (athletesQuestion.quizQuestions.size() > 0)
                athletesQuestion.setAnsweredDate(Funciones.getDateFromString(jsonObject.getString("answeredDate")));
            else
                athletesQuestion.setQuizQuestions(quizQuestions);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return athletesQuestion;
    }

    public AthletesQuestion() {
    }

    public AthletesQuestion(String id, String athleteId, String quizId, Date answeredDate, String userName, String pictureUrl, List<QuizQuestions> quizQuestions) {
        this.id = id;
        this.athleteId = athleteId;
        this.quizId = quizId;
        this.answeredDate = answeredDate;
        this.userName = userName;
        this.pictureUrl = pictureUrl;
        this.quizQuestions = quizQuestions;
    }

    public String getPictureUrlCoach() {
        return pictureUrlCoach;
    }

    public void setPictureUrlCoach(String pictureUrlCoach) {
        this.pictureUrlCoach = pictureUrlCoach;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public Date getAnsweredDate() {
        return answeredDate;
    }

    public void setAnsweredDate(Date answeredDate) {
        this.answeredDate = answeredDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public List<QuizQuestions> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestions> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }
}
