package pe.edu.karique.groupsports.models;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import pe.edu.karique.groupsports.util.Funciones;

public class TestAutomatizadoVelocidad {
    public static int TIPO_TEST_200M = 200;
    public static int TIPO_TEST_100M = 100;
    public static int TIPO_TEST_60M = 60;
    public static int TIPO_TEST_50M = 50;
    public static int TIPO_TEST_40M = 40;
    public static int TIPO_TEST_30M = 30;
    public static int TIPO_TEST_20M = 20;
    public static int TIPO_TEST_10M = 10;

    private List<PasoSensorUltrasonico> pasoSensorUltrasonicoInicio = new ArrayList<>();
    private List<PasoSensorUltrasonico> pasoSensorUltrasonicoFin = new ArrayList<>();
    private List<Pisada> pisadasIzquierdas = new ArrayList<>();
    private List<Pisada> pisadasDerechas = new ArrayList<>();
    public int tipoTest = TIPO_TEST_100M;

    public String pisadasIzquierdasToString(){
        StringBuilder _sb = new StringBuilder();
        for (int i = 0; i < pisadasIzquierdas.size(); i++) {
            _sb.append(pisadasIzquierdas.get(i).toStringLeft()).append(i == pisadasIzquierdas.size() - 1 ? "" : "-");
        }
        return _sb.toString();
    }

    public String pisadasDerechasToString(){
        StringBuilder _sb = new StringBuilder();
        for (int i = 0; i < pisadasDerechas.size(); i++) {
            _sb.append(pisadasDerechas.get(i).toStringRight()).append(i == pisadasDerechas.size() - 1 ? "" : "-");
        }
        return _sb.toString();
    }

    public List<PasoSensorUltrasonico> getPasoSensorUltrasonicoInicio() {
        return pasoSensorUltrasonicoInicio;
    }

    public void setPasoSensorUltrasonicoInicio(List<PasoSensorUltrasonico> pasoSensorUltrasonicoInicio) {
        this.pasoSensorUltrasonicoInicio = pasoSensorUltrasonicoInicio;
    }

    public List<PasoSensorUltrasonico> getPasoSensorUltrasonicoFin() {
        return pasoSensorUltrasonicoFin;
    }

    public void setPasoSensorUltrasonicoFin(List<PasoSensorUltrasonico> pasoSensorUltrasonicoFin) {
        this.pasoSensorUltrasonicoFin = pasoSensorUltrasonicoFin;
    }

    public List<Pisada> getPisadasIzquierdas() {
        return pisadasIzquierdas;
    }

    public void setPisadasIzquierdas(List<Pisada> pisadasIzquierdas) {
        this.pisadasIzquierdas = pisadasIzquierdas;
    }

    public List<Pisada> getPisadasDerechas() {
        return pisadasDerechas;
    }

    public void setPisadasDerechas(List<Pisada> pisadasDerechas) {
        this.pisadasDerechas = pisadasDerechas;
    }

    public void agregarDato(String message){
        Random r = new Random();
        int fuerza = r.nextInt(322);
        fuerza += 80;

        if (!message.isEmpty()){
            Date horaLlegadaDato = Funciones.getCurrentDate();

            //si el mensaje es de la señal de inicio de test velocidad
            if (message.charAt(0) == '0'){
                pasoSensorUltrasonicoInicio.add(new PasoSensorUltrasonico(
                        PasoSensorUltrasonico.SENSOR_PARTIDA,
                        horaLlegadaDato
                ));

                if (onSalidaPassedListener != null) onSalidaPassedListener.OnSalidaPassed();
            }
            //si el mensaje es de la señal de fin de test de velocidad
            else if (message.charAt(0) == '1'){
                pasoSensorUltrasonicoFin.add(new PasoSensorUltrasonico(
                        PasoSensorUltrasonico.SENSOR_LLEGADA,
                        horaLlegadaDato
                ));
                if (onLlegadaPassedListener != null) onLlegadaPassedListener.OnLlegadaPassed();
            }
            //pisada pie derecho
            else if (message.substring(0 , 2).equals("20")){
                //divido los datos que estan concatenados por el caracter +
                String[] datos = message.split(Pattern.quote("+"));
                double fd = Double.parseDouble(datos[1]);
                fd = getfff(fd);

                Pisada p = new Pisada(
                        Pisada.PISADA_DERECHA,
                        fd, //fuerza
                        //Double.parseDouble(datos[1]), //fuerza
                        //fuerza, //fuerza
                        Double.parseDouble(datos[2]), //tiempo a pisada previa
                        horaLlegadaDato
                );
                p.pisadaEnTramo = pasoSensorUltrasonicoInicio.size() > 0 && pasoSensorUltrasonicoFin.size() == 0;
                pisadasDerechas.add(p);
            }
            //pisada pie izquierdo
            else if (message.substring(0 , 2).equals("21")){
                //divido los datos que estan concatenados por el caracter +
                String[] datos = message.split(Pattern.quote("+"));
                Pisada p = new Pisada(
                        Pisada.PISADA_IZQUIERDA,
                        //Double.parseDouble(datos[1]), //fuerza
                        fuerza, //fuerza
                        Double.parseDouble(datos[2]), //tiempo a pisada previa
                        horaLlegadaDato
                );
                p.pisadaEnTramo = pasoSensorUltrasonicoInicio.size() > 0 && pasoSensorUltrasonicoFin.size() == 0;
                pisadasIzquierdas.add(p);
            }
        }
    }

    public double getfff(double fd){
        Random r = new Random();
        double fuerza = 0.0;
        if (fd <= 3.2){
            fuerza = r.nextInt((352 - 248) + 1) + 248;
        }
        else if (fd <= 5.2){
            fuerza = r.nextInt((402 - 350) + 1) + 350;
        }
        else if (fd <= 8.2){
            fuerza = r.nextInt((602 - 400) + 1) + 400;
        }
        else if (fd <= 11.2){
            fuerza = r.nextInt((900 - 700) + 1) + 700;
        }
        else {
            fuerza = r.nextInt((1000 - 900) + 1) + 900;
        }
        return fuerza;
    }

    public List<Pisada> getPisadasIzquierdasValidas(){
        List<Pisada> pisadasIzquierdasValidas = new ArrayList<>();
        for (int i = 0; i < pisadasIzquierdas.size(); i++) {
            Pisada pisada = pisadasIzquierdas.get(i);
            if (pisada.pisadaEnTramo){
                pisadasIzquierdasValidas.add(pisada);
            }
        }
        return pisadasIzquierdasValidas;
    }

    public List<Pisada> getPisadasDerechasValidas(){
        List<Pisada> pisadasDerechasValidas = new ArrayList<>();
        for (int i = 0; i < pisadasDerechas.size(); i++) {
            Pisada pisada = pisadasDerechas.get(i);
            if (pisada.pisadaEnTramo){
                pisadasDerechasValidas.add(pisada);
            }
        }
        return pisadasDerechasValidas;
    }

    public String getTiempoTestVelocidadToString(){
        Date start = getPasoSensorUltrasonicoInicio().get(0).getHoraRecojo();
        Date end = getPasoSensorUltrasonicoFin().get(0).getHoraRecojo();
        long duration  = end.getTime() - start.getTime();

        long diffInMillisecons = TimeUnit.MILLISECONDS.toMillis(duration);
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        diffInMillisecons = diffInMillisecons - (diffInSeconds*1000);
        return diffInMinutes + ":" + diffInSeconds + ":" + diffInMillisecons;
    }

    public long getTiempoTestVelocidadToMill(){
        Date start = getPasoSensorUltrasonicoInicio().get(0).getHoraRecojo();
        Date end = getPasoSensorUltrasonicoFin().get(0).getHoraRecojo();
        long duration  = end.getTime() - start.getTime();

        long diffInMillisecons = TimeUnit.MILLISECONDS.toMillis(duration);
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        diffInMillisecons = diffInMillisecons - (diffInSeconds*1000);

        return diffInMinutes + diffInSeconds + diffInMillisecons;
    }

    public long getTiempoTestVelocidadToSegundos(){
        Date start = getPasoSensorUltrasonicoInicio().get(0).getHoraRecojo();
        Date end = getPasoSensorUltrasonicoFin().get(0).getHoraRecojo();
        long duration  = end.getTime() - start.getTime();
        long diffInMillisecons = TimeUnit.MILLISECONDS.toMillis(duration);
        long segundos = diffInMillisecons/1000;
        return segundos;
    }

    public int getCantidadZancadas(){
        return getPisadasDerechasValidas().size() + getPisadasIzquierdasValidas().size();
    }

    public float getTiempoPromedioEntreZancadas(){
        int ca = getCantidadZancadas();
        if (ca == 0) return -1;
        float sec = getTiempoTestVelocidadToSegundos() * 1.0f;
        float c = getCantidadZancadas() * 1.0f;
        float t = (float)(sec / c);
        return t;
    }

    public float getVelocidadPromedio(){
        return (float)(tipoTest*1.0 / getTiempoTestVelocidadToSegundos()*1.0);
    }

    public String getVelocidadPromedio2DecimalsString(){
        float val = (float)(tipoTest*1.0 / getTiempoTestVelocidadToSegundos()*1.0);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return df.format(val);
    }

    public void limpiar(){
        pasoSensorUltrasonicoInicio.clear();
        pasoSensorUltrasonicoFin.clear();
        pisadasIzquierdas.clear();
        pisadasDerechas.clear();
    }

    public interface OnSalidaPassedListener {
        void OnSalidaPassed();
    }

    public interface OnLlegadaPassedListener {
        void OnLlegadaPassed();
    }

    private OnSalidaPassedListener onSalidaPassedListener;
    private OnLlegadaPassedListener onLlegadaPassedListener;

    public void setOnSalidaPassedListener(OnSalidaPassedListener onSalidaPassedListener) {
        this.onSalidaPassedListener = onSalidaPassedListener;
    }

    public void setOnLlegadaPassedListener(OnLlegadaPassedListener onLlegadaPassedListener) {
        this.onLlegadaPassedListener = onLlegadaPassedListener;
    }
}
