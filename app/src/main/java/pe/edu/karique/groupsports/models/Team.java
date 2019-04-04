package pe.edu.karique.groupsports.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karique on 3/05/2018.
 */

public class Team {
    private String id;
    private String teamName;

    public Team() {
    }

    public Team(String id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public static Team toTeam(JSONObject jsonObject){
        Team team = new Team();

        try {

            team.setId(jsonObject.getString("id"));
            team.setTeamName(jsonObject.getString("teamName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return team;
    }
}
