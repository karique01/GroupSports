package pe.edu.upc.groupsports.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.util.Constantes;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 21/08/2018.
 */

public class Assistance {
    private String userId;
    private String athleteId;
    private String pictureURL;
    private String fullName;
    private String disciplineName;
    private String currentMesocycle;
    private String totalMesocycles;
    private String currentWeek;
    private String totalWeeks;
    private String trainingPlanName;
    private String mesocycleName;
    private Date dateTime;
    private int shiftTurn = 0;
    private List<AssistanceShift> assistanceShifts;

    public Assistance() {
    }

    public Assistance(String userId, String athleteId, String pictureURL, String fullName, String disciplineName, String currentMesocycle, String totalMesocycles, String currentWeek, String totalWeeks, Date dateTime) {
        this.userId = userId;
        this.athleteId = athleteId;
        this.pictureURL = pictureURL;
        this.fullName = fullName;
        this.disciplineName = disciplineName;
        this.currentMesocycle = currentMesocycle;
        this.totalMesocycles = totalMesocycles;
        this.currentWeek = currentWeek;
        this.totalWeeks = totalWeeks;
        this.dateTime = dateTime;
    }

    public String getTrainingPlanName() {
        return trainingPlanName;
    }

    public void setTrainingPlanName(String trainingPlanName) {
        this.trainingPlanName = trainingPlanName;
    }

    public String getMesocycleName() {
        return mesocycleName;
    }

    public void setMesocycleName(String mesocycleName) {
        this.mesocycleName = mesocycleName;
    }

    public void setAttendanceOfCurrentShift(boolean attendanceBoolean){
        AssistanceShift assistanceShift = assistanceShifts.get(getShiftTurn());
        assistanceShift.setAttendance(attendanceBoolean);
    }

    public void setIntensityPercentageOfCurrentShift(double intensity){
        AssistanceShift assistanceShift = assistanceShifts.get(getShiftTurn());
        assistanceShift.setIntensityPercentage(intensity);
    }

    public AssistanceShift getCurrentShift(){
        return assistanceShifts.get(shiftTurn);
    }

    public void setInitialShiftTurn(){
        int hora = Funciones.getCurrentHour();
        int shiftTurnId = hora >= 6 && hora < 12 ? Constantes.SHIFT_ID_MAÑANA - 1:
                    hora >= 12 && hora < 19 ? Constantes.SHIFT_ID_TARDE - 1:
                            Constantes.SHIFT_ID_NOCHE;

        shiftTurn = shiftTurnId > assistanceShifts.size() - 1 ? assistanceShifts.size() - 1 : shiftTurnId;
    }

    public void setPrevShiftTurn(){
        shiftTurn--;
        if (shiftTurn < 0)
            shiftTurn = assistanceShifts.size() - 1;
    }

    public void setNextShiftTurn(){
        shiftTurn++;
        if (shiftTurn >= assistanceShifts.size())
            shiftTurn = 0;
    }

    public int getShiftTurn() {
        return shiftTurn;
    }

    public void setShiftTurn(int shiftTurn) {
        this.shiftTurn = shiftTurn;
    }

    public List<AssistanceShift> getAssistanceShifts() {
        return assistanceShifts;
    }

    public void setAssistanceShifts(List<AssistanceShift> assistanceShifts) {
        this.assistanceShifts = assistanceShifts;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getCurrentMesocycle() {
        return currentMesocycle;
    }

    public int getCurrentMesocycleInt() {
        return Integer.parseInt(currentMesocycle);
    }

    public void setCurrentMesocycle(String currentMesocycle) {
        this.currentMesocycle = currentMesocycle;
    }

    public String getTotalMesocycles() {
        return totalMesocycles;
    }

    public int getTotalMesocyclesInt() {
        return Integer.parseInt(totalMesocycles);
    }

    public void setTotalMesocycles(String totalMesocycles) {
        this.totalMesocycles = totalMesocycles;
    }

    public String getCurrentWeek() {
        return currentWeek;
    }

    public int getCurrentWeekInt() {
        return Integer.parseInt(currentWeek);
    }

    public void setCurrentWeek(String currentWeek) {
        this.currentWeek = currentWeek;
    }

    public String getTotalWeeks() {
        return totalWeeks;
    }

    public int getTotalWeeksInt() {
        return Integer.parseInt(totalWeeks);
    }

    public void setTotalWeeks(String totalWeeks) {
        this.totalWeeks = totalWeeks;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public static Assistance toAssistance(JSONObject jsonObject){
        Assistance assistance = new Assistance();

        try {
            assistance.setUserId(jsonObject.getString("userId"));
            assistance.setAthleteId(jsonObject.getString("athleteId"));
            assistance.setPictureURL(jsonObject.getString("pictureURL"));
            assistance.setFullName(Funciones.capitalize(jsonObject.getString("fullName")));
            assistance.setDisciplineName(jsonObject.getString("disciplineName"));
            assistance.setCurrentMesocycle(jsonObject.getString("currentMesocycle"));
            assistance.setTotalMesocycles(jsonObject.getString("totalMesocycles"));
            assistance.setCurrentWeek(jsonObject.getString("currentWeek"));
            assistance.setTotalWeeks(jsonObject.getString("totalWeeks"));
            assistance.setTrainingPlanName(jsonObject.getString("trainingPlanName"));
            assistance.setMesocycleName(jsonObject.getString("mesocycleName"));
            Assistance.setDateTime(assistance,jsonObject);

            JSONArray jsonAssistanceShifts = jsonObject.getJSONArray("assistanceShifts");
            List<AssistanceShift> assistanceShifts = new ArrayList<>();

            for (int i = 0; i < jsonAssistanceShifts.length(); i++) {
                assistanceShifts.add(AssistanceShift.toAssistanceShift(jsonAssistanceShifts.getJSONObject(i)));
            }

            assistance.setAssistanceShifts(assistanceShifts);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return assistance;
    }

    public static void setDateTime(Assistance assistance, JSONObject jsonObject){
        //fecha de la session
        try {
            String startDateString = null;
            try {
                startDateString = jsonObject.getString("dateTime");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Date dateTime = new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);
            assistance.setDateTime(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
