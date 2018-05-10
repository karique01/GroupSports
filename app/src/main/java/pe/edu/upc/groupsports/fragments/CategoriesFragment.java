package pe.edu.upc.groupsports.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.AthletesCategoriesAdapter;
import pe.edu.upc.groupsports.adapters.TeamAdapter;
import pe.edu.upc.groupsports.models.AthleteCategory;
import pe.edu.upc.groupsports.models.Team;
import pe.edu.upc.groupsports.repositories.AthletesCategoriesRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment {
    //recycler things
    RecyclerView categoriesRecyclerView;
    AthletesCategoriesAdapter athletesCategoriesAdapter;
    RecyclerView.LayoutManager categoriesLayoutManager;
    List<AthleteCategory> athleteCategories;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        categoriesRecyclerView = (RecyclerView) view.findViewById(R.id.categoriesRecyclerView);
        athleteCategories = AthletesCategoriesRepository.getInstance().getAthleteCategories();
        athletesCategoriesAdapter = new AthletesCategoriesAdapter(athleteCategories);
        categoriesLayoutManager = new LinearLayoutManager(view.getContext());
        categoriesRecyclerView.setAdapter(athletesCategoriesAdapter);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);

        return view;
    }

}
