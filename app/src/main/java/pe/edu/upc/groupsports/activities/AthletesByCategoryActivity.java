package pe.edu.upc.groupsports.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.AthleteAdapter;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

public class AthletesByCategoryActivity extends AppCompatActivity {

    SessionManager session;

    //recycler things
    RecyclerView athletesRecyclerView;
    AthleteAdapter athletesAdapter;
    RecyclerView.LayoutManager athletesLayoutManager;
    List<Athlete> athletes;
    public static String categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athletes_by_category);

        session = new SessionManager(this);
        athletesRecyclerView = (RecyclerView) findViewById(R.id.athletesRecyclerView);
        athletes = new ArrayList<>();
        athletesAdapter = new AthleteAdapter(athletes);
        athletesLayoutManager = new LinearLayoutManager(this);
        athletesRecyclerView.setAdapter(athletesAdapter);
        athletesRecyclerView.setLayoutManager(athletesLayoutManager);

        updateData();
    }

    private void updateData(){
        int coachId = Integer.parseInt(session.getuserLoggedTypeId());
        AndroidNetworking.get(GroupSportsApiService.ATHELETES_BY_COACH_BY_CATEGORY_URL(coachId,categoria))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athletes.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                athletes.add(Athlete.toAthlete(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        athletesAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
