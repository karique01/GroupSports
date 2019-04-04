package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 4/09/2018.
 */

public class BinnacleDetail {
    private String id;
    private String detail;

    public static BinnacleDetail toBinnacleDetail(JSONObject binnacleDetailJsonObject){
        BinnacleDetail binnacleDetail = new BinnacleDetail();

        try {
            binnacleDetail.setId(binnacleDetailJsonObject.getString("id"));
            binnacleDetail.setDetail(binnacleDetailJsonObject.getString("detail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return binnacleDetail;
    }

    public BinnacleDetail() {
    }

    public BinnacleDetail(String id, String detail) {
        this.id = id;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
