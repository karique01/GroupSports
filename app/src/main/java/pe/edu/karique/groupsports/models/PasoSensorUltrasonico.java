package pe.edu.karique.groupsports.models;

import java.util.Date;

public class PasoSensorUltrasonico {
    public static int SENSOR_PARTIDA = 0;
    public static int SENSOR_LLEGADA = 0;

    private int tipoSensor;
    private Date horaRecojo;

    public PasoSensorUltrasonico() {
    }

    public PasoSensorUltrasonico(int tipoSensor, Date horaRecojo) {
        this.tipoSensor = tipoSensor;
        this.horaRecojo = horaRecojo;
    }

    public int getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(int tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public Date getHoraRecojo() {
        return horaRecojo;
    }

    public void setHoraRecojo(Date horaRecojo) {
        this.horaRecojo = horaRecojo;
    }
}
