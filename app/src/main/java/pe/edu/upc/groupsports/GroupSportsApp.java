package pe.edu.upc.groupsports;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by karique on 28/02/2018.
 */

public class GroupSportsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
