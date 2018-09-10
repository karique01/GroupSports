package pe.edu.upc.groupsports.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.AthletesQuestion;

/**
 * Created by karique on 3/05/2018.
 */

public class ChooseAthleteQuizAdapter extends RecyclerView.Adapter<ChooseAthleteQuizAdapter.AthleteViewHolder> {
    private List<AthletesQuestion> athletesQuestions;
    Context c;

    public ChooseAthleteQuizAdapter() {
    }

    public ChooseAthleteQuizAdapter(List<AthletesQuestion> athletesQuestions) {
        this.athletesQuestions = athletesQuestions;
    }

    @Override
    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        c = parent.getContext();
        return new AthleteViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_choose_athlete_quiz, parent, false));
    }

    @Override
    public void onBindViewHolder(final AthleteViewHolder holder, final int position) {
        final AthletesQuestion athlete = athletesQuestions.get(position);
        holder.athleteNameTextView.setText(athlete.getUserName());

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
                onAthleteSelectedListener.OnAthleteSelected(position);
            }
        });

        Picasso.with(c)
                .load(athlete.getPictureUrl())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(holder.athleteCircleImageView);
    }

    private void deseleccionarTodo(){
        for (int i = 0; i < athletesQuestions.size(); i++) {
            athletesQuestions.get(i).setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return athletesQuestions.size();
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

    public interface OnAthleteSelectedListener {
        void OnAthleteSelected(int athletePos);
    }

    private OnAthleteSelectedListener onAthleteSelectedListener;

    public void setOnAthleteSelectedListener(OnAthleteSelectedListener onAthleteSelectedListener) {
        this.onAthleteSelectedListener = onAthleteSelectedListener;
    }
}
















