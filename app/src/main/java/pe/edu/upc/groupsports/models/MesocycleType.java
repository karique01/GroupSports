package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 26/08/2018.
 */

public class MesocycleType {
    private String id;
    private String mesocycleName;

    public MesocycleType() {
    }

    public MesocycleType(String id, String mesocycleName) {
        this.id = id;
        this.mesocycleName = mesocycleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMesocycleName() {
        return mesocycleName;
    }

    public void setMesocycleName(String mesocycleName) {
        this.mesocycleName = mesocycleName;
    }

    public static MesocycleType toMesocycleType(JSONObject jsonObject){
        MesocycleType mesocycleType = new MesocycleType();

        try {
            mesocycleType.setId(jsonObject.getString("id"));
            mesocycleType.setMesocycleName(jsonObject.getString("mesocycleName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mesocycleType;
    }

    @Override
    public String toString() {
        return getMesocycleName();
    }
}
