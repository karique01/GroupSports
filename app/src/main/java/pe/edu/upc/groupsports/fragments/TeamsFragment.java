package pe.edu.upc.groupsports.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import pe.edu.upc.groupsports.adapters.TeamAdapter;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.Team;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamsFragment extends Fragment {
    SessionManager session;

    //recycler things
    RecyclerView teamsRecyclerView;
    TeamAdapter teamsAdapter;
    RecyclerView.LayoutManager teamsLayoutManager;
    List<Team> teams;

    public TeamsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teams, container, false);

        session = new SessionManager(view.getContext());
        teamsRecyclerView = (RecyclerView) view.findViewById(R.id.teamsRecyclerView);
        teams = new ArrayList<>();
        teamsAdapter = new TeamAdapter(teams);
        teamsLayoutManager = new LinearLayoutManager(view.getContext());
        teamsRecyclerView.setAdapter(teamsAdapter);
        teamsRecyclerView.setLayoutManager(teamsLayoutManager);

        updateData();
        return view;
    }

    private void updateData(){
        AndroidNetworking.get(GroupSportsApiService.TEAMS_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        teams.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                teams.add(Team.toTeam(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        teamsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
