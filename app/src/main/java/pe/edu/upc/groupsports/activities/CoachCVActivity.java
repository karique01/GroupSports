package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import pe.edu.upc.groupsports.adapters.CoachCurriculumDetailAdapter;
import pe.edu.upc.groupsports.dialogs.AddCVDetailsDialog;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.CoachCurriculumDetail;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

public class CoachCVActivity extends AppCompatActivity {

    SessionManager session;
    Context context;

    RecyclerView myCVRecyclerView;
    CoachCurriculumDetailAdapter coachCurriculumDetailAdapter;
    RecyclerView.LayoutManager coachCurriculumDetailLayoutManager;
    List<CoachCurriculumDetail> coachCurriculumDetails;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    FloatingActionButton addCVItemFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_cv);

        context = this;
        session = new SessionManager(context);

        setupToolBar();

        myCVRecyclerView = (RecyclerView) findViewById(R.id.myCVRecyclerView);
        coachCurriculumDetails = new ArrayList<>();
        coachCurriculumDetailAdapter = new CoachCurriculumDetailAdapter(coachCurriculumDetails);
        coachCurriculumDetailAdapter.setOnCVDetailLongClickListener(new CoachCurriculumDetailAdapter.OnCVDetailLongClickListener() {
            @Override
            public void OnCVDetailLongClicked(CoachCurriculumDetail coachCurriculumDetail) {
                showEditDeleteDialog(coachCurriculumDetail);
            }
        });

        coachCurriculumDetailLayoutManager = new LinearLayoutManager(this);
        myCVRecyclerView.setAdapter(coachCurriculumDetailAdapter);
        myCVRecyclerView.setLayoutManager(coachCurriculumDetailLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        addCVItemFloatingActionButton = (FloatingActionButton) findViewById(R.id.addCVItemFloatingActionButton);
        addCVItemFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCVDetailDialog();
            }
        });

        getCurriculumDetailsData();
    }

    private void showEditDeleteDialog(final CoachCurriculumDetail coachCurriculumDetail){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context, true);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteCVDetail(coachCurriculumDetail);
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

    private void deleteCVDetail(CoachCurriculumDetail coachCurriculumDetail) {
        AndroidNetworking.delete(GroupSportsApiService.COACH_CURRICULUM_DETAILS_URL + coachCurriculumDetail.getId())
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
                                getCurriculumDetailsData();
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

    private void showAddCVDetailDialog(){
        final AddCVDetailsDialog addCVDetailsDialog = new AddCVDetailsDialog(context);
        addCVDetailsDialog.show();
        addCVDetailsDialog.setOnOkButtonClickListener(new AddCVDetailsDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String title, String description) {
                addCVDetailsDialog.dismiss();
                uploadCVDetail(title,description);
            }
        });
        addCVDetailsDialog.setOnCancelButtonClickListener(new AddCVDetailsDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addCVDetailsDialog.dismiss();
            }
        });
    }

    private void uploadCVDetail(String title, String description) {
        JSONObject jsonObjectCVDetail = new JSONObject();

        try {
            jsonObjectCVDetail.put("title", title);
            jsonObjectCVDetail.put("description", description);
            jsonObjectCVDetail.put("coachId", session.getuserLoggedTypeId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.COACH_CURRICULUM_DETAILS_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectCVDetail)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                getCurriculumDetailsData();
                            } else {
                                Toast.makeText(context,"Error al agregar el logro",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context,"Error en el sistema",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getCurriculumDetailsData(){
        AndroidNetworking.get(GroupSportsApiService.CURRICULUM_DETAILS_BY_COACH(session.getuserLoggedTypeId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        coachCurriculumDetails.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                coachCurriculumDetails.add(CoachCurriculumDetail.toCoachCurriculumDetail(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        coachCurriculumDetailAdapter.notifyDataSetChanged();

                        if (response.length() == 0) {
                            noAthletesConstraintLayout.bringToFront();
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

    private void setupToolBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Mi curriculum vitae");
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
