package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.MoodAdapter;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.Mood;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteMoodTestFragment extends Fragment {
    SessionManager session;
    Context context;

    //recycler moods
    RecyclerView lastMoodsRecyclerView;
    MoodAdapter moodsAdapter;
    RecyclerView.LayoutManager moodsLayoutManager;
    List<Mood> moods;
    List<Mood> moodsToResponse;

    Athlete currentAthlete;

    public AthleteMoodTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_athlete_mood_test, container, false);
        context = view.getContext();
        session = new SessionManager(view.getContext());
        lastMoodsRecyclerView = (RecyclerView) view.findViewById(R.id.lastMoodsRecyclerView);
        moods = new ArrayList<>();
        moodsToResponse = new ArrayList<>();
        moodsAdapter = new MoodAdapter(moods);
        moodsAdapter.setLongClickListener(new MoodAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(Mood mood) {
                showEditDeleteDialog(mood);
            }
        });
        moodsLayoutManager = new LinearLayoutManager(context);
        lastMoodsRecyclerView.setAdapter(moodsAdapter);
        lastMoodsRecyclerView.setLayoutManager(moodsLayoutManager);
        updateData();
        return view;
    }

    private void showEditDeleteDialog(final Mood mood){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context, true);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteSpeedTestDetail(mood);
            }
        });
        editDeleteDialog.setOnEditButtonClickListener(new EditDeleteDialog.OnEditButtonClickListener() {
            @Override
            public void OnEditButtonClicked() {
                editDeleteDialog.dismiss();
            }
        });
    }

    private void deleteSpeedTestDetail(Mood mood) {
        AndroidNetworking.delete(GroupSportsApiService.MOOD_TEST_URL + mood.getId())
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
                                Toast.makeText(context,"Error al eliminar el test",Toast.LENGTH_LONG).show();
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

    private void updateData(){
        AndroidNetworking.get(GroupSportsApiService.MOODS_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        moods.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Mood mood = Mood.toMood(response.getJSONObject(i));
                                mood.setSelected(true);
                                moods.add(mood);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        moodsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

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
