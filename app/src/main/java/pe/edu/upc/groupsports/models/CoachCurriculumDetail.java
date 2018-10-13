package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 25/09/2018.
 */

public class CoachCurriculumDetail {
    private String id;
    private String title;
    private String description;
    private String coachId;

    public static CoachCurriculumDetail toCoachCurriculumDetail(JSONObject jsonObject){
        CoachCurriculumDetail coachCurriculumDetail = new CoachCurriculumDetail();
        try {
            coachCurriculumDetail.setId(jsonObject.getString("id"));
            coachCurriculumDetail.setTitle(jsonObject.getString("title"));
            coachCurriculumDetail.setDescription(jsonObject.getString("description"));
            coachCurriculumDetail.setCoachId(jsonObject.getString("coachId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coachCurriculumDetail;
    }

    public CoachCurriculumDetail() {
    }

    public CoachCurriculumDetail(String id, String title, String description, String coachId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coachId = coachId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }
}
