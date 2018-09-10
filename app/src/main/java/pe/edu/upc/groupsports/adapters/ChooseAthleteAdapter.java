package pe.edu.upc.groupsports.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.Athlete;

/**
 * Created by karique on 3/05/2018.
 */

public class ChooseAthleteAdapter extends RecyclerView.Adapter<ChooseAthleteAdapter.AthleteViewHolder> {
    private List<Athlete> athletes;

    public ChooseAthleteAdapter() {
    }

    public ChooseAthleteAdapter(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    @Override
    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AthleteViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_choose_athlete, parent, false));
    }

    @Override
    public void onBindViewHolder(final AthleteViewHolder holder, final int position) {
        final Context c = holder.itemView.getContext();
        final Athlete athlete = athletes.get(position);
        holder.athleteNameTextView.setText(athlete.getFullName());

        boolean pertenece = athlete.isSelected();
        holder.chooseAthletePlanCardView.setCardBackgroundColor(pertenece ? c.getResources().getColor(R.color.colorAccent) : Color.WHITE);
        holder.chooseAthletePlanCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean pertenece = athlete.isSelected();
                holder.chooseAthletePlanCardView.setCardBackgroundColor(pertenece ? c.getResources().getColor(R.color.colorAccent) : Color.WHITE);
                deseleccionarTodo();
                athlete.setSelected(!pertenece);
                notifyDataSetChanged();
            }
        });

        Picasso.with(holder.itemView.getContext())
                .load(athlete.getPictureUrl())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(holder.athleteCircleImageView);
    }

    public Athlete getSelectedAthlete(){
        for (int i = 0; i < athletes.size(); i++) {
            if (athletes.get(i).isSelected())
                return athletes.get(i);
        }
        return null;
    }

    private void deseleccionarTodo(){
        for (int i = 0; i < athletes.size(); i++) {
            athletes.get(i).setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return athletes.size();
    }

    public List<Athlete> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    class AthleteViewHolder extends RecyclerView.ViewHolder{
        CircleImageView athleteCircleImageView;
        TextView athleteNameTextView;
        CardView chooseAthletePlanCardView;
        public AthleteViewHolder(View itemView) {
            super(itemView);
            athleteCircleImageView = (CircleImageView) itemView.findViewById(R.id.athleteCircleImageView);
            athleteNameTextView = (TextView) itemView.findViewById(R.id.athleteNameTextView);
            chooseAthletePlanCardView = (CardView) itemView.findViewById(R.id.chooseAthletePlanCardView);
        }
    }
}
