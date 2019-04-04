package pe.edu.karique.groupsports.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.activities.MesocycleDetailActivity;
import pe.edu.karique.groupsports.models.Mesocycle;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class MesocyclesAdapter extends RecyclerView.Adapter<MesocyclesAdapter.MesocycleViewHolder> {
    private List<Mesocycle> mesocycles;
    private String pictureUrl;
    private String athleteId;
    private Context context;

    public MesocyclesAdapter() {
    }

    public MesocyclesAdapter(List<Mesocycle> mesocycles) {
        this.mesocycles = mesocycles;
    }

    @Override
    public MesocycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MesocycleViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_mesocycle, parent, false));
    }

    @Override
    public void onBindViewHolder(MesocycleViewHolder holder, int position) {
        final Mesocycle mesocycle = mesocycles.get(position);
        Context c = holder.itemView.getContext();

        if (!(mesocycle.getStartDate() == null)) {
            Date today = Funciones.getCurrentDate();

            boolean contieneMC = today.after(mesocycle.getStartDate()) ||
                                 Funciones.equalDates(today,mesocycle.getStartDate());

            boolean contieneMCDown = today.after(mesocycle.getEndDate()) ||
                                     Funciones.equalDates(today,mesocycle.getEndDate());

            int colorToSet = c.getResources().getColor(R.color.colorAccent);
            int colorNotHold = c.getResources().getColor(R.color.colorMyGray);

            holder.updView.setBackgroundColor(contieneMC ? colorToSet : colorNotHold);
            holder.downdView.setBackgroundColor(contieneMCDown ? colorToSet : colorNotHold);
            holder.mesocyclePointImageView.setImageResource(contieneMC ? R.drawable.ic_current_mesocycle : R.drawable.ic_future_mesocycle);
        }

        if (position == mesocycles.size() - 1)
            holder.downdView.setVisibility(View.GONE);
        else
            holder.downdView.setVisibility(View.VISIBLE);

        holder.mesocycleNameTextView.setText(mesocycle.getMesocycleName());
        holder.rangeDateTextView.setText(String.format("%s - %s", Funciones.formatDate(mesocycle.getStartDate()), Funciones.formatDate(mesocycle.getEndDate())));
        holder.ciclajeTextView.setText(String.format("Ciclaje %s:%s", mesocycle.getNumberOfIntenseWeeks(), mesocycle.getNumberOfRestWeeks()));

        holder.mesocycleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intentMC = new Intent(context, MesocycleDetailActivity.class);
                intentMC.putExtra("pictureUrl", getPictureUrl());
                intentMC.putExtra("athleteId", getAthleteId());
                intentMC.putExtras(mesocycle.toBundle());
                ((Activity)context).startActivityForResult(intentMC,MesocycleDetailActivity.REQUEST_FOR_ACTIVITY_CODE_MESOCYCLE_DETAIL);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mesocycles.size();
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public List<Mesocycle> getMesocycles() {
        return mesocycles;
    }

    public void setAthletes(List<Mesocycle> mesocycles) {
        this.mesocycles = mesocycles;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    class MesocycleViewHolder extends RecyclerView.ViewHolder{
        View updView;
        View downdView;
        ImageView mesocyclePointImageView;
        CardView mesocycleCardView;
        TextView mesocycleNameTextView;
        TextView rangeDateTextView;
        TextView ciclajeTextView;
        public MesocycleViewHolder(View itemView) {
            super(itemView);
            updView = (View) itemView.findViewById(R.id.updView);
            downdView = (View) itemView.findViewById(R.id.downdView);
            mesocyclePointImageView = (ImageView) itemView.findViewById(R.id.mesocyclePointImageView);
            mesocycleCardView = (CardView) itemView.findViewById(R.id.mesocycleCardView);
            mesocycleNameTextView = (TextView) itemView.findViewById(R.id.mesocycleNameTextView);
            rangeDateTextView = (TextView) itemView.findViewById(R.id.rangeDateTextView);
            ciclajeTextView = (TextView) itemView.findViewById(R.id.ciclajeTextView);
        }
    }
}
