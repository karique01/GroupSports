package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.SpeedTestAdapter;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.CoachCurriculumDetail;
import pe.edu.upc.groupsports.models.SpeedTest;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteSpeedTestFragment extends Fragment {
    SessionManager session;

    Context context;

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

        context = view.getContext();
        session = new SessionManager(context);
        speedTestRecyclerView = (RecyclerView) view.findViewById(R.id.speedTestRecyclerView);
        speedTests = new ArrayList<>();
        speedTestAdapter = new SpeedTestAdapter(speedTests);
        speedTestAdapter.setLongClickListener(new SpeedTestAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(SpeedTest speedTest) {
                showEditDeleteDialog(speedTest);
            }
        });
        speedTestLayoutManager = new LinearLayoutManager(view.getContext());
        speedTestRecyclerView.setAdapter(speedTestAdapter);
        speedTestRecyclerView.setLayoutManager(speedTestLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        updateData();

        return view;
    }

    private void showEditDeleteDialog(final SpeedTest speedTest){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context, true);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteSpeedTestDetail(speedTest);
            }
        });
        editDeleteDialog.setOnEditButtonClickListener(new EditDeleteDialog.OnEditButtonClickListener() {
            @Override
            public void OnEditButtonClicked() {
                editDeleteDialog.dismiss();
                //showEditAthleteAchievement(athleteAchievement);
            }
        });
    }

    private void deleteSpeedTestDetail(SpeedTest speedTest) {
        AndroidNetworking.delete(GroupSportsApiService.SPEED_TEST_URL + speedTest.getId())
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
                                updateData();
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar el detalle",Toast.LENGTH_LONG).show();
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

                        sortSpeedTestsByDesc();
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


    private void sortSpeedTestsByDesc(){
        if (speedTests.size() > 1) {
            Collections.sort(speedTests, new Comparator<SpeedTest>() {
                public int compare(SpeedTest o1, SpeedTest o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
            Collections.reverse(speedTests);
        }
    }

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }

}
