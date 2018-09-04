package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.adapters.AnnouncementCoachAdapter;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.models.Announcement;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAnnouncementsFragment extends Fragment {
    SessionManager session;

    //recycler things
    RecyclerView announcementsRecyclerView;
    AnnouncementCoachAdapter announcementCoachAdapter;
    RecyclerView.LayoutManager announcementLayoutManager;
    List<Announcement> announcements;
    Context context;

    ConstraintLayout noAthletesConstraintLayout;
    TextView messageTextView;

    public MyAnnouncementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_announcements, container, false);
        context = view.getContext();
        session = new SessionManager(context);
        announcementsRecyclerView = (RecyclerView) view.findViewById(R.id.announcementsRecyclerView);
        announcements = new ArrayList<>();
        announcementCoachAdapter = new AnnouncementCoachAdapter(announcements);
        announcementLayoutManager = new LinearLayoutManager(view.getContext());
        announcementsRecyclerView.setAdapter(announcementCoachAdapter);
        announcementsRecyclerView.setLayoutManager(announcementLayoutManager);

        noAthletesConstraintLayout = (ConstraintLayout) view.findViewById(R.id.noAthletesConstraintLayout);
        messageTextView = (TextView) view.findViewById(R.id.messageTextView);

        announcementCoachAdapter.setOnDeleteAnnouncementListener(new AnnouncementCoachAdapter.OnDeleteAnnouncementListener() {
            @Override
            public void deleteAnnouncement(String announcementId) {
                showWarningDialog(announcementId);
            }
        });

        updateData();
        return view;
    }

    private void showWarningDialog(final String announcementId){
        final WarningDialog warningDialog = new WarningDialog(
                context,
                "Anuncio",
                "Se eliminará el anuncio\n\n¿Está seguro que desea eliminar este anuncio?"
        );
        warningDialog.show();
        warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                deleteAnnouncement(announcementId);
                warningDialog.dismiss();
            }
        });
        warningDialog.setOnCancelButtonClickListener(new WarningDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                warningDialog.dismiss();
            }
        });
    }

    public void updateData(){
        AndroidNetworking.get(GroupSportsApiService.ANNOUNCEMENTS_BY_COACH(session.getuserLoggedTypeId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        announcements.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                announcements.add(Announcement.toAnnouncement(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        announcementCoachAdapter.notifyDataSetChanged();
                        if (response.length() == 0) {
                            noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                            noAthletesConstraintLayout.bringToFront();
                        }
                        else {
                            noAthletesConstraintLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        messageTextView.setText(getResources().getString(R.string.connection_error));
                        noAthletesConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void deleteAnnouncement(String announcementId){
        AndroidNetworking.delete(GroupSportsApiService.ANNOUNCEMENT_URL + announcementId)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")){
                                updateData();
                                onAnnouncementDeleted.AnnouncementDeleted();
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar el anuncio",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Hubo un error en el sistema", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public interface OnAnnouncementDeleted {
        void AnnouncementDeleted();
    }

    OnAnnouncementDeleted onAnnouncementDeleted;

    public void setOnAnnouncementDeleted(OnAnnouncementDeleted onAnnouncementDeleted) {
        this.onAnnouncementDeleted = onAnnouncementDeleted;
    }
}
