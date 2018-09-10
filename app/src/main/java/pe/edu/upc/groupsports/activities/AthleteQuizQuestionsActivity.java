package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.QuizQuestionAdapter;
import pe.edu.upc.groupsports.models.AthletesQuestion;

public class AthleteQuizQuestionsActivity extends AppCompatActivity {
    public static int REQUEST_FOR_ACTIVITY_CODE_ATHLETE_QUIZ_QUESTIONS;

    SessionManager session;
    Context context;

    RecyclerView coachQuizQuestionsRecyclerView;
    QuizQuestionAdapter quizQuestionAdapter;
    RecyclerView.LayoutManager quizQuestionLayoutManager;
    public static AthletesQuestion athletesQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_quiz_questions);

        setupToolBar();

        context = this;
        session = new SessionManager(context);

        coachQuizQuestionsRecyclerView = (RecyclerView) findViewById(R.id.coachQuizQuestionsRecyclerView);
        quizQuestionAdapter = new QuizQuestionAdapter(athletesQuestion);
        quizQuestionAdapter.setSessionManager(session);
        quizQuestionLayoutManager = new LinearLayoutManager(context);
        coachQuizQuestionsRecyclerView.setAdapter(quizQuestionAdapter);
        coachQuizQuestionsRecyclerView.setLayoutManager(quizQuestionLayoutManager);

        quizQuestionAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void setupToolBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(athletesQuestion.getQuizTitle());
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

}
