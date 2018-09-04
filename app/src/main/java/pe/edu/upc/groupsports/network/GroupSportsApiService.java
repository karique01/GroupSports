package pe.edu.upc.groupsports.network;

import java.util.Date;

/**
 * Created by karique on 28/02/2018.
 */

public class GroupSportsApiService {
    public static String BASE_URL = "http://52.15.243.101:80/";  //aws
    //public static String BASE_URL = "http://192.168.1.8:8080/"; //casa
    //public static String BASE_URL = "http://172.16.198.178:8080/"; //starbucks UPC San isidro
    //public static String BASE_URL = "http://172.16.249.244:8080/"; //starbucks UPC Villa
    //public static String BASE_URL = "http://192.168.137.1:8080/"; //Cel luis
    //public static String BASE_URL = "http://10.21.130.44:8080/"; //UPC San isidro
    //public static String BASE_URL = "http://1d576257.ngrok.io"; //UPC San isidro tunel
    public static String LOGIN_URL = BASE_URL + "token";

    public static String COACHS_URL = BASE_URL + "api/coachs/";
    public static String TEAMS_URL = BASE_URL + "api/teams/";
    public static String ATHLETES_URL = BASE_URL + "api/athletes/";
    public static String TRAINING_PLANS_URL = BASE_URL + "api/trainingPlans/";
    public static String MESOCYCLES_URL = BASE_URL + "api/mesocycles/";
    public static String MESOCYCLE_TYPES_URL = BASE_URL + "api/mesocycleType/";
    public static String WEEKS_URL = BASE_URL + "api/weeks/";
    public static String WORK_SESSIONS_URL = BASE_URL + "api/workSessions/";
    public static String SPEED_TEST_URL = BASE_URL + "api/SpeedTest/";
    public static String MOOD_TEST_URL = BASE_URL + "api/moods/";
    public static String ANNOUNCEMENT_URL = BASE_URL + "api/announcement/";

    public static String ATHELETES_BY_COACH_URL(int id) {
        return COACHS_URL + id + "/atheletes/";
    }
    public static String ATHELETES_BY_COACH_BY_CATEGORY_URL(int id, String categoryName) {
        return ATHELETES_BY_COACH_URL(id) + categoryName;
    }
    public static String TRAINING_PLANS_BY_COACH_URL(int id) {
        return COACHS_URL + id + "/trainingPlans/";
    }
    public static String ASSISTANCE_BY_COACH_BY_DATE(int id, String assistanceDay) {
        return COACHS_URL + id + "/assistance/" + assistanceDay;
    }
    public static String ASSISTANCE_BY_COACH(int id) {
        return COACHS_URL + id + "/assistance/";
    }
    public static String MESOCYCLES_BY_TRAINING_PLAN(String id) {
        return TRAINING_PLANS_URL + id + "/mesocycles/";
    }
    public static String WEEKS_BY_MESOCYCLE(String mesocycleId) {
        return MESOCYCLES_URL + mesocycleId + "/weeks/";
    }
    public static String WORKSESSIONS_BY_WEEK(String weekId) {
        return WEEKS_URL + weekId + "/workSessions/";
    }
    public static String SPEED_TEST_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/speedTest/";
    }
    public static String MOODS_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/moods/";
    }
    public static String MOOD_TEST_BY_ATHLETE_BY_DATE(String id, String day) {
        return ATHLETES_URL + id + "/moods/" + day;
    }
    public static String ANNOUNCEMENTS_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/announcement/";
    }
    public static String ANNOUNCEMENTS_BY_COACH(String id) {
        return COACHS_URL + id + "/announcement/";
    }
}







































