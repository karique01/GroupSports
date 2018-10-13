package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.dialogs.AddAntropometricTestDialog;
import pe.edu.upc.groupsports.dialogs.AddAthleteAchievementDialog;
import pe.edu.upc.groupsports.dialogs.AddSaltabilityTestDialog;
import pe.edu.upc.groupsports.dialogs.AddShotPutTestDialog;
import pe.edu.upc.groupsports.dialogs.AddSpeedTestDialog;
import pe.edu.upc.groupsports.dialogs.AddStrengthTestDialog;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.fragments.AthleteAchievementFragment;
import pe.edu.upc.groupsports.fragments.AthleteDetailsFragment;
import pe.edu.upc.groupsports.fragments.AthleteTestFragment;
import pe.edu.upc.groupsports.fragments.AthletesStatisticsFragment;
import pe.edu.upc.groupsports.fragments.FodaFragment;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

public class AthleteDetailActivity extends AppCompatActivity {
    private Athlete currentAthlete;
    private FloatingActionButton speedDialView;
    private SessionManager session;
    private AthleteTestFragment athleteTestFragment;
    private AthletesStatisticsFragment athStaFragment;
    private AthleteDetailsFragment athleteDetailsFragment;
    private FodaFragment fodaFragment;
    private AthleteAchievementFragment athleteAchievementFragment;
    private Context context;
    private int currentFragmentId = R.id.navigation_athlete_statistics;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return navigateAcordingTo(item.getItemId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_detail);

        context = this;
        currentAthlete = Athlete.from(getIntent().getExtras());
        session = new SessionManager(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        speedDialView = findViewById(R.id.speedDial);
        speedDialView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragmentId == R.id.navigation_athlete_test) {
                    if (athleteTestFragment != null) {
                        switch (athleteTestFragment.getCurrentTestFragmentSelected()) {
                            case AthleteTestFragment.MOOD_TEST_FRAGMENT_SELECTED:
                                addMoodTestDialog();
                                break;
                            case AthleteTestFragment.SPEED_TEST_FRAGMENT_SELECTED:
                                addSpeedTestDialog();
                                break;
                            case AthleteTestFragment.SALTABILITY_TEST_FRAGMENT_SELECTED:
                                showAddSaltabilityTestDialog();
                                break;
                            case AthleteTestFragment.STRENGTH_TEST_FRAGMENT_SELECTED:
                                showAddStrengthTestDialog();
                                break;
                            case AthleteTestFragment.SHOT_PUT_TEST_FRAGMENT_SELECTED:
                                addShotPutTestDialog();
                                break;
                            case AthleteTestFragment.WEIGHT_TEST_BY_SESSION_FRAGMENT_SELECTED:
                                chooseSessionWork();
                                break;
                            case AthleteTestFragment.ANTROPOMETRIC_TEST_FRAGMENT_SELECTED:
                                showAddAntropometricTestDialog();
                                break;
                            default:
                                break;
                        }
                    }
                }
                else if (currentFragmentId == R.id.navigation_athlete_achievements){
                    showAddAthleteAchievement();
                }
            }
        });

        modifyToolbar();
        navigateAcordingTo(R.id.navigation_athlete_statistics);
    }

    private void showAddAthleteAchievement() {
        final AddAthleteAchievementDialog addAthleteAchievementDialog = new AddAthleteAchievementDialog(context);
        addAthleteAchievementDialog.show();
        addAthleteAchievementDialog.setOnOkButtonClickListener(new AddAthleteAchievementDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String place, String description, String athleteAchievementTypeId, String resultTime, String resultDistance) {
                addAthleteAchievementDialog.dismiss();
                uploadAthleteAchievement(place, description, athleteAchievementTypeId, resultTime, resultDistance);
            }
        });
        addAthleteAchievementDialog.setOnCancelButtonClickListener(new AddAthleteAchievementDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addAthleteAchievementDialog.dismiss();
            }
        });
    }

    private void uploadAthleteAchievement(String place, String description, String athleteAchievementTypeId, String resultTime, String resultDistance) {
        JSONObject jsonObjectAchievement = new JSONObject();

        try {
            jsonObjectAchievement.put("place", place);
            jsonObjectAchievement.put("description", description);
            jsonObjectAchievement.put("athleteId", currentAthlete.getId());
            jsonObjectAchievement.put("athleteAchievementTypeId", athleteAchievementTypeId);
            jsonObjectAchievement.put("resultTime", resultTime);
            jsonObjectAchievement.put("resultDistance", resultDistance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.ATHLETE_ACHIEVEMENT_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectAchievement)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                showSnackBar("Se agregó el logro correctamente");
                                if (athleteAchievementFragment != null){
                                    athleteAchievementFragment.getAchievementsData();
                                }
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

    private void showAddStrengthTestDialog(){
        final AddStrengthTestDialog addStrengthTestDialog = new AddStrengthTestDialog(context);
        addStrengthTestDialog.show();
        addStrengthTestDialog.setOnOkButtonClickListener(new AddStrengthTestDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String maxRepetitionWeightValue, String strengthTestTypeId, String date) {
                addStrengthTestDialog.dismiss();
                uploadStrengthTest(maxRepetitionWeightValue, strengthTestTypeId, date);
            }
        });
        addStrengthTestDialog.setOnCancelButtonClickListener(new AddStrengthTestDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addStrengthTestDialog.dismiss();
            }
        });
    }

    private void uploadStrengthTest(String maxRepetitionWeightValue, String strengthTestTypeId, String date){
        JSONObject jsonObjectSaltaTest = new JSONObject();

        try {
            jsonObjectSaltaTest.put("maxRepetitionWeightValue", maxRepetitionWeightValue);
            jsonObjectSaltaTest.put("strengthTestTypeId", strengthTestTypeId);
            jsonObjectSaltaTest.put("date", date);
            jsonObjectSaltaTest.put("athleteId", currentAthlete.getId());
            jsonObjectSaltaTest.put("coachId", session.getuserLoggedTypeId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.STRENGTH_TEST_SESSION_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectSaltaTest)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                showSnackBar("Se agregó el test correctamente");
                                if (athleteTestFragment != null){
                                    athleteTestFragment.athleteStrengthTestFragment.updateStrengthTestData();
                                }
                            } else {
                                Toast.makeText(context,"Error al agregar el test",Toast.LENGTH_LONG).show();
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

    private void showAddSaltabilityTestDialog(){
        final AddSaltabilityTestDialog addSaltabilityTestDialog = new AddSaltabilityTestDialog(context);
        addSaltabilityTestDialog.show();
        addSaltabilityTestDialog.setOnOkButtonClickListener(new AddSaltabilityTestDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String distanceResult, String jumpTestTypeId, String date) {
                addSaltabilityTestDialog.dismiss();
                uploadSaltabilityTest(distanceResult, jumpTestTypeId, date);
            }
        });
        addSaltabilityTestDialog.setOnCancelButtonClickListener(new AddSaltabilityTestDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addSaltabilityTestDialog.dismiss();
            }
        });
    }

    private void uploadSaltabilityTest(String distanceResult, String jumpTestTypeId, String date){
        JSONObject jsonObjectSaltaTest = new JSONObject();

        try {
            jsonObjectSaltaTest.put("distanceResult", distanceResult);
            jsonObjectSaltaTest.put("jumpTestTypeId", jumpTestTypeId);
            jsonObjectSaltaTest.put("date", date);
            jsonObjectSaltaTest.put("athleteId", currentAthlete.getId());
            jsonObjectSaltaTest.put("coachId", session.getuserLoggedTypeId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.SALTABILITY_TEST_SESSION_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectSaltaTest)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                showSnackBar("Se agregó el test correctamente");
                                if (athleteTestFragment != null){
                                    if (athleteTestFragment.athleteSaltabilityTestFragment != null) {
                                        athleteTestFragment.athleteSaltabilityTestFragment.updateSaltabilityTestData();
                                    }
                                }
                            } else {
                                Toast.makeText(context,"Error al agregar el test",Toast.LENGTH_LONG).show();
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

    private void showAddAntropometricTestDialog(){
        final AddAntropometricTestDialog addAntropometricTestDialog = new AddAntropometricTestDialog(context);
        addAntropometricTestDialog.show();
        addAntropometricTestDialog.setOnOkButtonClickListener(new AddAntropometricTestDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String size, String weight, String wingspan, String bodyFatPercentage, String leanBodyPercentage, String date) {
                uploadAntropometricTest(size, weight, wingspan, bodyFatPercentage, leanBodyPercentage, date);
                addAntropometricTestDialog.dismiss();
            }
        });
        addAntropometricTestDialog.setOnCancelButtonClickListener(new AddAntropometricTestDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addAntropometricTestDialog.dismiss();
            }
        });
    }

    private void uploadAntropometricTest(String size, String weight, String wingspan, String bodyFatPercentage, String leanBodyPercentage, String date){
        JSONObject jsonObjectMoodTest = new JSONObject();

        try {
            jsonObjectMoodTest.put("size", size);
            jsonObjectMoodTest.put("weight", weight);
            jsonObjectMoodTest.put("wingspan", wingspan);
            jsonObjectMoodTest.put("bodyFatPercentage", bodyFatPercentage);
            jsonObjectMoodTest.put("leanBodyPercentage", leanBodyPercentage);
            jsonObjectMoodTest.put("date", date);
            jsonObjectMoodTest.put("athleteId", currentAthlete.getId());
            jsonObjectMoodTest.put("coachId", session.getuserLoggedTypeId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.ANTROPOMETRIC_TEST_SESSION_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectMoodTest)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                showSnackBar("Se agregó el test correctamente");
                                if (athleteTestFragment != null){
                                    athleteTestFragment.antropometricTestFragment.updateAntropometricTestData();
                                }
                            } else {
                                Toast.makeText(context,"Error al agregar el test",Toast.LENGTH_LONG).show();
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

    private void chooseSessionWork(){
        Intent intent = new Intent(context,ChooseSessionWorkActivity.class);
        startActivity(intent);
    }

    private void modifyToolbar(){
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white);
            actionBar.setTitle(currentAthlete.getFullName());
        }
    }

    private void addShotPutTestDialog() {
        final AddShotPutTestDialog addShotPutTestDialog = new AddShotPutTestDialog(context);
        addShotPutTestDialog.show();
        addShotPutTestDialog.setOnOkButtonClickListener(new AddShotPutTestDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String resultMeters, String weightBall, String shotPutType) {
                uploadShotPutTest(resultMeters, weightBall, shotPutType);
                addShotPutTestDialog.dismiss();
            }
        });
        addShotPutTestDialog.setOnCancelButtonClickListener(new AddShotPutTestDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addShotPutTestDialog.dismiss();
            }
        });
    }

    private void uploadShotPutTest(String resultMeters, String weightBall, String shotPutType){
        JSONObject jsonObjectShotPutTest = new JSONObject();

        try {
            jsonObjectShotPutTest.put("result", resultMeters);
            jsonObjectShotPutTest.put("ballWeight", weightBall);
            jsonObjectShotPutTest.put("shotPutTypeId", shotPutType);
            jsonObjectShotPutTest.put("coachId", session.getuserLoggedTypeId());
            jsonObjectShotPutTest.put("athleteId", currentAthlete.getId());
            jsonObjectShotPutTest.put("date", Funciones.formatDateForAPIWithHour(Funciones.getCurrentDate()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.SHOT_PUT_TEST_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectShotPutTest)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                showSnackBar("Se agregó el test correctamente");
                                if (athleteTestFragment != null){
                                    athleteTestFragment.shotPutTestFragment.updateData();
                                }
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

    private void addMoodTestDialog() {
        final WarningDialog warningDialog = new WarningDialog(
                context,
                "Test de estado de animo",
                "Se enviará un test \n\n¿Desea programarle un test de estado de animo a este atleta?",
                context.getString(R.string.send)
        );
        warningDialog.show();
        warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                uploadMoodTest();
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

    private void uploadMoodTest(){
        JSONObject jsonObjectMoodTest = new JSONObject();

        try {
            jsonObjectMoodTest.put("dayOfMood", Funciones.formatDateForAPIWithHour(Funciones.getCurrentDate()));
            jsonObjectMoodTest.put("athleteId", currentAthlete.getId());
            jsonObjectMoodTest.put("coachId", session.getuserLoggedTypeId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.MOOD_TEST_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectMoodTest)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                showSnackBar("Se agregó el test correctamente");
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

    private void addSpeedTestDialog() {
        final AddSpeedTestDialog addSpeedTestDialog = new AddSpeedTestDialog(context);
        addSpeedTestDialog.setCancelable(false);
        addSpeedTestDialog.show();
        addSpeedTestDialog.setOnCancelButtonClickListener(new AddSpeedTestDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addSpeedTestDialog.dismiss();
            }
        });
        addSpeedTestDialog.setOnOkButtonClickListener(new AddSpeedTestDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String hours, String minutes, String seconds, String milliseconds, String meters) {
                uploadSpedTest(hours, minutes, seconds, milliseconds, meters);
                addSpeedTestDialog.dismiss();
            }
        });
    }

    private void uploadSpedTest(String hours, String minutes, String seconds, String milliseconds, String meters) {

        JSONObject jsonObjectSpeedTest = new JSONObject();

        try {
            jsonObjectSpeedTest.put("hours", hours);
            jsonObjectSpeedTest.put("minutes", minutes);
            jsonObjectSpeedTest.put("seconds", seconds);
            jsonObjectSpeedTest.put("milliseconds", milliseconds);
            jsonObjectSpeedTest.put("meters", meters);
            jsonObjectSpeedTest.put("coachId", session.getuserLoggedTypeId());
            jsonObjectSpeedTest.put("athleteId", currentAthlete.getId());
            jsonObjectSpeedTest.put("date", Funciones.formatDateForAPI(Funciones.getCurrentDate()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.SPEED_TEST_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectSpeedTest)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                showSnackBar("Se agregó el test correctamente");
                                if (athleteTestFragment != null){
                                    athleteTestFragment.athleteSpeedTestFragment.updateData();
                                }
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

    private void showSnackBar(String message){
        View view = findViewById(android.R.id.content);
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    }

    boolean navigateAcordingTo(int id){
        try
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content,getFragmentFor(id))
                    .commit();
            return true;
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    Fragment getFragmentFor(int id) {
        currentFragmentId = id;
        if (id == R.id.navigation_athlete_statistics) {
            speedDialView.hide();
            athStaFragment = new AthletesStatisticsFragment();
            athStaFragment.setCurrentAthlete(currentAthlete);
            return athStaFragment;
        }
        else if (id == R.id.navigation_athlete_test) {
            speedDialView.show();
            athleteTestFragment = new AthleteTestFragment();
            athleteTestFragment.setCurrentAthlete(currentAthlete);
            return athleteTestFragment;
        }
        else if (id == R.id.navigation_athlete_details) {
            speedDialView.hide();
            athleteDetailsFragment = new AthleteDetailsFragment();
            athleteDetailsFragment.setCurrentAthlete(currentAthlete);
            athleteDetailsFragment.setOnDetailEditedListener(new AthleteDetailsFragment.OnDetailEditedListener() {
                @Override
                public void OnDetailEdited() {
                    showSnackBar("Detalle editado correctamente");
                }
            });
            return athleteDetailsFragment;
        }
        else if (id == R.id.navigation_athlete_foda) {
            speedDialView.hide();
            fodaFragment = new FodaFragment();
            fodaFragment.setCurrentAthlete(currentAthlete);
            return fodaFragment;
        }
        else if (id == R.id.navigation_athlete_achievements) {
            speedDialView.show();
            athleteAchievementFragment = new AthleteAchievementFragment();
            athleteAchievementFragment.setCurrentAthlete(currentAthlete);
            return athleteAchievementFragment;
        }
        return null;
    }
}
