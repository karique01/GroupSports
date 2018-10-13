package pe.edu.upc.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.models.MesocycleType;
import pe.edu.upc.groupsports.models.SaltabilityTestType;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.MesocycleTypesRepository;
import pe.edu.upc.groupsports.repositories.SaltabilityTypesRepository;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AddSaltabilityTestDialog extends AlertDialog {
    private Button cancelButton;
    private Button okButton;

    private Spinner saltabilityTypeSpinner;

    private Context context;
    private List<SaltabilityTestType> saltabilityTestTypes;
    int pos = 0;

    EditText distanceEditText;

    public AddSaltabilityTestDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddSaltabilityTestDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddSaltabilityTestDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_saltability_test, null);

        distanceEditText = view.findViewById(R.id.distanceEditText);

        saltabilityTestTypes = SaltabilityTypesRepository.getInstance().getSaltabilityTestTypes();
        saltabilityTypeSpinner = view.findViewById(R.id.saltabilityTypeSpinner);
        saltabilityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                if (distanceEditText.getText().length() > 0 &&
                        SaltabilityTypesRepository.getInstance().getSaltabilityTestTypes().size() > 0) {
                    onOkButtonClickListener.OnOkButtonClicked(
                            distanceEditText.getText().toString(),
                            SaltabilityTypesRepository.getInstance().getSaltabilityTestTypeByPos(pos).getId(),
                            Funciones.formatDateForAPI(Funciones.getCurrentDate())
                    );
                }
                else {
                    Toast.makeText(context,"No deje campos vacios",Toast.LENGTH_LONG).show();
                }
            }
        });

        setSpinnerData();
        setView(view);
    }

    private void setSpinnerData() {
        List<String> saltabilityStrings = new ArrayList<>();
        for (int i = 0; i < saltabilityTestTypes.size(); i++) {
            saltabilityStrings.add(saltabilityTestTypes.get(i).getDescription());
        }
        String[] saltabilityTypesArr = saltabilityStrings.toArray(new String[]{});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,saltabilityTypesArr
        );
        saltabilityTypeSpinner.setAdapter(adapter);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String distanceResult, String jumpTestTypeId, String date);
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






























