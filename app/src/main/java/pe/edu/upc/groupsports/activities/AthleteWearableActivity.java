package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.dialogs.WearableChartDialog;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

public class AthleteWearableActivity extends AppCompatActivity {

    Button caloriesButton;
    Button stepsButton;
    Button frecuenciaButton;

    SessionManager session;
    MaterialCalendarView materialCalendarView;
    Context context;
    Date currentDate;

    String athleteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_wearable);

        context = this;
        session = new SessionManager(context);
        athleteId = getIntent().getStringExtra("athleteId");

        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                currentDate = calendarDay.getDate();
            }
        });
        currentDate = Funciones.getCurrentDate();
        materialCalendarView.setSelectedDate(currentDate);

        caloriesButton = findViewById(R.id.caloriesButton);
        caloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCalories();
            }
        });
        stepsButton = findViewById(R.id.stepsButton);
        stepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshSteps();
            }
        });
        frecuenciaButton = findViewById(R.id.frecuenciaButton);
        frecuenciaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshFrecuencia();
            }
        });
    }

    public void refreshCalories() {
        String date = Funciones.formatDateForAPI(currentDate);
        AndroidNetworking.get(GroupSportsApiService.ACTIVE_CALORIES_BY_DATE_BY_ATHLETEID(date,athleteId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String totalActiveCalories = "";
                        String activeCaloriesArray = "";
                        List<Float> yValues = new ArrayList<>();
                        try {
                            totalActiveCalories = response.getString("totalActiveCalories");
                            activeCaloriesArray = response.getString("activeCalories");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String[] yValuesArr = activeCaloriesArray.split(",");
                        for (int i = 0; i < yValuesArr.length; i++) {
                            yValues.add(Float.valueOf(yValuesArr[i]));
                        }

                        WearableChartDialog caloriesChartDialog = new WearableChartDialog(
                                context,
                                "Calorías quemadas",
                                totalActiveCalories+" KCal",
                                "Conjunto de calorías registradas",
                                yValues,
                                true
                        );
                        caloriesChartDialog.show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        handleError(anError);
                    }
                });
    }
    public void refreshSteps() {
        String date = Funciones.formatDateForAPI(currentDate);
        AndroidNetworking.get(GroupSportsApiService.STEPS_COUNT_BY_DATE_BY_ATHLETEID(date,athleteId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String totalStepsCount = "";
                        String stepsCountArray = "";
                        List<Integer> yValues = new ArrayList<>();
                        try {
                            totalStepsCount = response.getString("totalStepsCount");
                            stepsCountArray = response.getString("stepsCount");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String[] yValuesArr = stepsCountArray.split(",");
                        for (int i = 0; i < yValuesArr.length; i++) {
                            yValues.add(Integer.valueOf(yValuesArr[i]));
                        }

                        WearableChartDialog stepsChartDialog = new WearableChartDialog(
                                context,
                                "Pasos dados",
                                totalStepsCount,
                                "Conjunto de pasos registradas",
                                yValues
                        );
                        stepsChartDialog.show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        handleError(anError);
                    }
                });
    }
    public void refreshFrecuencia() {
        String date = Funciones.formatDateForAPI(currentDate);
        AndroidNetworking.get(GroupSportsApiService.HEART_RATE_BY_DATE_BY_ATHLETEID(date,athleteId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String averageHeartRate = "";
                        String heartRatesArray = "";
                        List<Integer> yValues = new ArrayList<>();
                        try {
                            averageHeartRate = response.getString("averageHeartRate");
                            heartRatesArray = response.getString("heartRates");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String[] yValuesArr = heartRatesArray.split(",");
                        for (int i = 0; i < yValuesArr.length; i++) {
                            yValues.add(Integer.valueOf(yValuesArr[i]));
                        }

                        WearableChartDialog heartRatesChartDialog = new WearableChartDialog(
                                context,
                                "Frecuencia cardiaca media",
                                averageHeartRate + " lat/min",
                                "Frecuencias cardiacas registradas",
                                yValues
                        );
                        heartRatesChartDialog.show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        handleError(anError);
                    }
                });
    }
    public void handleError(ANError anError){
        if (anError.getErrorCode() == 0) {
            final WarningDialog warningDialog = new WarningDialog(
                    context,
                    "Mensaje",
                    "No se registró contenido del wearable",
                    "Ok",
                    true
            );
            warningDialog.show();
            warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
                @Override
                public void OnOkButtonClicked() {
                    warningDialog.dismiss();
                }
            });
        }
    }
}
