package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.karique.groupsports.util.Funciones;

public class SpeedTestAdvanced {
    private String id;
    private String coachId;
    private String athleteId;
    private String tiempoTotal;
    private String pisadasIzquierdas;
    private String pisadasDerechas;
    private String cantidadZancadas;
    private String tiempoMedioEntreZancadas;
    private String velocidadPromedio;
    private String pisadasIzquierdasConcatenadas;
    private String pisadasDerechasConcatenadas;
    private String meters;
    private Date date;

    private List<Pisada> pisadasIzquierdasDTO = new ArrayList<>();
    private List<Pisada> pisadasDerechasDTO = new ArrayList<>();

    public static SpeedTestAdvanced toSpeedTestAdvanced(JSONObject jsonObject){
        SpeedTestAdvanced speedTestAdvanced = new SpeedTestAdvanced();

        try {
            speedTestAdvanced.setId(jsonObject.getString("id"));
            speedTestAdvanced.setCoachId(jsonObject.getString("coachId"));
            speedTestAdvanced.setAthleteId(jsonObject.getString("athleteId"));
            speedTestAdvanced.setTiempoTotal(jsonObject.getString("tiempoTotal"));
            speedTestAdvanced.setPisadasIzquierdas(jsonObject.getString("pisadasIzquierdas"));
            speedTestAdvanced.setPisadasDerechas(jsonObject.getString("pisadasDerechas"));
            speedTestAdvanced.setCantidadZancadas(jsonObject.getString("cantidadZancadas"));
            speedTestAdvanced.setTiempoMedioEntreZancadas(jsonObject.getString("tiempoMedioEntreZancadas"));
            speedTestAdvanced.setVelocidadPromedio(jsonObject.getString("velocidadPromedio"));
            speedTestAdvanced.setPisadasIzquierdasConcatenadas(jsonObject.getString("pisadasIzquierdasConcatenadas"));
            speedTestAdvanced.setPisadasDerechasConcatenadas(jsonObject.getString("pisadasDerechasConcatenadas"));
            speedTestAdvanced.setMeters(jsonObject.getString("meters"));
            speedTestAdvanced.setDate(Funciones.getDateFromString(jsonObject.getString("date")));

            speedTestAdvanced.pisadasIzquierdasDTO.clear();
            String[] pisadasIzquierdasSeparadas = speedTestAdvanced.pisadasIzquierdasConcatenadas.split("-");
            for (int i = 0; i < pisadasIzquierdasSeparadas.length; i++) {
                String pisadaString = pisadasIzquierdasSeparadas[i];
                Pisada pisada = Pisada.fromPisadaString(pisadaString);
                speedTestAdvanced.pisadasIzquierdasDTO.add(pisada);
            }

            speedTestAdvanced.pisadasDerechasDTO.clear();
            String[] pisadasDerechasSeparadas = speedTestAdvanced.pisadasDerechasConcatenadas.split("-");
            for (int i = 0; i < pisadasDerechasSeparadas.length; i++) {
                String pisadaString = pisadasDerechasSeparadas[i];
                Pisada pisada = Pisada.fromPisadaString(pisadaString);
                speedTestAdvanced.pisadasDerechasDTO.add(pisada);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return speedTestAdvanced;
    }

    public SpeedTestAdvanced() {
    }

    public SpeedTestAdvanced(String id, String coachId, String athleteId, String tiempoTotal, String pisadasIzquierdas, String pisadasDerechas, String cantidadZancadas, String tiempoMedioEntreZancadas, String velocidadPromedio, String pisadasIzquierdasConcatenadas, String pisadasDerechasConcatenadas, String meters, Date date) {
        this.id = id;
        this.coachId = coachId;
        this.athleteId = athleteId;
        this.tiempoTotal = tiempoTotal;
        this.pisadasIzquierdas = pisadasIzquierdas;
        this.pisadasDerechas = pisadasDerechas;
        this.cantidadZancadas = cantidadZancadas;
        this.tiempoMedioEntreZancadas = tiempoMedioEntreZancadas;
        this.velocidadPromedio = velocidadPromedio;
        this.pisadasIzquierdasConcatenadas = pisadasIzquierdasConcatenadas;
        this.pisadasDerechasConcatenadas = pisadasDerechasConcatenadas;
        this.meters = meters;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(String tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public String getPisadasIzquierdas() {
        return pisadasIzquierdas;
    }

    public void setPisadasIzquierdas(String pisadasIzquierdas) {
        this.pisadasIzquierdas = pisadasIzquierdas;
    }

    public String getPisadasDerechas() {
        return pisadasDerechas;
    }

    public void setPisadasDerechas(String pisadasDerechas) {
        this.pisadasDerechas = pisadasDerechas;
    }

    public String getCantidadZancadas() {
        return cantidadZancadas;
    }

    public void setCantidadZancadas(String cantidadZancadas) {
        this.cantidadZancadas = cantidadZancadas;
    }

    public String getTiempoMedioEntreZancadas() {
        return tiempoMedioEntreZancadas;
    }

    public void setTiempoMedioEntreZancadas(String tiempoMedioEntreZancadas) {
        this.tiempoMedioEntreZancadas = tiempoMedioEntreZancadas;
    }

    public String getVelocidadPromedio() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(Float.valueOf(velocidadPromedio));
    }

    public void setVelocidadPromedio(String velocidadPromedio) {
        this.velocidadPromedio = velocidadPromedio;
    }

    public String getPisadasIzquierdasConcatenadas() {
        return pisadasIzquierdasConcatenadas;
    }

    public void setPisadasIzquierdasConcatenadas(String pisadasIzquierdasConcatenadas) {
        this.pisadasIzquierdasConcatenadas = pisadasIzquierdasConcatenadas;
    }

    public String getPisadasDerechasConcatenadas() {
        return pisadasDerechasConcatenadas;
    }

    public void setPisadasDerechasConcatenadas(String pisadasDerechasConcatenadas) {
        this.pisadasDerechasConcatenadas = pisadasDerechasConcatenadas;
    }

    public String getMeters() {
        return meters;
    }

    public void setMeters(String meters) {
        this.meters = meters;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
