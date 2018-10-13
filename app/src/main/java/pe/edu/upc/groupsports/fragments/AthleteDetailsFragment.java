package pe.edu.upc.groupsports.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.Session.SessionManager;
import pe.edu.upc.groupsports.dialogs.AdminValueDialog;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.AthleteDetails;
import pe.edu.upc.groupsports.network.GroupSportsApiService;

/**
 * A simple {@link Fragment} subclass.
 */
public class AthleteDetailsFragment extends Fragment implements View.OnClickListener{

    Context context;
    Athlete currentAthlete;
    SessionManager session;
    AthleteDetails ad;

    TextInputEditText weightTextInputEditText;
    TextInputEditText heightTextInputEditText;
    TextInputEditText bodySpanTextInputEditText;
    TextInputEditText legLengthTextInputEditText;
    TextInputEditText halfSquatToFloorTextInputEditText;
    TextInputEditText shirtSizeTextInputEditText;
    TextInputEditText pantsSizeTextInputEditText;
    TextInputEditText footwearSizeTextInputEditText;

    final int weightTextInputEditTextClicked = 0;
    final int heightTextInputEditTextClicked = 1;
    final int bodySpanTextInputEditTextClicked = 2;
    final int legLengthTextInputEditTextClicked = 3;
    final int halfSquatToFloorTextInputEditTextClicked = 4;
    final int shirtSizeTextInputEditTextClicked = 5;
    final int pantsSizeTextInputEditTextClicked = 6;
    final int footwearSizeTextInputEditTextClicked = 7;

    int currentValueClicked = 0;

    public AthleteDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_athlete_details, container, false);

        context = view.getContext();
        session = new SessionManager(context);

        weightTextInputEditText = (TextInputEditText) view.findViewById(R.id.weightTextInputEditText);
        weightTextInputEditText.setOnClickListener(this);
        heightTextInputEditText = (TextInputEditText) view.findViewById(R.id.heightTextInputEditText);
        heightTextInputEditText.setOnClickListener(this);
        bodySpanTextInputEditText = (TextInputEditText) view.findViewById(R.id.bodySpanTextInputEditText);
        bodySpanTextInputEditText.setOnClickListener(this);
        legLengthTextInputEditText = (TextInputEditText) view.findViewById(R.id.legLengthTextInputEditText);
        legLengthTextInputEditText.setOnClickListener(this);
        halfSquatToFloorTextInputEditText = (TextInputEditText) view.findViewById(R.id.halfSquatToFloorTextInputEditText);
        halfSquatToFloorTextInputEditText.setOnClickListener(this);
        shirtSizeTextInputEditText = (TextInputEditText) view.findViewById(R.id.shirtSizeTextInputEditText);
        shirtSizeTextInputEditText.setOnClickListener(this);
        pantsSizeTextInputEditText = (TextInputEditText) view.findViewById(R.id.pantsSizeTextInputEditText);
        pantsSizeTextInputEditText.setOnClickListener(this);
        footwearSizeTextInputEditText = (TextInputEditText) view.findViewById(R.id.footwearSizeTextInputEditText);
        footwearSizeTextInputEditText.setOnClickListener(this);

        getDetailsData();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.weightTextInputEditText:
                currentValueClicked = weightTextInputEditTextClicked;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.weight),false);
                break;
            case R.id.heightTextInputEditText:
                currentValueClicked = heightTextInputEditTextClicked;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.height),false);
                break;
            case R.id.bodySpanTextInputEditText:
                currentValueClicked = bodySpanTextInputEditTextClicked;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.body_span),false);
                break;
            case R.id.legLengthTextInputEditText:
                currentValueClicked = legLengthTextInputEditTextClicked;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.leg_length),false);
                break;
            case R.id.halfSquatToFloorTextInputEditText:
                currentValueClicked = halfSquatToFloorTextInputEditTextClicked;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.half_squat_to_floor),false);
                break;
            case R.id.shirtSizeTextInputEditText:
                currentValueClicked = shirtSizeTextInputEditTextClicked;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.shirt_size),true);
                break;
            case R.id.pantsSizeTextInputEditText:
                currentValueClicked = pantsSizeTextInputEditTextClicked;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.pants_size),true);
                break;
            case R.id.footwearSizeTextInputEditText:
                currentValueClicked = footwearSizeTextInputEditTextClicked;
                showAdminValueDialog(context.getString(R.string.edit),context.getString(R.string.footwear_size),true);
                break;
        }
    }

    private void showAdminValueDialog(String titleString, String editTextString, boolean inputTypeText){
        final AdminValueDialog adminValueDialog = new AdminValueDialog(context,titleString,editTextString,inputTypeText);
        adminValueDialog.show();
        adminValueDialog.setOnOkButtonClickListener(new AdminValueDialog.OnOkButtonClickListener() {
            @Override
            public void OnOkButtonClicked(String value) {
                setClickedValueNewString(value);
                updateAthleteDetails();
                adminValueDialog.dismiss();
            }
        });
        adminValueDialog.setOnCancelButtonClickListener(new AdminValueDialog.OnCancelButtonClickListener() {
            @Override
            public void OnCancelButtonClicked() {
                adminValueDialog.dismiss();
            }
        });
    }

    private void setClickedValueNewString(String value){
        switch (currentValueClicked){
            case weightTextInputEditTextClicked:
                ad.setWeight(value);
                break;
            case heightTextInputEditTextClicked:
                ad.setHeight(value);
                break;
            case bodySpanTextInputEditTextClicked:
                ad.setBodySpan(value);
                break;
            case legLengthTextInputEditTextClicked:
                ad.setLegLength(value);
                break;
            case halfSquatToFloorTextInputEditTextClicked:
                ad.setHalfSquatToFloor(value);
                break;
            case shirtSizeTextInputEditTextClicked:
                ad.setShirtSize(value);
                break;
            case pantsSizeTextInputEditTextClicked:
                ad.setPantsSize(value);
                break;
            case footwearSizeTextInputEditTextClicked:
                ad.setFootwearSize(value);
                break;
        }
    }

    public void getDetailsData(){
        AndroidNetworking.get(GroupSportsApiService.ATHLETE_DETAILS_BY_ATHLETE(currentAthlete.getId()))
                .addHeaders("Authorization", "bearer " + session.getaccess_token())
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ad = AthleteDetails.toAthleteDetails(response);
                        if (!(ad == null)) {
                            weightTextInputEditText.setText(ad.getWeight() == null ? "" : String.format("%s kg", ad.getWeight()));
                            heightTextInputEditText.setText(ad.getHeight() == null ? "" : String.format("%s m", ad.getHeight()));
                            bodySpanTextInputEditText.setText(ad.getBodySpan() == null ? "" : String.format("%s m", ad.getBodySpan()));
                            legLengthTextInputEditText.setText(ad.getLegLength() == null ? "" : String.format("%s m", ad.getLegLength()));
                            halfSquatToFloorTextInputEditText.setText(ad.getHalfSquatToFloor() == null ? "" : String.format("%s m", ad.getHalfSquatToFloor()));
                            shirtSizeTextInputEditText.setText(ad.getShirtSize() == null ? "" : ad.getShirtSize());
                            pantsSizeTextInputEditText.setText(ad.getPantsSize() == null ? "" : ad.getPantsSize());
                            footwearSizeTextInputEditText.setText(ad.getFootwearSize() == null ? "" : ad.getFootwearSize());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void updateAthleteDetails(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",ad.getId());
            jsonObject.put("weight",ad.getWeight());
            jsonObject.put("height",ad.getHeight());
            jsonObject.put("bodySpan",ad.getBodySpan());
            jsonObject.put("legLength",ad.getLegLength());
            jsonObject.put("halfSquatToFloor",ad.getHalfSquatToFloor());
            jsonObject.put("shirtSize",ad.getShirtSize());
            jsonObject.put("pantsSize",ad.getPantsSize());
            jsonObject.put("footwearSize",ad.getFootwearSize());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.put(GroupSportsApiService.ATHLETE_DETAILS_URL+ad.getId())
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
                                getDetailsData();
                                if (onDetailEditedListener != null){
                                    onDetailEditedListener.OnDetailEdited();
                                }
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

    public Athlete getCurrentAthlete() {
        return currentAthlete;
    }

    public void setCurrentAthlete(Athlete currentAthlete) {
        this.currentAthlete = currentAthlete;
    }

    public interface OnDetailEditedListener {
        void OnDetailEdited();
    }

    OnDetailEditedListener onDetailEditedListener;

    public void setOnDetailEditedListener(OnDetailEditedListener onDetailEditedListener) {
        this.onDetailEditedListener = onDetailEditedListener;
    }
}
