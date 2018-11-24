package pe.edu.upc.groupsports.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthletePerformancesFragment extends Fragment {
    private TabLayout _homeTabLayout;
    private ViewPager _homeViewPager;
    private ViewPagerAdapter adapter;

    public PerformaceSpeedFragment performaceSpeedFragment;
    public PerformanceSaltabilityFragment performanceSaltabilityFragment;

    private int CURRENT_TEST_FRAGMENT_SELECTED = 0;

    public AthletePerformancesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_athlete_performances, container, false);
        initializaTab(view);
        return view;
    }

    private void initializaTab(View view){
        _homeTabLayout = (TabLayout) view.findViewById(R.id.home_tabs);
        _homeViewPager = (ViewPager) view.findViewById(R.id.home_viewpager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        performaceSpeedFragment = new PerformaceSpeedFragment();
        performanceSaltabilityFragment = new PerformanceSaltabilityFragment();

        adapter.addFragment(performaceSpeedFragment, "Velocidad");
        adapter.addFragment(performanceSaltabilityFragment, "Saltabilidad");

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
}
