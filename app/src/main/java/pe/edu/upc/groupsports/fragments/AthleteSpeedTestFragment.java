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
import pe.edu.upc.groupsports.adapters.SpeedTestAdapter;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.SpeedTest;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteSpeedTestFragment extends Fragment {
    SessionManager session;

    RecyclerView speedTestRecyclerView;
    SpeedTestAdapter speedTestAdapter;
    RecyclerView.LayoutManager speedTestLayoutManager;
    List<SpeedTest> speedTests;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    Athlete currentAthlete;


    public AthleteSpeedTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_athlete_speed_test, container, false);

        session = new SessionManager(view.getContext());
        speedTestRecyclerView = (RecyclerView) view.findViewById(R.id.speedTestRecyclerView);
        speedTests = new ArrayList<>();
        speedTestAdapter = new SpeedTestAdapter(speedTests);
        speedTestLayoutManager = new LinearLayoutManager(view.getContext());
        speedTestRecyclerView.setAdapter(speedTestAdapter);
        speedTestRecyclerView.setLayoutManager(speedTestLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        updateData();

        return view;
    }

    public void updateData(){
        AndroidNetworking.get(GroupSportsApiService.SPEED_TEST_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        speedTests.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                speedTests.add(SpeedTest.toSpeedTest(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        speedTestAdapter.notifyDataSetChanged();
                        if (response.length() == 0) {
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
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

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }

}
