package pe.edu.upc.groupsports.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.SessionWorkAdapter;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.models.SessionWork;
import pe.edu.upc.groupsports.models.Week;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;
import pe.edu.upc.groupsports.widget.PercentageCropImageView;

public class WeekDetailActivity extends AppCompatActivity {
    public static int REQUEST_FOR_ACTIVITY_CODE_WEEK_DETAIL = 33;
    SessionManager session;
    Week currentWeek;
    MaterialCalendarView materialCalendarView;
    Context context;
    String pictureUrl;
    PercentageCropImageView athleteImageView;

    RecyclerView sessionWorksRecyclerView;
    SessionWorkAdapter sessionWorkAdapter;
    RecyclerView.LayoutManager sessionWorkLayoutManager;
    List<SessionWork> currentSessionWorksCurrentWeek;
    List<SessionWork> currentSessionWorksToday;

    Date currentDate;
    String athleteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_detail);

        context = this;
        session = new SessionManager(context);
        currentWeek = Week.from(getIntent().getExtras());
        pictureUrl = getIntent().getStringExtra("pictureUrl");
        athleteId = getIntent().getStringExtra("athleteId");
        updateToolbarTitle(currentWeek.getActivity());

        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.state()
                .edit()
                .setMinimumDate(currentWeek.getStartDate())
                .setMaximumDate(currentWeek.getEndDate())
                .commit();
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                currentDate = calendarDay.getDate();
                refreshTodaySessions();
                sessionWorkAdapter.notifyDataSetChanged();
            }
        });
        materialCalendarView.setSelectedDate(currentWeek.getStartDate());
        currentDate = currentWeek.getStartDate();

        athleteImageView = findViewById(R.id.athleteImageView);
        updateAthleteImage();

        sessionWorksRecyclerView = (RecyclerView) findViewById(R.id.sessionWorksRecyclerView);
        currentSessionWorksCurrentWeek = new ArrayList<>();
        currentSessionWorksToday = new ArrayList<>();
        sessionWorkAdapter = new SessionWorkAdapter(currentSessionWorksToday);
        sessionWorkLayoutManager = new LinearLayoutManager(context);
        sessionWorksRecyclerView.setAdapter(sessionWorkAdapter);
        sessionWorksRecyclerView.setLayoutManager(sessionWorkLayoutManager);
        sessionWorkAdapter.setOnAddSessionListener(new SessionWorkAdapter.OnAddSessionListener() {
            @Override
            public void OnAddSessionClicked(int shiftId) {
                addWorkSession(shiftId);
            }
        });
        sessionWorkAdapter.setOnDeleteSessionListener(new SessionWorkAdapter.OnDeleteSessionListener() {
            @Override
            public void OnDeleteSessionClicked(SessionWork sessionWork) {
                handleDeleteWorkSession(sessionWork);
            }
        });

        updateWorkSessions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BinnacleActivity.REQUEST_FOR_ACTIVITY_CODE_BINNACLE_DETAIL) {
            updateWorkSessions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_week, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete_week){
            warningDeleteWeek();
        }else{
            finish();
        }
        return true;
    }

    private void handleDeleteWorkSession(SessionWork sessionWork){
        if (sessionWork.getBinnacleDetails().size() == 0) {
            deleteWorkSession(sessionWork.getId());
        }
        else {
            warningDeleteSession(sessionWork.getId());
        }
    }

    private void warningDeleteSession(final String workSessionId){
        final WarningDialog warningDialog = new WarningDialog(
                this,
                "Eliminar sesión",
                "¿Está seguro que desea eliminar esta sesión?\n\nSe eliminarán todas las entradas de la bitacora ligadas es esta sesión"
        );
        warningDialog.show();
        warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                deleteWorkSession(workSessionId);
                warningDialog.dismiss();
            }
        });
        warningDialog.setOnCancelButtonClickListener(new WarningDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                warningDialog.dismiss();
            }
        });
    }

    private void warningDeleteWeek(){
        final WarningDialog warningDialog = new WarningDialog(this,"Eliminar semana","¿Está seguro que desea eliminar esta semana?\n\nSe eliminarán todas sesiones ligadas es esta");
        warningDialog.show();
        warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                deleteWeek();
                warningDialog.dismiss();
            }
        });
        warningDialog.setOnCancelButtonClickListener(new WarningDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                warningDialog.dismiss();
            }
        });
    }

    private void deleteWeek() {
        AndroidNetworking.delete(GroupSportsApiService.WEEKS_URL + currentWeek.getId())
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
                                finish();
                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se eliminó la semana correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar la semana",Toast.LENGTH_LONG).show();
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

    private void deleteWorkSession(String workSessionId) {
        AndroidNetworking.delete(GroupSportsApiService.WORK_SESSIONS_URL + workSessionId)
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
                                updateWorkSessions();
                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se eliminó la sesión correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar la sessión",Toast.LENGTH_LONG).show();
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

    private void addWorkSession(final int shiftId) {

        JSONObject jsonObjectWeekSession = new JSONObject();

        try {
            jsonObjectWeekSession.put("sessionDay", Funciones.formatDateForAPI(currentDate));//2018-05-06
            jsonObjectWeekSession.put("shiftId", shiftId);
            jsonObjectWeekSession.put("weekId", currentWeek.getId());
            jsonObjectWeekSession.put("coachId", session.getuserLoggedTypeId());
            jsonObjectWeekSession.put("athleteId", athleteId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.WORK_SESSIONS_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectWeekSession)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                updateWorkSessions();
                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se agrego la sesión correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }
                            else {
                                Toast.makeText(context, "Hubo un error al agregar la sesión", Toast.LENGTH_LONG).show();
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

    private void updateWorkSessions() {
        AndroidNetworking.get(GroupSportsApiService.WORKSESSIONS_BY_WEEK(currentWeek.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        currentSessionWorksCurrentWeek.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                currentSessionWorksCurrentWeek.add(SessionWork.toSessionWorkForBinnacle(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        refreshTodaySessions();
                        sessionWorkAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void refreshTodaySessions(){
        currentSessionWorksToday.clear();
        currentSessionWorksToday.add(null);//mañana = 0
        currentSessionWorksToday.add(null);//tarde = 1
        currentSessionWorksToday.add(null);//noche = 2

        //lleno las sesiones si no hay sesion registrada para un turno
        for (int i = 0; i < currentSessionWorksCurrentWeek.size(); i++) {
            SessionWork sw = currentSessionWorksCurrentWeek.get(i);
            if (Funciones.equalDates(sw.getSessionDay(),currentDate)){
                currentSessionWorksToday.set(Integer.parseInt(sw.getShiftId()) - 1, sw);
            }
        }
    }

    private void updateToolbarTitle(String title){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(title);
        }
    }

    private void updateAthleteImage() {
        Picasso.with(context)
                .load(pictureUrl)
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(athleteImageView);
    }
}
