package pe.edu.karique.groupsports.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.Team;

/**
 * Created by karique on 3/05/2018.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<Team> athletes;

    public TeamAdapter() {
    }

    public TeamAdapter(List<Team> athletes) {
        this.athletes = athletes;
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeamViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_team, parent, false));
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        final Team athlete = athletes.get(position);
        // TODO: Assign value to ImageView
        holder.nameTextView.setText(athlete.getTeamName());
    }

    @Override
    public int getItemCount() {
        return athletes.size();
    }

    public List<Team> getAthletes() {
        return athletes;
    }

    public void setAthletes(List<Team> athletes) {
        this.athletes = athletes;
    }

    class TeamViewHolder extends RecyclerView.ViewHolder{
        ImageView teamImageView;
        TextView nameTextView;
        public TeamViewHolder(View itemView) {
            super(itemView);
            teamImageView = (ImageView) itemView.findViewById(R.id.teamImageView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
        }
    }
}
