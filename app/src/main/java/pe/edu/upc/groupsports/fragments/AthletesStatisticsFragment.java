package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.custom.DayAxisValueFormatter;
import pe.edu.upc.groupsports.custom.KilosAxisValueFormatter;
import pe.edu.upc.groupsports.custom.MetersAxisValueFormatter;
import pe.edu.upc.groupsports.custom.SecondsAxisValueFormatter;
import pe.edu.upc.groupsports.custom.XYMarkerSaltabilityView;
import pe.edu.upc.groupsports.custom.XYMarkerStrengthView;
import pe.edu.upc.groupsports.custom.XYMarkerView;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.JumpTest;
import pe.edu.upc.groupsports.models.SaltabilityTestType;
import pe.edu.upc.groupsports.models.SpeedTest;
import pe.edu.upc.groupsports.models.StrengthTest;
import pe.edu.upc.groupsports.models.StrengthTestType;
import pe.edu.upc.groupsports.network.GroupSportsApiService;
import pe.edu.upc.groupsports.repositories.SaltabilityTypesRepository;
import pe.edu.upc.groupsports.repositories.StrengthTypesRepository;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthletesStatisticsFragment extends Fragment implements OnChartValueSelectedListener {
    TabLayout speedTimingTabLayout;
    BarChart speedBarChart;
    Context context;
    List<SpeedTest> speedTests;
    Athlete currentAthlete;
    SessionManager session;
    View mView;
    String currentMetersOfSpeedTest = "100";
    Date startDateSpeed, endDateSpeed;

    private AutoCompleteTextView metersAutoCompleteTextView;
    String[] weekTypesArr = new String[]{"10","20","30","40","50","55","60","70","80","90","100","120","140","160","180","200","250","300","350","400"};

    ImageButton nextDataSpeedTest;
    ImageButton prevDataSpeedTest;

    //Saltabilidad--------------------------------------------------------------------------------------------------------------------------------

    TabLayout saltabilityTimingTabLayout;
    BarChart saltabilityBarChart;

    ImageButton prevDataSaltabilityTest;
    ImageButton nextDataSaltabilityTest;

    AppCompatSpinner saltabilityTypeSpinner;
    Date startDateSaltability, endDateSpeedSaltability;

    List<SaltabilityTestType> saltabilityTestTypes;
    int currentSaltabilityTestDataTypePos = 0;

    //Fuerza--------------------------------------------------------------------------------------------------------------------------------------
    TabLayout strengthTimingTabLayout;
    BarChart strengthBarChart;

    ImageButton prevDataStrengthTest;
    ImageButton nextDataStrengthTest;

    AppCompatSpinner strengthTypeSpinner;
    Date startDateStrength, endDateStrength;

    List<StrengthTestType> strengthTestTypes;
    int currentStrengthTestDataTypePos = 0;

    public AthletesStatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_athletes_statistics, container, false);

        mView = view;
        context = view.getContext();
        session = new SessionManager(context);
        speedTimingTabLayout = (TabLayout) view.findViewById(R.id.speedTimingTabLayout);
        speedTimingTabLayout.addTab(speedTimingTabLayout.newTab().setText(R.string.week));
        metersAutoCompleteTextView = view.findViewById(R.id.metersAutoCompleteTextView);
        setAutoCompleteSpeedMetersData();

        nextDataSpeedTest = (ImageButton) view.findViewById(R.id.nextDataSpeedTest);
        nextDataSpeedTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDateToNextWeekSpeed();
            }
        });
        prevDataSpeedTest = (ImageButton) view.findViewById(R.id.prevDataSpeedTest);
        prevDataSpeedTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDateToPrevWeekSpeed();
            }
        });

        speedTests = new ArrayList<>();
        speedBarChart = (BarChart) view.findViewById(R.id.speedBarChart);
        configurateChartSpeed(speedBarChart);
        getCurrentStartDateAndEndDateOfWeekForSpeed();
        updateDataForSpeedTest();

        //Saltabilidad--------------------------------------------------------------------------------------------------------------------------------

        saltabilityTestTypes = new ArrayList<>();
        saltabilityTimingTabLayout = (TabLayout) view.findViewById(R.id.saltabilityTimingTabLayout);
        saltabilityTimingTabLayout.addTab(saltabilityTimingTabLayout.newTab().setText(R.string.week));

        saltabilityBarChart = (BarChart) view.findViewById(R.id.saltabilityBarChart);
        nextDataSaltabilityTest = (ImageButton) view.findViewById(R.id.nextDataSaltabilityTest);
        nextDataSaltabilityTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDateToNextWeekSaltability();
            }
        });
        prevDataSaltabilityTest = (ImageButton) view.findViewById(R.id.prevDataSaltabilityTest);
        prevDataSaltabilityTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDateToPrevWeekSaltability();
            }
        });
        saltabilityTypeSpinner = (AppCompatSpinner) view.findViewById(R.id.saltabilityTypeSpinner);
        saltabilityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentSaltabilityTestDataTypePos = i;
                setSaltabilityTestData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        configurateChartSaltability(saltabilityBarChart);
        getCurrentStartDateAndEndDateOfWeekForSaltability();
        updateSaltabilityTestTypeData();

        //Fuerza--------------------------------------------------------------------------------------------------------------------------------------

        strengthTestTypes = new ArrayList<>();
        strengthTimingTabLayout = (TabLayout) view.findViewById(R.id.strengthTimingTabLayout);
        strengthTimingTabLayout.addTab(strengthTimingTabLayout.newTab().setText(R.string.week));

        strengthBarChart = (BarChart) view.findViewById(R.id.strengthBarChart);
        nextDataStrengthTest = (ImageButton) view.findViewById(R.id.nextDataStrengthTest);
        nextDataStrengthTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDateToNextWeekStrength();
            }
        });
        prevDataStrengthTest = (ImageButton) view.findViewById(R.id.prevDataStrengthTest);
        prevDataStrengthTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDateToPrevWeekStrength();
            }
        });
        strengthTypeSpinner = (AppCompatSpinner) view.findViewById(R.id.strengthTypeSpinner);
        strengthTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentStrengthTestDataTypePos = i;
                setStrengthTestData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        configurateChartStrength(strengthBarChart);
        getCurrentStartDateAndEndDateOfWeekForStrength();
        updateStrengthTestTypeData();

        //--------------------------------------------------------------------------------------------------------------------------------------

        speedTimingTabLayout.requestFocus();
        Funciones.hideKeyboardFromContext(context,mView);

        return view;
    }

    private void setAutoCompleteSpeedMetersData() {
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(
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
                updateDataForSpeedTest();
            }
        });
    }

    private void getCurrentStartDateAndEndDateOfWeekForSpeed(){
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

//        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//            startDateSpeed = Funciones.operateDate(cal.getTime(),-6);
//            endDateSpeed = Funciones.operateDate(startDateSpeed, 6);
//        }
//        else {
            startDateSpeed = cal.getTime();
            endDateSpeed = Funciones.operateDate(startDateSpeed, 6);
        //}
    }

    private void getCurrentStartDateAndEndDateOfWeekForSaltability(){
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

//        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//            startDateSaltability = Funciones.operateDate(cal.getTime(),-6);
//            endDateSpeedSaltability = Funciones.operateDate(startDateSaltability, 6);
//        }
//        else {
            startDateSaltability = cal.getTime();
            endDateSpeedSaltability = Funciones.operateDate(startDateSaltability, 6);
        //}
    }

    private void getCurrentStartDateAndEndDateOfWeekForStrength(){
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

//        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//            startDateSaltability = Funciones.operateDate(cal.getTime(),-6);
//            endDateSpeedSaltability = Funciones.operateDate(startDateSaltability, 6);
//        }
//        else {
        startDateStrength = cal.getTime();
        endDateStrength = Funciones.operateDate(startDateStrength, 6);
        //}
    }

    private void updateDateToPrevWeekSpeed(){
        startDateSpeed = Funciones.operateDate(startDateSpeed,-7);
        endDateSpeed = Funciones.operateDate(endDateSpeed,-7);
        updateDataForSpeedTest();
    }

    private void updateDateToNextWeekSpeed(){
        startDateSpeed = Funciones.operateDate(startDateSpeed,7);
        endDateSpeed = Funciones.operateDate(endDateSpeed,7);
        updateDataForSpeedTest();
    }

    private void updateDateToPrevWeekSaltability(){
        startDateSaltability = Funciones.operateDate(startDateSaltability,-7);
        endDateSpeedSaltability = Funciones.operateDate(endDateSpeedSaltability,-7);
        updateSaltabilityTestData();
    }

    private void updateDateToNextWeekSaltability(){
        startDateSaltability = Funciones.operateDate(startDateSaltability,7);
        endDateSpeedSaltability = Funciones.operateDate(endDateSpeedSaltability,7);
        updateSaltabilityTestData();
    }

    private void updateDateToPrevWeekStrength(){
        startDateStrength = Funciones.operateDate(startDateStrength,-7);
        endDateStrength = Funciones.operateDate(endDateStrength,-7);
        updateStrengthTestTypeData();
    }

    private void updateDateToNextWeekStrength(){
        startDateStrength = Funciones.operateDate(startDateStrength,7);
        endDateStrength = Funciones.operateDate(endDateStrength,7);
        updateStrengthTestTypeData();
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
                            updateSaltabilityTestData();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {}
                });
    }

    public void updateStrengthTestTypeData() {
        AndroidNetworking.get(GroupSportsApiService.STRENGTH_TEST_TYPE_SESSION_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        strengthTestTypes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                strengthTestTypes.add(StrengthTestType.toStrengthTestType(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        StrengthTypesRepository.getInstance().setStrengthTestTypes(strengthTestTypes);
                        if (isAdded()) {
                            setStrengthTypeSpinnerData();
                            updateStrengthTestData();
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
        String[] saltaTypesArr = saltaStrings.toArray(new String[]{});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,saltaTypesArr
        );
        saltabilityTypeSpinner.setAdapter(adapter);
    }

    private void setStrengthTypeSpinnerData() {
        List<String> saltaStrings = new ArrayList<>();
        for (int i = 0; i < strengthTestTypes.size(); i++) {
            saltaStrings.add(strengthTestTypes.get(i).getDescription());
        }
        String[] saltaTypesArr = saltaStrings.toArray(new String[]{});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_dropdown_item,saltaTypesArr
        );
        strengthTypeSpinner.setAdapter(adapter);
    }

    public void updateSaltabilityTestData() {
        String urlSalto = GroupSportsApiService.SALTABILITY_TEST_BY_ATHLETE_BY_RANGE_DATE(currentAthlete.getId(),Funciones.formatDateForAPI(startDateSaltability),Funciones.formatDateForAPI(endDateSpeedSaltability));
        AndroidNetworking.get(urlSalto)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        clearTest();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JumpTest jumpTest = JumpTest.toJumpTest(response.getJSONObject(i));
                                setJumpTestInJumpTestType(jumpTest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setSaltabilityTestData();
                    }

                    @Override
                    public void onError(ANError anError) {}
                });
    }

    public void updateStrengthTestData() {
        String urlSalto = GroupSportsApiService.STRENGTH_TEST_BY_ATHLETE_BY_RANGE_DATE(
                currentAthlete.getId(),
                Funciones.formatDateForAPI(startDateStrength),
                Funciones.formatDateForAPI(endDateStrength)
        );

        AndroidNetworking.get(urlSalto)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        clearStrengthTest();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                StrengthTest strengthTest = StrengthTest.toStrengthTest(response.getJSONObject(i));
                                setStrengthTestInJumpTestType(strengthTest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setStrengthTestData();
                    }

                    @Override
                    public void onError(ANError anError) {}
                });
    }

    private void setStrengthTestData() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<StrengthTest> strengthTests = strengthTestTypes.get(currentStrengthTestDataTypePos).getStrengthTests();
        for (int i = 0; i < strengthTests.size(); i++) {
            StrengthTest st = strengthTests.get(i);
            yVals1.add(new BarEntry(st.getDayOfYear(), Float.valueOf(st.getMaxRepetitionWeightValue())));
        }

        BarDataSet set1;

        if (strengthBarChart.getData() != null &&
                strengthBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) strengthBarChart.getData().getDataSetByIndex(0);
            set1.setLabel(Funciones.formatDate(startDateStrength) + " - " + Funciones.formatDate(endDateStrength));
            set1.setValues(yVals1);
            strengthBarChart.getData().notifyDataChanged();
            strengthBarChart.notifyDataSetChanged();
            strengthBarChart.invalidate();
        } else {
            set1 = new BarDataSet(yVals1,Funciones.formatDate(startDateStrength) + " - " + Funciones.formatDate(endDateStrength));
            set1.setDrawIcons(false);
            set1.setColor(context.getResources().getColor(R.color.colorPrimaryLight));
            set1.setDrawValues(false);
            set1.setHighLightColor(context.getResources().getColor(R.color.colorAccent));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            strengthBarChart.setData(data);
            strengthBarChart.invalidate();
        }
    }

    private void setSaltabilityTestData() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<JumpTest> jumpTests = saltabilityTestTypes.get(currentSaltabilityTestDataTypePos).getJumpTests();
        for (int i = 0; i < jumpTests.size(); i++) {
            JumpTest st = jumpTests.get(i);
            yVals1.add(new BarEntry(st.getDayOfYear(), Float.valueOf(st.getDistanceResult())));
        }

        BarDataSet set1;

        if (saltabilityBarChart.getData() != null &&
                saltabilityBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) saltabilityBarChart.getData().getDataSetByIndex(0);
            set1.setLabel(Funciones.formatDate(startDateSaltability) + " - " + Funciones.formatDate(endDateSpeedSaltability));
            set1.setValues(yVals1);
            saltabilityBarChart.getData().notifyDataChanged();
            saltabilityBarChart.notifyDataSetChanged();
            saltabilityBarChart.invalidate();
        } else {
            set1 = new BarDataSet(yVals1,Funciones.formatDate(startDateSaltability) + " - " + Funciones.formatDate(endDateSpeedSaltability));
            set1.setDrawIcons(false);
            set1.setColor(context.getResources().getColor(R.color.colorPrimaryLight));
            set1.setDrawValues(false);
            set1.setHighLightColor(context.getResources().getColor(R.color.colorAccent));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            saltabilityBarChart.setData(data);
            saltabilityBarChart.invalidate();
        }
    }

    private void clearTest(){
        for (int i = 0; i < saltabilityTestTypes.size(); i++) {
            saltabilityTestTypes.get(i).clearTests();
        }
    }

    private void clearStrengthTest(){
        for (int i = 0; i < strengthTestTypes.size(); i++) {
            strengthTestTypes.get(i).clearTests();
        }
    }

    private void setJumpTestInJumpTestType(JumpTest jumpTest){
        for (int i = 0; i < saltabilityTestTypes.size(); i++) {
            SaltabilityTestType stt = saltabilityTestTypes.get(i);
            if (stt.getId().equals(jumpTest.getJumpTestTypeId())){
                stt.addSaltabilityTest(jumpTest);
            }
        }
    }

    private void setStrengthTestInJumpTestType(StrengthTest strengthTest){
        for (int i = 0; i < strengthTestTypes.size(); i++) {
            StrengthTestType stt = strengthTestTypes.get(i);
            if (stt.getId().equals(strengthTest.getStrengthTestTypeId())){
                stt.addStrengthTest(strengthTest);
            }
        }
    }

    public void updateDataForSpeedTest(){
        String urlSpeedTest = GroupSportsApiService.SPEED_TEST_BY_ATHLETE_BY_RANGE_DATE(currentAthlete.getId(),Funciones.formatDateForAPI(startDateSpeed),Funciones.formatDateForAPI(endDateSpeed));
        AndroidNetworking.get(urlSpeedTest)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        speedTests.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                SpeedTest speedTest = SpeedTest.toSpeedTest(response.getJSONObject(i));
                                if (currentMetersOfSpeedTest.equals(speedTest.getMeters())) {
                                    speedTests.add(speedTest);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setSpeedTestData();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void setSpeedTestData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < speedTests.size(); i++) {
            SpeedTest st = speedTests.get(i);
            yVals1.add(new BarEntry(st.getDayOfYear(), st.getResultFloat()));
        }

        BarDataSet set1;

        if (speedBarChart.getData() != null &&
                speedBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) speedBarChart.getData().getDataSetByIndex(0);
            set1.setLabel(Funciones.formatDate(startDateSpeed) + " - " + Funciones.formatDate(endDateSpeed));
            set1.setValues(yVals1);
            speedBarChart.getData().notifyDataChanged();
            speedBarChart.notifyDataSetChanged();
            speedBarChart.invalidate();
        } else {
            set1 = new BarDataSet(yVals1,Funciones.formatDate(startDateSpeed) + " - " + Funciones.formatDate(endDateSpeed));
            set1.setDrawIcons(false);
            set1.setColor(context.getResources().getColor(R.color.colorPrimaryLight));
            set1.setDrawValues(false);
            set1.setHighLightColor(context.getResources().getColor(R.color.colorAccent));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            speedBarChart.setData(data);
            speedBarChart.invalidate();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void configurateChartSpeed(BarChart barChart) {
        barChart.setOnChartValueSelectedListener(this);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(300);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f); // intervalos de 1 dia
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new SecondsAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        XYMarkerView mv = new XYMarkerView(context, xAxisFormatter);
        mv.setChartView(barChart); // control de limites constraints
        barChart.setMarker(mv); // coloco el marcador al chart

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
    }

    private void configurateChartSaltability(BarChart barChart) {
        barChart.setOnChartValueSelectedListener(this);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(300);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f); // intervalos de 1 dia
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MetersAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        XYMarkerSaltabilityView mv = new XYMarkerSaltabilityView(context, xAxisFormatter);
        mv.setChartView(barChart); // control de limites constraints
        barChart.setMarker(mv); // coloco el marcador al chart

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
    }

    private void configurateChartStrength(BarChart barChart) {
        barChart.setOnChartValueSelectedListener(this);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(false);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(300);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f); // intervalos de 1 dia
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new KilosAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        XYMarkerStrengthView mv = new XYMarkerStrengthView(context, xAxisFormatter);
        mv.setChartView(barChart); // control de limites constraints
        barChart.setMarker(mv); // coloco el marcador al chart

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
    }

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }
}






























