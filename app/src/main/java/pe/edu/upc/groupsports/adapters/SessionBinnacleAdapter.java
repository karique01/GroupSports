package pe.edu.upc.groupsports.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.activities.BinnacleActivity;
import pe.edu.upc.groupsports.activities.WeightTestBySessionActivity;
import pe.edu.upc.groupsports.models.SessionWork;

/**
 * Created by karique on 3/05/2018.
 */

public class SessionBinnacleAdapter extends RecyclerView.Adapter<SessionBinnacleAdapter.BinnacleDetailViewHolder> {
    private List<SessionWork> sessionWorks;
    Context context;

    public SessionBinnacleAdapter() {
    }

    public SessionBinnacleAdapter(List<SessionWork> sessionWorks) {
        this.sessionWorks = sessionWorks;
    }

    @Override
    public BinnacleDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BinnacleDetailViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_binnacle_work_session, parent, false));
    }

    @Override
    public void onBindViewHolder(BinnacleDetailViewHolder holder, int position) {
        final SessionWork sessionWork = sessionWorks.get(position);
        // TODO: Assign value to ImageView
        holder.trainingPlanNameTextView.setText(sessionWork.getTrainingPlanName());
        holder.mesocycleNameTextView.setText(String.format("Mesociclo: %s", sessionWork.getMesocycleName()));
        holder.shiftTextView.setText(String.format("Turno: %s", sessionWork.getShiftName()));
        holder.athleteNameTextView.setText(sessionWork.getUsername());
        holder.seeBinnacleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,BinnacleActivity.class);
                intent.putExtras(sessionWork.toBundle());
                ((Activity)context).startActivityForResult(
                        intent, BinnacleActivity.REQUEST_FOR_ACTIVITY_CODE_BINNACLE_DETAIL
                );
            }
        });
        holder.weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,WeightTestBySessionActivity.class);
                intent.putExtras(sessionWork.toBundle());
                ((Activity)context).startActivityForResult(
                        intent, WeightTestBySessionActivity.REQUEST_FOR_ACTIVITY_CODE_WEIGHT_TEST_BY_SESSSION
                );
            }
        });
        Picasso.with(context)
                .load(sessionWork.getPictureUrl())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(holder.athleteCircleImageView);

        int size = sessionWork.getBinnacleDetails().size();
        holder.seeBinnacleButton.setText(String.format("Ver Bitacora %s", (size > 0) ? ("(" + size + " entradas)") : ("")));
    }

    @Override
    public int getItemCount() {
        return sessionWorks.size();
    }

    class BinnacleDetailViewHolder extends RecyclerView.ViewHolder{
        CircleImageView athleteCircleImageView;
        TextView trainingPlanNameTextView;
        TextView mesocycleNameTextView;
        TextView shiftTextView;
        TextView athleteNameTextView;
        Button seeBinnacleButton;
        Button weightButton;
        public BinnacleDetailViewHolder(View itemView) {
            super(itemView);
            athleteCircleImageView = (CircleImageView) itemView.findViewById(R.id.athleteCircleImageView);
            trainingPlanNameTextView = (TextView) itemView.findViewById(R.id.trainingPlanNameTextView);
            mesocycleNameTextView = (TextView) itemView.findViewById(R.id.mesocycleNameTextView);
            shiftTextView = (TextView) itemView.findViewById(R.id.shiftTextView);
            athleteNameTextView = (TextView) itemView.findViewById(R.id.athleteNameTextView);
            seeBinnacleButton = (Button) itemView.findViewById(R.id.seeBinnacleButton);
            weightButton = (Button) itemView.findViewById(R.id.weightButton);
        }
    }
}
