package pe.edu.upc.groupsports.activities;

import android.content.Context;
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
import pe.edu.upc.groupsports.adapters.WeeksAdapter;
import pe.edu.upc.groupsports.dialogs.AddWeekDialog;
import pe.edu.upc.groupsports.dialogs.EditMesocycleDialog;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.models.Mesocycle;
import pe.edu.upc.groupsports.models.MesocycleType;
import pe.edu.upc.groupsports.models.SessionWork;
import pe.edu.upc.groupsports.models.Week;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.MesocycleTypesRepository;
import pe.edu.upc.groupsports.util.Funciones;

public class MesocycleDetailActivity extends AppCompatActivity {
    public static int REQUEST_FOR_ACTIVITY_CODE_MESOCYCLE_DETAIL = 4;
    SessionManager session;

    //todo ArcProgressStackView
    public final static int MODEL_COUNT = 4;
    private int[] mStartColors = new int[MODEL_COUNT];
    private int[] mEndColors = new int[MODEL_COUNT];

    //variables
    private Context context;
    private Mesocycle mesocycle;
    private CircleImageView athleteCircleImageView;
    private ArcProgressStackView arcProgressStackView;
    private Week currentWeek;
    private List<Week> currentWeeks;

    RecyclerView weeksRecyclerView;
    WeeksAdapter weeksAdapter;
    RecyclerView.LayoutManager weeksLayoutManager;
    List<SessionWork> currentSessionWorks;
    TextView mesocyclesYearsTextView;
    float porceWeek;
    float porceSession;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;
    SpeedDialView speedDialView;
    String pictureUrl;
    String athleteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesocycle_detail);

        context = this;
        session = new SessionManager(context);
        mesocycle = Mesocycle.from(getIntent().getExtras());
        pictureUrl = getIntent().getStringExtra("pictureUrl");
        athleteId = getIntent().getStringExtra("athleteId");
        athleteCircleImageView = (CircleImageView) findViewById(R.id.athleteCircleImageView);
        arcProgressStackView = (ArcProgressStackView) findViewById(R.id.apsv);
        updateArcProgressStackView(0,0);

        weeksRecyclerView = (RecyclerView) findViewById(R.id.weeksRecyclerView);
        currentWeeks = new ArrayList<>();
        weeksAdapter = new WeeksAdapter(currentWeeks);
        weeksAdapter.setPictureUrl(pictureUrl);
        weeksAdapter.setAthleteId(athleteId);
        weeksLayoutManager = new LinearLayoutManager(context);
        weeksRecyclerView.setAdapter(weeksAdapter);
        weeksRecyclerView.setLayoutManager(weeksLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        mesocyclesYearsTextView = (TextView) findViewById(R.id.mesocyclesYearsTextView);

        speedDialView = findViewById(R.id.speedDial);
        speedDialView.inflate(R.menu.main_menu_inside_mesocycle);
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.action_delete_mesocycle:
                        deleteDialog();
                        return false;
                    case R.id.action_edit_mesocycle:
                        editMCDialog();
                        return false;
                    case R.id.action_add_week:
                        addWeek();
                        return false;
                    default:
                        return false;
                }
            }
        });

        setMesocycleTime();
        updateWeeksData();
        updateAthleteImage();
        updateToolbarTitle(mesocycle.getMesocycleName());
    }

    private void addWeek() {
        final AddWeekDialog addWeekDialog = new AddWeekDialog(context);
        addWeekDialog.show();
        addWeekDialog.setOnCancelButtonClickListener(new AddWeekDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addWeekDialog.dismiss();
            }
        });
        addWeekDialog.setOnOkButtonClickListener(new AddWeekDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(int weekTypeId, String startDate, String endDate, String activityWeek) {
                uploadWeek(weekTypeId, startDate, endDate, activityWeek);
                addWeekDialog.dismiss();
            }
        });
    }

    private void uploadWeek(final int weekTypeId, final String startDate, final String endDate, String activityWeek) {

        JSONObject jsonObjectWeek = new JSONObject();

        try {
            jsonObjectWeek.put("weekTypeId", weekTypeId);
            jsonObjectWeek.put("startDate", startDate);
            jsonObjectWeek.put("endDate", endDate);
            jsonObjectWeek.put("mesocycleId", mesocycle.getId());
            jsonObjectWeek.put("activity", activityWeek);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.WEEKS_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectWeek)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                updateWeeksData();
                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se agrego la semana correctamente", Snackbar.LENGTH_LONG)
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

    private void editMCDialog() {
        final EditMesocycleDialog editMesocycleDialog = new EditMesocycleDialog(
                context,mesocycle.getMesocycleTypeId(),mesocycle.getStartDate(),mesocycle.getEndDate()
        );

        editMesocycleDialog.show();
        editMesocycleDialog.setOnCancelButtonClickListener(new EditMesocycleDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                editMesocycleDialog.dismiss();
            }
        });
        editMesocycleDialog.setOnOkButtonClickListener(new EditMesocycleDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(int mesocycleTypeId, String startDate, String endDate) {
                updateMesocycleData(mesocycleTypeId,startDate,endDate);
                editMesocycleDialog.dismiss();
            }
        });
    }

    private void updateMesocycleData(final int mesocycleTypeId, final String startDate, final String endDate) {

        JSONObject jsonObjectTP = new JSONObject();

        try {
            jsonObjectTP.put("mesocycleTypeId", mesocycleTypeId);
            jsonObjectTP.put("startDate", startDate);
            jsonObjectTP.put("endDate", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(GroupSportsApiService.MESOCYCLES_URL + mesocycle.getId())
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
                                    MesocycleType mt = MesocycleTypesRepository.getInstance().getMesocycleByTypeId(String.valueOf(mesocycleTypeId));
                                    if (mt != null) {
                                        mesocycle.setMesocycleTypeId(mt.getId());
                                        mesocycle.setMesocycleName(mt.getMesocycleName());
                                        if (startDate != null)
                                            mesocycle.setStartDate(Funciones.getDateFromString(startDate));
                                        if (endDate != null)
                                            mesocycle.setEndDate(Funciones.getDateFromString(endDate));
                                        setMesocycleTime();
                                    }
                                    updateToolbarTitle(mesocycle.getMesocycleName());
                                    Snackbar.make(view, "Se editó el mesociclo correctamente", Snackbar.LENGTH_LONG)
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

    private void deleteDialog() {
        final WarningDialog warningDialog = new WarningDialog(context, "Eliminar Mesociclo", "¿Está seguro que desea eliminar el mesociclo?\n\nSe eliminarán todas las semanas y sesiones liagadas es este");
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
        AndroidNetworking.delete(GroupSportsApiService.MESOCYCLES_URL + mesocycle.getId())
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
                                Toast.makeText(context,"Error al eliminar el mesociclo",Toast.LENGTH_LONG).show();
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

    private void setMesocycleTime() {
        if (mesocycle.getStartDate() != null || mesocycle.getEndDate() != null)
            mesocyclesYearsTextView.setText(String.format("%d - %d", Funciones.getYearFromDate(mesocycle.getStartDate()), Funciones.getYearFromDate(mesocycle.getEndDate())));
    }

    private void updateAthleteImage() {
        Picasso.with(context)
                .load(pictureUrl)
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(athleteCircleImageView);
    }

    private void updateWeeksData() {
        AndroidNetworking.get(GroupSportsApiService.WEEKS_BY_MESOCYCLE(mesocycle.getId()))
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

                        if (currentWeeks.size() == 0) {
                            noAthletesConstraintLayout.bringToFront();
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                        } else{
                            noAthletesConstraintLayout.setVisibility(View.GONE);
                        }
                        weeksAdapter.notifyDataSetChanged();

                        //actualizo el porcentaje del mesociclo
                        porceWeek = (getCurrentWeek()/(float)currentWeeks.size())*100;
                        //busco las semanas si se enconcontró un mesociclo en la fecha actual
                        if (currentWeek != null)
                            updateWorkSessions();
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private int getCurrentWeek(){
        Date date = Funciones.getCurrentDate();

        for (int i = 0; i < currentWeeks.size(); i++) {
            Week w = currentWeeks.get(i);
            if (w.getStartDate() != null && w.getEndDate() != null) {
                boolean mayor = (date.after(w.getStartDate()) || Funciones.equalDates(date,w.getStartDate()));
                boolean antes = (date.before(w.getEndDate()) || Funciones.equalDates(date,w.getEndDate()));
                if (mayor && antes) {
                    currentWeek = w;
                    return i + 1;
                }
            }
        }
        //por si ya pasó todas las semanas
        if (currentWeeks.size() > 0) {
            Week ww = currentWeeks.get(currentWeeks.size() - 1);
            if (ww.getEndDate() != null) {
                if (date.after(ww.getEndDate()) || Funciones.equalDates(date, ww.getEndDate())) {
                    updateArcProgressStackView(100, 100);
                }
            }
        }
        return 1;
    }

    private void updateWorkSessions() {
        currentSessionWorks = new ArrayList<>();
        AndroidNetworking.get(GroupSportsApiService.WORKSESSIONS_BY_WEEK(currentWeek.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        currentSessionWorks.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                currentSessionWorks.add(SessionWork.toSessionWork(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //actualizo el porcentaje del mesociclo
                        porceSession = (getWorkSessionsSoFar()/(float)currentSessionWorks.size())*100;
                        updateArcProgressStackView(porceWeek,porceSession);
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private int getWorkSessionsSoFar(){
        int cont = 0;
        for (int i = 0; i < currentSessionWorks.size(); i++) {
            SessionWork sw = currentSessionWorks.get(i);
            Date today = Funciones.getCurrentDate();

            if (today.after(sw.getSessionDay()) || Funciones.equalDates(today,sw.getSessionDay())) {
                cont++;
            }
        }
        return cont;
    }

    private void updateToolbarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(title);
        }
    }
}
