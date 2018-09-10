package pe.edu.upc.groupsports.models;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 7/09/2018.
 */

public class CoachQuiz {
    private String id;
    private String coachId;
    private String quizTitle;
    private Date date;

    public static CoachQuiz from(Bundle bundle){
        CoachQuiz coachQuiz = new CoachQuiz();
        coachQuiz.setId(bundle.getString("id"));
        coachQuiz.setCoachId(bundle.getString("coachId"));
        coachQuiz.setQuizTitle(bundle.getString("quizTitle"));
        coachQuiz.setDate((Date)bundle.getSerializable("date"));
        return coachQuiz;
    }

    public Bundle toBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("coachId",coachId);
        bundle.putString("quizTitle",quizTitle);
        bundle.putSerializable("date",date);
        return bundle;
    }

    public static CoachQuiz toCoachQuiz(JSONObject jsonObject){
        CoachQuiz coachQuiz = new CoachQuiz();

        try {
            coachQuiz.setId(jsonObject.getString("id"));
            coachQuiz.setCoachId(jsonObject.getString("coachId"));
            coachQuiz.setQuizTitle(jsonObject.getString("quizTitle"));
            coachQuiz.setDate(Funciones.getDateFromString(jsonObject.getString("date")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coachQuiz;
    }

    public CoachQuiz() {
    }

    public CoachQuiz(String id, String coachId, String quizTitle, Date date) {
        this.id = id;
        this.coachId = coachId;
        this.quizTitle = quizTitle;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
