package pe.edu.karique.groupsports.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;
import pe.edu.karique.groupsports.adapters.PisadasAdpater;
import pe.edu.karique.groupsports.models.Athlete;
import pe.edu.karique.groupsports.models.Pisada;
import pe.edu.karique.groupsports.models.TestAutomatizadoVelocidad;
import pe.edu.karique.groupsports.network.GroupSportsApiService;
import pe.edu.karique.groupsports.util.Funciones;

public class SpeedTestAdvancedDetailsActivity extends AppCompatActivity {

    TextView tiempoTotalValueTextView;
    TextView pisadasIzquierdasValueTextView;
    TextView pisadasDerechasValueTextView;
    TextView cantidadZancadasValueTextView;
    TextView tiempoPromedioValueTextView;
    TextView velocidadPromedioValueTextView;
    public static TestAutomatizadoVelocidad testAutomatizado;

    Context mContext;
    RecyclerView pieIzquierdoPisadasRecyclerView;
    PisadasAdpater pieIzquierdoPisadasAdpater;
    LinearLayoutManager pieIzquierdoPisadasLayoutManager;

    RecyclerView pieDerechoPisadasRecyclerView;
    PisadasAdpater pieDerechoPisadasAdpater;
    LinearLayoutManager pieDerechoPisadasLayoutManager;

    LineChart barChartDerecho;
    LineChart barChartIzquierdo;

    CardView finalizarCardView;

    private SessionManager session;
    private Athlete currentAthlete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_test_advanced_details);

        mContext = this;
        session = new SessionManager(mContext);
        currentAthlete = Athlete.from(getIntent().getExtras());

        tiempoTotalValueTextView = findViewById(R.id.tiempoTotalValueTextView);
        pisadasIzquierdasValueTextView = findViewById(R.id.pisadasIzquierdasValueTextView);
        pisadasDerechasValueTextView = findViewById(R.id.pisadasDerechasValueTextView);
        cantidadZancadasValueTextView = findViewById(R.id.cantidadZancadasValueTextView);
        tiempoPromedioValueTextView = findViewById(R.id.tiempoPromedioValueTextView);
        velocidadPromedioValueTextView = findViewById(R.id.velocidadPromedioValueTextView);

        finalizarCardView = findViewById(R.id.finalizarCardView);
        finalizarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSpeedTestAdvanced();
            }
        });

        barChartDerecho = findViewById(R.id.barChartDerecho);
        barChartIzquierdo = findViewById(R.id.barChartIzquierdo);

        tiempoTotalValueTextView.setText(testAutomatizado.getTiempoTestVelocidadToString());
        pisadasIzquierdasValueTextView.setText(String.valueOf(testAutomatizado.getPisadasIzquierdasValidas().size()));
        pisadasDerechasValueTextView.setText(String.valueOf(testAutomatizado.getPisadasDerechasValidas().size()));
        cantidadZancadasValueTextView.setText(String.valueOf(testAutomatizado.getCantidadZancadas()));
        tiempoPromedioValueTextView.setText(String.valueOf(testAutomatizado.getTiempoPromedioEntreZancadas()));
        velocidadPromedioValueTextView.setText(String.valueOf(testAutomatizado.getVelocidadPromedio2DecimalsString()));

        pieIzquierdoPisadasRecyclerView = (RecyclerView) findViewById(R.id.pieIzquierdoPisadasRecyclerView);
        pieIzquierdoPisadasAdpater = new PisadasAdpater(testAutomatizado.getPisadasIzquierdasValidas(), new PisadasAdpater.ContextProvider() {
            @Override
            public Context getContext() {
                return mContext;
            }
        });
        pieIzquierdoPisadasLayoutManager = new LinearLayoutManager(mContext);
        pieIzquierdoPisadasRecyclerView.setAdapter(pieIzquierdoPisadasAdpater);
        pieIzquierdoPisadasRecyclerView.setLayoutManager(pieIzquierdoPisadasLayoutManager);
        pieIzquierdoPisadasAdpater.notifyDataSetChanged();

        pieDerechoPisadasRecyclerView = (RecyclerView) findViewById(R.id.pieDerechoPisadasRecyclerView);
        pieDerechoPisadasAdpater = new PisadasAdpater(testAutomatizado.getPisadasDerechasValidas(), new PisadasAdpater.ContextProvider() {
            @Override
            public Context getContext() {
                return mContext;
            }
        });
        pieDerechoPisadasLayoutManager = new LinearLayoutManager(mContext);
        pieDerechoPisadasRecyclerView.setAdapter(pieDerechoPisadasAdpater);
        pieDerechoPisadasRecyclerView.setLayoutManager(pieDerechoPisadasLayoutManager);
        pieDerechoPisadasAdpater.notifyDataSetChanged();

        cargarGrafico();
        hideActionBar();
    }

    private void postSpeedTestAdvanced(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("coachId", session.getuserLoggedTypeId());
            jsonObject.put("athleteId", currentAthlete.getId());
            jsonObject.put("tiempoTotal", testAutomatizado.getTiempoTestVelocidadToString());
            jsonObject.put("pisadasIzquierdas", testAutomatizado.getPisadasIzquierdasValidas().size());
            jsonObject.put("pisadasDerechas", testAutomatizado.getPisadasDerechasValidas().size());
            jsonObject.put("cantidadZancadas", testAutomatizado.getCantidadZancadas());
            jsonObject.put("tiempoMedioEntreZancadas", testAutomatizado.getTiempoPromedioEntreZancadas());
            jsonObject.put("velocidadPromedio", testAutomatizado.getVelocidadPromedio());
            jsonObject.put("pisadasIzquierdasConcatenadas", testAutomatizado.pisadasIzquierdasToString());
            jsonObject.put("pisadasDerechasConcatenadas", testAutomatizado.pisadasDerechasToString());
            jsonObject.put("meters", testAutomatizado.tipoTest);
            jsonObject.put("date", Funciones.formatDateForAPI(Funciones.getCurrentDate()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.SPEED_TEST_ADVANCED_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addJSONObjectBody(jsonObject)
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                testAutomatizado.limpiar();
                                finish();
                            }
                            else {
                                Toast.makeText(mContext, "Error en el sistema", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(mContext, "Hubo un error al agregar el test (Conexión Wifi)", Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void cargarGrafico(){
        //pisadas derechas
        ArrayList<Entry> lineEntriesDerechos = new ArrayList<>();
        lineEntriesDerechos = new ArrayList<Entry>();
        for (int i = 0; i < testAutomatizado.getPisadasDerechas().size(); i++) {
            Pisada p = testAutomatizado.getPisadasDerechas().get(i);
            lineEntriesDerechos.add(new Entry(i,(float)p.getFuerza()));
        }

        LineDataSet lineDataSetDerecho = new LineDataSet(lineEntriesDerechos, "Pisadas derechas");
        lineDataSetDerecho.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSetDerecho.setHighlightEnabled(true);
        lineDataSetDerecho.setLineWidth(2);
        lineDataSetDerecho.setColor(Color.RED);
        lineDataSetDerecho.setCircleColor(Color.RED);
        lineDataSetDerecho.setCircleRadius(6);
        lineDataSetDerecho.setCircleHoleRadius(3);
        lineDataSetDerecho.setDrawHighlightIndicators(true);
        lineDataSetDerecho.setHighLightColor(Color.RED);
        lineDataSetDerecho.setValueTextSize(12);

        LineData lineDataDerecho = new LineData(lineDataSetDerecho);

        barChartDerecho.getDescription().setTextSize(12);
        barChartDerecho.getDescription().setText("");
        barChartDerecho.setDrawMarkers(true);
        barChartDerecho.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        barChartDerecho.animateY(100);
        barChartDerecho.getXAxis().setGranularityEnabled(true);
        barChartDerecho.getXAxis().setGranularity(1.0f);
        barChartDerecho.getXAxis().setLabelCount(lineDataSetDerecho.getEntryCount());
        barChartDerecho.getXAxis().setDrawLabels(false);
        barChartDerecho.setData(lineDataDerecho);

        if (lineEntriesDerechos.isEmpty()) barChartDerecho.clear();
        else barChartDerecho.setData(lineDataDerecho);

        XAxis xAxisDerecho = barChartDerecho.getXAxis();
        xAxisDerecho.setLabelRotationAngle(-45);
        xAxisDerecho.setDrawGridLines(true);

        barChartDerecho.setTouchEnabled(true);
        barChartDerecho.setDragEnabled(true);
        barChartDerecho.setScaleEnabled(true);
        barChartDerecho.notifyDataSetChanged();
        barChartDerecho.invalidate();

        //pisadas izquierdas
        ArrayList<Entry> lineEntriesIzquierdos = new ArrayList<>();
        lineEntriesIzquierdos = new ArrayList<Entry>();
        for (int i = 0; i < testAutomatizado.getPisadasIzquierdas().size(); i++) {
            Pisada p = testAutomatizado.getPisadasIzquierdas().get(i);
            lineEntriesIzquierdos.add(new Entry(i,(float)p.getFuerza()));
        }

        LineDataSet lineDataSetIzquierda = new LineDataSet(lineEntriesIzquierdos, "Pisadas derechas");
        lineDataSetIzquierda.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSetIzquierda.setHighlightEnabled(true);
        lineDataSetIzquierda.setLineWidth(2);
        lineDataSetIzquierda.setColor(Color.RED);
        lineDataSetIzquierda.setCircleColor(Color.RED);
        lineDataSetIzquierda.setCircleRadius(6);
        lineDataSetIzquierda.setCircleHoleRadius(3);
        lineDataSetIzquierda.setDrawHighlightIndicators(true);
        lineDataSetIzquierda.setHighLightColor(Color.RED);
        lineDataSetIzquierda.setValueTextSize(12);

        LineData lineDataIzquierda = new LineData(lineDataSetIzquierda);

        barChartIzquierdo.getDescription().setTextSize(12);
        barChartIzquierdo.getDescription().setText("");
        barChartIzquierdo.setDrawMarkers(true);
        barChartIzquierdo.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        barChartIzquierdo.animateY(100);
        barChartIzquierdo.getXAxis().setGranularityEnabled(true);
        barChartIzquierdo.getXAxis().setGranularity(1.0f);
        barChartIzquierdo.getXAxis().setLabelCount(lineDataSetIzquierda.getEntryCount());
        barChartIzquierdo.getXAxis().setDrawLabels(false);
        barChartIzquierdo.setData(lineDataIzquierda);

        if (lineEntriesIzquierdos.isEmpty()) barChartIzquierdo.clear();
        else barChartIzquierdo.setData(lineDataIzquierda);

        XAxis xAxisIzquierdo = barChartIzquierdo.getXAxis();
        xAxisIzquierdo.setLabelRotationAngle(-45);
        xAxisIzquierdo.setDrawGridLines(true);

        barChartIzquierdo.setTouchEnabled(true);
        barChartIzquierdo.setDragEnabled(true);
        barChartIzquierdo.setScaleEnabled(true);
        barChartIzquierdo.notifyDataSetChanged();
        barChartIzquierdo.invalidate();
    }
}
