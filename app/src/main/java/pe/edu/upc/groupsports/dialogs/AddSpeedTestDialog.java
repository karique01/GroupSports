package pe.edu.upc.groupsports.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
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
    int pos = 0;

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private ImageButton restartImageButton;
    private ImageButton playStopImageButton;
    private Context context;

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
        final View view = inflater.inflate(R.layout.dialog_add_speed_test, null);

        session = new SessionManager(view.getContext());

        restartImageButton = view.findViewById(R.id.restartImageButton);
        restartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetChronometer();
            }
        });
        playStopImageButton = view.findViewById(R.id.playStopImageButton);
        playStopImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) startChronometer();
                else pauseChronometer();
            }
        });

        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());

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
                if (pauseOffset != 0){
                    onOkButtonClickListener.OnOkButtonClicked(
                            String.valueOf(pauseOffset*1.0/1000),
                            mesocycleTypeSpinner.getSelectedItem().toString()
                    );
                }
                else {
                    Toast.makeText(view.getContext(),"Primero haga una medida",Toast.LENGTH_LONG).show();
                }
            }
        });
        setSpinnerData();
        setView(view);
    }

    public void startChronometer() {
        playStopImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_stop));
        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        chronometer.start();
        running = true;
    }

    public void pauseChronometer() {
        playStopImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_arrow));
        chronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        running = false;
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    private void setSpinnerData() {
        String[] weekTypesArr = new String[]{"30","50","100","200"};

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






























