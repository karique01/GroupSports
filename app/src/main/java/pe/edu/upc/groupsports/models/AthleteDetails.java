package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 12/09/2018.
 */

public class AthleteDetails {
    private String id;
    private String weight;
    private String height;
    private String bodySpan;
    private String legLength;
    private String halfSquatToFloor;
    private String shirtSize;
    private String pantsSize;
    private String footwearSize;
    private String athleteId;
    private Date dateOfDetailInserted;

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("weight",weight);
            jsonObject.put("height",height);
            jsonObject.put("bodySpan",bodySpan);
            jsonObject.put("legLength",legLength);
            jsonObject.put("halfSquatToFloor",halfSquatToFloor);
            jsonObject.put("shirtSize",shirtSize);
            jsonObject.put("pantsSize",pantsSize);
            jsonObject.put("footwearSize",footwearSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static AthleteDetails toAthleteDetails(JSONObject jsonObject){
        AthleteDetails athleteDetails = new AthleteDetails();

        try {
            athleteDetails.setId(jsonObject.getString("id"));

            if (!jsonObject.isNull("weight"))
            athleteDetails.setWeight(jsonObject.getString("weight"));
            if (!jsonObject.isNull("height"))
            athleteDetails.setHeight(jsonObject.getString("height"));
            if (!jsonObject.isNull("bodySpan"))
            athleteDetails.setBodySpan(jsonObject.getString("bodySpan"));
            if (!jsonObject.isNull("legLength"))
            athleteDetails.setLegLength(jsonObject.getString("legLength"));
            if (!jsonObject.isNull("halfSquatToFloor"))
            athleteDetails.setHalfSquatToFloor(jsonObject.getString("halfSquatToFloor"));
            if (!jsonObject.isNull("shirtSize"))
            athleteDetails.setShirtSize(jsonObject.getString("shirtSize"));
            if (!jsonObject.isNull("pantsSize"))
            athleteDetails.setPantsSize(jsonObject.getString("pantsSize"));
            if (!jsonObject.isNull("footwearSize"))
            athleteDetails.setFootwearSize(jsonObject.getString("footwearSize"));

            if (!jsonObject.isNull("athleteId"))
            athleteDetails.setAthleteId(jsonObject.getString("athleteId"));
            if (!jsonObject.isNull("dateOfDetailInserted"))
            athleteDetails.setDateOfDetailInserted(Funciones.getDateFromString(jsonObject.getString("dateOfDetailInserted")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return athleteDetails;
    }

    public AthleteDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBodySpan() {
        return bodySpan;
    }

    public void setBodySpan(String bodySpan) {
        this.bodySpan = bodySpan;
    }

    public String getLegLength() {
        return legLength;
    }

    public void setLegLength(String legLength) {
        this.legLength = legLength;
    }

    public String getHalfSquatToFloor() {
        return halfSquatToFloor;
    }

    public void setHalfSquatToFloor(String halfSquatToFloor) {
        this.halfSquatToFloor = halfSquatToFloor;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public String getPantsSize() {
        return pantsSize;
    }

    public void setPantsSize(String pantsSize) {
        this.pantsSize = pantsSize;
    }

    public String getFootwearSize() {
        return footwearSize;
    }

    public void setFootwearSize(String footwearSize) {
        this.footwearSize = footwearSize;
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public Date getDateOfDetailInserted() {
        return dateOfDetailInserted;
    }

    public void setDateOfDetailInserted(Date dateOfDetailInserted) {
        this.dateOfDetailInserted = dateOfDetailInserted;
    }
}
