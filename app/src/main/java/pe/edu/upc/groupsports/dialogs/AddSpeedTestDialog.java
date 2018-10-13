package pe.edu.upc.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AddSpeedTestDialog extends AlertDialog {
    private Button cancelButton;
    private Button okButton;

    private AutoCompleteTextView metersAutoCompleteTextView;

    private SessionManager session;
    int pos = 0;
    private Context context;

    EditText hourEditText;
    EditText minuteEditText;
    EditText secondsEditText;
    EditText millisecondsEditText;

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

        session = new SessionManager(context);

        hourEditText = view.findViewById(R.id.hourEditText);
        minuteEditText = view.findViewById(R.id.minuteEditText);
        secondsEditText = view.findViewById(R.id.secondsEditText);
        secondsEditText.requestFocus();

        millisecondsEditText = view.findViewById(R.id.millisecondsEditText);
        metersAutoCompleteTextView = view.findViewById(R.id.metersAutoCompleteTextView);

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
                okButtonClicked(view);
            }
        });
        setSpinnerData(view);
        setView(view);
    }

    private void setSpinnerData(final View mView) {
        String[] weekTypesArr = new String[]{"10","20","30","40","50","55","60","70","80","90","100","120","140","160","180","200","250","300","350","400"};
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item, weekTypesArr
        );
        metersAutoCompleteTextView.setThreshold(1);
        metersAutoCompleteTextView.setAdapter(arrayAdapter);
        metersAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                metersAutoCompleteTextView.showDropDown();
                Funciones.hideKeyboardFromContext(context,mView);
            }
        });
        metersAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metersAutoCompleteTextView.showDropDown();
                Funciones.hideKeyboardFromContext(context,mView);
            }
        });
        metersAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Funciones.hideKeyboardFromContext(context,mView);
            }
        });
    }

    public void okButtonClicked(View view){
        if (hourEditText.getText().length() > 0 &&
                minuteEditText.getText().length() > 0 &&
                secondsEditText.getText().length() > 0 &&
                millisecondsEditText.getText().length() > 0) {
            if (metersAutoCompleteTextView.getText().length() > 0) {
                if (hourEditText.getText().toString().equals("0") &&
                        minuteEditText.getText().toString().equals("0") &&
                        secondsEditText.getText().toString().equals("0") &&
                        millisecondsEditText.getText().toString().equals("0")) {
                    Toast.makeText(view.getContext(), "El tiempo no puede ser cero", Toast.LENGTH_LONG).show();
                }
                else{
                    if (Integer.valueOf(hourEditText.getText().toString()) <= 24) {
                        if (Integer.valueOf(minuteEditText.getText().toString()) <= 59) {
                            if (Integer.valueOf(secondsEditText.getText().toString()) <= 59) {
                                if (Integer.valueOf(millisecondsEditText.getText().toString()) <= 999) {
                                    onOkButtonClickListener.OnOkButtonClicked(
                                            hourEditText.getText().toString(),
                                            minuteEditText.getText().toString(),
                                            secondsEditText.getText().toString(),
                                            millisecondsEditText.getText().toString(),
                                            metersAutoCompleteTextView.getText().toString()
                                    );
                                }
                                else {
                                    Toast.makeText(view.getContext(), "Los milisegundos deben ser menor o igual a 999", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(view.getContext(), "Los segundos deben ser menor o igual a 59", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(view.getContext(), "Los minutos deben ser menor o igual a 59", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(view.getContext(), "Las horas deben ser menor o igual a 24", Toast.LENGTH_LONG).show();
                    }
                }
            }
            else {
                Toast.makeText(view.getContext(), "No deje los metros en vacio", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(view.getContext(), "No deje ningun campo de tiempo vacio", Toast.LENGTH_LONG).show();
        }
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String hours, String minutes, String seconds, String milliseconds, String meters);
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






























