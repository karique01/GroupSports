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
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.AthleteAchievementAdapter;
import pe.edu.upc.groupsports.dialogs.AddAthleteAchievementDialog;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.AthleteAchievement;
import pe.edu.upc.groupsports.models.AthleteFodaItem;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteAchievementFragment extends Fragment {
    Athlete currentAthlete;
    SessionManager session;
    Context context;

    RecyclerView achievementsRecyclerView;
    AthleteAchievementAdapter athleteAchievementAdapter;
    RecyclerView.LayoutManager athleteAchievementsLayoutManager;
    List<AthleteAchievement> athleteAchievements;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public AthleteAchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_athlete_achievement, container, false);
        context = view.getContext();
        session = new SessionManager(context);

        achievementsRecyclerView = (RecyclerView) view.findViewById(R.id.achievementsRecyclerView);
        athleteAchievements = new ArrayList<>();
        athleteAchievementAdapter = new AthleteAchievementAdapter(athleteAchievements);
        athleteAchievementsLayoutManager = new LinearLayoutManager(context);
        achievementsRecyclerView.setAdapter(athleteAchievementAdapter);
        achievementsRecyclerView.setLayoutManager(athleteAchievementsLayoutManager);
        athleteAchievementAdapter.setOnCardClickedListener(new AthleteAchievementAdapter.OnCardClickedListener() {
            @Override
            public void onCardClicked(AthleteAchievement athleteAchievement) {
                showEditDeleteDialog(athleteAchievement);
            }
        });

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        getAchievementsData();
        return view;
    }

    private void showEditDeleteDialog(final AthleteAchievement athleteAchievement){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteAthleteAchievement(athleteAchievement);
            }
        });
        editDeleteDialog.setOnEditButtonClickListener(new EditDeleteDialog.OnEditButtonClickListener() {
            @Override
            public void OnEditButtonClicked() {
                editDeleteDialog.dismiss();
                showEditAthleteAchievement(athleteAchievement);
            }
        });
    }

    private void deleteAthleteAchievement(AthleteAchievement athleteAchievement) {
        AndroidNetworking.delete(GroupSportsApiService.ATHLETE_ACHIEVEMENT_URL + athleteAchievement.getId())
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
                                getAchievementsData();
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar la entrada",Toast.LENGTH_LONG).show();
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

    private void showEditAthleteAchievement(final AthleteAchievement athleteAchievement) {
        final AddAthleteAchievementDialog addAthleteAchievementDialog = new AddAthleteAchievementDialog(
                context,
                athleteAchievement.getDescription(),
                athleteAchievement.getPlace(),
                athleteAchievement.getAthleteAchievementTypeId().equals("1") ? athleteAchievement.getResultTime() : athleteAchievement.getResultDistance()
        );

        addAthleteAchievementDialog.show();
        addAthleteAchievementDialog.setOnOkButtonClickListener(new AddAthleteAchievementDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String place, String description, String athleteAchievementTypeId, String resultTime, String resultDistance) {
                addAthleteAchievementDialog.dismiss();
                updateAthleteAchievement(
                        athleteAchievement.getId(),
                        place,
                        description,
                        athleteAchievementTypeId,
                        resultTime,
                        resultDistance
                );
            }
        });
        addAthleteAchievementDialog.setOnCancelButtonClickListener(new AddAthleteAchievementDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addAthleteAchievementDialog.dismiss();
            }
        });
    }

    private void updateAthleteAchievement(String achievementId, String place, String description, String athleteAchievementTypeId, String resultTime, String resultDistance){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("place",place);
            jsonObject.put("description",description);
            jsonObject.put("athleteAchievementTypeId",athleteAchievementTypeId);
            jsonObject.put("resultTime",resultTime);
            jsonObject.put("resultDistance",resultDistance);
            jsonObject.put("athleteId",currentAthlete.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(GroupSportsApiService.ATHLETE_ACHIEVEMENT_URL + achievementId)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //se actualizó correctamente
                        try {
                            if (response.getString("response").equals("Ok")){
                                getAchievementsData();
                            }
                            else {
                                Toast.makeText(context,"Error al actualizar el valor",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context,"Error de conexión",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void getAchievementsData(){
        AndroidNetworking.get(GroupSportsApiService.ATHLETE_ACHIEVEMENT_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athleteAchievements.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                athleteAchievements.add(AthleteAchievement.toAthleteAchievement(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        athleteAchievementAdapter.notifyDataSetChanged();
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
