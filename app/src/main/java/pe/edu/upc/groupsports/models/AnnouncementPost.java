package pe.edu.upc.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by karique on 4/09/2018.
 */

public class AnnouncementPost {
    private String announcementTitle;
    private String announcementDetail;
    private String coachId;
    private List<athleteIdClass> athletesId;

    public AnnouncementPost() {
    }

    public AnnouncementPost(String announcementTitle, String announcementDetail, String coachId, List<athleteIdClass> athletesId) {
        this.announcementTitle = announcementTitle;
        this.announcementDetail = announcementDetail;
        this.coachId = coachId;
        this.athletesId = athletesId;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public void setAnnouncementTitle(String announcementTitle) {
        this.announcementTitle = announcementTitle;
    }

    public String getAnnouncementDetail() {
        return announcementDetail;
    }

    public void setAnnouncementDetail(String announcementDetail) {
        this.announcementDetail = announcementDetail;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public List<athleteIdClass> getAthletesId() {
        return athletesId;
    }

    public void setAthletesId(List<athleteIdClass> athletesId) {
        this.athletesId = athletesId;
    }

    public static class athleteIdClass {

        public String athleteId;

        public athleteIdClass() {
        }

        public athleteIdClass(String athleteId) {
            this.athleteId = athleteId;
        }

        public JSONObject toJSONObject(){
            try {
                return new JSONObject().put("athleteId",athleteId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
