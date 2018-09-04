package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;

import pe.edu.upc.groupsports.fragments.LastUpdateFragment;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.fragments.MoodTestFragment;
import pe.edu.upc.groupsports.fragments.SessionQuestionFragment;

public class MainAthleteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager sessionManager;
    Context context;
    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_athlete);
        context = this;
        sessionManager = new SessionManager(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        navigateAccordingTo(R.id.nav_last_updates);
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
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
        getMenuInflater().inflate(R.menu.main_athlete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        int id = item.getItemId();
        boolean navigate = navigateAccordingTo(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_log_out){
            sessionManager.deleteUserSession();
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        }
//        if (id == R.id.nav_camera){
//            takePhoto();
//        }
        return navigate;
    }

    private Fragment getFragmentFor (int id) {
        if (id == R.id.nav_last_updates) {
            return getLastUpdateFragment();
        } else if (id == R.id.nav_today_session) {
            return new SessionQuestionFragment();
        } else if (id == R.id.nav_gallery) {
            return null;
        } else if (id == R.id.nav_mood_test) {
            return new MoodTestFragment();
        } else if (id == R.id.nav_share) {
            return null;
        }
        return null;
    }

    private LastUpdateFragment getLastUpdateFragment() {
        LastUpdateFragment lastUpdateFragment = new LastUpdateFragment();
        lastUpdateFragment.setOnAnnouncementDeleted(new LastUpdateFragment.OnAnnouncementDeleted() {
            @Override
            public void AnnouncementDeleted() {
                showLastUpdateOkResponse();
            }
        });
        return lastUpdateFragment;
    }

    private void showLastUpdateOkResponse(){
        View view = getCurrentFocus();
        if (view != null) {
            Snackbar.make(view, "Se eliminó el anuncio correctamente", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        }
    }

    private boolean navigateAccordingTo(int id) {
        try
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame_athlete,getFragmentFor(id))
                    .commit();
            return true;
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
