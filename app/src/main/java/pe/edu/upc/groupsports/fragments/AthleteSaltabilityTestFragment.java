package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
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
import pe.edu.upc.groupsports.adapters.SaltabilityTestTypeAdapter;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.JumpTest;
import pe.edu.upc.groupsports.models.SaltabilityTestType;
import pe.edu.upc.groupsports.models.SpeedTest;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.SaltabilityTypesRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteSaltabilityTestFragment extends Fragment {
    Athlete currentAthlete;
    SessionManager session;
    Context context;

    RecyclerView saltabilityTestTypesRecyclerView;
    SaltabilityTestTypeAdapter saltabilityTestTypeAdapter;
    RecyclerView.LayoutManager saltabilityTestLayoutManager;
    List<SaltabilityTestType> saltabilityTestTypes;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public AthleteSaltabilityTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_athlete_saltability_test, container, false);
        context = view.getContext();
        session = new SessionManager(context);
        saltabilityTestTypesRecyclerView = (RecyclerView) view.findViewById(R.id.saltabilityTestTypesRecyclerView);
        saltabilityTestTypes = new ArrayList<>();
        saltabilityTestTypeAdapter = new SaltabilityTestTypeAdapter(saltabilityTestTypes);
        saltabilityTestTypeAdapter.setLongClickListener(new SaltabilityTestTypeAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(JumpTest jumpTest) {
                showEditDeleteDialog(jumpTest);
            }
        });
        saltabilityTestLayoutManager = new LinearLayoutManager(view.getContext());
        saltabilityTestTypesRecyclerView.setAdapter(saltabilityTestTypeAdapter);
        saltabilityTestTypesRecyclerView.setLayoutManager(saltabilityTestLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        updateSaltabilityTestTypeData();

        return view;
    }

    private void showEditDeleteDialog(final JumpTest jumpTest){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context, true);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteTestDetail(jumpTest);
            }
        });
        editDeleteDialog.setOnEditButtonClickListener(new EditDeleteDialog.OnEditButtonClickListener() {
            @Override
            public void OnEditButtonClicked() {
                editDeleteDialog.dismiss();
            }
        });
    }

    private void deleteTestDetail(JumpTest jumpTest) {
        AndroidNetworking.delete(GroupSportsApiService.SALTABILITY_TEST_SESSION_URL + jumpTest.getId())
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
                                updateSaltabilityTestTypeData();
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

    public void updateSaltabilityTestTypeData() {
        AndroidNetworking.get(GroupSportsApiService.SALTABILITY_TEST_TYPE_SESSION_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        saltabilityTestTypes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                saltabilityTestTypes.add(SaltabilityTestType.toSaltabilityTestType(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SaltabilityTypesRepository.getInstance().setSaltabilityTestTypes(saltabilityTestTypes);
                        if (isAdded()) {
                            updateSaltabilityTestData();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void updateSaltabilityTestData() {
        AndroidNetworking.get(GroupSportsApiService.SALTABILITY_TEST_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        clearTest();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JumpTest jumpTest = JumpTest.toJumpTest(response.getJSONObject(i));
                                setJumpTestInJumpTestType(jumpTest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        saltabilityTestTypeAdapter.notifyDataSetChanged();
                        if (response.length() == 0) {
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                        } else {
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

    private void clearTest(){
        for (int i = 0; i < saltabilityTestTypes.size(); i++) {
            saltabilityTestTypes.get(i).clearTests();
        }
    }

    private void setJumpTestInJumpTestType(JumpTest jumpTest){
        for (int i = 0; i < saltabilityTestTypes.size(); i++) {
            SaltabilityTestType stt = saltabilityTestTypes.get(i);
            if (stt.getId().equals(jumpTest.getJumpTestTypeId())){
                stt.addSaltabilityTest(jumpTest);
            }
        }
    }

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }
}
