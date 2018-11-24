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
import pe.edu.upc.groupsports.adapters.AthleteJumpPerformancesAdapter;
import pe.edu.upc.groupsports.adapters.AthletesCategoriesForPerformanceAdapter;
import pe.edu.upc.groupsports.models.AthleteCategory;
import pe.edu.upc.groupsports.models.AthleteJumpPerformance;
import pe.edu.upc.groupsports.models.SaltabilityTestType;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.AthletesCategoriesRepository;
import pe.edu.upc.groupsports.repositories.SaltabilityTypesRepository;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformanceSaltabilityFragment extends Fragment {

    SessionManager session;

    RecyclerView categoriesRecyclerView;
    AthletesCategoriesForPerformanceAdapter athletesCategoriesForPerformanceAdapter;
    RecyclerView.LayoutManager categoriesLayoutManager;
    List<AthleteCategory> athleteCategories;

    View mView;

    Context context;
    public int categoryPos = 1;

    private AppCompatSpinner jumpTestTypeSpinner;
    String currentJumpTestType = "1";

    AppCompatSpinner categorySpinner;
    String[] categoriesTypesArr = new String[]{"Por mejor promedio","Por mejor marca"};

    RecyclerView athletePerformanceSpeedRecyclerView;
    AthleteJumpPerformancesAdapter athleteJumpPerformancesAdapter;
    RecyclerView.LayoutManager athleteSpeedPerformancesLayoutManager;
    List<AthleteJumpPerformance> athleteJumpPerformances;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    List<SaltabilityTestType> saltabilityTestTypes;
    String[] saltaTypesArr;

    public PerformanceSaltabilityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance_saltability, container, false);
        mView = view;

        context = view.getContext();

        session = new SessionManager(context);
        jumpTestTypeSpinner = view.findViewById(R.id.jumpTestTypeSpinner);
        saltabilityTestTypes = new ArrayList<>();

        categoriesRecyclerView = (RecyclerView) view.findViewById(R.id.categoriesRecyclerView);
        athleteCategories = AthletesCategoriesRepository.getInstance().getAthleteCategories();
        athletesCategoriesForPerformanceAdapter = new AthletesCategoriesForPerformanceAdapter(athleteCategories);
        athletesCategoriesForPerformanceAdapter.deseleccionarTodo();
        athletesCategoriesForPerformanceAdapter.setSelectedAthlete(categoryPos);
        categoriesLayoutManager = new LinearLayoutManager(context);
        athletesCategoriesForPerformanceAdapter.setOnCategorySelectedListener(new AthletesCategoriesForPerformanceAdapter.OnCategorySelectedListener() {
            @Override
            public void categorySelected() {
                getJumpPerformanceData();
            }
        });
        categoriesRecyclerView.setAdapter(athletesCategoriesForPerformanceAdapter);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);

        updateSaltabilityTestTypeData();

        categorySpinner = view.findViewById(R.id.categorySpinner);
        setSpinnerCategories();

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        athletePerformanceSpeedRecyclerView = (RecyclerView) view.findViewById(R.id.athletePerformanceSpeedRecyclerView);
        athleteJumpPerformances = new ArrayList<>();
        athleteJumpPerformancesAdapter = new AthleteJumpPerformancesAdapter(athleteJumpPerformances);
        athleteSpeedPerformancesLayoutManager = new LinearLayoutManager(context);
        athletePerformanceSpeedRecyclerView.setAdapter(athleteJumpPerformancesAdapter);
        athletePerformanceSpeedRecyclerView.setLayoutManager(athleteSpeedPerformancesLayoutManager);
        getJumpPerformanceData();

        return view;
    }

    public void getJumpPerformanceData(){
        AndroidNetworking.get(GroupSportsApiService.JUMP_PERFORMANCES_BY_COACH_BY_JUMPTESTTYPEID(session.getuserLoggedTypeId(), currentJumpTestType))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athleteJumpPerformances.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                addByCatgoryPos(AthleteJumpPerformance.toAthleteJumpPerformance(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        athleteJumpPerformancesAdapter.sort();
                        athleteJumpPerformancesAdapter.notifyDataSetChanged();
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

    private void addByCatgoryPos(AthleteJumpPerformance ajp){
        int categoryPosSelected = athletesCategoriesForPerformanceAdapter.getSelectedCategoryPos();
        if (categoryPosSelected == 0) {
            if (ajp.getAge() >= 30)
                athleteJumpPerformances.add(ajp);
        }
        else if (categoryPosSelected == 1){
            if (ajp.getAge() < 30)
                athleteJumpPerformances.add(ajp);
        }
        else if (categoryPosSelected == 2){
            if (ajp.getAge() < 23)
                athleteJumpPerformances.add(ajp);
        }
        else if (categoryPosSelected == 3){
            if (ajp.getAge() < 20)
                athleteJumpPerformances.add(ajp);
        }
        else if (categoryPosSelected == 4){
            if (ajp.getAge() < 18)
                athleteJumpPerformances.add(ajp);
        }
        else if (categoryPosSelected == 5){
            if (ajp.getAge() < 16)
                athleteJumpPerformances.add(ajp);
        }
        else if (categoryPosSelected == 6){
            if (ajp.getAge() < 14)
                athleteJumpPerformances.add(ajp);
        }
        else if (categoryPosSelected == 7){
            if (ajp.getAge() < 12)
                athleteJumpPerformances.add(ajp);
        }
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
                            setSaltabilityTypeSpinnerData();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {}
                });
    }

    private void setSaltabilityTypeSpinnerData() {
        List<String> saltaStrings = new ArrayList<>();
        for (int i = 0; i < saltabilityTestTypes.size(); i++) {
            saltaStrings.add(saltabilityTestTypes.get(i).getDescription());
        }
        saltaTypesArr = saltaStrings.toArray(new String[]{});
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item, saltaTypesArr
        );
        jumpTestTypeSpinner.setAdapter(arrayAdapter);
        jumpTestTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentJumpTestType = saltabilityTestTypes.get(i).getId();
                Funciones.hideKeyboardFromContext(context,mView);
                getJumpPerformanceData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
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
                    athleteJumpPerformancesAdapter.sortByMedia();
                }
                else {
                    athleteJumpPerformancesAdapter.sortByBestMark();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
