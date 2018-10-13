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
import pe.edu.upc.groupsports.adapters.AntropometricTestAdapter;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.AntropometricTest;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.ShotPutTest;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AntropometricTestFragment extends Fragment {
    Athlete currentAthlete;
    SessionManager session;
    Context context;

    RecyclerView antropometricTestRecyclerView;
    AntropometricTestAdapter antropometricTestAdapter;
    RecyclerView.LayoutManager antropometricTestLayoutManager;
    List<AntropometricTest> antropometricTests;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public AntropometricTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_antropometric_test, container, false);

        context = view.getContext();
        session = new SessionManager(context);
        antropometricTestRecyclerView = (RecyclerView) view.findViewById(R.id.antropometricTestRecyclerView);
        antropometricTests = new ArrayList<>();
        antropometricTestAdapter = new AntropometricTestAdapter(antropometricTests);
        antropometricTestAdapter.setLongClickListener(new AntropometricTestAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(AntropometricTest antropometricTest) {
                showEditDeleteDialog(antropometricTest);
            }
        });
        antropometricTestLayoutManager = new LinearLayoutManager(view.getContext());
        antropometricTestRecyclerView.setAdapter(antropometricTestAdapter);
        antropometricTestRecyclerView.setLayoutManager(antropometricTestLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        updateAntropometricTestData();

        return view;
    }

    private void showEditDeleteDialog(final AntropometricTest antropometricTest){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context, true);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteSpeedTestDetail(antropometricTest);
            }
        });
        editDeleteDialog.setOnEditButtonClickListener(new EditDeleteDialog.OnEditButtonClickListener() {
            @Override
            public void OnEditButtonClicked() {
                editDeleteDialog.dismiss();
            }
        });
    }

    private void deleteSpeedTestDetail(AntropometricTest antropometricTest) {
        AndroidNetworking.delete(GroupSportsApiService.ANTROPOMETRIC_TEST_SESSION_URL + antropometricTest.getId())
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
                                updateAntropometricTestData();
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

    public void updateAntropometricTestData(){
        AndroidNetworking.get(GroupSportsApiService.ANTROPOMETRIC_TEST_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        antropometricTests.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                antropometricTests.add(AntropometricTest.toAntropometricTest(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        antropometricTestAdapter.notifyDataSetChanged();
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
