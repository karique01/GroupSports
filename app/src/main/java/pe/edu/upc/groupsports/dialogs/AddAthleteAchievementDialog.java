package pe.edu.upc.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pe.edu.upc.groupsports.R;

/**
 * Created by karique on 4/05/2018.
 */

public class AddAthleteAchievementDialog extends AlertDialog {
    private Button cancelButton;
    private Button okButton;
    private Context context;

    EditText descriptionEditText;
    EditText placeEditText;
    EditText resultDistanceEditText;

    String descriptionEditTextVal = null;
    String placeEditTextVal = null;
    String resultEditTextVal = null;
    String rightButtonValue = "AGREGAR";

    EditText hourEditText;
    EditText minuteEditText;
    EditText secondsEditText;
    EditText millisecondsEditText;

    AppCompatSpinner achievementTypeSpinner;
    int pos = 0;
    TextInputLayout resultDistanceTextInputLayout;
    ConstraintLayout chronometerConstraintLayout;

    public AddAthleteAchievementDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddAthleteAchievementDialog(Context context, String descriptionEditTextVal, String placeEditTextVal, String resultEditTextVal) {
        super(context);
        this.context = context;
        this.descriptionEditTextVal = descriptionEditTextVal;
        this.placeEditTextVal = placeEditTextVal;
        //this.resultEditTextVal = resultEditTextVal;
        rightButtonValue = "Editar";
        init();
    }

    public AddAthleteAchievementDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddAthleteAchievementDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_athlete_achievement, null);

        achievementTypeSpinner = view.findViewById(R.id.achievementTypeSpinner);
        setSpinnerData();

        resultDistanceTextInputLayout = view.findViewById(R.id.resultDistanceTextInputLayout);
        chronometerConstraintLayout = view.findViewById(R.id.chronometerConstraintLayout);

        hourEditText = view.findViewById(R.id.hourEditText);
        minuteEditText = view.findViewById(R.id.minuteEditText);
        millisecondsEditText = view.findViewById(R.id.millisecondsEditText);
        secondsEditText = view.findViewById(R.id.secondsEditText);
        secondsEditText.requestFocus();

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        if (descriptionEditTextVal != null)
            descriptionEditText.setText(descriptionEditTextVal);

        placeEditText = view.findViewById(R.id.placeEditText);
        if (placeEditTextVal != null)
            placeEditText.setText(placeEditTextVal);

        resultDistanceEditText = view.findViewById(R.id.resultDistanceEditText);
        if (resultEditTextVal != null)
            resultDistanceEditText.setText(resultEditTextVal);

        cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelButtonClickListener.OnCancelButtonClicked();
            }
        });

        okButton = view.findViewById(R.id.okButton);
        okButton.setText(rightButtonValue);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okButtonClicked(view);
            }
        });

        setView(view);
    }

    private void okButtonClicked(View view){
        if (descriptionEditText.getText().length() > 0 &&
                placeEditText.getText().length() > 0 ) {
            if (!placeEditText.getText().toString().equals("0")) {
                //pos de tiempo
                if (pos == 0){
                    if (hourEditText.getText().toString().equals("0") &&
                            minuteEditText.getText().toString().equals("0") &&
                            secondsEditText.getText().toString().equals("0") &&
                            millisecondsEditText.getText().toString().equals("0")) {
                        Toast.makeText(view.getContext(), "El tiempo no puede ser cero", Toast.LENGTH_LONG).show();
                    } else {
                        if (Integer.valueOf(hourEditText.getText().toString()) <= 24) {
                            if (Integer.valueOf(minuteEditText.getText().toString()) <= 59) {
                                if (Integer.valueOf(secondsEditText.getText().toString()) <= 59) {
                                    if (Integer.valueOf(millisecondsEditText.getText().toString()) <= 999) {
                                        onOkButtonClickListener.OnOkButtonClicked(
                                                placeEditText.getText().toString(),
                                                descriptionEditText.getText().toString(),
                                                pos == 0 ? "1" : "2",
                                                pos == 0 ? getConcatenatedTime() : null,
                                                pos == 0 ? null : resultDistanceEditText.getText().toString()
                                        );
                                    } else {
                                        Toast.makeText(view.getContext(), "Los milisegundos deben ser menor o igual a 999", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(view.getContext(), "Los segundos deben ser menor o igual a 59", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(view.getContext(), "Los minutos deben ser menor o igual a 59", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Las horas deben ser menor o igual a 24", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                //pos de distancia
                else{
                    if (!resultDistanceEditText.getText().toString().equals("0")) {
                        onOkButtonClickListener.OnOkButtonClicked(
                                placeEditText.getText().toString(),
                                descriptionEditText.getText().toString(),
                                pos == 0 ? "1" : "2",
                                pos == 0 ? getConcatenatedTime() : null,
                                pos == 0 ? null : resultDistanceEditText.getText().toString()
                        );
                    } else {
                        Toast.makeText(context, "El resultado del logro no puede ser cero", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(context, "La posicion no puede ser cero", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(context,"No deje campos vacios",Toast.LENGTH_LONG).show();
        }
    }

    private String getConcatenatedTime(){
        return hourEditText.getText().toString() + ":"
                + minuteEditText.getText().toString() + ":"
                + secondsEditText.getText().toString() + "."
                + millisecondsEditText.getText();
    }

    private void setSpinnerData() {
        String[] weekTypesArr = new String[]{"Tiempo","Distancia"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,weekTypesArr
        );
        achievementTypeSpinner.setAdapter(adapter);
        achievementTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                resultDistanceTextInputLayout.setVisibility(pos == 0 ? View.GONE : View.VISIBLE);
                chronometerConstraintLayout.setVisibility(pos == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String place, String description, String athleteAchievementTypeId, String resultTime, String resultDistance);
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






























