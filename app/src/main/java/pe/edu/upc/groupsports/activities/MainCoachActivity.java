package pe.edu.upc.groupsports.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.dialogs.AddAnnouncementDialog;
import pe.edu.upc.groupsports.dialogs.AddAthleteDialog;
import pe.edu.upc.groupsports.fragments.AssistanceFragment;
import pe.edu.upc.groupsports.fragments.AthletesFragment;
import pe.edu.upc.groupsports.fragments.CategoriesFragment;
import pe.edu.upc.groupsports.fragments.MyAnnouncementsFragment;
import pe.edu.upc.groupsports.fragments.TrainingPlansFragment;
import pe.edu.upc.groupsports.models.AnnouncementPost;
import pe.edu.upc.groupsports.models.Assistance;
import pe.edu.upc.groupsports.models.AssistanceShift;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

public class MainCoachActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager sessionManager;
    Context context;
    SpeedDialView speedDialView;
    Toolbar toolbar;
    AthletesFragment athletesFragment;
    MyAnnouncementsFragment myAnnouncementsFragment;
    int id;
    int nav_menu_selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coach);

        context = this;
        sessionManager = new SessionManager(context);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        speedDialView = findViewById(R.id.speedDial);
        speedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                showAddAnnouncementDialog();
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
                    case R.id.action_register_athlete:
                        showRegisterAthleteDialog();
                        return false;
                    case R.id.action_add_athlete:
                        showAddAthleteDialog();
                        return false;
                    case R.id.action_add_training_plan:
                        startActivityForResult(new Intent(context,AddTrainingPlanActivity.class),AddTrainingPlanActivity.REQUEST_FOR_ACTIVITY_CODE_ADD_TRAINING_PLAN);
                        return false;
                    default:
                        return false;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ImageView photoImageView = (ImageView) headerView.findViewById(R.id.photoImageView);
        TextView tittleNavViewTextView = (TextView) headerView.findViewById(R.id.tittleNavViewTextView);
        tittleNavViewTextView.setText(sessionManager.getfirstName());
        TextView descriptionNavViewTextView = (TextView) headerView.findViewById(R.id.descriptionNavViewTextView);
        descriptionNavViewTextView.setText(sessionManager.getuserTypeDetail());

        //primer fragment que se ve
        navigateFirstFragment();
    }

    private void showAddAnnouncementDialog(){
        if (nav_menu_selected == 3) {
            final AddAnnouncementDialog addAnnouncementDialog = new AddAnnouncementDialog(context);
            addAnnouncementDialog.show();
            addAnnouncementDialog.setOnCancelButtonClickListener(new AddAnnouncementDialog.OnCancelButtonClickListener() {
                @Override
                public void OnCancelButtonClicked() {
                    addAnnouncementDialog.dismiss();
                }
            });
            addAnnouncementDialog.setOnOkButtonClickListener(new AddAnnouncementDialog.OnOkButtonClickListener() {
                @Override
                public void OnOkButtonClicked(AnnouncementPost announcementPost) {
                    uploadAnnouncement(announcementPost);
                    addAnnouncementDialog.dismiss();
                }
            });
        }
    }

    private void uploadAnnouncement(AnnouncementPost announcementPost){
        AndroidNetworking.post(GroupSportsApiService.ANNOUNCEMENT_URL)
                .addHeaders("Authorization", "bearer " + sessionManager.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(getAnnouncementJson(announcementPost))
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")){
                                myAnnouncementsFragment.updateData();
                                showAnnouncementOkResponse();
                            }
                            else {
                                Toast.makeText(context,"Error al enviar el anuncio",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context,"Error de conexión",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public JSONObject getAnnouncementJson(AnnouncementPost announcementPost){
        JSONObject jsonObjectAnnouncement = new JSONObject();
        try {
            jsonObjectAnnouncement.put("announcementTitle",announcementPost.getAnnouncementTitle());
            jsonObjectAnnouncement.put("announcementDetail",announcementPost.getAnnouncementDetail());
            jsonObjectAnnouncement.put("coachId",sessionManager.getuserLoggedTypeId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray athletesIdJSONArray = new JSONArray();
        List<AnnouncementPost.athleteIdClass> athleteIdClasses = announcementPost.getAthletesId();
        for (int i = 0; i < athleteIdClasses.size(); i++) {
            athletesIdJSONArray.put(athleteIdClasses.get(i).toJSONObject());
        }

        try {
            jsonObjectAnnouncement.put("athletesId", athletesIdJSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectAnnouncement;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_coach, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        id = item.getItemId();
        boolean navigate = navigateAccordingTo(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_log_out_coach){
            sessionManager.deleteUserSession();
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
        return navigate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TrainingPlanDetailActivity.REQUEST_FOR_ACTIVITY_CODE_TRAINING_PLAN_DETAIL) {
            if(resultCode == Activity.RESULT_OK){
                View view = getCurrentFocus();
                if (view != null) {
                    Snackbar.make(view, "Plan eliminado correctamente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            }
        }
        else if (requestCode == AddTrainingPlanActivity.REQUEST_FOR_ACTIVITY_CODE_ADD_TRAINING_PLAN) {
            if(resultCode == Activity.RESULT_OK){
                View view = getCurrentFocus();
                if (view != null) {
                    Snackbar.make(view, "Se agregó el plan correctamente", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            }
        }
    }

    private Fragment getFragmentFor (int id) {
        if (id == R.id.nav_my_athletes) {
            changeToolbarText("Mis Atletas");
            updateFabDial(id);
            athletesFragment = new AthletesFragment();
            return athletesFragment;
        } else if (id == R.id.nav_assistance) {
            changeToolbarText("Asistencia");
            updateFabDial(id);
            return getAssistanceFragment();
        } else if (id == R.id.nav_categories) {
            changeToolbarText("Categorias");
            updateFabDial(id);
            return new CategoriesFragment();
        } else if (id == R.id.nav_work_plan) {
            changeToolbarText("Planes de trabajo");
            updateFabDial(id);
            return new TrainingPlansFragment();
        } else if (id == R.id.nav_announces) {
            changeToolbarText("Mis anuncios");
            updateFabDial(id);
            return getMyAnnouncementsFragment();
        }

        return null;
    }

    private MyAnnouncementsFragment getMyAnnouncementsFragment() {
        myAnnouncementsFragment = new MyAnnouncementsFragment();
        myAnnouncementsFragment.setOnAnnouncementDeleted(new MyAnnouncementsFragment.OnAnnouncementDeleted() {
            @Override
            public void AnnouncementDeleted() {
                showLastUpdateOkResponse();
            }
        });
        return myAnnouncementsFragment;
    }

    private void showAnnouncementOkResponse(){
        View view = getCurrentFocus();
        if (view != null) {
            Snackbar.make(view, "Se agregó el anuncio correctamente", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    }

    private void showLastUpdateOkResponse(){
        View view = getCurrentFocus();
        if (view != null) {
            Snackbar.make(view, "Se eliminó el anuncio correctamente", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    }

    private void changeToolbarText(String text){
        toolbar.setTitle(text);
    }

    private AssistanceFragment getAssistanceFragment(){
        AssistanceFragment assistanceFragment = new AssistanceFragment();
        assistanceFragment.setOnAssistanceButtonPressed(new AssistanceFragment.OnAssistanceButtonPressed() {
            @Override
            public void OnAssistanceSaved() {
                assistanceSaved();
            }
        });

        return assistanceFragment;
    }

    private void updateFabDial(int id){
        if (id == R.id.nav_my_athletes){
            speedDialView.inflate(R.menu.main_menu_coach);
            speedDialView.hide();
            speedDialView.show();
            nav_menu_selected = 0;
        }
        else if (id == R.id.nav_assistance){
            speedDialView.hide();
            nav_menu_selected = 1;
        }
        else if (id == R.id.nav_work_plan){
            speedDialView.inflate(R.menu.main_menu_coach_training_plan);
            speedDialView.hide();
            speedDialView.show();
            nav_menu_selected = 2;
        }
        else if (id == R.id.nav_categories){
            speedDialView.inflate(R.menu.main_menu_coach);
            speedDialView.hide();
            speedDialView.show();
        }
        else if (id == R.id.nav_announces){
            speedDialView.clearActionItems();
            speedDialView.hide();
            speedDialView.show();
            nav_menu_selected = 3;
        }
    }

    private boolean navigateAccordingTo(int id) {
        try
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame_coach,getFragmentFor(id))
                    .commit();
            return true;
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    void navigateFirstFragment(){
        id = R.id.nav_my_athletes;
        navigateAccordingTo(id);
    }

    void assistanceSaved(){
        if (id == R.id.nav_assistance) {

            View view = getCurrentFocus();
            if (view != null) {
                Snackbar.make(view, "Asistencia de las sesiones guardada", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }

            navigateFirstFragment();
        }
    }

    void showAddAthleteDialog(){
        final AddAthleteDialog addAthleteDialog = new AddAthleteDialog(this);
        addAthleteDialog.show();

        addAthleteDialog.setOnCancelButtonClickListener(new AddAthleteDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                addAthleteDialog.dismiss();
            }
        });

        addAthleteDialog.setOnOkButtonClickListener(new AddAthleteDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                if (athletesFragment != null) {
                    athletesFragment.updateData();
                }
                addAthleteDialog.dismiss();
            }
        });
    }

    void showRegisterAthleteDialog(){
        startActivity(new Intent(this,RegisterAthleteActivity.class));
    }
}



























