package pe.edu.karique.groupsports.models;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class Athlete {
    private String id;
    private String username;
    private String password;
    private String userType;
    private String firstName;
    private String lastName;
    private String cellPhone;
    private String address;
    private String emailAddress;
    private String disciplineName;
    private String disciplineId;
    private String userId;
    private String pictureUrl;
    private Date   birthDate;
    private boolean selected = false;

    public Bundle toBundle(){
        Bundle athleteBundle = new Bundle();
        athleteBundle.putString("id",id);
        athleteBundle.putString("username",username);
        athleteBundle.putString("password",password);
        athleteBundle.putString("userType",userType);
        athleteBundle.putString("firstName",firstName);
        athleteBundle.putString("lastName",lastName);
        athleteBundle.putString("cellPhone",cellPhone);
        athleteBundle.putString("address",address);
        athleteBundle.putString("emailAddress",emailAddress);
        athleteBundle.putString("disciplineName",disciplineName);
        athleteBundle.putString("disciplineId",disciplineId);
        athleteBundle.putString("userId",userId);
        athleteBundle.putString("pictureUrl",pictureUrl);
        athleteBundle.putSerializable("birthDate",birthDate);
        athleteBundle.putBoolean("selected",selected);

        return athleteBundle;
    }

    public static Athlete from(Bundle bundle){
        Athlete athlete = new Athlete();
        athlete.setId(bundle.getString("id"));
        athlete.setUsername(bundle.getString("username"));
        athlete.setPassword(bundle.getString("password"));
        athlete.setUserType(bundle.getString("userType"));
        athlete.setFirstName(bundle.getString("firstName"));
        athlete.setLastName(bundle.getString("lastName"));
        athlete.setCellPhone(bundle.getString("cellPhone"));
        athlete.setAddress(bundle.getString("address"));
        athlete.setEmailAddress(bundle.getString("emailAddress"));
        athlete.setDisciplineName(bundle.getString("disciplineName"));
        athlete.setDisciplineId(bundle.getString("disciplineId"));
        athlete.setUserId(bundle.getString("userId"));
        athlete.setPictureUrl(bundle.getString("pictureUrl"));
        athlete.setBirthDate((Date)bundle.getSerializable("birthDate"));
        athlete.setSelected(bundle.getBoolean("selected"));
        return athlete;
    }

    public Athlete() {
    }

    public Athlete(String id, String username, String password, String userType, String firstName, String lastName, String cellPhone, String address, String emailAddress, String disciplineName, String disciplineId, String userId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.address = address;
        this.emailAddress = emailAddress;
        this.disciplineName = disciplineName;
        this.disciplineId = disciplineId;
        this.userId = userId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public String getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(String disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public static Athlete toAthlete(JSONObject jsonObject){
        Athlete athlete = new Athlete();

        try {

            athlete.setId(jsonObject.getString("id"));
            athlete.setFirstName(Funciones.capitalize(jsonObject.getString("firstName")));
            athlete.setLastName(Funciones.capitalize(jsonObject.getString("lastName")));
            athlete.setCellPhone(jsonObject.getString("cellPhone"));
            athlete.setDisciplineName(jsonObject.getString("disciplineName"));
            athlete.setPictureUrl(jsonObject.getString("pictureUrl"));
            String fechaString = jsonObject.getString("birthDate");

            try {
                Date birthDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(fechaString);
                athlete.setBirthDate(birthDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return athlete;
    }
}
