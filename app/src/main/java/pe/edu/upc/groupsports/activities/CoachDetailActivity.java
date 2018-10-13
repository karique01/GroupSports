package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;

public class CoachDetailActivity extends AppCompatActivity {

    SessionManager sessionManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_detail);

        context = this;
        sessionManager = new SessionManager(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(sessionManager.getusername());
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,CoachCVActivity.class);
                startActivity(intent);
            }
        });

        setCoachData();
    }

    private void setCoachData(){
        ImageView athleteBannerImageView = (ImageView) findViewById(R.id.athleteBannerImageView);
        Picasso.with(context)
                .load(sessionManager.getPictureUrl())
                .placeholder(R.drawable.coach_banner)
                .error(R.drawable.coach_banner)
                .into(athleteBannerImageView);

        TextInputEditText usernameTextInputEditText = findViewById(R.id.usernameTextInputEditText);
        TextInputEditText firstNameTextInputEditText = findViewById(R.id.firstNameTextInputEditText);
        TextInputEditText lastNameTextInputEditText = findViewById(R.id.lastNameTextInputEditText);
        TextInputEditText cellPhoneTextInputEditText = findViewById(R.id.cellPhoneTextInputEditText);
        TextInputEditText addressTextInputEditText = findViewById(R.id.addressTextInputEditText);
        TextInputEditText emailAddressTextInputEditText = findViewById(R.id.emailAddressTextInputEditText);


        usernameTextInputEditText.setText(sessionManager.getusername());
        firstNameTextInputEditText.setText(sessionManager.getfirstName());
        lastNameTextInputEditText.setText(sessionManager.getlastName());
        cellPhoneTextInputEditText.setText(sessionManager.getcellPhone());
        addressTextInputEditText.setText(sessionManager.getaddress());
        emailAddressTextInputEditText.setText(sessionManager.getemailAddress());
    }
}







































