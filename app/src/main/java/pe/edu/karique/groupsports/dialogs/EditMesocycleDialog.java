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
import android.widget.Spinner;
import android.widget.TextView;

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

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;
import pe.edu.karique.groupsports.models.MesocycleType;
import pe.edu.karique.groupsports.network.GroupSportsApiService;
import pe.edu.karique.groupsports.repositories.MesocycleTypesRepository;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class EditMesocycleDialog extends AlertDialog implements DatePickerDialog.OnDateSetListener{
    private Button cancelButton;
    private Button okButton;
    private CardView startDateCarddView;
    private CardView endDateCarddView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private DatePickerDialog dpd;
    private String dateStateResult;
    private String startDateStateResult;
    private String endDateStateResult;

    private ConstraintLayout mesocycleTypeConstraintLayout;
    private Spinner mesocycleTypeSpinner;

    private String dateState;
    private String startDateState = "Fecha Inicial";
    private String endDateState = "Fecha final";

    private SessionManager session;
    private Context context;
    private List<MesocycleType> mesocycleTypes;
    int pos = 0;

    String mesocycleTypeId;
    Date startDate;
    Date endDate;

    public EditMesocycleDialog(Context context, String mesocycleTypeId, Date startDate, Date endDate) {
        super(context);
        this.context = context;
        this.mesocycleTypeId = mesocycleTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        init();
    }

    public EditMesocycleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public EditMesocycleDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_edit_mesocycle, null);

        session = new SessionManager(view.getContext());
        mesocycleTypes = new ArrayList<>();

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
                onOkButtonClickListener.OnOkButtonClicked(
                        Integer.parseInt(mesocycleTypes.get(pos).getId()),
                        startDateStateResult,
                        endDateStateResult
                );
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
        if (startDate != null) {
            startDateStateResult = Funciones.formatDateForAPI(startDate);
            startDateTextView.setText(Funciones.formatDate(startDate));
        }

        endDateTextView = view.findViewById(R.id.endDateTextView);
        if (endDate != null) {
            endDateStateResult = Funciones.formatDateForAPI(endDate);
            endDateTextView.setText(Funciones.formatDate(endDate));
        }

        getMesocycleTypes();
        setView(view);
    }

    private void getMesocycleTypes(){
        AndroidNetworking.get(GroupSportsApiService.MESOCYCLE_TYPES_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(context.getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mesocycleTypes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                mesocycleTypes.add(MesocycleType.toMesocycleType(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        MesocycleTypesRepository.getInstance().setMesocycleTypes(mesocycleTypes);
                        setSpinnerData();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void setSpinnerData() {
        mesocycleTypeConstraintLayout.setVisibility(View.VISIBLE);
        List<String> mesoStrings = new ArrayList<>();
        for (int i = 0; i < mesocycleTypes.size(); i++) {
            mesoStrings.add(mesocycleTypes.get(i).getMesocycleName());
        }
        String[] mesocycleTypesArr = mesoStrings.toArray(new String[]{});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,mesocycleTypesArr
        );
        mesocycleTypeSpinner.setAdapter(adapter);
        mesocycleTypeSpinner.setSelection(MesocycleTypesRepository.getInstance().getPosById(mesocycleTypeId));
    }

    public void showCalendar(String estado){
        Calendar now = Calendar.getInstance();
        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }
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
        void OnOkButtonClicked(int mesocycleTypeId, String startDate, String endDate);
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






























