package pe.edu.upc.groupsports.adapters;

import android.content.Intent;
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
import pe.edu.upc.groupsports.activities.AthleteDetailActivity;
import pe.edu.upc.groupsports.models.Coach;

/**
 * Created by karique on 3/05/2018.
 */

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.AthleteViewHolder> {
    private List<Coach> coaches;

    public CoachAdapter() {
    }

    public CoachAdapter(List<Coach> coaches) {
        this.coaches = coaches;
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
        final Coach coach = coaches.get(position);
        // TODO: Assign value to ImageView
        holder.nameTextView.setText(coach.getFullName());
        holder.disciplineNameTextView.setText(coach.getEmailAddress());

        Picasso.with(holder.itemView.getContext())
                .load(coach.getPictureUrl())
                .placeholder(R.drawable.coach_banner)
                .error(R.drawable.coach_banner)
                .into(holder.bigImageView);

        Picasso.with(holder.itemView.getContext())
                .load(coach.getPictureUrl())
                .placeholder(R.drawable.coach_banner)
                .error(R.drawable.coach_banner)
                .into(holder.athleteImageView);

        holder.athleteCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), AthleteDetailActivity.class);
//                intent.putExtras(coach.toBundle());
//                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coaches.size();
    }

    class AthleteViewHolder extends RecyclerView.ViewHolder{
        CircleImageView athleteImageView;
        TextView nameTextView;
        TextView disciplineNameTextView;
        CardView athleteCardView;
        ImageView bigImageView;
        public AthleteViewHolder(View itemView) {
            super(itemView);
            athleteImageView = (CircleImageView) itemView.findViewById(R.id.athleteImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            disciplineNameTextView = (TextView) itemView.findViewById(R.id.disciplineNameTextView);
            athleteCardView = (CardView) itemView.findViewById(R.id.athleteCardView);
            bigImageView = (ImageView) itemView.findViewById(R.id.bigImageView);
        }
    }
}
