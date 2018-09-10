package pe.edu.upc.groupsports.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.activities.AthleteQuizQuestionsActivity;
import pe.edu.upc.groupsports.activities.AthleteReplyQuizActivity;
import pe.edu.upc.groupsports.activities.CoachQuizQuestionsActivity;
import pe.edu.upc.groupsports.activities.SessionHistoryActivity;
import pe.edu.upc.groupsports.adapters.CoachQuizAdapter;
import pe.edu.upc.groupsports.models.AthletesQuestion;
import pe.edu.upc.groupsports.models.CoachQuiz;
import pe.edu.upc.groupsports.network.GroupSportsApiService;


/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteQuizFragment extends Fragment {

    SessionManager session;
    Context context;

    RecyclerView coachQuizRecyclerView;
    CoachQuizAdapter coachQuizAdapter;
    RecyclerView.LayoutManager coachQuizLayoutManager;
    List<CoachQuiz> currentCoachQuiz;
    List<AthletesQuestion> currentAthletesQuestions;
    List<CoachQuiz> currentCoachQuizUnreplied;
    List<AthletesQuestion> currentAthletesQuestionsUnreplied;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    CardView numberQuizzesCardView;
    TextView numberQuizzesTextView;
    CardView newQuizzesWarningCardView;

    Button replyButton;

    public AthleteQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_athlete_quiz, container, false);

        context = view.getContext();
        session = new SessionManager(context);

        coachQuizRecyclerView = (RecyclerView) view.findViewById(R.id.coachQuizRecyclerView);
        currentCoachQuiz = new ArrayList<>();
        currentCoachQuizUnreplied = new ArrayList<>();
        currentAthletesQuestions = new ArrayList<>();
        currentAthletesQuestionsUnreplied = new ArrayList<>();

        coachQuizAdapter = new CoachQuizAdapter(currentCoachQuiz);
        coachQuizLayoutManager = new LinearLayoutManager(context);
        coachQuizRecyclerView.setAdapter(coachQuizAdapter);
        coachQuizRecyclerView.setLayoutManager(coachQuizLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        numberQuizzesCardView = (CardView) view.findViewById(R.id.numberQuizzesCardView);
        numberQuizzesTextView = (TextView) view.findViewById(R.id.numberQuizzesTextView);
        newQuizzesWarningCardView = (CardView) view.findViewById(R.id.newQuizzesWarningCardView);

        replyButton = (Button) view.findViewById(R.id.replyButton);

        coachQuizAdapter.setOnCardClickedListenerPos(new CoachQuizAdapter.OnCardClickedListenerPos() {
            @Override
            public void onCardClicked(int pos) {
                Intent intent = new Intent(context, AthleteQuizQuestionsActivity.class);
                AthleteQuizQuestionsActivity.athletesQuestion = currentAthletesQuestions.get(pos);
                ((Activity)context).startActivityForResult(
                        intent,
                        AthleteQuizQuestionsActivity.REQUEST_FOR_ACTIVITY_CODE_ATHLETE_QUIZ_QUESTIONS
                );
            }
        });

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AthleteReplyQuizActivity.class);
                AthletesQuestion athletesQuestion = currentAthletesQuestionsUnreplied.get(currentAthletesQuestionsUnreplied.size() - 1);
                intent.putExtra("quizId",athletesQuestion.getQuizId());
                intent.putExtra("athleteQuizId",athletesQuestion.getId());
                intent.putExtra("quizTitle",athletesQuestion.getQuizTitle());
                ((Activity)context).startActivityForResult(
                        intent,
                        AthleteReplyQuizActivity.REQUEST_FOR_ACTIVITY_CODE_ATHLETE_REPLY_QUIZ
                );
            }
        });

        refreshQuizzesSessions();

        return view;
    }

    public void refreshQuizzesSessions() {
        AndroidNetworking.get(GroupSportsApiService.ATHLETES_QUESTIONS_BY_ATHLETES(session.getuserLoggedTypeId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        currentCoachQuiz.clear();
                        currentAthletesQuestions.clear();
                        currentCoachQuizUnreplied.clear();
                        currentAthletesQuestionsUnreplied.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject cqJsonObject = response.getJSONObject(i);
                                CoachQuiz cq = CoachQuiz.toCoachQuiz(cqJsonObject);
                                if (cqJsonObject.getJSONArray("quizQuestionByAthlete").length() > 0) {
                                    currentCoachQuiz.add(cq);
                                    currentAthletesQuestions.add(AthletesQuestion.toAthletesQuestionToAthlete(cqJsonObject));
                                }
                                else {
                                    currentCoachQuizUnreplied.add(cq);
                                    currentAthletesQuestionsUnreplied.add(AthletesQuestion.toAthletesQuestionToAthlete(cqJsonObject));
                                }
                                numberQuizzesTextView.setText(String.valueOf(currentCoachQuizUnreplied.size()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        coachQuizAdapter.notifyDataSetChanged();
                        if (currentCoachQuizUnreplied.size() == 0) {
                            numberQuizzesCardView.setVisibility(View.GONE);
                            numberQuizzesTextView.setVisibility(View.GONE);
                            newQuizzesWarningCardView.setVisibility(View.GONE);
                        } else {
                            numberQuizzesCardView.setVisibility(View.VISIBLE);
                            numberQuizzesCardView.setVisibility(View.VISIBLE);
                            newQuizzesWarningCardView.setVisibility(View.VISIBLE);
                        }

                        if (response.length() == 0) {
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                            noAthletesConstraintLayout.bringToFront();
                        }
                        else {
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
