package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karique on 17/09/2018.
 */

public class SaltabilityTestType {
    private String id;
    private String description;
    private List<JumpTest> jumpTests = new ArrayList<>();

    public static SaltabilityTestType toSaltabilityTestType(JSONObject jsonObject){
        SaltabilityTestType saltabilityTestType = new SaltabilityTestType();
        try {
            saltabilityTestType.setId(jsonObject.getString("id"));
            saltabilityTestType.setDescription(jsonObject.getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return saltabilityTestType;
    }

    public SaltabilityTestType() {
    }

    public SaltabilityTestType(String id, String description, List<JumpTest> jumpTests) {
        this.id = id;
        this.description = description;
        this.jumpTests = jumpTests;
    }
    public void clearTests(){
        jumpTests.clear();
    }

    public void addSaltabilityTest(JumpTest jumpTest){
        jumpTests.add(jumpTest);
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

    public List<JumpTest> getJumpTests() {
        return jumpTests;
    }

    public void setJumpTests(List<JumpTest> jumpTests) {
        this.jumpTests = jumpTests;
    }
}
