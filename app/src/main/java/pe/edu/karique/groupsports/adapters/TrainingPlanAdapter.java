package pe.edu.karique.groupsports.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.activities.TrainingPlanDetailActivity;
import pe.edu.karique.groupsports.models.TrainingPlan;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class TrainingPlanAdapter extends RecyclerView.Adapter<TrainingPlanAdapter.TrainingPlanViewHolder> {
    private List<TrainingPlan> trainingPlans;
    Context context;

    public TrainingPlanAdapter() {
    }

    public TrainingPlanAdapter(List<TrainingPlan> trainingPlans) {
        this.trainingPlans = trainingPlans;
    }

    @Override
    public TrainingPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new TrainingPlanViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_training_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(TrainingPlanViewHolder holder, int position) {
        final TrainingPlan trainingPlan = trainingPlans.get(position);
        // TODO: Assign value to ImageView
        holder.nameTrainingPlan.setText(trainingPlan.getName());
        holder.datesTrainingPlanTextView.setText(String.format("%s - %s", Funciones.formatDate(trainingPlan.getStartDate()), Funciones.formatDate(trainingPlan.getEndDate())));
        holder.athleteNameTextView.setText(trainingPlan.getAthleteFullName());

        Picasso.with(holder.itemView.getContext())
                .load(trainingPlan.getAthletePictureUrl())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(holder.athleteCircleImageView);

        holder.trainingPlanCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intentTP = new Intent(context, TrainingPlanDetailActivity.class);
                intentTP.putExtras(trainingPlan.toBundle());
                ((Activity)context).startActivityForResult(intentTP, TrainingPlanDetailActivity.REQUEST_FOR_ACTIVITY_CODE_TRAINING_PLAN_DETAIL);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trainingPlans.size();
    }

    public List<TrainingPlan> getTrainingPlans() {
        return trainingPlans;
    }

    public void setTrainingPlans(List<TrainingPlan> trainingPlans) {
        this.trainingPlans = trainingPlans;
    }

    class TrainingPlanViewHolder extends RecyclerView.ViewHolder{
        TextView nameTrainingPlan;
        TextView datesTrainingPlanTextView;
        CircleImageView athleteCircleImageView;
        TextView athleteNameTextView;
        CardView trainingPlanCardView;
        public TrainingPlanViewHolder(View itemView) {
            super(itemView);
            nameTrainingPlan = (TextView) itemView.findViewById(R.id.nameTrainingPlan);
            datesTrainingPlanTextView = (TextView) itemView.findViewById(R.id.datesTrainingPlanTextView);
            athleteCircleImageView = (CircleImageView) itemView.findViewById(R.id.athleteCircleImageView);
            athleteNameTextView = (TextView) itemView.findViewById(R.id.athleteNameTextView);
            trainingPlanCardView = (CardView) itemView.findViewById(R.id.trainingPlanCardView);
        }
    }
}
