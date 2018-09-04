package pe.edu.upc.groupsports.activities;

import android.content.Context;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.dialogs.AddSpeedTestDialog;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.fragments.AthleteDetailsFragment;
import pe.edu.upc.groupsports.fragments.AthleteTestFragment;
import pe.edu.upc.groupsports.fragments.AthletesStatisticsFragment;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

public class AthleteDetailActivity extends AppCompatActivity {
    private Athlete currentAthlete;
    private FloatingActionButton speedDialView;
    private SessionManager session;
    private AthleteTestFragment athleteTestFragment;
    private Context context;

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
                if (athleteTestFragment != null) {
                    switch (athleteTestFragment.getCurrentTestFragmentSelected()) {
                        case AthleteTestFragment.MOOD_TEST_FRAGMENT_SELECTED:
                            addMoodTestDialog();
                            break;
                        case AthleteTestFragment.SPEED_TEST_FRAGMENT_SELECTED:
                            addSpeedTestDialog();
                            break;
                        case AthleteTestFragment.SALTABILITY_TEST_FRAGMENT_SELECTED:

                            break;
                        case AthleteTestFragment.STRENGTH_TEST_FRAGMENT_SELECTED:

                            break;
                        default:
                            break;
                    }
                }
            }
        });
//        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
//            @Override
//            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
//                switch (speedDialActionItem.getId()) {
//                    case R.id.action_test_animo:
//                        addMoodTestDialog();
//                        return false;
//                    case R.id.action_test_velocity:
//                        addSpeedTestDialog();
//                        return false;
//                    case R.id.action_test_saltability:
//
//                        return false;
//                    case R.id.action_test_fuerza:
//
//                        return false;
//                    default:
//                        return false;
//                }
//            }
//        });

        modifyToolbar();
        navigateAcordingTo(R.id.navigation_athlete_statistics);
    }

    private void modifyToolbar(){
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white);
            actionBar.setTitle(currentAthlete.getFullName());
        }
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

                                View view = findViewById(android.R.id.content);
                                if (view != null) {
                                    Snackbar.make(view, "Se agregó el test correctamente", Snackbar.LENGTH_LONG)
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

                    }
                });
    }

    private void addSpeedTestDialog() {
        final AddSpeedTestDialog addSpeedTestDialog = new AddSpeedTestDialog(context);
        addSpeedTestDialog.show();
        addSpeedTestDialog.setOnCancelButtonClickListener(new AddSpeedTestDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addSpeedTestDialog.dismiss();
            }
        });
        addSpeedTestDialog.setOnOkButtonClickListener(new AddSpeedTestDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String result, String meters) {
                uploadSpedTest(result,meters);
                addSpeedTestDialog.dismiss();
            }
        });
    }

    private void uploadSpedTest(final String result, final String meters) {

        JSONObject jsonObjectSpeedTest = new JSONObject();

        try {
            jsonObjectSpeedTest.put("result", result);
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

                                View view = findViewById(android.R.id.content);
                                if (view != null) {
                                    Snackbar.make(view, "Se agregó el test correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }

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
        if (id == R.id.navigation_athlete_statistics) {
            speedDialView.hide();
            return new AthletesStatisticsFragment();
        } else if (id == R.id.navigation_athlete_test) {
            speedDialView.show();
            athleteTestFragment = new AthleteTestFragment();
            athleteTestFragment.setCurrentAthlete(currentAthlete);
            return athleteTestFragment;
        }
        else if (id == R.id.navigation_athlete_details) {
            speedDialView.hide();
            return new AthleteDetailsFragment();
        }
        return null;
    }

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }
}
