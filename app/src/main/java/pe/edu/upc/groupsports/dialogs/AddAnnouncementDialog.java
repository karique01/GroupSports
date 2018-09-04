package pe.edu.upc.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.libizo.CustomEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.ChooseAthleteAnnouncementAdapter;
import pe.edu.upc.groupsports.models.AnnouncementPost;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * Created by karique on 4/05/2018.
 */

public class AddAnnouncementDialog extends AlertDialog {
    private Button cancelButton;
    private Button okButton;
    private CustomEditText detailCustomEditText;
    private EditText titleEditText;

    private SessionManager session;
    private Context context;

    TextView countTextView;

    RecyclerView athletesRecyclerView;
    ChooseAthleteAnnouncementAdapter athletesAdapter;
    RecyclerView.LayoutManager athletesLayoutManager;
    List<Athlete> athletes;

    public AddAnnouncementDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AddAnnouncementDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public AddAnnouncementDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_add_announcement, null);

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
                List<AnnouncementPost.athleteIdClass> athletesIds = athletesAdapter.getSelectedAthletesIds();
                if (detailCustomEditText.getText().length() > 0 ||
                        titleEditText.getText().length() > 0 ||
                        athletesIds.size() > 0) {

                    AnnouncementPost announcementPost = new AnnouncementPost();
                    announcementPost.setCoachId(session.getuserLoggedTypeId());
                    announcementPost.setAnnouncementDetail(detailCustomEditText.getText().toString());
                    announcementPost.setAnnouncementTitle(titleEditText.getText().toString());
                    announcementPost.setAthletesId(athletesIds);

                    onOkButtonClickListener.OnOkButtonClicked(announcementPost);
                }
                else {
                    Toast.makeText(context,"Ingrese un titulo, descripción y seleccione atletas",Toast.LENGTH_LONG).show();
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

        titleEditText = view.findViewById(R.id.titleEditText);
        ColorStateList colorStateList = ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent));
        ViewCompat.setBackgroundTintList(titleEditText, colorStateList);

        athletesRecyclerView = (RecyclerView) view.findViewById(R.id.athletesRecyclerView);
        athletes = new ArrayList<>();
        athletesAdapter = new ChooseAthleteAnnouncementAdapter(athletes);
        athletesLayoutManager = new GridLayoutManager(context, 3);
        athletesRecyclerView.setAdapter(athletesAdapter);
        athletesRecyclerView.setLayoutManager(athletesLayoutManager);

        updateData();
        setView(view);
    }

    public interface OnCancelButtonClickListener {
        void OnCancelButtonClicked();
    }
    public interface OnOkButtonClickListener {
        void OnOkButtonClicked(AnnouncementPost announcementPost);
    }

    public OnCancelButtonClickListener onCancelButtonClickListener;
    public OnOkButtonClickListener onOkButtonClickListener;

    public void setOnCancelButtonClickListener(OnCancelButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
    }

    public void setOnOkButtonClickListener(OnOkButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
    }

    public void updateData(){
        int coachId = Integer.parseInt(session.getuserLoggedTypeId());
        AndroidNetworking.get(GroupSportsApiService.ATHELETES_BY_COACH_URL(coachId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(context.getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athletes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                athletes.add(Athlete.toAthlete(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        athletesAdapter.notifyDataSetChanged();
                        if (response.length() == 0) {
                            //noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        //messageTextView.setText(getResources().getString(R.string.connection_error));
                        //noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }
}






























