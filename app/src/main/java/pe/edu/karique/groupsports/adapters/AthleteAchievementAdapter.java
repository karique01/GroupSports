package pe.edu.karique.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.AthleteAchievement;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class AthleteAchievementAdapter extends RecyclerView.Adapter<AthleteAchievementAdapter.AthleteAchievementViewHolder> {
    private List<AthleteAchievement> athleteAchievements;
    Context context;

    public AthleteAchievementAdapter() {
    }

    public AthleteAchievementAdapter(List<AthleteAchievement> athleteAchievements) {
        this.athleteAchievements = athleteAchievements;
    }

    @Override
    public AthleteAchievementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AthleteAchievementViewHolder(
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.card_athlete_achievement, parent, false));
    }

    @Override
    public void onBindViewHolder(final AthleteAchievementViewHolder holder, final int position) {
        final AthleteAchievement aa = athleteAchievements.get(position);

        holder.achievementTextView.setText(aa.getDescription());

        int puesto = Integer.parseInt(aa.getPlace());
        String result = aa.getAthleteAchievementTypeId().equals("1") ? aa.getResultTime().substring(0,aa.getResultTime().length() - 5) : aa.getResultDistance();
        String units = aa.getAthleteAchievementTypeId().equals("1") ? "" : "metros";
        holder.placeTimesTextView.setText(
                String.format("%s puesto \nMarca: %s %s", Funciones.toOrdinal(puesto), result, units)
        );

        holder.athleteAchievementCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onCardClickedListener != null){
                    onCardClickedListener.onCardClicked(aa);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return athleteAchievements.size();
    }

    class AthleteAchievementViewHolder extends RecyclerView.ViewHolder {
        TextView achievementTextView;
        TextView placeTimesTextView;
        CardView athleteAchievementCardView;

        public AthleteAchievementViewHolder(View itemView) {
            super(itemView);
            achievementTextView = (TextView) itemView.findViewById(R.id.achievementTextView);
            placeTimesTextView = (TextView) itemView.findViewById(R.id.placeTimesTextView);
            athleteAchievementCardView = (CardView) itemView.findViewById(R.id.athleteAchievementCardView);
        }
    }

    public interface OnCardClickedListener {
        void onCardClicked(AthleteAchievement athleteAchievement);
    }

    private OnCardClickedListener onCardClickedListener;

    public void setOnCardClickedListener(OnCardClickedListener onCardClickedListener) {
        this.onCardClickedListener = onCardClickedListener;
    }
}
