package pe.edu.upc.groupsports.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import devlight.io.library.ArcProgressStackView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.MesocyclesAdapter;
import pe.edu.upc.groupsports.dialogs.AddMesocycleDialog;
import pe.edu.upc.groupsports.dialogs.EditTrainingPlanDialog;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.models.Mesocycle;
import pe.edu.upc.groupsports.models.TrainingPlan;
import pe.edu.upc.groupsports.models.Week;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

public class TrainingPlanDetailActivity extends AppCompatActivity {
    public static int REQUEST_FOR_ACTIVITY_CODE_TRAINING_PLAN_DETAIL = 69;
    SessionManager session;

    //todo ArcProgressStackView
    public final static int MODEL_COUNT = 4;
    private int[] mStartColors = new int[MODEL_COUNT];
    private int[] mEndColors = new int[MODEL_COUNT];

    //variables
    private Context context;
    private TrainingPlan trainingPlan;
    private CircleImageView athleteCircleImageView;
    private ArcProgressStackView arcProgressStackView;
    private Mesocycle currentMesocycle;
    private List<Week> currentWeeks;

    RecyclerView mesocyclesRecyclerView;
    MesocyclesAdapter mesocyclesAdapter;
    RecyclerView.LayoutManager mesocyclesLayoutManager;
    List<Mesocycle> mesocycles;
    TextView trainingPlanYearsTextView;
    float porceMesociclo;
    float porceWeek;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    SpeedDialView speedDialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plan_detail);

        context = this;
        session = new SessionManager(context);
        trainingPlan = TrainingPlan.from(getIntent().getExtras());
        athleteCircleImageView = (CircleImageView) findViewById(R.id.athleteCircleImageView);
        arcProgressStackView = (ArcProgressStackView) findViewById(R.id.apsv);
        updateArcProgressStackView(0,0);

        mesocyclesRecyclerView = (RecyclerView) findViewById(R.id.mesocyclesRecyclerView);
        mesocycles = new ArrayList<>();
        mesocyclesAdapter = new MesocyclesAdapter(mesocycles);
        mesocyclesAdapter.setPictureUrl(trainingPlan.getAthletePictureUrl());
        mesocyclesAdapter.setAthleteId(trainingPlan.getAthleteId());
        mesocyclesLayoutManager = new LinearLayoutManager(context);
        mesocyclesRecyclerView.setAdapter(mesocyclesAdapter);
        mesocyclesRecyclerView.setLayoutManager(mesocyclesLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        trainingPlanYearsTextView = (TextView) findViewById(R.id.trainingPlanYearsTextView);

        speedDialView = findViewById(R.id.speedDial);
        speedDialView.inflate(R.menu.main_menu_inside_training_plan);
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.action_delete_plan:
                        deleteDialog();
                        return false;
                    case R.id.action_edit_plan:
                        editTPDialog();
                        return false;
                    case R.id.action_add_mesocycle:
                        addMesocycle();
                        return false;
                    default:
                        return false;
                }
            }
        });

        setTrainingPlanYears();
        updateMesocyclesData();
        updateAthleteImage();
        updateToolbarTitle(trainingPlan.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MesocycleDetailActivity.REQUEST_FOR_ACTIVITY_CODE_MESOCYCLE_DETAIL) {
            if(resultCode == Activity.RESULT_OK){
                View view = getCurrentFocus();
                if (view != null) {
                    updateMesocyclesData();
                    Snackbar.make(view, "Mesociclo eliminado correctamente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            }
            else {
                updateMesocyclesData();
            }
        }
    }

    private void addMesocycle() {
        final AddMesocycleDialog addMesocycleDialog = new AddMesocycleDialog(context);
        addMesocycleDialog.show();
        addMesocycleDialog.setOnCancelButtonClickListener(new AddMesocycleDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addMesocycleDialog.dismiss();
            }
        });
        addMesocycleDialog.setOnOkButtonClickListener(new AddMesocycleDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(int mesocycleTypeId, String startDate, String endDate) {
                addMesocycleDialog.dismiss();
                uploadMesocycle(mesocycleTypeId,startDate,endDate);
            }
        });
    }

    private void uploadMesocycle(final int mesocycleTypeId, final String startDate, final String endDate) {

        JSONObject jsonObjectTP = new JSONObject();

        try {
            jsonObjectTP.put("trainingPlanId", trainingPlan.getId());
            jsonObjectTP.put("numberOfIntenseWeeks", 0);
            jsonObjectTP.put("numberOfRestWeeks", 0);
            jsonObjectTP.put("startDate", startDate);
            jsonObjectTP.put("endDate", endDate);
            jsonObjectTP.put("mesocycleTypeId", mesocycleTypeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.MESOCYCLES_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectTP)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                updateMesocyclesData();
                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se agrego el mesociclo correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
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

    private void editTPDialog(){
        final EditTrainingPlanDialog editTrainingPlanDialog = new EditTrainingPlanDialog(
                context,trainingPlan.getName(),trainingPlan.getStartDate(),trainingPlan.getEndDate()
        );
        editTrainingPlanDialog.show();
        editTrainingPlanDialog.setOnCancelButtonClickListener(new EditTrainingPlanDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                editTrainingPlanDialog.dismiss();
            }
        });
        editTrainingPlanDialog.setOnOkButtonClickListener(new EditTrainingPlanDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String trainingPlanName, String startDate, String endDate) {
                uploadTrainingPlan(trainingPlanName, startDate, endDate);
                editTrainingPlanDialog.dismiss();
            }
        });
    }

    private void uploadTrainingPlan(final String trainingPlanName, final String startDate, final String endDate) {

        JSONObject jsonObjectTP = new JSONObject();

        try {
            jsonObjectTP.put("name", trainingPlanName);
            jsonObjectTP.put("coachId", session.getuserLoggedTypeId());
            jsonObjectTP.put("athleteId", trainingPlan.getAthleteId());
            jsonObjectTP.put("startDate", startDate);
            jsonObjectTP.put("endDate", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(GroupSportsApiService.TRAINING_PLANS_URL + trainingPlan.getId())
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectTP)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                View view = getCurrentFocus();
                                if (view != null) {
                                    trainingPlan.setName(trainingPlanName);
                                    if (startDate != null)
                                        trainingPlan.setStartDate(Funciones.getDateFromString(startDate));
                                    if (endDate != null)
                                        trainingPlan.setEndDate(Funciones.getDateFromString(endDate));
                                    setTrainingPlanYears();

                                    updateToolbarTitle(trainingPlanName);
                                    Snackbar.make(view, "Se editó el plan correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
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

    private void deleteDialog(){
        final WarningDialog warningDialog = new WarningDialog(context, "Eliminar Plan", "¿Está seguro que desea eliminar el plan?\n\nSe eliminarán todos los mesociclos, semanas y sesiones liagados es este");
        warningDialog.show();
        warningDialog.setOnCancelButtonClickListener(new WarningDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                warningDialog.dismiss();
            }
        });
        warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                deleteTrainingPlan();
            }
        });
    }

    private void deleteTrainingPlan() {
        AndroidNetworking.delete(GroupSportsApiService.TRAINING_PLANS_URL+trainingPlan.getId())
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
                                setResult(RESULT_OK);
                                finish();
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar el plan",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void setTrainingPlanYears() {
        if (trainingPlan.getStartDate() != null || trainingPlan.getEndDate() != null)
            trainingPlanYearsTextView.setText(String.format("%d - %d", Funciones.getYearFromDate(trainingPlan.getStartDate()), Funciones.getYearFromDate(trainingPlan.getEndDate())));
    }

    private void updateMesocyclesData() {
        AndroidNetworking.get(GroupSportsApiService.MESOCYCLES_BY_TRAINING_PLAN(trainingPlan.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mesocycles.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                mesocycles.add(Mesocycle.toMesocycle(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (mesocycles.size() == 0) {
                            noAthletesConstraintLayout.bringToFront();
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                        } else{
                            noAthletesConstraintLayout.setVisibility(View.GONE);
                        }
                        mesocyclesAdapter.notifyDataSetChanged();

                        //actualizo el porcentaje del mesociclo
                        porceMesociclo = (getCurrentMesociclo()/(float)mesocycles.size())*100;
                        //busco las semanas si se enconcontró un mesociclo en la fecha actual
                        if (currentMesocycle != null)
                            updateWeek();
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void updateWeek() {
        currentWeeks = new ArrayList<>();
        AndroidNetworking.get(GroupSportsApiService.WEEKS_BY_MESOCYCLE(currentMesocycle.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        currentWeeks.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                currentWeeks.add(Week.toWeek(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //actualizo el porcentaje del mesociclo
                        porceWeek = (getCurrentWeek()/(float)currentWeeks.size())*100;
                        updateArcProgressStackView(porceMesociclo,porceWeek);
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void updateAthleteImage() {
        Picasso.with(context)
                .load(trainingPlan.getAthletePictureUrl())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(athleteCircleImageView);
    }

    private void updateToolbarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(title);
        }
    }

    private void updateArcProgressStackView(float porceMesociclo, float porceWeek){
        // Get colors
        final String[] startColors = getResources().getStringArray(R.array.startColors);
        final String[] endColors = getResources().getStringArray(R.array.endColors);
        final String[] bgColors = getResources().getStringArray(R.array.bgColors);

        // Parse colors
        for (int i = 0; i < MODEL_COUNT; i++) {
            mStartColors[i] = Color.parseColor(startColors[i]);
            mEndColors[i] = Color.parseColor(endColors[i]);
        }

        ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new ArcProgressStackView.Model("", porceMesociclo, Color.parseColor(bgColors[0]), mStartColors[0]));
        models.add(new ArcProgressStackView.Model("", porceWeek, Color.parseColor(bgColors[1]), mStartColors[1]));
        arcProgressStackView.setModels(models);
        arcProgressStackView.animateProgress();
    }

    private int getCurrentMesociclo(){
        Date date = Funciones.getCurrentDate();
        for (int i = 0; i < mesocycles.size(); i++) {
            Mesocycle m = mesocycles.get(i);

            if (m.getStartDate() != null && m.getEndDate() != null) {
                if ((date.after(m.getStartDate()) || date.equals(m.getStartDate())) &&
                        (date.before(m.getEndDate()) || date.equals(m.getEndDate()))) {
                    currentMesocycle = m;
                    return i + 1;
                }
            }
        }
        if (mesocycles.size() > 0) {
            //por si ya pasó todas las semanas
            Mesocycle mm = mesocycles.get(mesocycles.size() - 1);
            if (mm.getEndDate() != null) {
                if (date.after(mm.getEndDate()) || Funciones.equalDates(date, mm.getEndDate())) {
                    updateArcProgressStackView(100, 100);
                }
            }
        }
        return 1;
    }

    private int getCurrentWeek(){
        for (int i = 0; i < currentWeeks.size(); i++) {
            Week w = currentWeeks.get(i);
            Date date = Funciones.getCurrentDate();

            if ((date.after(w.getStartDate()) || date.equals(w.getStartDate())) &&
                    (date.before(w.getEndDate()) || date.equals(w.getEndDate()))) {
                return i + 1;
            }
        }
        return 1;
    }
}





















