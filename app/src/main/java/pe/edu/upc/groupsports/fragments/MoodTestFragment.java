package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.MoodAdapter;
import pe.edu.upc.groupsports.adapters.MoodColorAdapter;
import pe.edu.upc.groupsports.dialogs.EditDeleteDialog;
import pe.edu.upc.groupsports.models.Mood;
import pe.edu.upc.groupsports.models.MoodColor;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.MoodColorsRepository;
import pe.edu.upc.groupsports.util.Funciones;

public class MoodTestFragment extends Fragment {
    SessionManager session;
    Context context;

    //recycler moods
    RecyclerView lastMoodsRecyclerView;
    MoodAdapter moodsAdapter;
    RecyclerView.LayoutManager moodsLayoutManager;
    List<Mood> moods;
    List<Mood> moodsToResponse;

    //recycler color moods
    RecyclerView moodsColorRecyclerView;
    MoodColorAdapter moodcolorsAdapter;
    RecyclerView.LayoutManager moodColorsLayoutManager;
    List<MoodColor> moodColors;

    CardView evaluatedMoodCardView;
    TextView moodDate;
    TextView hourTextView;
    Button sendButton;
    CardView numberTestCardView;
    TextView numberTestTextView;

    Mood currentMoodTesting;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public MoodTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood_test, container, false);

        context = view.getContext();

        evaluatedMoodCardView = (CardView) view.findViewById(R.id.evaluatedMoodCardView);
        moodDate = (TextView) view.findViewById(R.id.moodDate);
        hourTextView = (TextView) view.findViewById(R.id.hourTextView);
        sendButton = (Button) view.findViewById(R.id.sendButton);
        numberTestCardView = (CardView) view.findViewById(R.id.numberTestCardView);
        numberTestTextView = (TextView) view.findViewById(R.id.numberTestTextView);

        session = new SessionManager(view.getContext());
        lastMoodsRecyclerView = (RecyclerView) view.findViewById(R.id.lastMoodsRecyclerView);
        moods = new ArrayList<>();
        moodsToResponse = new ArrayList<>();
        moodsAdapter = new MoodAdapter(moods);
        moodsLayoutManager = new LinearLayoutManager(context);
        lastMoodsRecyclerView.setAdapter(moodsAdapter);
        lastMoodsRecyclerView.setLayoutManager(moodsLayoutManager);

        moodsColorRecyclerView = (RecyclerView) view.findViewById(R.id.moodsColorRecyclerView);
        moodColors = MoodColorsRepository.getInstance().getMoodColors();
        moodcolorsAdapter = new MoodColorAdapter(moodColors);
        moodColorsLayoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        moodsColorRecyclerView.setAdapter(moodcolorsAdapter);
        moodsColorRecyclerView.setLayoutManager(moodColorsLayoutManager);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoodColor moodColorSelected = moodcolorsAdapter.getSelected();
                if (moodColorSelected != null){
                    evaluatedMoodCardView.setVisibility(View.GONE);
                    updateMoodTest(moodColorSelected);
                }
                else{
                    Toast.makeText(context,"Seleccione una tarjeta",Toast.LENGTH_LONG).show();
                }
            }
        });

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        updateData();
        getCurrentTests();

        return view;
    }

    private void updateData(){
        AndroidNetworking.get(GroupSportsApiService.MOODS_BY_ATHLETE(session.getuserLoggedTypeId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        moods.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                moods.add(Mood.toMood(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        moodsAdapter.notifyDataSetChanged();
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

    private void getCurrentTests(){
        String day = Funciones.formatDateForAPI(Funciones.getCurrentDate());
        AndroidNetworking.get(GroupSportsApiService.MOOD_TEST_BY_ATHLETE_BY_DATE(session.getuserLoggedTypeId(),day))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        moodsToResponse.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                moodsToResponse.add(Mood.toMood(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        showMoodTests();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void showMoodTests() {
        numberTestTextView.setText(String.valueOf(moodsToResponse.size()));
        if (moodsToResponse.size() > 0){
            evaluatedMoodCardView.setVisibility(View.VISIBLE);
            numberTestCardView.setVisibility(View.VISIBLE);

            currentMoodTesting = moodsToResponse.get(0);
            moodDate.setText(Funciones.formatDate(currentMoodTesting.getDayOfMood()));
            hourTextView.setText(Funciones.formatDateToHour(currentMoodTesting.getDayOfMood()));
        }
        else {
            evaluatedMoodCardView.setVisibility(View.GONE);
            numberTestCardView.setVisibility(View.GONE);
        }
    }

    private void updateMoodTest(MoodColor moodColorSelected) {

        JSONObject moodTestJSONObject = new JSONObject();

        try {
            moodTestJSONObject.put("colorOfMoodId", moodColorSelected.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(GroupSportsApiService.MOOD_TEST_URL+currentMoodTesting.getId())
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

                                FragmentActivity fa = getActivity();
                                if (fa != null) {
                                    View view = fa.getCurrentFocus();
                                    if (view != null) {
                                        Snackbar.make(view, "Test evaluado correctamente", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null)
                                                .show();
                                        evaluatedMoodCardView.setVisibility(View.VISIBLE);
                                        updateData();
                                        getCurrentTests();
                                    }
                                }
                            }
                            else {
                                Toast.makeText(context, "Hubo un error al subir el test", Toast.LENGTH_LONG).show();
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
}
