package pe.edu.karique.groupsports.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.adapters.PisadasAdpater;
import pe.edu.karique.groupsports.models.Athlete;
import pe.edu.karique.groupsports.models.Pisada;
import pe.edu.karique.groupsports.models.TestAutomatizadoVelocidad;

public class AddSpeedTestAdvancedActivity extends AppCompatActivity {
    static  int REQUEST_FOR_ACTIVITY_CODE_ADD_SPEED_TEST_ADVANCED = 9944;

    static int LISTADO_ARRIBA_ABAJO = 1;
    static int LISTADO_ABAJO_ARRIBA = 2;

    Context mContext;

    String topico = "nodemcu/photocell";
    String serverUrl = "tcp://m16.cloudmqtt.com:10207";
    String username = "vduvhwog";
    String password = "ZGTLsOl_Houw";

    //String topico = "node/deposport";
    //String serverUrl = "tcp://m11.cloudmqtt.com:14246";
    //String username = "iqbcgwmx";
    //String password = "tBB5hdFtLa6m";

    public static TestAutomatizadoVelocidad testAutomatizado = new TestAutomatizadoVelocidad();

    RecyclerView pieIzquierdoPisadasRecyclerView;
    PisadasAdpater pieIzquierdoPisadasAdpater;
    LinearLayoutManager pieIzquierdoPisadasLayoutManager;

    RecyclerView pieDerechoPisadasRecyclerView;
    PisadasAdpater pieDerechoPisadasAdpater;
    LinearLayoutManager pieDerechoPisadasLayoutManager;

    LineChart barChartDerecho;
    LineChart barChartIzquierdo;
    AppCompatImageButton conectadoImageButton;
    AppCompatImageButton limpiarImageButton;

    boolean currentlyConected = false;

    CardView pieIzquierdoCarview;
    CardView pieDerechoCardView;
    TextView pieIzquierdoTextView;
    TextView pieDerechoTextView;

    int tipoListado = LISTADO_ARRIBA_ABAJO;

    //tramo sprint
    AppCompatImageView salidaAppCompatImageView;
    TextView salidaTextView;
    AppCompatImageView llegadaAppCompatImageView;
    TextView llegadaTextView;
    ProgressBar esperrarLlegadaProgressBar;

    AppCompatImageButton visibilityImageButton;
    boolean chartVisible;

    private Athlete currentAthlete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_speed_test_advanced);

        mContext = this;
        int distance = getIntent().getIntExtra("distance", 200);
        testAutomatizado.limpiar();
        testAutomatizado.tipoTest = distance;

        currentAthlete = Athlete.from(getIntent().getExtras());

        barChartDerecho = findViewById(R.id.barChartDerecho);
        barChartIzquierdo = findViewById(R.id.barChartIzquierdo);
        conectadoImageButton = findViewById(R.id.conectadoImageButton);
        limpiarImageButton = findViewById(R.id.limpiarImageButton);
        pieIzquierdoCarview = findViewById(R.id.pieIzquierdoCarview);
        pieDerechoCardView = findViewById(R.id.pieDerechoCardView);
        pieIzquierdoTextView = findViewById(R.id.pieIzquierdoTextView);
        pieDerechoTextView = findViewById(R.id.pieDerechoTextView);
        visibilityImageButton = findViewById(R.id.visibilityImageButton);
        chartVisible = true;

        hideActionBar();
        connect();
        cargarGrafico();

        pieIzquierdoPisadasRecyclerView = (RecyclerView) findViewById(R.id.pieIzquierdoPisadasRecyclerView);
        pieIzquierdoPisadasAdpater = new PisadasAdpater(testAutomatizado.getPisadasIzquierdas(), new PisadasAdpater.ContextProvider() {
            @Override
            public Context getContext() {
                return mContext;
            }
        });
        pieIzquierdoPisadasLayoutManager = new LinearLayoutManager(mContext);
        pieIzquierdoPisadasRecyclerView.setAdapter(pieIzquierdoPisadasAdpater);
        pieIzquierdoPisadasRecyclerView.setLayoutManager(pieIzquierdoPisadasLayoutManager);

        pieDerechoPisadasRecyclerView = (RecyclerView) findViewById(R.id.pieDerechoPisadasRecyclerView);
        pieDerechoPisadasAdpater = new PisadasAdpater(testAutomatizado.getPisadasDerechas(), new PisadasAdpater.ContextProvider() {
            @Override
            public Context getContext() {
                return mContext;
            }
        });
        pieDerechoPisadasLayoutManager = new LinearLayoutManager(mContext);
        pieDerechoPisadasRecyclerView.setAdapter(pieDerechoPisadasAdpater);
        pieDerechoPisadasRecyclerView.setLayoutManager(pieDerechoPisadasLayoutManager);

        conectadoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentlyConected){
                    Toast.makeText(mContext, "Ya esta conectado en tiempo real",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(mContext, "Conectando...",Toast.LENGTH_SHORT).show();
                    connect();
                }
            }
        });
        limpiarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testAutomatizado.limpiar();
                pieIzquierdoPisadasAdpater.notifyDataSetChanged();
                pieDerechoPisadasAdpater.notifyDataSetChanged();
                setTramoSprintVisibility(View.GONE);
                cargarGrafico();
            }
        });
        pieIzquierdoCarview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarOrdenListado();
            }
        });
        pieDerechoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarOrdenListado();
            }
        });

        //tramo sprint controles
        salidaAppCompatImageView = findViewById(R.id.salidaAppCompatImageView);
        salidaTextView = findViewById(R.id.salidaTextView);
        llegadaAppCompatImageView = findViewById(R.id.llegadaAppCompatImageView);
        llegadaTextView = findViewById(R.id.llegadaTextView);
        esperrarLlegadaProgressBar = findViewById(R.id.esperrarLlegadaProgressBar);
        setTramoSprintVisibility(View.GONE);

        //para detectar paso por la salida y la llegada
        testAutomatizado.setOnSalidaPassedListener(new TestAutomatizadoVelocidad.OnSalidaPassedListener() {
            @Override
            public void OnSalidaPassed() {
                setTramoSalidaVisible(View.VISIBLE);
            }
        });
        testAutomatizado.setOnLlegadaPassedListener(new TestAutomatizadoVelocidad.OnLlegadaPassedListener() {
            @Override
            public void OnLlegadaPassed() {
                setTramoLlegadaVisible();
                startSpeedTestDetails();
            }
        });

        visibilityImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chartVisible) {
                    chartVisible = false;
                    visibilityImageButton.setImageResource(R.drawable.ic_visibility);
                    barChartDerecho.setVisibility(View.GONE);
                    barChartIzquierdo.setVisibility(View.GONE);
                }
                else {
                    chartVisible = true;
                    visibilityImageButton.setImageResource(R.drawable.ic_visibility_off_off);
                    barChartDerecho.setVisibility(View.VISIBLE);
                    barChartIzquierdo.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void startSpeedTestDetails(){
        SpeedTestAdvancedDetailsActivity.testAutomatizado = testAutomatizado;
        Intent intent = new Intent(mContext, SpeedTestAdvancedDetailsActivity.class);
        intent.putExtras(currentAthlete.toBundle());
        mContext.startActivity(intent);
        finish();
    }

    public void connect() {
        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client = new MqttAndroidClient(mContext,serverUrl,clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
        options.setCleanSession(false);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("file", "onSuccess");
                    subscribe(client, topico);
                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable cause) {
                            Toast.makeText(mContext, "se perdió conexión",Toast.LENGTH_LONG).show();
                            currentlyConected = false;
                            changeConectedButtonState();
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) throws Exception {
                            testAutomatizado.agregarDato(message.toString());
                            pieIzquierdoPisadasAdpater.notifyDataSetChanged();
                            pieDerechoPisadasAdpater.notifyDataSetChanged();

                            cargarGrafico();

                            pieDerechoPisadasRecyclerView.smoothScrollToPosition(testAutomatizado.getPisadasDerechas().size() - 1);
                            pieIzquierdoPisadasRecyclerView.smoothScrollToPosition(testAutomatizado.getPisadasIzquierdas().size() - 1);
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {

                        }
                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(AddSpeedTestAdvancedActivity.this, "Error colocando callback",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            Toast.makeText(mContext, "Error al conectar con token",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void subscribe(MqttAndroidClient client , String topic) {
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    //Toast.makeText(mContext, "Conectado",Toast.LENGTH_SHORT).show();
                    currentlyConected = true;
                    changeConectedButtonState();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Toast.makeText(mContext, "Error al conectarse",Toast.LENGTH_LONG).show();
                    currentlyConected = false;
                    changeConectedButtonState();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
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

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void changeConectedButtonState(){
        if (currentlyConected){
            conectadoImageButton.setImageResource(R.drawable.ic_cloud_queue);
        }
        else {
            conectadoImageButton.setImageResource(R.drawable.ic_cloud_off_black_24dp);
        }
    }

    private void cambiarOrdenListado(){
        if (tipoListado == LISTADO_ARRIBA_ABAJO){
            tipoListado = LISTADO_ABAJO_ARRIBA;
            pieDerechoTextView.setText("Pie derecho ↑");
            pieIzquierdoTextView.setText("Pie izquierdo ↑");

            pieDerechoPisadasLayoutManager.setStackFromEnd(true);
            pieDerechoPisadasLayoutManager.setReverseLayout(true);
            pieIzquierdoPisadasLayoutManager.setStackFromEnd(true);
            pieIzquierdoPisadasLayoutManager.setReverseLayout(true);
        }
        else if (tipoListado == LISTADO_ABAJO_ARRIBA){
            tipoListado = LISTADO_ARRIBA_ABAJO;
            pieDerechoTextView.setText("Pie derecho ↓");
            pieIzquierdoTextView.setText("Pie izquierdo ↓");

            pieDerechoPisadasLayoutManager.setStackFromEnd(false);
            pieDerechoPisadasLayoutManager.setReverseLayout(false);
            pieIzquierdoPisadasLayoutManager.setStackFromEnd(false);
            pieIzquierdoPisadasLayoutManager.setReverseLayout(false);
        }
    }

    private void setTramoSprintVisibility(int visibility){
        salidaAppCompatImageView.setVisibility(visibility);
        salidaTextView.setVisibility(visibility);
        llegadaAppCompatImageView.setVisibility(visibility);
        llegadaTextView.setVisibility(visibility);
        esperrarLlegadaProgressBar.setVisibility(visibility);
    }

    private void setTramoSalidaVisible(int visibility){
        salidaAppCompatImageView.setVisibility(visibility);
        salidaTextView.setVisibility(visibility);
        esperrarLlegadaProgressBar.setVisibility(visibility);
        llegadaAppCompatImageView.setVisibility(visibility);
        llegadaTextView.setVisibility(visibility);
        esperrarLlegadaProgressBar.setIndeterminate(true);
    }

    private void setTramoLlegadaVisible(){
        esperrarLlegadaProgressBar.setIndeterminate(false);
        esperrarLlegadaProgressBar.setMax(100);
        esperrarLlegadaProgressBar.setProgress(100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
