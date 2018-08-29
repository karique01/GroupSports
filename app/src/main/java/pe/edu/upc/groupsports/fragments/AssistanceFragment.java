package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.AthleteAdapter;
import pe.edu.upc.groupsports.adapters.AthleteAssistanceAdapter;
import pe.edu.upc.groupsports.models.Assistance;
import pe.edu.upc.groupsports.models.AssistanceShift;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistanceFragment extends Fragment {
    SessionManager session;

    //recycler things
    RecyclerView athletesRecyclerView;
    AthleteAssistanceAdapter athletesAdapter;
    RecyclerView.LayoutManager athletesLayoutManager;
    List<Assistance> athletesAssistances;
    CardView saveAssistanceButton;
    CardView prevCardView;
    CardView nextCardView;
    Context context;
    int dias = 0;
    Date currentDate = null;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public AssistanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistance, container, false);

        context = view.getContext();
        session = new SessionManager(view.getContext());
        athletesRecyclerView = (RecyclerView) view.findViewById(R.id.athletesRecyclerView);

        athletesAssistances = new ArrayList<>();
        athletesAdapter = new AthleteAssistanceAdapter(athletesAssistances);
        athletesLayoutManager = new LinearLayoutManager(view.getContext());
        athletesRecyclerView.setAdapter(athletesAdapter);
        athletesRecyclerView.setLayoutManager(athletesLayoutManager);
        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        dateToday();

        saveAssistanceButton = (CardView) view.findViewById(R.id.saveAssistanceButton);
        saveAssistanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAssistances();
            }
        });
        prevCardView = (CardView) view.findViewById(R.id.prevCardView);
        prevCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePrev();
            }
        });
        nextCardView = (CardView) view.findViewById(R.id.nextCardView);
        nextCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateNext();
            }
        });

        return view;
    }

    private void dateToday(){
        currentDate = Funciones.getCurrentDate();
        updateData(Funciones.formatDateForAPI(currentDate));
        updateToolbarTitle(Funciones.formatDate(currentDate));
    }
    private void datePrev(){
        currentDate = Funciones.subtractDayToDate(currentDate);
        updateData(Funciones.formatDateForAPI(currentDate));
        updateToolbarTitle(Funciones.formatDate(currentDate));
    }
    private void dateNext(){
        currentDate = Funciones.addDayToDate(currentDate);
        updateData(Funciones.formatDateForAPI(currentDate));
        updateToolbarTitle(Funciones.formatDate(currentDate));
    }

    private void updateToolbarTitle(String title){
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("Asistencia " + title);
        }
    }

    private void updateData(String assistanceDay){
        int coachId = Integer.parseInt(session.getuserLoggedTypeId());
        AndroidNetworking.get(GroupSportsApiService.ASSISTANCE_BY_COACH_BY_DATE(coachId, assistanceDay))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athletesAssistances.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                athletesAssistances.add(Assistance.toAssistance(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        athletesAdapter.notifyDataSetChanged();
                        if (response.length() == 0) {
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                            saveAssistanceButton.setEnabled(false);
                        }
                        else {
                            noAthletesConstraintLayout.setVisibility(View.GONE);
                            saveAssistanceButton.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void updateAssistances(){
        int coachId = Integer.parseInt(session.getuserLoggedTypeId());
        AndroidNetworking.put(GroupSportsApiService.ASSISTANCE_BY_COACH(coachId))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(getAssistanceJson())
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //se actualizó correctamente
                        try {
                            if (response.getString("response").equals("Ok")){
                                onAssistanceButtonPressed.OnAssistanceSaved();
                            }
                            else {
                                Toast.makeText(context,"Error al guardar las asistencias",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context,"Error de conexión",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public JSONObject getAssistanceJson(){
        JSONObject jsonObjectAssistanceUpdate = new JSONObject();
        List<AssistanceShift> assistanceShifts = new ArrayList<>();
        JSONArray assistanceArray = new JSONArray();

        for (int i = 0; i < athletesAssistances.size(); i++) {
            Assistance assistance = athletesAssistances.get(i);
            assistanceShifts.addAll(assistance.getAssistanceShifts());
        }
        for (int i = 0; i < assistanceShifts.size(); i++) {
            assistanceArray.put(assistanceShifts.get(i).toJSONAssistanceShift());
        }

        try {
            jsonObjectAssistanceUpdate.put("assistanceShifts", assistanceArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectAssistanceUpdate;
    }

    public interface OnAssistanceButtonPressed {
        public void OnAssistanceSaved();
    }

    OnAssistanceButtonPressed onAssistanceButtonPressed;

    public void setOnAssistanceButtonPressed(OnAssistanceButtonPressed onAssistanceButtonPressed) {
        this.onAssistanceButtonPressed = onAssistanceButtonPressed;
    }
}
