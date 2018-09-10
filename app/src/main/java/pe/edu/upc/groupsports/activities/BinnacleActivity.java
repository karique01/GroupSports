package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
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
import pe.edu.upc.groupsports.dialogs.AddBinnacleDetailDialog;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.models.BinnacleDetail;
import pe.edu.upc.groupsports.models.SessionWork;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

public class BinnacleActivity extends AppCompatActivity {
    public static int REQUEST_FOR_ACTIVITY_CODE_BINNACLE_DETAIL = 77;
    SessionWork currentSessionWork;

    SessionManager session;

    RecyclerView binnacleRecyclerView;
    BinnaclDetailsAdapter binnacleDetailAdapter;
    RecyclerView.LayoutManager binnacleDetailsLayoutManager;
    List<BinnacleDetail> binnacleDetails;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    Context context;

    SpeedDialView speedDial;

    TextView trainingPlanNameTextView;
    TextView mesocycleNameTextView;
    TextView shiftTextView;
    TextView athleteNameTextView;
    CircleImageView athleteCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binnacle);

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

        binnacleRecyclerView = (RecyclerView) findViewById(R.id.binnacleRecyclerView);
        binnacleDetails = new ArrayList<>();
        binnacleDetailAdapter = new BinnaclDetailsAdapter(binnacleDetails);
        binnacleDetailsLayoutManager = new LinearLayoutManager(this);
        binnacleRecyclerView.setAdapter(binnacleDetailAdapter);
        binnacleRecyclerView.setLayoutManager(binnacleDetailsLayoutManager);

        binnacleDetailAdapter.setOnBinnacleDetailDeleted(new BinnaclDetailsAdapter.OnBinnacleDetailDeleted() {
            @Override
            public void BinnacleDetailDeleted(BinnacleDetail binnacleDetail) {
                showDeleteBinnacleDetailDialog(binnacleDetail);
            }
        });

        noAthletesConstraintLayout = (ConstraintLayout) findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        speedDial = (SpeedDialView) findViewById(R.id.speedDial);
        speedDial.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                showAddBinnacleDetailDialog();
                return false;
            }

            @Override
            public void onToggleChanged(boolean isOpen) {

            }
        });

        updateData();
    }

    private void showAddBinnacleDetailDialog(){
        final AddBinnacleDetailDialog addBinnacleDetailDialog = new AddBinnacleDetailDialog(context);
        addBinnacleDetailDialog.show();
        addBinnacleDetailDialog.setOnCancelButtonClickListener(new AddBinnacleDetailDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addBinnacleDetailDialog.dismiss();
            }
        });
        addBinnacleDetailDialog.setOnOkButtonClickListener(new AddBinnacleDetailDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String detail) {
                addBinnacleDetail(detail);
                addBinnacleDetailDialog.dismiss();
            }
        });
    }

    private void addBinnacleDetail(String detail){
        JSONObject binnacleDetailJsonObject = new JSONObject();

        try {
            binnacleDetailJsonObject.put("detail", detail);
            binnacleDetailJsonObject.put("sessionId", currentSessionWork.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.BINNACLE_DETAILS_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(binnacleDetailJsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                updateData();
                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se agrego la entrada correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }
                            else {
                                Toast.makeText(context, "Hubo un error al agregar la entrada", Toast.LENGTH_LONG).show();
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

    private void showDeleteBinnacleDetailDialog(final BinnacleDetail binnacleDetail){
        final WarningDialog warningDialog = new WarningDialog(
                context,
                "Eliminar entrada",
                "¿Está seguro que desea eliminar esta entrada?\n\nSe eliminará esta entrada permanentemente"
        );
        warningDialog.show();
        warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                deleteBinnacleDetail(binnacleDetail);
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

    private void deleteBinnacleDetail(BinnacleDetail binnacleDetail) {
        AndroidNetworking.delete(GroupSportsApiService.BINNACLE_DETAILS_URL + binnacleDetail.getId())
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
                                updateData();
                                View view = getCurrentFocus();
                                if (view != null) {
                                    Snackbar.make(view, "Se eliminó la entrada correctamente", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar la entrada",Toast.LENGTH_LONG).show();
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

    private void updateData(){
        AndroidNetworking.get(GroupSportsApiService.BINNACLE_DETAILS_BY_WORKSESSIONS(currentSessionWork.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        binnacleDetails.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                binnacleDetails.add(BinnacleDetail.toBinnacleDetail(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        binnacleDetailAdapter.notifyDataSetChanged();

                        if (response.length() == 0) {
                            noAthletesConstraintLayout.bringToFront();
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
            actionBar.setTitle(Funciones.formatDate(currentSessionWork.getSessionDay()));
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
