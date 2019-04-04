package pe.edu.karique.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 4/05/2018.
 */

public class AdminValueDialog extends AlertDialog {
    Button cancelButton;
    Button okButton;
    SessionManager session;
    EditText valueEditText;
    TextView valueTittleTextView;
    Context context;

    String titleString;
    String editTextString;
    String okButtontString = "AGREGAR";
    boolean inputTypeText = false;
    String editTextValueString = "";

    TextInputLayout valueTextInputLayout;

    public AdminValueDialog(Context context, String titleString, String editTextString) {
        super(context);
        this.titleString = titleString;
        this.editTextString = editTextString;
        init();
    }

    public AdminValueDialog(Context context, String titleString, String editTextString, boolean inputTypeText) {
        super(context);
        this.titleString = titleString;
        this.editTextString = editTextString;
        this.inputTypeText = inputTypeText;
        init();
    }

    public AdminValueDialog(Context context, String titleString, String editTextString, String okButtonStringg) {
        super(context);
        this.titleString = titleString;
        this.editTextString = editTextString;
        this.okButtontString = okButtonStringg;
        init();
    }

    public AdminValueDialog(Context context, String titleString, String editTextString, String okButtonStringg, String editTextValueString, boolean inputTypeText) {
        super(context);
        this.titleString = titleString;
        this.editTextString = editTextString;
        this.okButtontString = okButtonStringg;
        this.editTextValueString = editTextValueString;
        this.inputTypeText = inputTypeText;
        init();
    }

    public AdminValueDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AdminValueDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_admin_value, null);

        session = new SessionManager(view.getContext());
        context = view.getContext();

        valueTittleTextView = view.findViewById(R.id.valueTittleTextView);
        valueEditText = view.findViewById(R.id.valueEditText);
        if (editTextValueString.length() > 0){
            valueEditText.setText(editTextValueString);
            valueEditText.setSelection(valueEditText.getText().length());
        }
        valueTextInputLayout = view.findViewById(R.id.valueTextInputLayout);
        valueTextInputLayout.setHint(editTextString);
        valueTittleTextView.setText(titleString);
        if (inputTypeText){
            valueEditText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        }

        cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Funciones.hideKeyboardFromContext(context,view);
                onCancelButtonClickListener.OnCancelButtonClicked();
            }
        });

        okButton = view.findViewById(R.id.okButton);
        okButton.setText(okButtontString);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valueEditText.getText().length() > 0) {
                    Funciones.hideKeyboardFromContext(context,view);
                    onOkButtonClickListener.OnOkButtonClicked(valueEditText.getText().toString());
                }
                else {
                    Toast.makeText(context,"No deje el campo vacio",Toast.LENGTH_LONG).show();
                }
            }
        });

        setView(view);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String value);
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






























