package pe.edu.upc.groupsports.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.activities.CoachQuizQuestionsActivity;
import pe.edu.upc.groupsports.adapters.CoachQuizAdapter;
import pe.edu.upc.groupsports.adapters.SessionBinnacleAdapter;
import pe.edu.upc.groupsports.models.CoachQuiz;
import pe.edu.upc.groupsports.models.SessionWork;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * A simple {@link Fragment} subclass.
 */
public class CoachQuizByDateFragment extends Fragment {
    SessionManager session;
    MaterialCalendarView materialCalendarView;
    Context context;

    RecyclerView coachQuizRecyclerView;
    CoachQuizAdapter coachQuizAdapter;
    RecyclerView.LayoutManager coachQuizLayoutManager;
    List<CoachQuiz> currentCoachQuiz;

    Date currentDate;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public CoachQuizByDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coach_quiz_by_date, container, false);

        context = view.getContext();
        session = new SessionManager(context);

        materialCalendarView = view.findViewById(R.id.calendarView);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                currentDate = calendarDay.getDate();
                refreshQuizzesSessions();
            }
        });

        currentDate = Funciones.getCurrentDate();
        materialCalendarView.setSelectedDate(currentDate);

        coachQuizRecyclerView = (RecyclerView) view.findViewById(R.id.coachQuizRecyclerView);
        currentCoachQuiz = new ArrayList<>();
        coachQuizAdapter = new CoachQuizAdapter(currentCoachQuiz);
        coachQuizLayoutManager = new LinearLayoutManager(context);
        coachQuizRecyclerView.setAdapter(coachQuizAdapter);
        coachQuizRecyclerView.setLayoutManager(coachQuizLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        coachQuizAdapter.setOnCardClickedListener(new CoachQuizAdapter.OnCardClickedListener() {
            @Override
            public void onCardClicked(CoachQuiz coachQuiz) {
                Intent intent = new Intent(context, CoachQuizQuestionsActivity.class);
                intent.putExtras(coachQuiz.toBundle());
                ((Activity)context).startActivityForResult(
                        intent,
                        CoachQuizQuestionsActivity.REQUEST_FOR_ACTIVITY_CODE_COACH_QUIZ_QUESTIONS
                );
            }
        });

        refreshQuizzesSessions();
        return view;
    }

    public void refreshQuizzesSessions() {
        AndroidNetworking.get(GroupSportsApiService.QUIZZES_BY_COACH_BY_DATE(session.getuserLoggedTypeId(), Funciones.formatDateForAPI(currentDate)))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        currentCoachQuiz.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                currentCoachQuiz.add(CoachQuiz.toCoachQuiz(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        coachQuizAdapter.notifyDataSetChanged();
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
