package pe.edu.upc.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pe.edu.upc.groupsports.R;

/**
 * Created by karique on 4/05/2018.
 */

public class EditDeleteDialog extends AlertDialog {
    private Button deleteButton;
    private Button editButton;
    private Context context;
    private boolean hideEditButton = false;

    public EditDeleteDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EditDeleteDialog(Context context, boolean hideEditButton) {
        super(context);
        this.context = context;
        this.hideEditButton = hideEditButton;
        init();
    }

    public EditDeleteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        init();
    }

    public EditDeleteDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_edit_delete, null);

        deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteButtonClickListener != null){
                    onDeleteButtonClickListener.OnDeleteButtonClicked();
                }
            }
        });

        editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEditButtonClickListener != null) {
                    onEditButtonClickListener.OnEditButtonClicked();
                }
            }
        });
        if (hideEditButton) {
            editButton.setVisibility(View.GONE);
        }

        setView(view);
    }

    public interface OnDeleteButtonClickListener {
        void OnDeleteButtonClicked();
    }
    public interface OnEditButtonClickListener {
        void OnEditButtonClicked();
    }

    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    private OnEditButtonClickListener onEditButtonClickListener;

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener onDeleteButtonClickListener) {
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener onEditButtonClickListener) {
        this.onEditButtonClickListener = onEditButtonClickListener;
    }
}






























