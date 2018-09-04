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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.models.MesocycleType;
import pe.edu.upc.groupsports.models.WeekType;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.MesocycleTypesRepository;
import pe.edu.upc.groupsports.repositories.WeekTypeRepository;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AddWeekDialog extends AlertDialog implements DatePickerDialog.OnDateSetListener{
    private Button cancelButton;
    private Button okButton;
    private CardView startDateCarddView;
    private CardView endDateCarddView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private DatePickerDialog dpd;
    private String dateStateResult;
    private String startDateStateResult = null;
    private String endDateStateResult = null;

    private ConstraintLayout mesocycleTypeConstraintLayout;
    private Spinner mesocycleTypeSpinner;

    private String dateState;
    private String startDateState = "Fecha Inicial";
    private String endDateState = "Fecha final";

    private SessionManager session;
    private Context context;
    int pos = 0;
    private EditText activityEditText;
    private TextView warningTextView;

    public AddWeekDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddWeekDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddWeekDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_week, null);

        session = new SessionManager(view.getContext());

        mesocycleTypeConstraintLayout = view.findViewById(R.id.mesocycleTypeConstraintLayout);
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
        warningTextView = view.findViewById(R.id.warningTextView);

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
                if (activityEditText.getText().length() > 0 &&
                        startDateStateResult != null &&
                        endDateStateResult != null) {
                    onOkButtonClickListener.OnOkButtonClicked(
                            WeekTypeRepository.getInstance().getWeekTypes().get(pos).getId(),
                            startDateStateResult,
                            endDateStateResult,
                            activityEditText.getText().toString()
                    );
                }
                else {
                    Toast.makeText(view.getContext(),"Escriba una actividad y las fechas de la semana",Toast.LENGTH_LONG).show();
                }
            }
        });

        startDateCarddView = view.findViewById(R.id.startDateCarddView);
        startDateCarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateState = startDateState;
                showCalendar(startDateState);
            }
        });
        endDateCarddView = view.findViewById(R.id.endDateCarddView);
        endDateCarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateState = endDateState;
                showCalendar(endDateState);
            }
        });

        startDateTextView = view.findViewById(R.id.startDateTextView);
        endDateTextView = view.findViewById(R.id.endDateTextView);

        getMesocycleTypes();
        setView(view);
    }

    private void getMesocycleTypes(){
        setSpinnerData();
    }

    private void setSpinnerData() {
        mesocycleTypeConstraintLayout.setVisibility(View.VISIBLE);
        List<String> weekStrings = new ArrayList<>();
        List<WeekType> weekTypes = WeekTypeRepository.getInstance().getWeekTypes();

        for (int i = 0; i < weekTypes.size(); i++) {
            weekStrings.add(weekTypes.get(i).getDescription());
        }

        String[] weekTypesArr = weekStrings.toArray(new String[]{});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,weekTypesArr
        );
        mesocycleTypeSpinner.setAdapter(adapter);
    }

    public void showCalendar(String estado){
        Calendar now = Calendar.getInstance();

        int day = dateStateResult == null ? now.get(Calendar.DAY_OF_MONTH) :
                  dateState.equals(startDateState) ? Funciones.getDayFromDate(Funciones.getDateFromString(startDateStateResult)) :
                  Funciones.getDayFromDate(Funciones.getDateFromString(endDateStateResult));

        int month = dateStateResult == null ? now.get(Calendar.MONTH) :
                    dateState.equals(startDateState) ? Funciones.getMonthFromDate(Funciones.getDateFromString(startDateStateResult)) :
                    Funciones.getMonthFromDate(Funciones.getDateFromString(endDateStateResult));

        int year = dateStateResult == null ? now.get(Calendar.YEAR) :
                   dateState.equals(startDateState) ? Funciones.getYearFromDate(Funciones.getDateFromString(startDateStateResult)) :
                   Funciones.getYearFromDate(Funciones.getDateFromString(endDateStateResult));

        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(this,year,month,day);
        } else {
            dpd.initialize(this,year,month,day);
        }
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(Color.parseColor("#FF9800"));
        dpd.setTitle(estado);
        dpd.show(((Activity) context).getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateStateResult = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
        Date dateResult = Funciones.getDateFromString(dateStateResult);

        String dateFormated = dayOfMonth + "/" + String.format("%02d", monthOfYear) + "/" + year;

        if (dateState.equals(startDateState)) {
            if (Funciones.isDateMonday(dateResult)) {
                startDateStateResult = dateStateResult;
                startDateTextView.setText(dateFormated);

                Date weekEndFromDayChoosed = Funciones.operateDate(dateResult, 6);
                endDateStateResult = Funciones.formatDateForAPI(weekEndFromDayChoosed);
                endDateTextView.setText(Funciones.formatDate(weekEndFromDayChoosed));
            } else {
                warningTextView.setVisibility(View.VISIBLE);
            }
        } else {
            endDateStateResult = dateStateResult;
            endDateTextView.setText(dateFormated);
        }
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(int weekTypeId, String startDate, String endDate, String activityWeek);
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






























