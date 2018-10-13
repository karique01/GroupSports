package pe.edu.upc.groupsports.fragments;


import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.CoachAdapter;
import pe.edu.upc.groupsports.models.Coach;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCoachsFragment extends Fragment {
    SessionManager session;

    RecyclerView myCoachsRecyclerView;
    CoachAdapter coachAdapter;
    RecyclerView.LayoutManager coachsLayoutManager;
    List<Coach> coaches;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public MyCoachsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_coachs, container, false);

        session = new SessionManager(view.getContext());
        myCoachsRecyclerView = (RecyclerView) view.findViewById(R.id.myCoachsRecyclerView);
        coaches = new ArrayList<>();
        coachAdapter = new CoachAdapter(coaches);
        coachsLayoutManager = new LinearLayoutManager(view.getContext());
        myCoachsRecyclerView.setAdapter(coachAdapter);
        myCoachsRecyclerView.setLayoutManager(coachsLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        getCoachsData();

        return view;
    }

    public void getCoachsData(){
        int athleteId = Integer.parseInt(session.getuserLoggedTypeId());
        AndroidNetworking.get(GroupSportsApiService.COACHS_BY_ATHLETE_URL(athleteId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        coaches.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                coaches.add(Coach.toCoach(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        coachAdapter.notifyDataSetChanged();
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
