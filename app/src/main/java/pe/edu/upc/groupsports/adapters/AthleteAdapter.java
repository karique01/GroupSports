package pe.edu.upc.groupsports.adapters;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.activities.AthleteDeatilActivity;
import pe.edu.upc.groupsports.models.Athlete;

/**
 * Created by karique on 3/05/2018.
 */

public class AthleteAdapter extends RecyclerView.Adapter<AthleteAdapter.AthleteViewHolder> {
    private List<Athlete> athletes;

    public AthleteAdapter() {
    }

    public AthleteAdapter(List<Athlete> athletes) {
        this.athletes = athletes;
    }

    @Override
    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AthleteViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_athlete, parent, false));
    }

    @Override
    public void onBindViewHolder(AthleteViewHolder holder, int position) {
        final Athlete athlete = athletes.get(position);
        // TODO: Assign value to ImageView
        holder.nameTextView.setText(athlete.getFullName());
        holder.cellphoneTextView.setText(athlete.getCellPhone());
        holder.disciplineNameTextView.setText(athlete.getDisciplineName());

        Picasso.with(holder.itemView.getContext())
                .load(athlete.getPictureUrl())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(holder.athleteImageView);

        holder.cardConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), AthleteDeatilActivity.class));
            }
        });
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
        ConstraintLayout cardConstraintLayout;
        CircleImageView athleteImageView;
        TextView nameTextView;
        TextView cellphoneTextView;
        TextView disciplineNameTextView;
        public AthleteViewHolder(View itemView) {
            super(itemView);
            cardConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.cardConstraintLayout);
            athleteImageView = (CircleImageView) itemView.findViewById(R.id.athleteImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            cellphoneTextView = (TextView) itemView.findViewById(R.id.cellphoneTextView);
            disciplineNameTextView = (TextView) itemView.findViewById(R.id.disciplineNameTextView);
        }
    }
}
