package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.AthleteSpeedPerformancesAdapter;
import pe.edu.upc.groupsports.adapters.AthletesCategoriesForPerformanceAdapter;
import pe.edu.upc.groupsports.models.AthleteCategory;
import pe.edu.upc.groupsports.models.AthleteSpeedPerformance;
import pe.edu.upc.groupsports.models.Coach;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.AthletesCategoriesRepository;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformaceSpeedFragment extends Fragment {
    SessionManager session;

    RecyclerView categoriesRecyclerView;
    AthletesCategoriesForPerformanceAdapter athletesCategoriesForPerformanceAdapter;
    RecyclerView.LayoutManager categoriesLayoutManager;
    List<AthleteCategory> athleteCategories;

    View mView;

    Context context;
    public int categoryPos = 1;

    private AutoCompleteTextView metersAutoCompleteTextView;
    String currentMetersOfSpeedTest = "100";
    String[] weekTypesArr = new String[]{"10","20","30","40","50","55","60","70","80","90","100","120","140","160","180","200","250","300","350","400"};

    AppCompatSpinner categorySpinner;
    String[] categoriesTypesArr = new String[]{"Por mejor promedio","Por mejor marca"};

    RecyclerView athletePerformanceSpeedRecyclerView;
    AthleteSpeedPerformancesAdapter athleteSpeedPerformancesAdapter;
    RecyclerView.LayoutManager athleteSpeedPerformancesLayoutManager;
    List<AthleteSpeedPerformance> athleteSpeedPerformances;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public PerformaceSpeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_performace_speed, container, false);
        mView = view;

        context = view.getContext();

        session = new SessionManager(context);
        categoriesRecyclerView = (RecyclerView) view.findViewById(R.id.categoriesRecyclerView);
        athleteCategories = AthletesCategoriesRepository.getInstance().getAthleteCategories();
        athletesCategoriesForPerformanceAdapter = new AthletesCategoriesForPerformanceAdapter(athleteCategories);
        athletesCategoriesForPerformanceAdapter.deseleccionarTodo();
        athletesCategoriesForPerformanceAdapter.setSelectedAthlete(categoryPos);
        categoriesLayoutManager = new LinearLayoutManager(context);
        athletesCategoriesForPerformanceAdapter.setOnCategorySelectedListener(new AthletesCategoriesForPerformanceAdapter.OnCategorySelectedListener() {
            @Override
            public void categorySelected() {
                getSpeedPerformanceData();
            }
        });
        categoriesRecyclerView.setAdapter(athletesCategoriesForPerformanceAdapter);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);

        metersAutoCompleteTextView = view.findViewById(R.id.metersAutoCompleteTextView);
        setAutoCompleteSpeedMetersData();

        categorySpinner = view.findViewById(R.id.categorySpinner);
        setSpinnerCategories();

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        athletePerformanceSpeedRecyclerView = (RecyclerView) view.findViewById(R.id.athletePerformanceSpeedRecyclerView);
        athleteSpeedPerformances = new ArrayList<>();
        athleteSpeedPerformancesAdapter = new AthleteSpeedPerformancesAdapter(athleteSpeedPerformances);
        athleteSpeedPerformancesLayoutManager = new LinearLayoutManager(context);
        athletePerformanceSpeedRecyclerView.setAdapter(athleteSpeedPerformancesAdapter);
        athletePerformanceSpeedRecyclerView.setLayoutManager(athleteSpeedPerformancesLayoutManager);
        getSpeedPerformanceData();

        return view;
    }

    public void getSpeedPerformanceData(){
        AndroidNetworking.get(GroupSportsApiService.SPEED_PERFORMANCES_BY_COACH_BY_METERS(session.getuserLoggedTypeId(),currentMetersOfSpeedTest))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athleteSpeedPerformances.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                addByCatgoryPos(AthleteSpeedPerformance.toAthleteSpeedPerformance(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        athleteSpeedPerformancesAdapter.sort();
                        athleteSpeedPerformancesAdapter.notifyDataSetChanged();
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

    private void addByCatgoryPos(AthleteSpeedPerformance asp){
        int categoryPosSelected = athletesCategoriesForPerformanceAdapter.getSelectedCategoryPos();
        if (categoryPosSelected == 0) {
            if (asp.getAge() >= 30)
                athleteSpeedPerformances.add(asp);
        }
        else if (categoryPosSelected == 1){
            if (asp.getAge() < 30)
                athleteSpeedPerformances.add(asp);
        }
        else if (categoryPosSelected == 2){
            if (asp.getAge() < 23)
                athleteSpeedPerformances.add(asp);
        }
        else if (categoryPosSelected == 3){
            if (asp.getAge() < 20)
                athleteSpeedPerformances.add(asp);
        }
        else if (categoryPosSelected == 4){
            if (asp.getAge() < 18)
                athleteSpeedPerformances.add(asp);
        }
        else if (categoryPosSelected == 5){
            if (asp.getAge() < 16)
                athleteSpeedPerformances.add(asp);
        }
        else if (categoryPosSelected == 6){
            if (asp.getAge() < 14)
                athleteSpeedPerformances.add(asp);
        }
        else if (categoryPosSelected == 7){
            if (asp.getAge() < 12)
                athleteSpeedPerformances.add(asp);
        }
    }

    private void setSpinnerCategories() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item, categoriesTypesArr
        );
        categorySpinner.setAdapter(arrayAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0){
                    athleteSpeedPerformancesAdapter.sortByMedia();
                }
                else {
                    athleteSpeedPerformancesAdapter.sortByBestMark();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setAutoCompleteSpeedMetersData() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item, weekTypesArr
        );
        metersAutoCompleteTextView.setThreshold(1);
        metersAutoCompleteTextView.setAdapter(arrayAdapter);
        metersAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                metersAutoCompleteTextView.showDropDown();
                Funciones.hideKeyboardFromContext(context,mView);
            }
        });
        metersAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                metersAutoCompleteTextView.showDropDown();
                Funciones.hideKeyboardFromContext(context,mView);
            }
        });

        metersAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentMetersOfSpeedTest = weekTypesArr[i];
                Funciones.hideKeyboardFromContext(context,mView);
                getSpeedPerformanceData();
            }
        });
    }

}
