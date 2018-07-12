package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            athlete.setFirstName(jsonObject.getString("firstName"));
            athlete.setLastName(jsonObject.getString("lastName"));
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
