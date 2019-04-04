package pe.edu.karique.groupsports.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AddSpeedTestDialog extends AlertDialog implements DatePickerDialog.OnDateSetListener{
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

    private CardView startDateCarddView;
    String testDate;
    private DatePickerDialog dpd;
    private TextView startDateTextView;

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
                Funciones.hideKeyboardFromContext(context,view);
                onCancelButtonClickListener.OnCancelButtonClicked();
            }
        });

        startDateTextView = view.findViewById(R.id.startDateTextView);
        testDate = Funciones.formatDateForAPI(Funciones.getCurrentDate());
        startDateCarddView = view.findViewById(R.id.startDateCarddView);
        startDateCarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar(Funciones.getCurrentDate());
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

    public void showCalendar(Date date){
        Calendar now = Calendar.getInstance();
        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    this,
                    date != null ? Funciones.getYearFromDate(date) : now.get(Calendar.YEAR),
                    date != null ? Funciones.getMonthFromDate(date) : now.get(Calendar.MONTH),
                    date != null ? Funciones.getDayFromDate(date) : now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    this,
                    date != null ? Funciones.getYearFromDate(date) : now.get(Calendar.YEAR),
                    date != null ? Funciones.getMonthFromDate(date) : now.get(Calendar.MONTH),
                    date != null ? Funciones.getDayFromDate(date) : now.get(Calendar.DAY_OF_MONTH)
            );
        }
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(Color.parseColor("#FF9800"));
        dpd.setTitle("Dia del test");
        dpd.show(((Activity) context).getFragmentManager(), "Datepickerdialog");
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
                                    Funciones.hideKeyboardFromContext(context,view);
                                    onOkButtonClickListener.OnOkButtonClicked(
                                            hourEditText.getText().toString(),
                                            minuteEditText.getText().toString(),
                                            secondsEditText.getText().toString(),
                                            millisecondsEditText.getText().toString(),
                                            metersAutoCompleteTextView.getText().toString(),
                                            testDate
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        testDate = year+"-"+(++monthOfYear)+"-"+dayOfMonth;
        String dateFormated = dayOfMonth+"/"+String.format("%02d",monthOfYear)+"/"+year;
        startDateTextView.setText(dateFormated);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String hours, String minutes, String seconds, String milliseconds, String meters, String testDate);
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






























