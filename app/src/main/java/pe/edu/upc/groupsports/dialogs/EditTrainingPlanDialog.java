package pe.edu.upc.groupsports.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.activities.AddTrainingPlanActivity;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class EditTrainingPlanDialog extends AlertDialog implements DatePickerDialog.OnDateSetListener{
    private Button cancelButton;
    private Button okButton;
    private EditText trainingPlanNameEditText;
    private CardView startDateCarddView;
    private CardView endDateCarddView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private String trainingPlanName;
    private Date startDate;
    private Date endDate;
    private DatePickerDialog dpd;
    private String dateStateResult;
    private String startDateStateResult;
    private String endDateStateResult;

    private String dateState;
    private String startDateState = "Fecha Inicial";
    private String endDateState = "Fecha final";

    private SessionManager session;
    private Context context;

    public EditTrainingPlanDialog(Context context,String trainingPlanName,Date startDate,Date endDate) {
        super(context);
        this.context = context;
        this.trainingPlanName = trainingPlanName;
        this.startDate = startDate;
        this.endDate = endDate;
        init();
    }

    public EditTrainingPlanDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public EditTrainingPlanDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_edit_training_plan, null);

        session = new SessionManager(view.getContext());

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
                if (trainingPlanNameEditText.getText().length() > 0)
                    onOkButtonClickListener.OnOkButtonClicked(
                            trainingPlanNameEditText.getText().toString(), startDateStateResult, endDateStateResult
                    );
                else
                    Toast.makeText(context,"No deje el nombre vacio",Toast.LENGTH_LONG).show();
            }
        });

        trainingPlanNameEditText = view.findViewById(R.id.trainingPlanNameEditText);
        trainingPlanNameEditText.setText(trainingPlanName);
        trainingPlanNameEditText.setSelection(trainingPlanNameEditText.getText().length());

        startDateCarddView = view.findViewById(R.id.startDateCarddView);
        startDateCarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateState = startDateState;
                showCalendar(startDateState,startDate);
            }
        });
        endDateCarddView = view.findViewById(R.id.endDateCarddView);
        endDateCarddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateState = endDateState;
                showCalendar(endDateState,endDate);
            }
        });

        startDateTextView = view.findViewById(R.id.startDateTextView);
        if (startDate != null) {
            startDateStateResult = Funciones.formatDateForAPI(startDate);
            startDateTextView.setText(Funciones.formatDate(startDate));
        }

        endDateTextView = view.findViewById(R.id.endDateTextView);
        if (endDate != null) {
            endDateStateResult = Funciones.formatDateForAPI(endDate);
            endDateTextView.setText(Funciones.formatDate(endDate));
        }

        setView(view);
    }

    public void showCalendar(String estado, Date date){
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
        //dpd.setThemeDark(true);
        //dpd.dismissOnPause(true);
        //dpd.showYearPickerFirst(true);
        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
        dpd.setAccentColor(Color.parseColor("#FF9800"));
        dpd.setTitle(estado);
        dpd.show(((Activity) context).getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateStateResult = year+"-"+(++monthOfYear)+"-"+dayOfMonth;
        String dateFormated = dayOfMonth+"/"+String.format("%02d",monthOfYear)+"/"+year;
        if (dateState.equals(startDateState)){
            startDateStateResult = dateStateResult;
            startDateTextView.setText(dateFormated);
        }
        if (dateState.equals(endDateState)){
            endDateStateResult = dateStateResult;
            endDateTextView.setText(dateFormated);
        }
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String trainingPlanName,String startDate,String endDate);
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






























