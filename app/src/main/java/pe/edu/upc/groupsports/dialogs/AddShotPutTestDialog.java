package pe.edu.upc.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;

/**
 * Created by karique on 4/05/2018.
 */

public class AddShotPutTestDialog extends AlertDialog {
    private Button cancelButton;
    private Button okButton;

    private AutoCompleteTextView ballWeightAutoCompleteTextView;
    private AppCompatSpinner shotPutTypeIdSpinner;

    private SessionManager session;
    private Context context;

    EditText metersValueEditText;

    public AddShotPutTestDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddShotPutTestDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddShotPutTestDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_add_shot_put_test, null);

        session = new SessionManager(context);

        metersValueEditText = view.findViewById(R.id.metersValueEditText);
        ballWeightAutoCompleteTextView = view.findViewById(R.id.ballWeightAutoCompleteTextView);
        shotPutTypeIdSpinner = view.findViewById(R.id.shotPutTypeIdSpinner);

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
                if (metersValueEditText.getText().length() > 0) {
                    if (ballWeightAutoCompleteTextView.getText().length() > 0) {
                        onOkButtonClickListener.OnOkButtonClicked(
                                metersValueEditText.getText().toString(),
                                ballWeightAutoCompleteTextView.getText().toString(),
                                String.valueOf(shotPutTypeIdSpinner.getSelectedItemPosition()+1)
                        );
                    }
                    else {
                        Toast.makeText(view.getContext(), "No deje el peso de la bala sin un valor", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(view.getContext(), "No deje los metros en vacio", Toast.LENGTH_LONG).show();
                }
            }
        });
        setAutoCompleteData();
        setSpinnerData();
        setView(view);
    }

    private void setAutoCompleteData() {
        String[] weekTypesArr = new String[]{"4","5","6","7.256"};
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item, weekTypesArr
        );
        ballWeightAutoCompleteTextView.setThreshold(1);
        ballWeightAutoCompleteTextView.setAdapter(arrayAdapter);
        ballWeightAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ballWeightAutoCompleteTextView.showDropDown();
            }
        });
    }
    private void setSpinnerData() {
        String[] weekTypesArr = new String[]{"Frontal","Espalda"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,weekTypesArr
        );
        shotPutTypeIdSpinner.setAdapter(adapter);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String resultMeters, String weightBall, String shotPutType);
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






























