package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.AthleteAdapter;
import pe.edu.upc.groupsports.adapters.ChooseAthleteAdapter;
import pe.edu.upc.groupsports.adapters.TeamAdapter;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.Team;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Constantes;

public class AddTrainingPlanActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public static int REQUEST_FOR_ACTIVITY_CODE_ADD_TRAINING_PLAN = 666;

    SessionManager session;
    Context context;

    RecyclerView athletesRecyclerView;
    ChooseAthleteAdapter athletesAdapter;
    RecyclerView.LayoutManager athletesLayoutManager;
    List<Athlete> athletes;

    CardView startDateCarddView;
    TextView startDateTextView;
    CardView endDateCarddView;
    TextView endDateTextView;

    CardView addTrainingPlanCarddView;
    EditText trainingPlanNameEditText;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    private DatePickerDialog dpd;
    private String dateState;
    private String dateStateResult;
    private String startDateStateResult;
    private String endDateStateResult;
    private static String startDateState = "Fecha inicial";
    private static String endDateState = "Fecha final";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training_plan);

        context = this;
        session = new SessionManager(this);
        trainingPlanNameEditText = (EditText) findViewById(R.id.trainingPlanNameEditText);
        athletesRecyclerView = (RecyclerView) findViewById(R.id.chooseAthleteRecyclerView);
        athletes = new ArrayList<>();
        athletesAdapter = new ChooseAthleteAdapter(athletes);
        athletesLayoutManager = new GridLayoutManager(this, 3);
        athletesRecyclerView.setAdapter(athletesAdapter);
        athletesRecyclerView.setLayoutManager(athletesLayoutManager);

        startDateCarddView = (CardView) findViewById(R.id.startDateCarddView);
        startDateCarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateState = startDateState;
                showCalendar(dateState);
            }
        });
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateCarddView = (CardView) findViewById(R.id.endDateCarddView);
        endDateCarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateState = endDateState;
                showCalendar(dateState);
            }
        });
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);

        addTrainingPlanCarddView = (CardView) findViewById(R.id.addTrainingPlanCarddView);
        addTrainingPlanCarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarTrainingPlan();
            }
        });

        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        updateData();
    }

    private void registrarTrainingPlan(){
        if (trainingPlanNameEditText.getText().length() == 0 ||
                athletesAdapter.getSelectedAthlete() == null){
            Toast.makeText(this,"Escriba un nombre y elija un atleta",Toast.LENGTH_LONG).show();
        }
        else {
            JSONObject jsonObjectTP = new JSONObject();

            try {

                jsonObjectTP.put("name", trainingPlanNameEditText.getText().toString());
                jsonObjectTP.put("coachId", session.getuserLoggedTypeId());
                jsonObjectTP.put("athleteId", athletesAdapter.getSelectedAthlete().getId());
                jsonObjectTP.put("startDate", startDateStateResult);
                jsonObjectTP.put("endDate", endDateStateResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(GroupSportsApiService.TRAINING_PLANS_URL)
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
                                if (response.getString("response").equals("Ok")){
                                    setResult(RESULT_OK);
                                    finish();
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
    }

    public void showCalendar(String estado){
        Calendar now = Calendar.getInstance();
        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    AddTrainingPlanActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    AddTrainingPlanActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }
        //dpd.setThemeDark(true);
        //dpd.dismissOnPause(true);
        //dpd.showYearPickerFirst(true);
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(Color.parseColor("#FF9800"));
        dpd.setTitle(estado);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }

    public void updateData(){
        int coachId = Integer.parseInt(session.getuserLoggedTypeId());
        AndroidNetworking.get(GroupSportsApiService.ATHELETES_BY_COACH_URL(coachId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athletes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                athletes.add(Athlete.toAthlete(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        athletesAdapter.notifyDataSetChanged();
                        if (response.length() == 0) {
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                        } else {
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateStateResult = year+"-"+(++monthOfYear)+"-"+dayOfMonth;
        String dateFormated = dayOfMonth+"/"+String.format("%02d",monthOfYear)+"/"+year;
        if (dateState.equals(startDateState)){
            startDateStateResult = dateStateResult;
            startDateTextView.setText(dateFormated);
        }
        if (dateState.equals(endDateState)){
            endDateStateResult = dateStateResult;
            endDateTextView.setText(dateFormated);
        }
    }
}
