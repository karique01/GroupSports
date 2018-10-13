package pe.edu.upc.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
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
import pe.edu.upc.groupsports.models.StrengthTestType;
import pe.edu.upc.groupsports.repositories.StrengthTypesRepository;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AddStrengthTestDialog extends AlertDialog {
    private Button cancelButton;
    private Button okButton;

    private Spinner weightTypeSpinner;

    private Context context;
    private List<StrengthTestType> strengthTestTypes;
    int pos = 0;

    EditText weightEditText;

    public AddStrengthTestDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddStrengthTestDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddStrengthTestDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_strength_test, null);

        weightEditText = view.findViewById(R.id.weightEditText);

        strengthTestTypes = StrengthTypesRepository.getInstance().getStrengthTestTypes();
        weightTypeSpinner = view.findViewById(R.id.weightTypeSpinner);
        weightTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                if (weightEditText.getText().length() > 0 &&
                        StrengthTypesRepository.getInstance().getStrengthTestTypes().size() > 0) {
                    onOkButtonClickListener.OnOkButtonClicked(
                            weightEditText.getText().toString(),
                            StrengthTypesRepository.getInstance().getStrengthTestTypeByPos(pos).getId(),
                            Funciones.formatDateForAPI(Funciones.getCurrentDate())
                    );
                }
                else {
                    Toast.makeText(context,"No deje campos vacios",Toast.LENGTH_LONG).show();
                }
            }
        });

        setSpinnerData();
        setView(view);
    }

    private void setSpinnerData() {
        List<String> saltabilityStrings = new ArrayList<>();
        for (int i = 0; i < strengthTestTypes.size(); i++) {
            saltabilityStrings.add(strengthTestTypes.get(i).getDescription());
        }
        String[] saltabilityTypesArr = saltabilityStrings.toArray(new String[]{});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,saltabilityTypesArr
        );
        weightTypeSpinner.setAdapter(adapter);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String maxRepetitionWeightValue, String strengthTestTypeId, String date);
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






























