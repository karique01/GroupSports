package pe.edu.karique.groupsports.network;

/**
 * Created by karique on 28/02/2018.
 */

public class GroupSportsApiService {
    public static String BASE_URL = "http://13.59.172.169:80/";  //aws
    //public static String BASE_URL = "http://192.168.1.8:27425/"; //casa
    //public static String BASE_URL = "http://192.168.43.79:27425/"; //cel viejo
    //public static String BASE_URL = "http://172.16.198.7:27425/"; //starbucks UPC San isidro
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
    public static String BINNACLE_DETAILS_URL = BASE_URL + "api/BinnacleDetails/";
    public static String COACH_QUIZZES_URL = BASE_URL + "api/quizzes/";
    public static String ANSWERS_URL = BASE_URL + "api/QuizQuestions/answers";
    public static String ATHLETE_DETAILS_URL = BASE_URL + "api/AthleteDetails/";
    public static String SHOT_PUT_TEST_URL = BASE_URL + "api/ShotPutTest/";
    public static String WEIGHT_TEST_BY_SESSION_URL = BASE_URL + "api/WeightTestBySession/";
    public static String ANTROPOMETRIC_TEST_SESSION_URL = BASE_URL + "api/AntropometricTest/";
    public static String SALTABILITY_TEST_TYPE_SESSION_URL = BASE_URL + "api/JumpTestTypes/";
    public static String STRENGTH_TEST_TYPE_SESSION_URL = BASE_URL + "api/StrengthTestTypes/";
    public static String SALTABILITY_TEST_SESSION_URL = BASE_URL + "api/JumpTest/";
    public static String STRENGTH_TEST_SESSION_URL = BASE_URL + "api/StrengthTest/";
    public static String ATHLETE_FODA_URL = BASE_URL + "api/AthleteFodas/";
    public static String ATHLETE_ACHIEVEMENT_URL = BASE_URL + "api/AthleteAchievements/";
    public static String COACH_CURRICULUM_DETAILS_URL = BASE_URL + "api/CoachCurriculumDetails/";
    public static String ACTIVE_CALORIES_URL = BASE_URL + "api/ActiveCaloriesWearable/";
    public static String STEPS_COUNT_URL = BASE_URL + "api/StepsCountWearable/";
    public static String HEART_RATE_URL = BASE_URL + "api/HeartRateWearable/";

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
    public static String WORKSESSIONS_BY_COACH_BY_DATE(String coachId, String day) {
        return COACHS_URL + coachId + "/worksessions/" + day;
    }
    public static String WORKSESSIONS_BY_ATHLETE_BY_DATE(String athleteId, String day) {
        return ATHLETES_URL + athleteId + "/worksessions/" + day;
    }
    public static String SPEED_TEST_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/speedTest/";
    }
    public static String SPEED_TEST_BY_ATHLETE_BY_RANGE_DATE(String athleteId, String startDate, String endDate) {
        return ATHLETES_URL + athleteId + "/speedTest/?startDate="+startDate+"&endDate="+endDate;
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
    public static String BINNACLE_DETAILS_BY_WORKSESSIONS(String workSessionId) {
        return WORK_SESSIONS_URL + workSessionId + "/BinnacleDetails/";
    }
    public static String QUIZZES_BY_COACH(String coachId) {
        return COACHS_URL + coachId + "/quizzes/";
    }
    public static String QUIZZES_BY_COACH_BY_DATE(String coachId, String day) {
        return COACHS_URL + coachId + "/quizzes/" + day;
    }
    public static String ATHLETES_QUESTIONS_BY_QUIZ(String quizId) {
        return COACH_QUIZZES_URL + quizId + "/athletesQuestions/";
    }
    public static String QUIZ_QUESTIONS_BY_QUIZ(String quizId) {
        return COACH_QUIZZES_URL + quizId + "/QuizQuestions/";
    }
    public static String ATHLETES_QUESTIONS_BY_ATHLETES(String athleteId) {
        return ATHLETES_URL + athleteId + "/athletesQuestions/";
    }
    public static String ATHLETE_DETAILS_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/AthleteDetails/";
    }
    public static String SHOT_PUT_TEST_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/ShotPutTest/";
    }
    public static String WEIGHT_TEST_BY_SESSION_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/WeightTestBySession/";
    }
    public static String WEIGHT_TEST_BY_SESSION_BY_SESSION_WORK(String sessionId) {
        return WORK_SESSIONS_URL + sessionId + "/WeightTestBySession/";
    }
    public static String ANTROPOMETRIC_TEST_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/AntropometricTest/";
    }
    public static String SALTABILITY_TEST_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/JumpTest/";
    }
    public static String SALTABILITY_TEST_BY_ATHLETE_BY_RANGE_DATE(String athleteId, String startDate, String endDate) {
        return ATHLETES_URL + athleteId + "/JumpTest/?startDate="+startDate+"&endDate="+endDate;
    }
    public static String STRENGTH_TEST_BY_ATHLETE_BY_RANGE_DATE(String athleteId, String startDate, String endDate) {
        return ATHLETES_URL + athleteId + "/StrengthTest/?startDate="+startDate+"&endDate="+endDate;
    }
    public static String STRENGTH_TEST_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/StrengthTest/";
    }
    public static String ATHLETE_FODA_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/AthleteFodas/";
    }
    public static String ATHLETE_ACHIEVEMENT_BY_ATHLETE(String athleteId) {
        return ATHLETES_URL + athleteId + "/AthleteAchievements/";
    }
    public static String COACHS_BY_ATHLETE_URL(int id) {
        return ATHLETES_URL + id + "/coachs/";
    }
    public static String CURRICULUM_DETAILS_BY_COACH(String coachId) {
        return COACHS_URL + coachId + "/CoachCurriculumDetails/";
    }
    public static String SPEED_PERFORMANCES_BY_COACH_BY_METERS(String coachId, String meters) {
        return COACHS_URL + coachId + "/AthletesSpeedPerformance/" + meters;
    }
    public static String JUMP_PERFORMANCES_BY_COACH_BY_JUMPTESTTYPEID(String coachId, String jumpTestTypeId) {
        return COACHS_URL + coachId + "/AthletesSaltabilityPerformance/" + jumpTestTypeId;
    }
    public static String ACTIVE_CALORIES_BY_DATE_BY_ATHLETEID(String date, String athleteId) {
        return ACTIVE_CALORIES_URL + date + "/athleteId/" + athleteId;
    }
    public static String STEPS_COUNT_BY_DATE_BY_ATHLETEID(String date, String athleteId) {
        return STEPS_COUNT_URL + date + "/athleteId/" + athleteId;
    }
    public static String HEART_RATE_BY_DATE_BY_ATHLETEID(String date, String athleteId) {
        return HEART_RATE_URL + date + "/athleteId/" + athleteId;
    }
}







































