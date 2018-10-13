package pe.edu.upc.groupsports.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class KilosAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;

    public KilosAxisValueFormatter() {
        mFormat = new DecimalFormat("#####0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " kg";
    }
}
