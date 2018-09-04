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
import pe.edu.upc.groupsports.adapters.AthleteAdapter;
import pe.edu.upc.groupsports.adapters.TrainingPlanAdapter;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.TrainingPlan;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainingPlansFragment extends Fragment {

    SessionManager session;

    RecyclerView trainingPlansRecyclerView;
    TrainingPlanAdapter trainingPlanAdapter;
    RecyclerView.LayoutManager trainingPlansLayoutManager;
    List<TrainingPlan> trainingPlans;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public TrainingPlansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_training_plans, container, false);

        session = new SessionManager(view.getContext());
        trainingPlansRecyclerView = (RecyclerView) view.findViewById(R.id.trainingPlansRecyclerView);
        trainingPlans = new ArrayList<>();
        trainingPlanAdapter = new TrainingPlanAdapter(trainingPlans);
        trainingPlansLayoutManager = new LinearLayoutManager(view.getContext());
        trainingPlansRecyclerView.setAdapter(trainingPlanAdapter);
        trainingPlansRecyclerView.setLayoutManager(trainingPlansLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        updateData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData(){
        int coachId = Integer.parseInt(session.getuserLoggedTypeId());
        AndroidNetworking.get(GroupSportsApiService.TRAINING_PLANS_BY_COACH_URL(coachId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        trainingPlans.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                trainingPlans.add(TrainingPlan.toTrainingPlan(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        trainingPlanAdapter.notifyDataSetChanged();
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

}
