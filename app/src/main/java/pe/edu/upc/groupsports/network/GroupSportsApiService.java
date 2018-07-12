package pe.edu.upc.groupsports.network;

/**
 * Created by karique on 28/02/2018.
 */

public class GroupSportsApiService {
    public static String BASE_URL = "http://52.15.243.101/";  //aws
    //public static String BASE_URL = "http://192.168.1.8:8080/"; //casa
    //public static String BASE_URL = "http://10.21.130.31:8080/"; //upc
    public static String LOGIN_URL = BASE_URL + "token";

    public static String COACHS_URL = BASE_URL + "api/coachs/";
    public static String ATHELETES_BY_COACH_URL(int id) {
        return COACHS_URL + id + "/atheletes/";
    }
    public static String ATHELETES_BY_COACH_BY_CATEGORY_URL(int id, String categoryName) {
        return ATHELETES_BY_COACH_URL(id) + categoryName;
    }

    public static String TEAMS_URL = BASE_URL + "api/teams/";
    public static String ATHLETES_URL = BASE_URL + "api/athletes/";
}
