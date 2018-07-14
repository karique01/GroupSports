package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.AthleteAdapter;
import pe.edu.upc.groupsports.dialogs.AddAthleteDialog;
import pe.edu.upc.groupsports.dialogs.RegisterAthleteDialog;
import pe.edu.upc.groupsports.fragments.AssistanceFragment;
import pe.edu.upc.groupsports.fragments.AthletesFragment;
import pe.edu.upc.groupsports.fragments.CategoriesFragment;
import pe.edu.upc.groupsports.fragments.TeamsFragment;
import pe.edu.upc.groupsports.models.Athlete;

public class MainCoachActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager sessionManager;
    Context context;
    FabSpeedDial fabSpeedDial;
    Toolbar toolbar;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_coach);

        context = this;
        sessionManager = new SessionManager(context);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabSpeedDial = (FabSpeedDial) findViewById(R.id.fabSpeedDial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_register_athlete:
                        showRegisterAthleteDialog();
                        break;
                    case R.id.action_add_athlete:
                        showAddAthleteDialog();
                        break;
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

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

    AthletesFragment athletesFragment;

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
        }
        return null;
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
            fabSpeedDial.hide();
            fabSpeedDial.show();
        }
        else if (id == R.id.nav_assistance){
            fabSpeedDial.hide();
        }
        else if (id == R.id.nav_categories){
            fabSpeedDial.hide();
            fabSpeedDial.show();
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
                Snackbar.make(view, "Asistencia de la sesión guardada", Snackbar.LENGTH_LONG)
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
//        final RegisterAthleteDialog registerAthleteDialog = new RegisterAthleteDialog(this);
//        registerAthleteDialog.show();
//
//        registerAthleteDialog.setOnCancelButtonClickListener(new RegisterAthleteDialog.OnCancelButtonClickListener() {
//            @Override
//            public void OnCancelButtonClicked() {
//                registerAthleteDialog.dismiss();
//            }
//        });
//
//        registerAthleteDialog.setOnOkButtonClickListener(new RegisterAthleteDialog.OnOkButtonClickListener() {
//            @Override
//            public void OnOkButtonClicked() {
//                registerAthleteDialog.dismiss();
//                Snackbar.make(getCurrentFocus(), "Atleta registrado", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        startActivity(new Intent(this,RegisterAthleteActivity.class));
    }
}



























