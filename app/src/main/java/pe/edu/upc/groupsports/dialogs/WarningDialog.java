package pe.edu.upc.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * Created by karique on 4/05/2018.
 */

public class WarningDialog extends AlertDialog {
    Button cancelButton;
    Button okButton;
    TextView tittleTextView;
    TextView descriptionTextView;
    SessionManager session;
    Context context;
    String tittleString;
    String descriptionString;
    String okButtonText;
    String cancelButtonText;
    boolean onlyShowAcceptButton = false;

    public WarningDialog(Context context, String tittleString, String descriptionString) {
        super(context);
        this.tittleString = tittleString;
        this.descriptionString = descriptionString;
        this.okButtonText = context.getString(R.string.delete);
        this.cancelButtonText = context.getString(R.string.cancel);
        init();
    }

    public WarningDialog(Context context, String tittleString, String descriptionString, String acceptButtonText) {
        super(context);
        this.tittleString = tittleString;
        this.descriptionString = descriptionString;
        this.okButtonText = acceptButtonText;
        init();
    }

    public WarningDialog(Context context, String tittleString, String descriptionString, String acceptButtonText, boolean onlyShowAcceptButton) {
        super(context);
        this.tittleString = tittleString;
        this.descriptionString = descriptionString;
        this.okButtonText = acceptButtonText;
        this.onlyShowAcceptButton = onlyShowAcceptButton;
        init();
    }

    public WarningDialog(Context context, String tittleString, String descriptionString, String acceptButtonText, String cancelButtonText) {
        super(context);
        this.tittleString = tittleString;
        this.descriptionString = descriptionString;
        this.okButtonText = acceptButtonText;
        this.cancelButtonText = cancelButtonText;
        init();
    }

    public WarningDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public WarningDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_warning, null);

        session = new SessionManager(view.getContext());
        context = view.getContext();

        tittleTextView = view.findViewById(R.id.tittleTextView);
        tittleTextView.setText(tittleString);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(descriptionString);

        cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setText(cancelButtonText);
        cancelButton.setVisibility(onlyShowAcceptButton ? View.GONE : View.VISIBLE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelButtonClickListener.OnCancelButtonClicked();
            }
        });

        okButton = view.findViewById(R.id.okButton);
        okButton.setText(okButtonText);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOkButtonClickListener.OnOkButtonClicked();
            }
        });

        setView(view);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked();
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






























