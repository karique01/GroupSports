package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 24/09/2018.
 */

public class Coach {
    private String id;
    private String username;
    private String userType;
    private String firstName;
    private String lastName;
    private String cellPhone;
    private String address;
    private String emailAddress;
    private String pictureUrl;
    private Date birthDate;
    private String edad;
    private String yearsOfExperience;
    private String userId;

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public static Coach toCoach(JSONObject jsonObject){
        Coach coach = new Coach();
        try {
            coach.setId(jsonObject.getString("id"));
            coach.setUsername(jsonObject.getString("username"));
            coach.setUserType(jsonObject.getString("userType"));
            coach.setFirstName(jsonObject.getString("firstName"));
            coach.setLastName(jsonObject.getString("lastName"));
            coach.setCellPhone(jsonObject.getString("cellPhone"));
            coach.setAddress(jsonObject.getString("address"));
            coach.setEmailAddress(jsonObject.getString("emailAddress"));
            coach.setPictureUrl(jsonObject.getString("pictureUrl"));
            coach.setBirthDate(Funciones.getDateFromString(jsonObject.getString("birthDate")));
            coach.setEdad(jsonObject.getString("edad"));
            coach.setYearsOfExperience(jsonObject.getString("yearsOfExperience"));
            coach.setUserId(jsonObject.getString("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return coach;
    }

    public Coach() {
    }

    public Coach(String id, String username, String userType, String firstName, String lastName, String cellPhone, String address, String emailAddress, String pictureUrl, Date birthDate, String edad, String yearsOfExperience, String userId) {
        this.id = id;
        this.username = username;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellPhone = cellPhone;
        this.address = address;
        this.emailAddress = emailAddress;
        this.pictureUrl = pictureUrl;
        this.birthDate = birthDate;
        this.edad = edad;
        this.yearsOfExperience = yearsOfExperience;
        this.userId = userId;
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

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(String yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
