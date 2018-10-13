package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.WeightTestBySessionAdapter;
import pe.edu.upc.groupsports.dialogs.AdminValueDialog;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.MoodColor;
import pe.edu.upc.groupsports.models.WeightTestBySession;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeightTestBySessionFragment extends Fragment {
    Athlete currentAthlete;
    SessionManager session;

    RecyclerView weightTestBySesionRecyclerView;
    WeightTestBySessionAdapter weightTestBySessionAdapter;
    RecyclerView.LayoutManager weightTestBySesionLayoutManager;
    List<WeightTestBySession> weightTestBySessions;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;
    Context context;
    private int WEIGHT_EDITED = 0;

    private static int WEIGHT_BEFORE_CLICKED = 0;
    private static int WEIGHT_AFTER_CLICKED = 1;

    public WeightTestBySessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weight_test_by_session, container, false);

        context = view.getContext();
        session = new SessionManager(context);
        weightTestBySesionRecyclerView = (RecyclerView) view.findViewById(R.id.weightTestBySesionRecyclerView);
        weightTestBySessions = new ArrayList<>();
        weightTestBySessionAdapter = new WeightTestBySessionAdapter(weightTestBySessions);
        weightTestBySessionAdapter.setLongClickListener(new WeightTestBySessionAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(WeightTestBySession weightTestBySession) {
                showEditDeleteDialog(weightTestBySession);
            }
        });
        weightTestBySesionLayoutManager = new LinearLayoutManager(view.getContext());
        weightTestBySesionRecyclerView.setAdapter(weightTestBySessionAdapter);
        weightTestBySesionRecyclerView.setLayoutManager(weightTestBySesionLayoutManager);

        weightTestBySessionAdapter.setOnWeightBeforeEditListener(new WeightTestBySessionAdapter.OnWeightBeforeEditListener() {
            @Override
            public void onWeightBeforeEdited(WeightTestBySession weightTestBySession) {
                WEIGHT_EDITED = WEIGHT_BEFORE_CLICKED;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.weight_before_session),weightTestBySession);
            }
        });
        weightTestBySessionAdapter.setOnWeightAfterEditListener(new WeightTestBySessionAdapter.OnWeightAfterEditListener() {
            @Override
            public void onWeightAfterEdited(WeightTestBySession weightTestBySession) {
                WEIGHT_EDITED = WEIGHT_AFTER_CLICKED;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.weight_after_session),weightTestBySession);
            }
        });

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        getWeightTestBySessionData();

        return view;
    }

    private void showEditDeleteDialog(final WeightTestBySession weightTestBySession){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context, true);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteSpeedTestDetail(weightTestBySession);
            }
        });
        editDeleteDialog.setOnEditButtonClickListener(new EditDeleteDialog.OnEditButtonClickListener() {
            @Override
            public void OnEditButtonClicked() {
                editDeleteDialog.dismiss();
            }
        });
    }

    private void deleteSpeedTestDetail(WeightTestBySession weightTestBySession) {
        AndroidNetworking.delete(GroupSportsApiService.WEIGHT_TEST_BY_SESSION_URL + weightTestBySession.getId())
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")){
                                getWeightTestBySessionData();
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar el test",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Hubo un error en el sistema", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showAdminValueDialog(String titleString, String editTextString, final WeightTestBySession weightTestBySession){
        final AdminValueDialog adminValueDialog = new AdminValueDialog(context,titleString,editTextString,"EDITAR");
        adminValueDialog.show();
        adminValueDialog.setOnOkButtonClickListener(new AdminValueDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String value) {
                updateWeightTest(weightTestBySession, value);
                adminValueDialog.dismiss();
            }
        });
        adminValueDialog.setOnCancelButtonClickListener(new AdminValueDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                adminValueDialog.dismiss();
            }
        });
    }

    private void updateWeightTest(WeightTestBySession wbs, String newValue) {

        JSONObject moodTestJSONObject = new JSONObject();

        try {
            moodTestJSONObject.put("weightBefore", WEIGHT_EDITED == WEIGHT_BEFORE_CLICKED ? newValue : wbs.getWeightBefore());
            moodTestJSONObject.put("weightAfter", WEIGHT_EDITED == WEIGHT_AFTER_CLICKED ? newValue : wbs.getWeightAfter());
            moodTestJSONObject.put("sessionId", wbs.getSessionId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(GroupSportsApiService.WEIGHT_TEST_BY_SESSION_URL+wbs.getId())
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(moodTestJSONObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                getWeightTestBySessionData();
                            }
                            else {
                                Toast.makeText(context, "Hubo un error al editar el peso", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Hubo un error en el sistema", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void getWeightTestBySessionData(){
        AndroidNetworking.get(GroupSportsApiService.WEIGHT_TEST_BY_SESSION_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        weightTestBySessions.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                weightTestBySessions.add(WeightTestBySession.toWeightTestBySession(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        weightTestBySessionAdapter.notifyDataSetChanged();
                        if (response.length() == 0) {
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                        }
                        else {
                            noAthletesConstraintLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }
}
