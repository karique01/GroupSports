package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import pe.edu.upc.groupsports.adapters.ChooseAthleteQuizAdapter;
import pe.edu.upc.groupsports.adapters.QuizQuestionAdapter;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.models.AthletesQuestion;
import pe.edu.upc.groupsports.models.CoachQuiz;
import pe.edu.upc.groupsports.models.QuizQuestions;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

public class CoachQuizQuestionsActivity extends AppCompatActivity {
    public static int REQUEST_FOR_ACTIVITY_CODE_COACH_QUIZ_QUESTIONS;

    CoachQuiz currentCoachQuiz;

    SessionManager session;
    Context context;

    List<QuizQuestions> quizQuestions;

    RecyclerView coachQuizQuestionsRecyclerView;
    QuizQuestionAdapter quizQuestionAdapter;
    RecyclerView.LayoutManager quizQuestionLayoutManager;
    List<AthletesQuestion> athletesQuestions;

    RecyclerView athletesRecyclerView;
    ChooseAthleteQuizAdapter chooseAthleteQuizAdapter;
    RecyclerView.LayoutManager chooseAthleteQuizLayoutManager;

    CardView closeWarningCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_quiz_questions);

        currentCoachQuiz = CoachQuiz.from(getIntent().getExtras());
        setupToolBar();

        context = this;
        session = new SessionManager(context);

        closeWarningCardView = (CardView) findViewById(R.id.closeWarningCardView);
        closeWarningCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeWarningCardView.setVisibility(View.GONE);
            }
        });

        coachQuizQuestionsRecyclerView = (RecyclerView) findViewById(R.id.coachQuizQuestionsRecyclerView);
        athletesRecyclerView = (RecyclerView) findViewById(R.id.athletesRecyclerView);
        athletesQuestions = new ArrayList<>();
        quizQuestions = new ArrayList<>();

        updateDataQuestions();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_week, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete_week){
            warningDeleteQuiz();
        }else{
            finish();
        }
        return true;
    }

    private void warningDeleteQuiz() {
        final WarningDialog warningDialog = new WarningDialog(context,"Eliminar encuesta","¿Está seguro que desea eliminar esta encuesta?\n\nSe eliminarán todas las preguntas y respuestas ligadas es esta");
        warningDialog.show();
        warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                warningDialog.dismiss();
                deleteQuiz();
            }
        });
        warningDialog.setOnCancelButtonClickListener(new WarningDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                warningDialog.dismiss();
            }
        });
    }

    private void deleteQuiz() {
        AndroidNetworking.delete(GroupSportsApiService.COACH_QUIZZES_URL + currentCoachQuiz.getId())
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")){
                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar la encuesta",Toast.LENGTH_LONG).show();
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

    private void setupToolBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(currentCoachQuiz.getQuizTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

    private void updateDataQuestions(){
        AndroidNetworking.get(GroupSportsApiService.QUIZ_QUESTIONS_BY_QUIZ(currentCoachQuiz.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        quizQuestions.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                quizQuestions.add(QuizQuestions.toOnlyQuizQuestions(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        updateData();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void updateData(){
        AndroidNetworking.get(GroupSportsApiService.ATHLETES_QUESTIONS_BY_QUIZ(currentCoachQuiz.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athletesQuestions.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                athletesQuestions.add(AthletesQuestion.toAthletesQuestion(response.getJSONObject(i),quizQuestions));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        chooseAthleteQuizAdapter = new ChooseAthleteQuizAdapter(athletesQuestions);
                        chooseAthleteQuizAdapter.setOnAthleteSelectedListener(new ChooseAthleteQuizAdapter.OnAthleteSelectedListener() {
                            @Override
                            public void OnAthleteSelected(int athletePos) {
                                quizQuestionAdapter.setAthletesQuestion(athletesQuestions.get(athletePos));
                                if (athletesQuestions.get(athletePos).getAnsweredDate() == null)
                                    closeWarningCardView.setVisibility(View.VISIBLE);
                                else closeWarningCardView.setVisibility(View.GONE);

                                quizQuestionAdapter.notifyDataSetChanged();
                            }
                        });
                        chooseAthleteQuizLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
                        athletesRecyclerView.setAdapter(chooseAthleteQuizAdapter);
                        athletesRecyclerView.setLayoutManager(chooseAthleteQuizLayoutManager);

                        quizQuestionAdapter = new QuizQuestionAdapter(athletesQuestions.get(0));
                        athletesQuestions.get(0).setSelected(true);
                        if (athletesQuestions.get(0).getAnsweredDate() == null)
                            closeWarningCardView.setVisibility(View.VISIBLE);

                        quizQuestionAdapter.setSessionManager(session);
                        quizQuestionLayoutManager = new LinearLayoutManager(context);
                        coachQuizQuestionsRecyclerView.setAdapter(quizQuestionAdapter);
                        coachQuizQuestionsRecyclerView.setLayoutManager(quizQuestionLayoutManager);

                        quizQuestionAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
