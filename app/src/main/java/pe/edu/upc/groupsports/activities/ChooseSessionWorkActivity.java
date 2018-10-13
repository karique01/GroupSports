package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import pe.edu.upc.groupsports.adapters.SessionBinnacleAdapter;
import pe.edu.upc.groupsports.models.SessionWork;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

public class ChooseSessionWorkActivity extends AppCompatActivity {
    SessionManager session;
    MaterialCalendarView materialCalendarView;
    Context context;

    RecyclerView sessionWorksRecyclerView;
    SessionBinnacleAdapter sessionBinnacleAdapter;
    RecyclerView.LayoutManager sessionWorkLayoutManager;
    List<SessionWork> currentSessionWorksToday;

    Date currentDate;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_session_work);

        context = this;
        session = new SessionManager(context);
        setupToolBar();

        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                currentDate = calendarDay.getDate();
                refreshTodaySessions();
            }
        });

        currentDate = Funciones.getCurrentDate();
        materialCalendarView.setSelectedDate(currentDate);

        sessionWorksRecyclerView = (RecyclerView) findViewById(R.id.sessionWorksRecyclerView);
        currentSessionWorksToday = new ArrayList<>();
        sessionBinnacleAdapter = new SessionBinnacleAdapter(currentSessionWorksToday);
        sessionWorkLayoutManager = new LinearLayoutManager(context);
        sessionWorksRecyclerView.setAdapter(sessionBinnacleAdapter);
        sessionWorksRecyclerView.setLayoutManager(sessionWorkLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        refreshTodaySessions();
    }

    private void setupToolBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Escoja una sesión");
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }


    public void refreshTodaySessions() {
        AndroidNetworking.get(GroupSportsApiService.WORKSESSIONS_BY_COACH_BY_DATE(session.getuserLoggedTypeId(),Funciones.formatDateForAPI(currentDate)))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        currentSessionWorksToday.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                currentSessionWorksToday.add(SessionWork.toSessionWorkForBinnacle(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        sessionBinnacleAdapter.notifyDataSetChanged();
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
