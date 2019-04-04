package pe.edu.karique.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;

/**
 * Created by karique on 4/05/2018.
 */

public class AddWeightTestBySessionDialog extends AlertDialog {
    Button cancelButton;
    Button okButton;
    SessionManager session;
    EditText weightBeforeEditText;
    EditText weightAfterEditText;
    Context context;

    public AddWeightTestBySessionDialog(Context context) {
        super(context);
        init();
    }

    public AddWeightTestBySessionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddWeightTestBySessionDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_weight_test_by_session, null);

        session = new SessionManager(view.getContext());
        context = view.getContext();

        weightBeforeEditText = view.findViewById(R.id.weightBeforeEditText);
        weightAfterEditText = view.findViewById(R.id.weightAfterEditText);

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
                        weightBeforeEditText.getText().toString(),
                        weightAfterEditText.getText().toString()
                );
            }
        });

        setView(view);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String weightBefore,String weightAfter);
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






























