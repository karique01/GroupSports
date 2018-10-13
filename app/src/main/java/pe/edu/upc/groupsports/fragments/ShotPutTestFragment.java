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
import pe.edu.upc.groupsports.adapters.ShotPutTestAdapter;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.ShotPutTest;
import pe.edu.upc.groupsports.models.SpeedTest;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShotPutTestFragment extends Fragment {
    Athlete currentAthlete;
    SessionManager session;
    Context context;

    RecyclerView shotPutTestRecyclerView;
    ShotPutTestAdapter shotPutTestAdapter;
    RecyclerView.LayoutManager shotPutTestLayoutManager;
    List<ShotPutTest> shotPutTests;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public ShotPutTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shot_put_test, container, false);

        context = view.getContext();
        session = new SessionManager(context);
        shotPutTestRecyclerView = (RecyclerView) view.findViewById(R.id.shotPutTestRecyclerView);
        shotPutTests = new ArrayList<>();
        shotPutTestAdapter = new ShotPutTestAdapter(shotPutTests);
        shotPutTestAdapter.setLongClickListener(new ShotPutTestAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(ShotPutTest shotPutTest) {
                showEditDeleteDialog(shotPutTest);
            }
        });
        shotPutTestLayoutManager = new LinearLayoutManager(view.getContext());
        shotPutTestRecyclerView.setAdapter(shotPutTestAdapter);
        shotPutTestRecyclerView.setLayoutManager(shotPutTestLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        updateData();

        return view;
    }

    private void showEditDeleteDialog(final ShotPutTest shotPutTest){
        final EditDeleteDialog editDeleteDialog = new EditDeleteDialog(context, true);
        editDeleteDialog.show();
        editDeleteDialog.setOnDeleteButtonClickListener(new EditDeleteDialog.OnDeleteButtonClickListener() {
            @Override
            public void OnDeleteButtonClicked() {
                editDeleteDialog.dismiss();
                deleteSpeedTestDetail(shotPutTest);
            }
        });
        editDeleteDialog.setOnEditButtonClickListener(new EditDeleteDialog.OnEditButtonClickListener() {
            @Override
            public void OnEditButtonClicked() {
                editDeleteDialog.dismiss();
            }
        });
    }

    private void deleteSpeedTestDetail(ShotPutTest shotPutTest) {
        AndroidNetworking.delete(GroupSportsApiService.SHOT_PUT_TEST_URL + shotPutTest.getId())
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
                                updateData();
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

    public void updateData(){
        AndroidNetworking.get(GroupSportsApiService.SHOT_PUT_TEST_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        shotPutTests.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                shotPutTests.add(ShotPutTest.toShotPutTest(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        shotPutTestAdapter.notifyDataSetChanged();
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
