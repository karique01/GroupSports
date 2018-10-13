package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.BinnaclDetailsAdapter;
import pe.edu.upc.groupsports.adapters.WeightTestBySessionAdapter;
import pe.edu.upc.groupsports.dialogs.AddBinnacleDetailDialog;
import pe.edu.upc.groupsports.dialogs.AddWeightTestBySessionDialog;
import pe.edu.upc.groupsports.dialogs.AdminValueDialog;
import pe.edu.upc.groupsports.models.BinnacleDetail;
import pe.edu.upc.groupsports.models.SessionWork;
import pe.edu.upc.groupsports.models.WeightTestBySession;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

public class WeightTestBySessionActivity extends AppCompatActivity {
    public static int REQUEST_FOR_ACTIVITY_CODE_WEIGHT_TEST_BY_SESSSION = 77;
    SessionWork currentSessionWork;

    SessionManager session;

    RecyclerView weightTestBySesionRecyclerView;
    WeightTestBySessionAdapter weightTestBySessionAdapter;
    RecyclerView.LayoutManager weightTestBySesionLayoutManager;
    List<WeightTestBySession> weightTestBySessions;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    Context context;

    SpeedDialView speedDial;

    TextView trainingPlanNameTextView;
    TextView mesocycleNameTextView;
    TextView shiftTextView;
    TextView athleteNameTextView;
    CircleImageView athleteCircleImageView;

    private int WEIGHT_EDITED = 0;

    private static int WEIGHT_BEFORE_CLICKED = 0;
    private static int WEIGHT_AFTER_CLICKED = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_test_by_session);

        currentSessionWork = SessionWork.from(getIntent().getExtras());
        setupToolBar();

        context = this;
        session = new SessionManager(context);

        trainingPlanNameTextView = (TextView) findViewById(R.id.trainingPlanNameTextView);
        trainingPlanNameTextView.setText(currentSessionWork.getTrainingPlanName());
        mesocycleNameTextView = (TextView) findViewById(R.id.mesocycleNameTextView);
        mesocycleNameTextView.setText(String.format("Mesociclo: %s", currentSessionWork.getMesocycleName()));
        shiftTextView = (TextView) findViewById(R.id.shiftTextView);
        shiftTextView.setText(String.format("Turno: %s", currentSessionWork.getShiftName()));
        athleteNameTextView = (TextView) findViewById(R.id.athleteNameTextView);
        athleteNameTextView.setText(currentSessionWork.getUsername());
        athleteCircleImageView = (CircleImageView) findViewById(R.id.athleteCircleImageView);
        Picasso.with(context)
                .load(currentSessionWork.getPictureUrl())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(athleteCircleImageView);

        weightTestBySesionRecyclerView = (RecyclerView) findViewById(R.id.weightTestBySesionRecyclerView);
        weightTestBySessions = new ArrayList<>();
        weightTestBySessionAdapter = new WeightTestBySessionAdapter(weightTestBySessions);
        weightTestBySesionLayoutManager = new LinearLayoutManager(context);
        weightTestBySesionRecyclerView.setAdapter(weightTestBySessionAdapter);
        weightTestBySesionRecyclerView.setLayoutManager(weightTestBySesionLayoutManager);

        weightTestBySessionAdapter.setOnWeightBeforeEditListener(new WeightTestBySessionAdapter.OnWeightBeforeEditListener() {
            @Override
            public void onWeightBeforeEdited(WeightTestBySession weightTestBySession) {
                WEIGHT_EDITED = WEIGHT_BEFORE_CLICKED;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.weight_before_session),weightTestBySession);
            }
        });
        weightTestBySessionAdapter.setOnWeightAfterEditListener(new WeightTestBySessionAdapter.OnWeightAfterEditListener() {
            @Override
            public void onWeightAfterEdited(WeightTestBySession weightTestBySession) {
                WEIGHT_EDITED = WEIGHT_AFTER_CLICKED;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.weight_after_session),weightTestBySession);
            }
        });

        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        speedDial = (SpeedDialView) findViewById(R.id.speedDial);
        speedDial.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                showAddWeightTestDialog();
                return false;
            }

            @Override
            public void onToggleChanged(boolean isOpen) {

            }
        });

        getWeightTestBySessionData();
    }

    private void showAddWeightTestDialog() {
        final AddWeightTestBySessionDialog addWeightTestBySessionDialog = new AddWeightTestBySessionDialog(context);
        addWeightTestBySessionDialog.show();
        addWeightTestBySessionDialog.setOnOkButtonClickListener(new AddWeightTestBySessionDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String weightBefore, String weightAfter) {
                addWeightTestBySession(weightBefore, weightAfter);
                addWeightTestBySessionDialog.dismiss();
            }
        });
        addWeightTestBySessionDialog.setOnCancelButtonClickListener(new AddWeightTestBySessionDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addWeightTestBySessionDialog.dismiss();
            }
        });
    }

    private void showAdminValueDialog(String titleString, String editTextString, final WeightTestBySession weightTestBySession){
        final AdminValueDialog adminValueDialog = new AdminValueDialog(context,titleString,editTextString,"EDITAR");
        adminValueDialog.show();
        adminValueDialog.setOnOkButtonClickListener(new AdminValueDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String value) {
                updateWeightTest(weightTestBySession, value);
                adminValueDialog.dismiss();
            }
        });
        adminValueDialog.setOnCancelButtonClickListener(new AdminValueDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                adminValueDialog.dismiss();
            }
        });
    }

    private void updateWeightTest(WeightTestBySession wbs, String newValue) {

        JSONObject moodTestJSONObject = new JSONObject();

        try {
            moodTestJSONObject.put("weightBefore", WEIGHT_EDITED == WEIGHT_BEFORE_CLICKED ? newValue : wbs.getWeightBefore());
            moodTestJSONObject.put("weightAfter", WEIGHT_EDITED == WEIGHT_AFTER_CLICKED ? newValue : wbs.getWeightAfter());
            moodTestJSONObject.put("sessionId", wbs.getSessionId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(GroupSportsApiService.WEIGHT_TEST_BY_SESSION_URL+wbs.getId())
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(moodTestJSONObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                getWeightTestBySessionData();
                            }
                            else {
                                Toast.makeText(context, "Hubo un error al editar el peso", Toast.LENGTH_LONG).show();
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

    public void getWeightTestBySessionData(){
        AndroidNetworking.get(GroupSportsApiService.WEIGHT_TEST_BY_SESSION_BY_SESSION_WORK(currentSessionWork.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        weightTestBySessions.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                weightTestBySessions.add(WeightTestBySession.toWeightTestBySession(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        weightTestBySessionAdapter.notifyDataSetChanged();
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

    private void setupToolBar() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Test de peso");
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    private void addWeightTestBySession(String weightBefore, String weightAfter){
        JSONObject binnacleDetailJsonObject = new JSONObject();

        try {
            binnacleDetailJsonObject.put("weightBefore", weightBefore);
            binnacleDetailJsonObject.put("weightAfter", weightAfter);
            binnacleDetailJsonObject.put("sessionId", currentSessionWork.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.WEIGHT_TEST_BY_SESSION_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addJSONObjectBody(binnacleDetailJsonObject)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(context.getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                getWeightTestBySessionData();
                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se agrego el test correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }
                            else {
                                Toast.makeText(context, "Error en el sistema", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Hubo un error al agregar el test", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
