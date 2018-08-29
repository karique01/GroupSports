package pe.edu.upc.groupsports.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Constantes;

public class RegisterAthleteActivity extends AppCompatActivity {

    Button cancelButton;
    Button okButton;

    Spinner disciplineSpinner;
    EditText userNameEditText;
    EditText passwordEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText cellPhoneEditText;
    EditText addressEditText;
    EditText emailEditText;
    AppCompatCheckBox addToMyTeamCheckBox;
    CalendarView birthDateCalendarView;
    SessionManager session;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        disciplineSpinner = findViewById(R.id.disciplineSpinner);
        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        cellPhoneEditText = findViewById(R.id.cellPhoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addToMyTeamCheckBox = findViewById(R.id.addToMyTeamCheckBox);
        session = new SessionManager(this);

        birthDateCalendarView = findViewById(R.id.birthDateCalendarView);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1993, 11, 18);
        try {
            birthDateCalendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAthlete();
            }
        });
    }

    private void registerAthlete(){
        if (userNameEditText.getText().length() == 0 ||
                passwordEditText.getText().length() == 0 ||
                firstNameEditText.getText().length() == 0 ||
                lastNameEditText.getText().length() == 0 ||
                cellPhoneEditText.getText().length() == 0 ||
                addressEditText.getText().length() == 0 ||
                emailEditText.getText().length() == 0 ){
            Toast.makeText(this,"No deje campos vacios",Toast.LENGTH_LONG).show();
        }
        else {
            JSONObject jsonObjectAthlete = new JSONObject();

            try {

                jsonObjectAthlete.put("username", userNameEditText.getText().toString());
                jsonObjectAthlete.put("password", passwordEditText.getText().toString());
                jsonObjectAthlete.put("userType", Constantes.USER_TYPE_ATLETA);
                jsonObjectAthlete.put("firstName", firstNameEditText.getText().toString());
                jsonObjectAthlete.put("lastName", lastNameEditText.getText().toString());
                jsonObjectAthlete.put("cellPhone", cellPhoneEditText.getText().toString());
                jsonObjectAthlete.put("address", addressEditText.getText().toString());
                jsonObjectAthlete.put("emailAddress", emailEditText.getText().toString());
                jsonObjectAthlete.put("birthDate", new java.sql.Date(birthDateCalendarView.getFirstSelectedDate().getTimeInMillis()));
                int disciplineId = disciplineSpinner.getSelectedItemPosition() + 1;
                jsonObjectAthlete.put("disciplineId", disciplineId);
                jsonObjectAthlete.put(
                        "disciplineName", disciplineId == Constantes.DISCIPLINE_VELOCISTA ?
                                Constantes.DISCIPLINE_VELOCISTA_STR :
                                Constantes.DISCIPLINE_SALTO_VALLA_STR
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(GroupSportsApiService.ATHLETES_URL)
                    .addHeaders("Authorization", "bearer " + session.getaccess_token())
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(jsonObjectAthlete)
                    .setPriority(Priority.HIGH)
                    .setTag(getString(R.string.app_name))
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("response").equals("Usuario ya existe")){
                                    Toast.makeText(mContext, "El nombre de usuario ya existe", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    alertaRegistroYAgraAEquipo(response);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(mContext, "Hubo un error al registrar al atleta", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void alertaRegistroYAgraAEquipo(JSONObject response){
        try {
            String url = GroupSportsApiService.COACHS_URL+"/"+session.getuserLoggedTypeId()+"/"+response.getString("idAthleteAdded");
            AndroidNetworking.post(url)
                    .addHeaders("Authorization", "bearer " + session.getaccess_token())
                    .addHeaders("Content-Type", "application/json")
                    .setPriority(Priority.HIGH)
                    .setTag(getString(R.string.app_name))
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(mContext, "Atleta registrado!!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(mContext, "Hubo un error al agregar el atleta al equipo", Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

















