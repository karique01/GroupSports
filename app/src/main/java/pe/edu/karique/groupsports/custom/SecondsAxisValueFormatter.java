package pe.edu.karique.groupsports.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class SecondsAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;

    public SecondsAxisValueFormatter() {
        mFormat = new DecimalFormat("#####0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " s";
    }
}
