package pe.edu.upc.groupsports.Session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by karique on 28/02/2018.
 */

public class SessionManager {
    private static final String PREFERENCE_NAME = "pe.edu.upc.groupsports";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static final String USER_LOGIN = "userLogin";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String TOKEN_TYPE = "token_type";
    public static final String EXPIRES_IN = "expires_in";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String USER_TYPE = "userType";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String CELL_PHONE = "cellPhone";
    public static final String ADDRESS = "address";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String USER_LOGGED_TYPE_ID = "userLoggedTypeId";
    public static final String PICTURE_URL = "pictureUrl";

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setUserLogin(boolean userLogin) { editor.putBoolean(USER_LOGIN, userLogin).commit(); }
    public boolean getUserLogin() {
        return preferences.getBoolean(USER_LOGIN, false);
    }

    public void setaccess_token(String access_token) { editor.putString(ACCESS_TOKEN, access_token).commit(); }
    public String getaccess_token() { return preferences.getString(ACCESS_TOKEN, ""); }

    public void settoken_type(String token_type) { editor.putString(TOKEN_TYPE, token_type).commit(); }
    public String gettoken_type() { return preferences.getString(TOKEN_TYPE, ""); }

    public void setexpires_in(String expires_in) { editor.putString(EXPIRES_IN, expires_in).commit(); }
    public String getexpires_in() { return preferences.getString(EXPIRES_IN, ""); }

    public void setid(String id) { editor.putString(ID, id).commit(); }
    public String getid() { return preferences.getString(ID, ""); }

    public void setusername(String username) { editor.putString(USERNAME, username).commit(); }
    public String getusername() { return preferences.getString(USERNAME, ""); }

    public void setPictureUrl(String pictureUrl) { editor.putString(PICTURE_URL, pictureUrl).commit(); }
    public String getPictureUrl() { return preferences.getString(PICTURE_URL, ""); }

    public void setuserType(String userType) { editor.putString(USER_TYPE, userType).commit(); }
    public String getuserType() { return preferences.getString(USER_TYPE, ""); }
    public String getuserTypeDetail() {
        String userType = preferences.getString(USER_TYPE, "");
        return userType.equals("1") ? "Entrenador" : userType.equals("2") ? "Atleta" : "";
    }

    public void setfirstName(String firstName) { editor.putString(FIRST_NAME, firstName).commit(); }
    public String getfirstName() { return preferences.getString(FIRST_NAME, ""); }

    public void setlastName(String lastName) { editor.putString(LAST_NAME, lastName).commit(); }
    public String getlastName() { return preferences.getString(LAST_NAME, ""); }

    public void setcellPhone(String cellPhone) { editor.putString(CELL_PHONE, cellPhone).commit(); }
    public String getcellPhone() { return preferences.getString(CELL_PHONE, ""); }

    public void setaddress(String address) { editor.putString(ADDRESS, address).commit(); }
    public String getaddress() { return preferences.getString(ADDRESS, ""); }

    public void setemailAddress(String emailAddress) { editor.putString(EMAIL_ADDRESS, emailAddress).commit(); }
    public String getemailAddress() { return preferences.getString(EMAIL_ADDRESS, ""); }

    public void setuserLoggedTypeId(String userLoggedTypeId) { editor.putString(USER_LOGGED_TYPE_ID, userLoggedTypeId).commit(); }
    public String getuserLoggedTypeId() { return preferences.getString(USER_LOGGED_TYPE_ID, ""); }

    public void deleteUserSession(){
        setUserLogin(false);
        editor.putBoolean(USER_LOGIN, false).commit();
        editor.putString(ID, "").commit();
        editor.putString(ACCESS_TOKEN, "").commit();
        editor.putString(TOKEN_TYPE, "").commit();
        editor.putString(EXPIRES_IN, "").commit();
        editor.putString(USERNAME, "").commit();
        editor.putString(PICTURE_URL, "").commit();
        editor.putString(USER_TYPE, "").commit();
        editor.putString(FIRST_NAME, "").commit();
        editor.putString(LAST_NAME, "").commit();
        editor.putString(CELL_PHONE, "").commit();
        editor.putString(ADDRESS, "").commit();
        editor.putString(EMAIL_ADDRESS, "").commit();
        editor.putString(USER_LOGGED_TYPE_ID, "").commit();
    }
}
