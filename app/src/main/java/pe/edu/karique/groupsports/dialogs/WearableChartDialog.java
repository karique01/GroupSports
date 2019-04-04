package pe.edu.karique.groupsports.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import pe.edu.karique.groupsports.R;

/**
 * Created by karique on 2/06/2018.
 */

public class WearableChartDialog extends AlertDialog {
    ImageButton arrowBackImageView;
    LineChart lineChart;
    TextView tittleTextView;
    Context context;

    String tittle, tittleValue, legendName;
    List<Integer> yValues;
    List<Float> yValuesDouble;
    boolean isFloat = false;

    public WearableChartDialog(Context context, String tittle, String tittleValue, String legendName, List<Integer> yValues) {
        super(context);
        this.tittle = tittle;
        this.tittleValue = tittleValue;
        this.legendName = legendName;
        this.yValues = yValues;
        init();
    }

    public WearableChartDialog(Context context, String tittle, String tittleValue, String legendName, List<Float> yValuesDouble, boolean isFloat) {
        super(context);
        this.tittle = tittle;
        this.tittleValue = tittleValue;
        this.legendName = legendName;
        this.yValuesDouble = yValuesDouble;
        this.isFloat = isFloat;
        init();
    }

    public WearableChartDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public WearableChartDialog(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_wearable_chart, null);

        context = view.getContext();
        lineChart = view.findViewById(R.id.barChart);
        arrowBackImageView = view.findViewById(R.id.arrowBackImageView);
        tittleTextView = view.findViewById(R.id.tittleTextView);
        tittleTextView.setText(tittle + ": "+ tittleValue);
        arrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if (isFloat) { cargarGraficoFloat(); }
        else { cargarGrafico(); }

        setView(view);
    }


    public void cargarGrafico(){
        ArrayList<Entry> lineEntries = new ArrayList<>();

        lineEntries = new ArrayList<Entry>();
        for (int i = 0; i < yValues.size(); i++) {
            lineEntries.add(new Entry(i,yValues.get(i)));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, legendName);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.RED);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);

        LineData lineData = new LineData(lineDataSet);

        lineChart.getDescription().setTextSize(12);
        lineChart.getDescription().setText("");
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.setData(lineData);


        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelRotationAngle(-45);
        xAxis.setDrawGridLines(true);

        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    public void cargarGraficoFloat(){
        ArrayList<Entry> lineEntries = new ArrayList<>();

        lineEntries = new ArrayList<Entry>();
        for (int i = 0; i < yValuesDouble.size(); i++) {
            lineEntries.add(new Entry(i,yValuesDouble.get(i)));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, legendName);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.RED);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);

        LineData lineData = new LineData(lineDataSet);

        lineChart.getDescription().setTextSize(12);
        lineChart.getDescription().setText("");
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.setData(lineData);


        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelRotationAngle(-45);
        xAxis.setDrawGridLines(true);

        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }
}
