package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import pe.edu.upc.groupsports.adapters.AddQuestionsAdapter;
import pe.edu.upc.groupsports.adapters.ChooseAthleteAnnouncementAdapter;
import pe.edu.upc.groupsports.models.AnnouncementPost;
import pe.edu.upc.groupsports.models.Assistance;
import pe.edu.upc.groupsports.models.AssistanceShift;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.AthleteQuiz;
import pe.edu.upc.groupsports.models.Quiz;
import pe.edu.upc.groupsports.models.QuizQuestion;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.QuizQuestionsRepository;
import pe.edu.upc.groupsports.util.Funciones;

public class AddQuizActivity extends AppCompatActivity {

    public static int REQUEST_FOR_ACTIVITY_CODE_ADD_QUIZ = 61;

    SessionManager session;
    Context context;

    RecyclerView questionsRecyclerView;
    AddQuestionsAdapter addQuestionsAdapter;
    RecyclerView.LayoutManager questionsLayoutManager;
    //List<QuizQuestion> quizQuestions;
    List<AthleteQuiz> athleteQuizs;

    CardView addQuestionCardView;
    CardView chooseAthletesCardView;

    EditText titleEditText;

    ConstraintLayout questionsConstraintLayout;
    ConstraintLayout chooseAthleteConstraintLayout;

    RecyclerView athletesRecyclerView;
    ChooseAthleteAnnouncementAdapter athletesAdapter;
    RecyclerView.LayoutManager athletesLayoutManager;
    List<Athlete> athletes;

    CardView backQuestionsCardView;
    CardView sendQuizCardView;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);

        athleteQuizs = new ArrayList<>();
        //quizQuestions = QuizQuestionsRepository.getInstance().getQuizQuestions();

        context = this;
        session = new SessionManager(this);

        questionsConstraintLayout = (ConstraintLayout) findViewById(R.id.questionsConstraintLayout);
        chooseAthleteConstraintLayout = (ConstraintLayout) findViewById(R.id.chooseAthleteConstraintLayout);
        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        questionsRecyclerView = (RecyclerView) findViewById(R.id.questionsRecyclerView);
        addQuestionsAdapter = new AddQuestionsAdapter();
        questionsLayoutManager = new LinearLayoutManager(context);
        questionsRecyclerView.setAdapter(addQuestionsAdapter);
        questionsRecyclerView.setLayoutManager(questionsLayoutManager);

        addQuestionCardView = (CardView) findViewById(R.id.addQuestionCardView);
        addQuestionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizQuestionsRepository.getInstance().addQuestion();
                addQuestionsAdapter.notifyDataSetChanged();
            }
        });
        chooseAthletesCardView = (CardView) findViewById(R.id.chooseAthletesCardView);
        chooseAthletesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionsConstraintLayout.setVisibility(View.GONE);
                chooseAthleteConstraintLayout.setVisibility(View.VISIBLE);
                setupToolBar(context.getString(R.string.choose_athletes));
            }
        });

        athletesRecyclerView = (RecyclerView) findViewById(R.id.athletesRecyclerView);
        athletes = new ArrayList<>();
        athletesAdapter = new ChooseAthleteAnnouncementAdapter(athletes);
        athletesLayoutManager = new GridLayoutManager(context, 3);
        athletesRecyclerView.setAdapter(athletesAdapter);
        athletesRecyclerView.setLayoutManager(athletesLayoutManager);

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        titleEditText.setText(String.format("Encuesta %s", Funciones.formatDate(Funciones.getCurrentDate())));
        titleEditText.setSelection(titleEditText.getText().length());

        backQuestionsCardView = (CardView) findViewById(R.id.backQuestionsCardView);
        backQuestionsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionsConstraintLayout.setVisibility(View.VISIBLE);
                chooseAthleteConstraintLayout.setVisibility(View.GONE);
                setupToolBar(context.getString(R.string.add_quiz));
            }
        });
        sendQuizCardView = (CardView) findViewById(R.id.sendQuizCardView);
        sendQuizCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendQuiz();
            }
        });

        updateData();
        setupToolBar(context.getString(R.string.add_quiz));
    }

    private void sendQuiz() {
        if (titleEditText.getText().length() > 0 &&
                QuizQuestionsRepository.getInstance().areValidQuestions() &&
                QuizQuestionsRepository.getInstance().size() > 0 &&
                athletesAdapter.getSelectedAthletesIds().size() > 0){
            uploadQuiz();
        } else {
            Toast.makeText(context,"Agregue un titulo, ingrese preguntas y seleccione atletas",Toast.LENGTH_LONG).show();
        }
    }

    private void uploadQuiz(){
        AndroidNetworking.post(GroupSportsApiService.COACH_QUIZZES_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(getQuizJson())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //se subió correctamente
                        try {
                            if (response.getString("response").equals("Ok")){
                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                Toast.makeText(context,"Error al guardar el quiz",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context,"Error de conexión",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public JSONObject getQuizJson(){
        Quiz quiz = new Quiz();
        quiz.setCoachId(session.getuserLoggedTypeId());
        quiz.setQuizTitle(titleEditText.getText().toString());
        quiz.setDate(Funciones.formatDate(Funciones.getCurrentDate()));
        quiz.setAvailable(true);

        JSONObject jsonObjectQuizDTO = new JSONObject();

        try {
            jsonObjectQuizDTO.put("quiz",quiz.toJSONObject());

            JSONArray athletesJsonArray = new JSONArray();
            List<AnnouncementPost.athleteIdClass> aths = athletesAdapter.getSelectedAthletesIds();
            for (int i = 0; i < aths.size(); i++) {
                AthleteQuiz athleteQuiz = new AthleteQuiz();
                athleteQuiz.setAthleteId(aths.get(i).athleteId);
                athletesJsonArray.put(athleteQuiz.toJSONObject());
            }

            jsonObjectQuizDTO.put("athleteQuizzes",(Object) athletesJsonArray);

            JSONArray quizQuestionJsonArray = new JSONArray();
            for (int i = 0; i < QuizQuestionsRepository.getInstance().size(); i++) {
                QuizQuestion qq = QuizQuestionsRepository.getInstance().getQQ(i);
                quizQuestionJsonArray.put(qq.toJSONObject());
            }

            jsonObjectQuizDTO.put("quizQuestions",(Object)quizQuestionJsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjectQuizDTO;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void setupToolBar(String title) {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

    public void updateData(){
        int coachId = Integer.parseInt(session.getuserLoggedTypeId());
        AndroidNetworking.get(GroupSportsApiService.ATHELETES_BY_COACH_URL(coachId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(context.getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athletes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                athletes.add(Athlete.toAthlete(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        athletesAdapter.notifyDataSetChanged();
                        if (response.length() == 0) {
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                        } else {
                            noAthletesConstraintLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }
}
