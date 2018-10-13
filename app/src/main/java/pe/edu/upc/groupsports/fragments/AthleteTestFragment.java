package pe.edu.upc.groupsports.fragments;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import pe.edu.upc.groupsports.adapters.SpeedTestAdapter;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.SpeedTest;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteTestFragment extends Fragment {

    private TabLayout _homeTabLayout;
    private ViewPager _homeViewPager;
    private ViewPagerAdapter adapter;

    public AthleteSpeedTestFragment athleteSpeedTestFragment;
    public AthleteSaltabilityTestFragment athleteSaltabilityTestFragment;
    public AthleteStrengthTestFragment athleteStrengthTestFragment;
    public AthleteMoodTestFragment athleteMoodTestFragment;
    public ShotPutTestFragment shotPutTestFragment;
    public WeightTestBySessionFragment weightTestBySessionFragment;
    public AntropometricTestFragment antropometricTestFragment;

    Athlete currentAthlete;

    private int CURRENT_TEST_FRAGMENT_SELECTED = 0;
    public static final int SPEED_TEST_FRAGMENT_SELECTED = 0;
    public static final int SALTABILITY_TEST_FRAGMENT_SELECTED = 1;
    public static final int STRENGTH_TEST_FRAGMENT_SELECTED = 2;
    public static final int MOOD_TEST_FRAGMENT_SELECTED = 3;
    public static final int SHOT_PUT_TEST_FRAGMENT_SELECTED = 4;
    public static final int WEIGHT_TEST_BY_SESSION_FRAGMENT_SELECTED = 5;
    public static final int ANTROPOMETRIC_TEST_FRAGMENT_SELECTED = 6;

    public AthleteTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_athlete_test, container, false);
        initializaTab(view);
        return view;
    }

    private void initializaTab(View view){
        _homeTabLayout = (TabLayout) view.findViewById(R.id.home_tabs);
        _homeViewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        athleteSpeedTestFragment = new AthleteSpeedTestFragment();
        athleteSpeedTestFragment.setCurrentAthlete(currentAthlete);

        athleteSaltabilityTestFragment = new AthleteSaltabilityTestFragment();
        athleteSaltabilityTestFragment.setCurrentAthlete(currentAthlete);

        athleteStrengthTestFragment = new AthleteStrengthTestFragment();
        athleteStrengthTestFragment.setCurrentAthlete(currentAthlete);

        shotPutTestFragment = new ShotPutTestFragment();
        shotPutTestFragment.setCurrentAthlete(currentAthlete);

        athleteMoodTestFragment = new AthleteMoodTestFragment();
        athleteMoodTestFragment.setCurrentAthlete(currentAthlete);

        weightTestBySessionFragment = new WeightTestBySessionFragment();
        weightTestBySessionFragment.setCurrentAthlete(currentAthlete);

        antropometricTestFragment = new AntropometricTestFragment();
        antropometricTestFragment.setCurrentAthlete(currentAthlete);

        adapter.addFragment(athleteSpeedTestFragment, "Velocidad");
        adapter.addFragment(athleteSaltabilityTestFragment, "Salto");
        adapter.addFragment(athleteStrengthTestFragment, "Fuerza");
        adapter.addFragment(athleteMoodTestFragment, "Animo");
        adapter.addFragment(shotPutTestFragment, "Lanzamiento");
        adapter.addFragment(weightTestBySessionFragment, "Test Peso Corporal");
        adapter.addFragment(antropometricTestFragment, "Test Antropometrico");

        _homeViewPager.setAdapter(adapter);
        _homeTabLayout.setupWithViewPager(_homeViewPager);
        _homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                CURRENT_TEST_FRAGMENT_SELECTED = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }

    public int getCurrentTestFragmentSelected() {
        return CURRENT_TEST_FRAGMENT_SELECTED;
    }
}
