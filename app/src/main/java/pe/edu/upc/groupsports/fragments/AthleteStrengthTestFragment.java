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
import pe.edu.upc.groupsports.adapters.StrengthTestTypeAdapter;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.JumpTest;
import pe.edu.upc.groupsports.models.SaltabilityTestType;
import pe.edu.upc.groupsports.models.StrengthTest;
import pe.edu.upc.groupsports.models.StrengthTestType;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.StrengthTypesRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteStrengthTestFragment extends Fragment {
    Athlete currentAthlete;
    SessionManager session;
    Context context;

    RecyclerView strengthTestTypesRecyclerView;
    StrengthTestTypeAdapter strengthTestTypeAdapter;
    RecyclerView.LayoutManager strengthTestLayoutManager;
    List<StrengthTestType> strengthTestTypes;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public AthleteStrengthTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_athlete_strength_test, container, false);
        context = view.getContext();
        session = new SessionManager(context);
        strengthTestTypesRecyclerView = (RecyclerView) view.findViewById(R.id.strengthTestTypesRecyclerView);
        strengthTestTypes = new ArrayList<>();
        strengthTestTypeAdapter = new StrengthTestTypeAdapter(strengthTestTypes);
        strengthTestTypeAdapter.setLongClickListener(new StrengthTestTypeAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(StrengthTest strengthTest) {
                showEditDeleteDialog(strengthTest);
            }
        });
        strengthTestLayoutManager = new LinearLayoutManager(context);
        strengthTestTypesRecyclerView.setAdapter(strengthTestTypeAdapter);
        strengthTestTypesRecyclerView.setLayoutManager(strengthTestLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        updateStrengthTestTypeData();

        return view;
    }

    private void showEditDeleteDialog(final StrengthTest strengthTest){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context, true);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteTestDetail(strengthTest);
            }
        });
        editDeleteDialog.setOnEditButtonClickListener(new EditDeleteDialog.OnEditButtonClickListener() {
            @Override
            public void OnEditButtonClicked() {
                editDeleteDialog.dismiss();
            }
        });
    }

    private void deleteTestDetail(StrengthTest strengthTest) {
        AndroidNetworking.delete(GroupSportsApiService.STRENGTH_TEST_SESSION_URL + strengthTest.getId())
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
                                updateStrengthTestTypeData();
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

    public void updateStrengthTestTypeData() {
        AndroidNetworking.get(GroupSportsApiService.STRENGTH_TEST_TYPE_SESSION_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        strengthTestTypes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                strengthTestTypes.add(StrengthTestType.toStrengthTestType(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        StrengthTypesRepository.getInstance().setStrengthTestTypes(strengthTestTypes);
                        updateStrengthTestData();
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void updateStrengthTestData() {
        AndroidNetworking.get(GroupSportsApiService.STRENGTH_TEST_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                //.setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        clearTest();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                StrengthTest strengthTest = StrengthTest.toStrengthTest(response.getJSONObject(i));
                                setJumpTestInJumpTestType(strengthTest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        strengthTestTypeAdapter.notifyDataSetChanged();
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
        for (int i = 0; i < strengthTestTypes.size(); i++) {
            strengthTestTypes.get(i).clearTests();
        }
    }

    private void setJumpTestInJumpTestType(StrengthTest strengthTest){
        for (int i = 0; i < strengthTestTypes.size(); i++) {
            StrengthTestType stt = strengthTestTypes.get(i);
            if (stt.getId().equals(strengthTest.getStrengthTestTypeId())){
                stt.addStrengthTest(strengthTest);
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
