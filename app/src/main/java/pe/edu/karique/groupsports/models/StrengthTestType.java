package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karique on 17/09/2018.
 */

public class StrengthTestType {
    private String id;
    private String description;
    private List<StrengthTest> strengthTests = new ArrayList<>();

    public static StrengthTestType toStrengthTestType(JSONObject jsonObject){
        StrengthTestType strengthTestType = new StrengthTestType();
        try {
            strengthTestType.setId(jsonObject.getString("id"));
            strengthTestType.setDescription(jsonObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return strengthTestType;
    }

    public StrengthTestType() {
    }

    public StrengthTestType(String id, String description, List<StrengthTest> strengthTests) {
        this.id = id;
        this.description = description;
        this.strengthTests = strengthTests;
    }

    public void clearTests(){
        strengthTests.clear();
    }

    public void addStrengthTest(StrengthTest strengthTest){
        strengthTests.add(strengthTest);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StrengthTest> getStrengthTests() {
        return strengthTests;
    }

    public void setStrengthTests(List<StrengthTest> strengthTests) {
        this.strengthTests = strengthTests;
    }
}
