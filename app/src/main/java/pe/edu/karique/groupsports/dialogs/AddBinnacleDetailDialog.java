package pe.edu.karique.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.libizo.CustomEditText;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;

/**
 * Created by karique on 4/05/2018.
 */

public class AddBinnacleDetailDialog extends AlertDialog {
    private Button cancelButton;
    private Button okButton;
    private CustomEditText detailCustomEditText;

    private SessionManager session;
    private Context context;

    TextView countTextView;

    public AddBinnacleDetailDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddBinnacleDetailDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddBinnacleDetailDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_binnacle_detail, null);

        session = new SessionManager(context);

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
                if (detailCustomEditText.getText().length() > 0) {
                    onOkButtonClickListener.OnOkButtonClicked(detailCustomEditText.getText().toString());
                }
                else {
                    Toast.makeText(context,"Ingrese la entrada a la bitacora",Toast.LENGTH_LONG).show();
                }
            }
        });

        countTextView = view.findViewById(R.id.countTextView);
        detailCustomEditText = view.findViewById(R.id.detailCustomEditText);
        detailCustomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countTextView.setText(String.format("%d/800", charSequence.length()));
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        setView(view);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(String detail);
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






























