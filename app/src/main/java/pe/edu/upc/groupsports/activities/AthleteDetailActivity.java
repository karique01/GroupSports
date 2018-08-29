package pe.edu.upc.groupsports.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.dialogs.AddSpeedTestDialog;
import pe.edu.upc.groupsports.fragments.AthleteDetailsFragment;
import pe.edu.upc.groupsports.fragments.AthleteTestFragment;
import pe.edu.upc.groupsports.fragments.AthletesFragment;
import pe.edu.upc.groupsports.fragments.AthletesStatisticsFragment;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

public class AthleteDetailActivity extends AppCompatActivity {
    private Athlete currentAthlete;
    SpeedDialView speedDialView;
    SessionManager session;

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

        currentAthlete = Athlete.from(getIntent().getExtras());
        session = new SessionManager(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        speedDialView = findViewById(R.id.speedDial);
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.action_test_velocity:
                        addSpeedTestDialog();
                        return false;
                    case R.id.action_test_saltability:

                        return false;
                    case R.id.action_test_fuerza:

                        return false;
                    default:
                        return false;
                }
            }
        });

        navigateAcordingTo(R.id.navigation_athlete_statistics);
    }

    private void addSpeedTestDialog() {
        final AddSpeedTestDialog addSpeedTestDialog = new AddSpeedTestDialog(this);
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
            jsonObjectSpeedTest.put("coachId", session.getid());
            jsonObjectSpeedTest.put("athleteId", currentAthlete.getId());
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

                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se agrego el test correctamente", Snackbar.LENGTH_LONG)
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
            speedDialView.inflate(R.menu.main_menu_test);
            speedDialView.hide();
            speedDialView.show();
            speedDialView.open(true);
            AthleteTestFragment athleteTestFragment = new AthleteTestFragment();
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
