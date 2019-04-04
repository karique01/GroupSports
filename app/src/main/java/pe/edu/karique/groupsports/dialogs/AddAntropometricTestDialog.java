package pe.edu.karique.groupsports.dialogs;

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

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;
import pe.edu.karique.groupsports.models.Size;
import pe.edu.karique.groupsports.repositories.SizeRepository;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AddAntropometricTestDialog extends AlertDialog implements DatePickerDialog.OnDateSetListener{
    private Button cancelButton;
    private Button okButton;

    private ConstraintLayout mesocycleTypeConstraintLayout;
    private Spinner sizeSpinner;

    private SessionManager session;
    private Context context;
    int pos = 0;
    private EditText weightEditText;
    private EditText wingspanEditText;
    private EditText bodyFatPercentageEditText;
    private EditText leanBodyPercentageEditText;

    private CardView startDateCarddView;
    String testDate;
    private DatePickerDialog dpd;
    private TextView startDateTextView;

    public AddAntropometricTestDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddAntropometricTestDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddAntropometricTestDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_antropometric_test, null);

        session = new SessionManager(view.getContext());

        mesocycleTypeConstraintLayout = view.findViewById(R.id.mesocycleTypeConstraintLayout);
        sizeSpinner = view.findViewById(R.id.sizeSpinner);
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                ((TextView) view).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        weightEditText = view.findViewById(R.id.weightEditText);
        wingspanEditText = view.findViewById(R.id.wingspanEditText);
        bodyFatPercentageEditText = view.findViewById(R.id.bodyFatPercentageEditText);
        leanBodyPercentageEditText = view.findViewById(R.id.leanBodyPercentageEditText);

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
                if (weightEditText.getText().length() > 0 &&
                        wingspanEditText.getText().length() > 0 &&
                        bodyFatPercentageEditText.getText().length() > 0 &&
                        leanBodyPercentageEditText.getText().length() > 0) {
                    if (Float.valueOf(bodyFatPercentageEditText.getText().toString()) <= 100) {
                        if (Float.valueOf(leanBodyPercentageEditText.getText().toString()) <= 100) {
                            Funciones.hideKeyboardFromContext(context,view);
                            onOkButtonClickListener.OnOkButtonClicked(
                                    SizeRepository.getInstance().getSizes().get(pos).getSizeValue(),
                                    weightEditText.getText().toString(),
                                    wingspanEditText.getText().toString(),
                                    bodyFatPercentageEditText.getText().toString(),
                                    leanBodyPercentageEditText.getText().toString(),
                                    testDate
                            );
                        }
                        else {
                            Toast.makeText(view.getContext(),"Maximo porcentaje 100%",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(view.getContext(),"Maximo porcentaje 100%",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(view.getContext(),"No deje los campos vacios",Toast.LENGTH_LONG).show();
                }
            }
        });
        getMesocycleTypes();
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

    private void getMesocycleTypes(){
        setSpinnerData();
    }

    private void setSpinnerData() {
        mesocycleTypeConstraintLayout.setVisibility(View.VISIBLE);
        List<String> sizeStrings = new ArrayList<>();
        List<Size> sizes = SizeRepository.getInstance().getSizes();

        for (int i = 0; i < sizes.size(); i++) {
            sizeStrings.add(sizes.get(i).getSizeValue());
        }

        String[] sizesArr = sizeStrings.toArray(new String[]{});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,sizesArr
        );
        sizeSpinner.setAdapter(adapter);
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
        void OnOkButtonClicked(String size, String weight, String wingspan, String bodyFatPercentage, String leanBodyPercentage, String date);
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






























