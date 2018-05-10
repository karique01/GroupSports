package pe.edu.upc.groupsports.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.Athlete;

/**
 * Created by karique on 3/05/2018.
 */

public class AthleteAssistanceAdapter extends RecyclerView.Adapter<AthleteAssistanceAdapter.AthleteViewHolder> {
    private List<Athlete> athletes;

    public AthleteAssistanceAdapter() {
    }

    public AthleteAssistanceAdapter(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    @Override
    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AthleteViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_athlete_assistance, parent, false));
    }

    @Override
    public void onBindViewHolder(AthleteViewHolder holder, int position) {
        final Athlete athlete = athletes.get(position);
        // TODO: Assign value to ImageView
        holder.nameTextView.setText(athlete.getFullName());
        holder.cellphoneTextView.setText(athlete.getCellPhone());
        holder.disciplineNameTextView.setText(athlete.getDisciplineName());
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

    class AthleteViewHolder extends RecyclerView.ViewHolder {
        ImageView athleteImageView;
        TextView nameTextView;
        TextView cellphoneTextView;
        TextView disciplineNameTextView;

        public AthleteViewHolder(View itemView) {
            super(itemView);
            athleteImageView = (ImageView) itemView.findViewById(R.id.athleteImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            cellphoneTextView = (TextView) itemView.findViewById(R.id.cellphoneTextView);
            disciplineNameTextView = (TextView) itemView.findViewById(R.id.disciplineNameTextView);
        }
    }
}
