package pe.edu.karique.groupsports.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karique on 3/09/2018.
 */

public class Announcement {
    private String id;
    private String announcementTitle;
    private String announcementDetail;
    private String coachId;
    private List<String> athletesNames;
    private boolean selected = false;

    public String getConcatenatedNames(){
        StringBuilder atletas = new StringBuilder("\nEnviado a: \n\n");
        for (int i = 0; i < athletesNames.size(); i++) {
            atletas.append("- ").append(athletesNames.get(i)).append("\n");
        }
        return atletas.toString();
    }

    public static Announcement toAnnouncement(JSONObject announcementJsonObject){
        Announcement announcement = new Announcement();

        try {
            announcement.setId(announcementJsonObject.getString("id"));
            announcement.setAnnouncementTitle(announcementJsonObject.getString("announcementTitle"));
            announcement.setAnnouncementDetail(announcementJsonObject.getString("announcementDetail"));
            announcement.setCoachId(announcementJsonObject.getString("coachId"));

            List<String> names = new ArrayList<>();
            JSONArray jsonArray = announcementJsonObject.getJSONArray("athletesNames");
            for (int i = 0; i < jsonArray.length(); i++) {
                names.add(jsonArray.getString(i));
            }

            announcement.setAthletesNames(names);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return announcement;
    }

    public Announcement() {
    }

    public Announcement(String id, String announcementTitle, String announcementDetail, String coachId) {
        this.id = id;
        this.announcementTitle = announcementTitle;
        this.announcementDetail = announcementDetail;
        this.coachId = coachId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getAthletesNames() {
        return athletesNames;
    }

    public void setAthletesNames(List<String> athletesNames) {
        this.athletesNames = athletesNames;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
