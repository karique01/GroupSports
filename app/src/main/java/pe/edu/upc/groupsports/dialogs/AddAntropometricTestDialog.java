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

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.models.Size;
import pe.edu.upc.groupsports.models.WeekType;
import pe.edu.upc.groupsports.repositories.SizeRepository;
import pe.edu.upc.groupsports.repositories.WeekTypeRepository;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AddAntropometricTestDialog extends AlertDialog {
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
                onCancelButtonClickListener.OnCancelButtonClicked();
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
                            onOkButtonClickListener.OnOkButtonClicked(
                                    SizeRepository.getInstance().getSizes().get(pos).getSizeValue(),
                                    weightEditText.getText().toString(),
                                    wingspanEditText.getText().toString(),
                                    bodyFatPercentageEditText.getText().toString(),
                                    leanBodyPercentageEditText.getText().toString(),
                                    Funciones.formatDateForAPI(Funciones.getCurrentDate())
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






























