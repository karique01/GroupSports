package pe.edu.upc.groupsports.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.edu.upc.groupsports.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteDetailsFragment extends Fragment {


    public AthleteDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_athlete_details, container, false);
    }

}
