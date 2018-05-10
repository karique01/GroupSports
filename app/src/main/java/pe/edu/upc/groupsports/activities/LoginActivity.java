package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    private VideoView loginVideoVideoView;
    private Button signUpButton;
    private Button logInButton;
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private View startLoginBackgroundView;
    private boolean logInModeActivatedFlag = false;
    private ImageView backImageButton;
    private Button loginConfirmedButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        sessionManager = new SessionManager(this);
        validateIfIsLogged();
        initViews();
        initEvents();
    }

    private void validateIfIsLogged(){
        if (sessionManager.getUserLogin()){
            startMainActivity(sessionManager.getuserType());
        }
    }

    private void initViews(){
        signUpButton = findViewById(R.id.signUpButton);
        logInButton = findViewById(R.id.logInButton);

        usernameTextInputLayout = findViewById(R.id.usernameTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        startLoginBackgroundView = findViewById(R.id.startLoginBackgroundView);
        backImageButton = findViewById(R.id.backImageButton);
        loginConfirmedButton = findViewById(R.id.loginConfirmedButton);
    }

    private void initializeVideoView(){
        loginVideoVideoView = findViewById(R.id.loginVideoVideoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.login_video;
        Uri uri = Uri.parse(videoPath);
        loginVideoVideoView.setVideoURI(uri);
        loginVideoVideoView.start();
        loginVideoVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }

    private void initEvents(){
        signUpButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
        backImageButton.setOnClickListener(this);
        loginConfirmedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.signUpButton:
                startActivity(new Intent(context, RegisterActivity.class));
                break;
            case R.id.logInButton:
                activateLoginMode();
                break;
            case R.id.backImageButton:
                deactivateLoginMode();
                break;
            case R.id.loginConfirmedButton:
                login();
                break;
            default:
                    break;
        }
    }

    private void login() {
        AndroidNetworking.post(GroupSportsApiService.LOGIN_URL)
                .setContentType("application/x-www-form-urlencoded")
                .addBodyParameter("grant_type","password")
                .addBodyParameter("username",usernameEditText.getText().toString())
                .addBodyParameter("password",passwordEditText.getText().toString())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            sessionManager.setUserLogin(true);
                            sessionManager.setaccess_token(response.getString("access_token"));
                            sessionManager.settoken_type(response.getString("token_type"));
                            sessionManager.setexpires_in(response.getString("expires_in"));
                            sessionManager.setid(response.getString("id"));
                            sessionManager.setusername(response.getString("username"));
                            sessionManager.setuserType(response.getString("userType"));
                            sessionManager.setfirstName(response.getString("firstName"));
                            sessionManager.setlastName(response.getString("lastName"));
                            sessionManager.setcellPhone(response.getString("cellPhone"));
                            sessionManager.setaddress(response.getString("address"));
                            sessionManager.setemailAddress(response.getString("emailAddress"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startMainActivity(sessionManager.getuserType());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void startMainActivity(String userType){
        if (userType.equals("1")) //entrenador
            startActivity(new Intent(context, MainCoachActivity.class));
        else if (userType.equals("2")) //atleta
            startActivity(new Intent(context, MainAthleteActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        initializeVideoView();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (logInModeActivatedFlag)
            deactivateLoginMode();
        else
            super.onBackPressed();
    }

    private void activateLoginMode(){
        logInModeActivatedFlag = true;
        startLoginBackgroundView.setVisibility(View.VISIBLE);
        usernameTextInputLayout.setVisibility(View.VISIBLE);
        passwordTextInputLayout.setVisibility(View.VISIBLE);
        backImageButton.setVisibility(View.VISIBLE);
        loginConfirmedButton.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.GONE);
        logInButton.setVisibility(View.GONE);
    }

    private void deactivateLoginMode(){
        logInModeActivatedFlag = false;
        startLoginBackgroundView.setVisibility(View.GONE);
        usernameTextInputLayout.setVisibility(View.GONE);
        passwordTextInputLayout.setVisibility(View.GONE);
        backImageButton.setVisibility(View.GONE);
        loginConfirmedButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.VISIBLE);
        logInButton.setVisibility(View.VISIBLE);
    }
}
