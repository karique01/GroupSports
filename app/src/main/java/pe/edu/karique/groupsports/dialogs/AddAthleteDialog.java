package pe.edu.karique.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;
import pe.edu.karique.groupsports.network.GroupSportsApiService;

/**
 * Created by karique on 4/05/2018.
 */

public class AddAthleteDialog extends AlertDialog {
    Button cancelButton;
    Button okButton;
    SessionManager session;
    EditText athleteNameEditText;
    Context context;

    public AddAthleteDialog(Context context) {
        super(context);
        init();
    }

    public AddAthleteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddAthleteDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_athlete, null);

        session = new SessionManager(view.getContext());
        context = view.getContext();

        athleteNameEditText = view.findViewById(R.id.athleteNameEditText);

        cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelButtonClickListener.OnCancelButtonClicked();
            }
        });

        okButton = view.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (athleteNameEditText.getText().length() > 0) {
                    addAthleteToTeam();
                }
                else {
                    Toast.makeText(context,"Nombre de usuario vacio",Toast.LENGTH_LONG).show();
                }
            }
        });

        setView(view);
    }

    private void addAthleteToTeam(){
        String url = GroupSportsApiService.COACHS_URL+session.getuserLoggedTypeId()+"/"+athleteNameEditText.getText().toString();
        AndroidNetworking.post(url)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(context.getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("respuesta").equals("Atleta encontrado")) {
                                Toast.makeText(context, "Atleta agregado al equipo", Toast.LENGTH_LONG).show();
                                onOkButtonClickListener.OnOkButtonClicked();
                            }
                            else {
                                Toast.makeText(context, "Nombre de usuario invalido", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Hubo un error al agregar el atleta al equipo", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked();
    }

    public OnCancelButtonClickListener onCancelButtonClickListener;
    public OnOkButtonClickListener onOkButtonClickListener;

    public void setOnCancelButtonClickListener(OnCancelButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
    }

    public void setOnOkButtonClickListener(OnOkButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
    }
}






























