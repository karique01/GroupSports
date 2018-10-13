package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import pe.edu.upc.groupsports.adapters.AthleteFodaAdapter;
import pe.edu.upc.groupsports.dialogs.AdminValueDialog;
import pe.edu.upc.groupsports.dialogs.WarningDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.AthleteFodaItem;
import pe.edu.upc.groupsports.models.BinnacleDetail;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class FodaFragment extends Fragment {

    Athlete currentAthlete;
    SessionManager session;
    List<AthleteFodaItem> athleteFodaItems;

    RecyclerView weaknessRecyclerView;
    AthleteFodaAdapter weaknessAthleteFodaAdapter;
    RecyclerView.LayoutManager weaknessLayoutManager;
    List<AthleteFodaItem> athleteWeaknessFodaItems;

    RecyclerView threatRecyclerView;
    AthleteFodaAdapter threatAthleteFodaAdapter;
    RecyclerView.LayoutManager threatLayoutManager;
    List<AthleteFodaItem> athleteThreatsFodaItems;

    RecyclerView strengthRecyclerView;
    AthleteFodaAdapter strengthAthleteFodaAdapter;
    RecyclerView.LayoutManager strengthLayoutManager;
    List<AthleteFodaItem> athleteStrengthFodaItems;

    RecyclerView opportunitiesRecyclerView;
    AthleteFodaAdapter opportunitiesAthleteFodaAdapter;
    RecyclerView.LayoutManager opportunitiesLayoutManager;
    List<AthleteFodaItem> athleteOportunitiesFodaItems;

    ConstraintLayout weaknessConstraintLayout;
    TextView weaknessmessageTextView;
    Context context;

    ImageButton addWeaknessImageButton;
    ImageButton addThreatImageButton;
    ImageButton addStrengthImageButton;
    ImageButton addOportunityImageButton;

    public FodaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foda, container, false);

        context = view.getContext();
        session = new SessionManager(context);
        athleteFodaItems = new ArrayList<>();
        athleteWeaknessFodaItems = new ArrayList<>();
        athleteStrengthFodaItems = new ArrayList<>();
        athleteOportunitiesFodaItems = new ArrayList<>();
        athleteThreatsFodaItems = new ArrayList<>();

        addWeaknessImageButton = (ImageButton) view.findViewById(R.id.addWeaknessImageButton);
        addWeaknessImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFodaItemDialog("2");
            }
        });
        addThreatImageButton = (ImageButton) view.findViewById(R.id.addThreatImageButton);
        addThreatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFodaItemDialog("4");
            }
        });
        addStrengthImageButton = (ImageButton) view.findViewById(R.id.addStrengthImageButton);
        addStrengthImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFodaItemDialog("1");
            }
        });
        addOportunityImageButton = (ImageButton) view.findViewById(R.id.addOportunityImageButton);
        addOportunityImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFodaItemDialog("3");
            }
        });

        weaknessRecyclerView = (RecyclerView) view.findViewById(R.id.weaknessRecyclerView);
        weaknessAthleteFodaAdapter = new AthleteFodaAdapter(athleteWeaknessFodaItems);
        weaknessLayoutManager = new LinearLayoutManager(context);
        weaknessRecyclerView.setAdapter(weaknessAthleteFodaAdapter);
        weaknessRecyclerView.setLayoutManager(weaknessLayoutManager);
        weaknessAthleteFodaAdapter.setOnDeleteFodaItemListener(new AthleteFodaAdapter.OnDeleteFodaItemListener() {
            @Override
            public void OnDeleteFodaItemClicked(AthleteFodaItem athleteFodaItem) {
                showDeleteDialog(athleteFodaItem);
            }
        });
        weaknessAthleteFodaAdapter.setOnEditFodaItemListener(new AthleteFodaAdapter.OnEditFodaItemListener() {
            @Override
            public void OnEditFodaItemClicked(AthleteFodaItem athleteFodaItem) {
                showAdminValueDialog(athleteFodaItem);
            }
        });

        threatRecyclerView = (RecyclerView) view.findViewById(R.id.threatRecyclerView);
        threatAthleteFodaAdapter = new AthleteFodaAdapter(athleteThreatsFodaItems);
        threatLayoutManager = new LinearLayoutManager(context);
        threatRecyclerView.setAdapter(threatAthleteFodaAdapter);
        threatRecyclerView.setLayoutManager(threatLayoutManager);
        threatAthleteFodaAdapter.setOnDeleteFodaItemListener(new AthleteFodaAdapter.OnDeleteFodaItemListener() {
            @Override
            public void OnDeleteFodaItemClicked(AthleteFodaItem athleteFodaItem) {
                showDeleteDialog(athleteFodaItem);
            }
        });
        threatAthleteFodaAdapter.setOnEditFodaItemListener(new AthleteFodaAdapter.OnEditFodaItemListener() {
            @Override
            public void OnEditFodaItemClicked(AthleteFodaItem athleteFodaItem) {
                showAdminValueDialog(athleteFodaItem);
            }
        });

        strengthRecyclerView = (RecyclerView) view.findViewById(R.id.strengthRecyclerView);
        strengthAthleteFodaAdapter = new AthleteFodaAdapter(athleteStrengthFodaItems);
        strengthLayoutManager = new LinearLayoutManager(context);
        strengthRecyclerView.setAdapter(strengthAthleteFodaAdapter);
        strengthRecyclerView.setLayoutManager(strengthLayoutManager);
        strengthAthleteFodaAdapter.setOnDeleteFodaItemListener(new AthleteFodaAdapter.OnDeleteFodaItemListener() {
            @Override
            public void OnDeleteFodaItemClicked(AthleteFodaItem athleteFodaItem) {
                showDeleteDialog(athleteFodaItem);
            }
        });
        strengthAthleteFodaAdapter.setOnEditFodaItemListener(new AthleteFodaAdapter.OnEditFodaItemListener() {
            @Override
            public void OnEditFodaItemClicked(AthleteFodaItem athleteFodaItem) {
                showAdminValueDialog(athleteFodaItem);
            }
        });

        opportunitiesRecyclerView = (RecyclerView) view.findViewById(R.id.opportunitiesRecyclerView);
        opportunitiesAthleteFodaAdapter = new AthleteFodaAdapter(athleteOportunitiesFodaItems);
        opportunitiesLayoutManager = new LinearLayoutManager(context);
        opportunitiesRecyclerView.setAdapter(opportunitiesAthleteFodaAdapter);
        opportunitiesRecyclerView.setLayoutManager(opportunitiesLayoutManager);
        opportunitiesAthleteFodaAdapter.setOnDeleteFodaItemListener(new AthleteFodaAdapter.OnDeleteFodaItemListener() {
            @Override
            public void OnDeleteFodaItemClicked(AthleteFodaItem athleteFodaItem) {
                showDeleteDialog(athleteFodaItem);
            }
        });
        opportunitiesAthleteFodaAdapter.setOnEditFodaItemListener(new AthleteFodaAdapter.OnEditFodaItemListener() {
            @Override
            public void OnEditFodaItemClicked(AthleteFodaItem athleteFodaItem) {
                showAdminValueDialog(athleteFodaItem);
            }
        });

        weaknessConstraintLayout = (ConstraintLayout) view.findViewById(R.id.weaknessConstraintLayout);
        weaknessmessageTextView = (TextView) view.findViewById(R.id.weaknessmessageTextView);

        updateData();
        return view;
    }

    private void showAddFodaItemDialog(final String fodaItemType){
        final AdminValueDialog adminValueDialog = new AdminValueDialog(
                context,
                "FODA",
                "Item foda",
                "Agregar",
                "",
                true
        );
        adminValueDialog.show();
        adminValueDialog.setOnOkButtonClickListener(new AdminValueDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String value) {
                adminValueDialog.dismiss();
                uploadFodaItem(fodaItemType,value);
            }
        });
        adminValueDialog.setOnCancelButtonClickListener(new AdminValueDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                adminValueDialog.dismiss();
            }
        });
    }

    private void uploadFodaItem(String fodaItemType, String value){
        JSONObject jsonObjectFodaItem = new JSONObject();

        try {
            jsonObjectFodaItem.put("fodaItemValue", value);
            jsonObjectFodaItem.put("fodaItemTypeId", fodaItemType);
            jsonObjectFodaItem.put("athleteId", currentAthlete.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(GroupSportsApiService.ATHLETE_FODA_URL)
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObjectFodaItem)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("Ok")) {
                                updateData();
                            } else {
                                Toast.makeText(context,"Error al agregar el test",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context,"Error en el sistema",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showAdminValueDialog(final AthleteFodaItem athleteFodaItem){
        final AdminValueDialog adminValueDialog = new AdminValueDialog(
                context,
                "Editar FODA",
                "Item foda",
                "EDITAR",
                athleteFodaItem.getFodaItemValue(),
                true
        );
        adminValueDialog.show();
        adminValueDialog.setOnOkButtonClickListener(new AdminValueDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String value) {
                adminValueDialog.dismiss();
                updateFodaItem(athleteFodaItem, value);
            }
        });
        adminValueDialog.setOnCancelButtonClickListener(new AdminValueDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                adminValueDialog.dismiss();
            }
        });
    }

    private void updateFodaItem(AthleteFodaItem athleteFodaItem, String value){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fodaItemValue",value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(GroupSportsApiService.ATHLETE_FODA_URL + athleteFodaItem.getId())
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //se actualizó correctamente
                        try {
                            if (response.getString("response").equals("Ok")){
                                updateData();
                            }
                            else {
                                Toast.makeText(context,"Error al actualizar el valor",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context,"Error de conexión",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showDeleteDialog(final AthleteFodaItem athleteFodaItem){
        final WarningDialog warningDialog = new WarningDialog(context,
                "Eliminar item FODA",
                "¿Está seguro que desea eliminar este item?\n\nSe eliminará este item permanentemente"
        );
        warningDialog.show();
        warningDialog.setOnOkButtonClickListener(new WarningDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked() {
                warningDialog.dismiss();
                deleteFodaItem(athleteFodaItem);
            }
        });
        warningDialog.setOnCancelButtonClickListener(new WarningDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                warningDialog.dismiss();
            }
        });
    }

    private void deleteFodaItem(AthleteFodaItem athleteFodaItem) {
        AndroidNetworking.delete(GroupSportsApiService.ATHLETE_FODA_URL + athleteFodaItem.getId())
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
//                                View view = getCurrentFocus();
//                                if (view != null) {
//                                    Snackbar.make(view, "Se eliminó la entrada correctamente", Snackbar.LENGTH_LONG)
//                                            .setAction("Action", null)
//                                            .show();
//                                }
                            }
                            else {
                                Toast.makeText(context,"Error al eliminar la entrada",Toast.LENGTH_LONG).show();
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

    public void updateData(){
        AndroidNetworking.get(GroupSportsApiService.ATHLETE_FODA_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        athleteFodaItems.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                athleteFodaItems.add(AthleteFodaItem.toAthleteFodaItem(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        sortFodaItemTypes();
                    }

                    @Override
                    public void onError(ANError anError) {
                        weaknessmessageTextView.setText(getResources().getString(R.string.connection_error));
                        weaknessConstraintLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void sortFodaItemTypes(){
        athleteStrengthFodaItems.clear();
        athleteWeaknessFodaItems.clear();
        athleteOportunitiesFodaItems.clear();
        athleteThreatsFodaItems.clear();

        for (int i = 0; i < athleteFodaItems.size(); i++) {
            AthleteFodaItem afi = athleteFodaItems.get(i);
            switch (afi.getFodaItemTypeId()) {
                case "1":
                    athleteStrengthFodaItems.add(afi);
                    break;
                case "2":
                    athleteWeaknessFodaItems.add(afi);
                    break;
                case "3":
                    athleteOportunitiesFodaItems.add(afi);
                    break;
                case "4":
                    athleteThreatsFodaItems.add(afi);
                    break;
            }
        }

        weaknessAthleteFodaAdapter.notifyDataSetChanged();
        threatAthleteFodaAdapter.notifyDataSetChanged();
        strengthAthleteFodaAdapter.notifyDataSetChanged();
        opportunitiesAthleteFodaAdapter.notifyDataSetChanged();
        if (athleteWeaknessFodaItems.size() == 0) { weaknessConstraintLayout.setVisibility(View.VISIBLE);}
        else { weaknessConstraintLayout.setVisibility(View.GONE); }
    }

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }
}
