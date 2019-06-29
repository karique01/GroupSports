package pe.edu.karique.groupsports.models;

import java.util.Date;
import java.util.regex.Pattern;

public class Pisada {
    public static int PISADA_DERECHA = 20;
    public static int PISADA_IZQUIERDA = 21;

    private int ladoPisada;
    private double fuerza;
    private double tiempoRespectoAPisadaPrevia;
    private Date horaRecojo;
    public boolean pisadaEnTramo = false;

    public static Pisada fromPisadaString(String pisadaString){
        //divido los datos que estan concatenados por el caracter +
        String[] datos = pisadaString.split(Pattern.quote("+"));

        Pisada pisada = new Pisada();
        pisada.ladoPisada = Integer.parseInt(datos[0]);
        pisada.fuerza = Double.parseDouble(datos[1]);
        pisada.tiempoRespectoAPisadaPrevia = Double.parseDouble(datos[2]);
        pisada.pisadaEnTramo = true;

        return pisada;
    }

    public Pisada() {
    }

    public Pisada(int ladoPisada, double fuerza, double tiempoRespectoAPisadaPrevia, Date horaRecojo) {
        this.ladoPisada = ladoPisada;
        this.fuerza = fuerza;
        this.tiempoRespectoAPisadaPrevia = tiempoRespectoAPisadaPrevia;
        this.horaRecojo = horaRecojo;
    }

//    public String toString(){
//        return ladoPisada + "+" + fuerza + "+" + tiempoRespectoAPisadaPrevia;
//    }

    public String toStringLeft(){
        return 21 + "+" + fuerza + "+" + tiempoRespectoAPisadaPrevia;
    }

    public String toStringRight(){
        return 20 + "+" + fuerza + "+" + tiempoRespectoAPisadaPrevia;
    }

    public int getLadoPisada() {
        return ladoPisada;
    }

    public void setLadoPisada(int ladoPisada) {
        this.ladoPisada = ladoPisada;
    }

    public double getFuerza() {
        return fuerza;
    }

    public void setFuerza(double fuerza) {
        this.fuerza = fuerza;
    }

    public double getTiempoRespectoAPisadaPrevia() {
        return tiempoRespectoAPisadaPrevia;
    }

    public void setTiempoRespectoAPisadaPrevia(double tiempoRespectoAPisadaPrevia) {
        this.tiempoRespectoAPisadaPrevia = tiempoRespectoAPisadaPrevia;
    }

    public Date getHoraRecojo() {
        return horaRecojo;
    }

    public void setHoraRecojo(Date horaRecojo) {
        this.horaRecojo = horaRecojo;
    }
}
