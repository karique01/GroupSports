package pe.edu.upc.groupsports.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.activities.AthleteDetailActivity;
import pe.edu.upc.groupsports.models.AthleteSpeedPerformance;
import pe.edu.upc.groupsports.models.Coach;

/**
 * Created by karique on 3/05/2018.
 */

public class AthleteSpeedPerformancesAdapter extends RecyclerView.Adapter<AthleteSpeedPerformancesAdapter.AthleteViewHolder> {
    private List<AthleteSpeedPerformance> athleteSpeedPerformances;
    private boolean mediaSelected = true;

    public AthleteSpeedPerformancesAdapter() {
    }

    public AthleteSpeedPerformancesAdapter(List<AthleteSpeedPerformance> athleteSpeedPerformances) {
        this.athleteSpeedPerformances = athleteSpeedPerformances;
    }

    @Override
    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AthleteViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_athlete_speed_performance, parent, false));
    }

    @Override
    public void onBindViewHolder(AthleteViewHolder holder, int position) {
        final AthleteSpeedPerformance asp = athleteSpeedPerformances.get(position);
        // TODO: Assign value to ImageView
        holder.nameTextView.setText(asp.getAthleteName());
        holder.usernameTextView.setText(asp.getUsername());

        holder.mediaTextView.setText(
                String.format("%s:%s:%s,%s",
                        String.format(Locale.getDefault(),"%02d", asp.getAverageHours()),
                        String.format(Locale.getDefault(),"%02d", asp.getAverageMinutes()),
                        String.format(Locale.getDefault(),"%02d", asp.getAverageSeconds()),
                        String.format(Locale.getDefault(),"%03d", asp.getAverageMilliseconds())
                )
        );
        holder.bestMarkTextView.setText(
                String.format("%s:%s:%s,%s",
                        String.format(Locale.getDefault(),"%02d", asp.getBestMarkHour()),
                        String.format(Locale.getDefault(),"%02d", asp.getBestMarkMinute()),
                        String.format(Locale.getDefault(),"%02d", asp.getBestMarkSecond()),
                        String.format(Locale.getDefault(),"%03d", asp.getBestMarkMillisecond())
                )
        );

        if (mediaSelected){
            holder.mediaTextView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.bestMarkTextView.setTypeface(Typeface.DEFAULT);

            holder.bestMarkTextView.setVisibility(View.GONE);
            holder.starImageView.setVisibility(View.GONE);
            holder.mediaTextView.setVisibility(View.VISIBLE);
            holder.mediaImageView.setVisibility(View.VISIBLE);
        }
        else {
            holder.bestMarkTextView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.mediaTextView.setTypeface(Typeface.DEFAULT);

            holder.mediaTextView.setVisibility(View.GONE);
            holder.mediaImageView.setVisibility(View.GONE);
            holder.bestMarkTextView.setVisibility(View.VISIBLE);
            holder.starImageView.setVisibility(View.VISIBLE);
        }

        Picasso.with(holder.itemView.getContext())
                .load(asp.getAthletePictureUrl())
                .placeholder(R.drawable.coach_banner)
                .error(R.drawable.coach_banner)
                .into(holder.athleteCircleImageView);

        holder.athleteSpeedPerformanceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), AthleteDetailActivity.class);
//                intent.putExtras(coach.toBundle());
//                view.getContext().startActivity(intent);
            }
        });
    }

    public void sort(){
        if (mediaSelected){
            sortByMedia();
        }
        else {
            sortByBestMark();
        }
    }

    public void sortByMedia(){
        for (int i = 0; i < athleteSpeedPerformances.size() - 1; i++) {
            for (int j = i + 1; j < athleteSpeedPerformances.size(); j++) {
                if (athleteSpeedPerformances.get(j).getAverageResultFloat() < athleteSpeedPerformances.get(i).getAverageResultFloat()){
                    AthleteSpeedPerformance asp = athleteSpeedPerformances.get(i);
                    athleteSpeedPerformances.set(i,athleteSpeedPerformances.get(j));
                    athleteSpeedPerformances.set(j,asp);
                }
            }
        }
        mediaSelected = true;
        notifyDataSetChanged();
    }

    public void sortByBestMark(){
        for (int i = 0; i < athleteSpeedPerformances.size() - 1; i++) {
            for (int j = i + 1; j < athleteSpeedPerformances.size(); j++) {
                if (athleteSpeedPerformances.get(j).getBestMArkResultFloat() < athleteSpeedPerformances.get(i).getBestMArkResultFloat()){
                    AthleteSpeedPerformance asp = athleteSpeedPerformances.get(i);
                    athleteSpeedPerformances.set(i,athleteSpeedPerformances.get(j));
                    athleteSpeedPerformances.set(j,asp);
                }
            }
        }
        mediaSelected = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return athleteSpeedPerformances.size();
    }

    class AthleteViewHolder extends RecyclerView.ViewHolder{
        CircleImageView athleteCircleImageView;
        CardView athleteSpeedPerformanceCardView;
        TextView nameTextView;
        TextView usernameTextView;
        TextView mediaTextView;
        TextView bestMarkTextView;

        ImageView mediaImageView;
        ImageView starImageView;
        public AthleteViewHolder(View itemView) {
            super(itemView);
            athleteCircleImageView = (CircleImageView) itemView.findViewById(R.id.athleteCircleImageView);
            athleteSpeedPerformanceCardView = (CardView) itemView.findViewById(R.id.athleteSpeedPerformanceCardView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            usernameTextView = (TextView) itemView.findViewById(R.id.usernameTextView);
            mediaTextView = (TextView) itemView.findViewById(R.id.mediaTextView);
            bestMarkTextView = (TextView) itemView.findViewById(R.id.bestMarkTextView);

            mediaImageView = (ImageView) itemView.findViewById(R.id.mediaImageView);
            starImageView = (ImageView) itemView.findViewById(R.id.starImageView);
        }
    }
}
