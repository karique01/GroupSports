package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.MoodAdapter;
import pe.edu.upc.groupsports.adapters.ReplyQuestionsAdapter;
import pe.edu.upc.groupsports.models.Mood;
import pe.edu.upc.groupsports.models.QuizQuestionByAthlete;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

public class AthleteReplyQuizActivity extends AppCompatActivity {

    public static int REQUEST_FOR_ACTIVITY_CODE_ATHLETE_REPLY_QUIZ;

    String quizId;
    String athleteQuizId;
    String quizTitle;

    SessionManager session;
    Context context;

    RecyclerView questionsRecyclerView;
    ReplyQuestionsAdapter replyQuestionsAdapter;
    RecyclerView.LayoutManager replyLayoutManager;
    List<QuizQuestionByAthlete> quizQuestionByAthletes;

    CardView replyQuizCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_reply_quiz);

        quizId = getIntent().getStringExtra("quizId");
        athleteQuizId = getIntent().getStringExtra("athleteQuizId");
        quizTitle = getIntent().getStringExtra("quizTitle");

        context = this;
        session = new SessionManager(context);

        questionsRecyclerView = (RecyclerView) findViewById(R.id.questionsRecyclerView);
        quizQuestionByAthletes = new ArrayList<>();
        replyQuestionsAdapter = new ReplyQuestionsAdapter(quizQuestionByAthletes);
        replyLayoutManager = new LinearLayoutManager(context);
        questionsRecyclerView.setAdapter(replyQuestionsAdapter);
        questionsRecyclerView.setLayoutManager(replyLayoutManager);

        replyQuizCardView = (CardView) findViewById(R.id.replyQuizCardView);
        replyQuizCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (replyQuestionsAdapter.answerAreValids())
                    replyQuiz();
                else Toast.makeText(context,"Responda todas la preguntas",Toast.LENGTH_LONG).show();
            }
        });

        setupToolBar();
        updateData();
    }

    private void replyQuiz() {
        AndroidNetworking.post(GroupSportsApiService.ANSWERS_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONArrayBody(replyQuestionsAdapter.getAnswerJsonArray())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                Toast.makeText(context, "Hubo un error al responder la encuesta", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Hubo un error en el sistema", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void updateData(){
        AndroidNetworking.get(GroupSportsApiService.QUIZ_QUESTIONS_BY_QUIZ(String.valueOf(quizId)))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        quizQuestionByAthletes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                QuizQuestionByAthlete qqByAthlete = new QuizQuestionByAthlete();
                                JSONObject qqJsonObject = response.getJSONObject(i);

                                qqByAthlete.setQuestionTitle(qqJsonObject.getString("questionTitle"));
                                qqByAthlete.setAnswer("");
                                qqByAthlete.setQuizQuestionId(qqJsonObject.getString("id"));
                                qqByAthlete.setAthleteQuizId(String.valueOf(athleteQuizId));
                                qqByAthlete.setAvailable(true);

                                quizQuestionByAthletes.add(qqByAthlete);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        replyQuestionsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void setupToolBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(quizTitle);
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}





















