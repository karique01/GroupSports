package pe.edu.upc.groupsports.fragments;


import android.content.Context;
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
import pe.edu.upc.groupsports.adapters.SessionBinnacleAdapter;
import pe.edu.upc.groupsports.adapters.SessionWorkAdapter;
import pe.edu.upc.groupsports.models.SessionWork;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * A simple {@link Fragment} subclass.
 */
public class BinnacleFragment extends Fragment {
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

    public BinnacleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_binnacle, container, false);
        context = view.getContext();
        session = new SessionManager(context);

        materialCalendarView = view.findViewById(R.id.calendarView);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                currentDate = calendarDay.getDate();
                refreshTodaySessions();
            }
        });

        currentDate = Funciones.getCurrentDate();
        materialCalendarView.setSelectedDate(currentDate);

        sessionWorksRecyclerView = (RecyclerView) view.findViewById(R.id.sessionWorksRecyclerView);
        currentSessionWorksToday = new ArrayList<>();
        sessionBinnacleAdapter = new SessionBinnacleAdapter(currentSessionWorksToday);
        sessionWorkLayoutManager = new LinearLayoutManager(context);
        sessionWorksRecyclerView.setAdapter(sessionBinnacleAdapter);
        sessionWorksRecyclerView.setLayoutManager(sessionWorkLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        refreshTodaySessions();
        return view;
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
