package pe.edu.karique.groupsports.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;
import pe.edu.karique.groupsports.adapters.SpeedTestAdvancedAdapter;
import pe.edu.karique.groupsports.models.Athlete;
import pe.edu.karique.groupsports.models.SpeedTestAdvanced;
import pe.edu.karique.groupsports.models.TestAutomatizadoVelocidad;
import pe.edu.karique.groupsports.network.GroupSportsApiService;

public class MySpeedTestAdvanced extends AppCompatActivity {

    SessionManager session;

    RecyclerView speedTestAdvancedTypesRecyclerView;
    SpeedTestAdvancedAdapter speedTestAdvancedAdapter;
    RecyclerView.LayoutManager speedTestAdvancedLayoutManager;
    List<SpeedTestAdvanced> speedTestAdvanceds;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    SpeedDialView speedDialView;
    Athlete currentAthlete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_speed_test_advanced);

        session = new SessionManager(this);
        currentAthlete = Athlete.from(getIntent().getExtras());
        speedTestAdvancedTypesRecyclerView = (RecyclerView) findViewById(R.id.speedTestAdvancedTypesRecyclerView);
        speedTestAdvanceds = new ArrayList<>();
        speedTestAdvancedAdapter = new SpeedTestAdvancedAdapter(speedTestAdvanceds);
        speedTestAdvancedLayoutManager = new LinearLayoutManager(this);
        speedTestAdvancedTypesRecyclerView.setAdapter(speedTestAdvancedAdapter);
        speedTestAdvancedTypesRecyclerView.setLayoutManager(speedTestAdvancedLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        speedDialView = findViewById(R.id.speedDial);
        speedDialView.inflate(R.menu.menu_speed_test_advanced);
        speedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                //showAddActionDialog();
                return false;
            }

            @Override
            public void onToggleChanged(boolean isOpen) {

            }
        });

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.action_10m:
                        startAddSpeedTestAdvanced(TestAutomatizadoVelocidad.TIPO_TEST_10M);
                        return false;
                    case R.id.action_20m:
                        startAddSpeedTestAdvanced(TestAutomatizadoVelocidad.TIPO_TEST_20M);
                        return false;
                    case R.id.action_30m:
                        startAddSpeedTestAdvanced(TestAutomatizadoVelocidad.TIPO_TEST_30M);
                        return false;
                    case R.id.action_40m:
                        startAddSpeedTestAdvanced(TestAutomatizadoVelocidad.TIPO_TEST_40M);
                        return false;
                    case R.id.action_50m:
                        startAddSpeedTestAdvanced(TestAutomatizadoVelocidad.TIPO_TEST_50M);
                        return false;
                    case R.id.action_60m:
                        startAddSpeedTestAdvanced(TestAutomatizadoVelocidad.TIPO_TEST_60M);
                        return false;
                    case R.id.action_100m:
                        startAddSpeedTestAdvanced(TestAutomatizadoVelocidad.TIPO_TEST_100M);
                        return false;
                    default:
                        return false;
                }
            }
        });

        modifyToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSpeedTestAdvancedData();
    }

    private void startAddSpeedTestAdvanced(int distance){
        Intent intent = new Intent(MySpeedTestAdvanced.this,AddSpeedTestAdvancedActivity.class);
        intent.putExtra("distance", distance);
        intent.putExtras(currentAthlete.toBundle());
        startActivityForResult(intent, AddSpeedTestAdvancedActivity.REQUEST_FOR_ACTIVITY_CODE_ADD_SPEED_TEST_ADVANCED);
    }

    private void modifyToolbar(){
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white);
            actionBar.setTitle("Test Velocidad Automatico");
        }
    }

    public void getSpeedTestAdvancedData(){
        AndroidNetworking.get(GroupSportsApiService.SPEED_TEST_ADVANCED_BY_ATHLETE(session.getuserLoggedTypeId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        speedTestAdvanceds.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                speedTestAdvanceds.add(SpeedTestAdvanced.toSpeedTestAdvanced(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        speedTestAdvancedAdapter.notifyDataSetChanged();
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
}




























