package pe.edu.upc.groupsports.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.models.WeekType;
import pe.edu.upc.groupsports.repositories.WeekTypeRepository;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AddSpeedTestDialog extends AlertDialog {
    private Button cancelButton;
    private Button okButton;

    private Spinner mesocycleTypeSpinner;

    private SessionManager session;
    private Context context;
    int pos = 0;
    private EditText activityEditText;

    public AddSpeedTestDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddSpeedTestDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddSpeedTestDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_speed_test, null);

        session = new SessionManager(view.getContext());

        mesocycleTypeSpinner = view.findViewById(R.id.mesocycleTypeSpinner);
        mesocycleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                ((TextView) view).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        activityEditText = view.findViewById(R.id.activityEditText);

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
                if (activityEditText.getText().length() > 0){
                    onOkButtonClickListener.OnOkButtonClicked(
                            activityEditText.getText().toString(),
                            mesocycleTypeSpinner.getSelectedItem().toString()
                    );
                }
                else {
                    Toast.makeText(view.getContext(),"Escriba resultado",Toast.LENGTH_LONG).show();
                }
            }
        });
        setSpinnerData();
        setView(view);
    }

    private void setSpinnerData() {
        String[] weekTypesArr = new String[]{"100","200","30","50"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,weekTypesArr
        );
        mesocycleTypeSpinner.setAdapter(adapter);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String result, String meters);
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






























