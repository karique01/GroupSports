package pe.edu.karique.groupsports.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.activities.WeekDetailActivity;
import pe.edu.karique.groupsports.models.Week;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class WeeksAdapter extends RecyclerView.Adapter<WeeksAdapter.WeekViewHolder> {
    private List<Week> weeks;
    Context context;
    private String pictureUrl;
    private String athleteId;

    public WeeksAdapter() {
    }

    public WeeksAdapter(List<Week> weeks) {
        this.weeks = weeks;
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new WeekViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_week, parent, false));
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {
        final Week week = weeks.get(position);
        Context c = holder.itemView.getContext();

        if (!(week.getStartDate() == null)) {
            Date today = Funciones.getCurrentDate();

            boolean contieneMC = today.after(week.getStartDate()) ||
                                 Funciones.equalDates(today,week.getStartDate());

            boolean contieneMCDown = today.after(week.getEndDate()) ||
                                     Funciones.equalDates(today,week.getEndDate());

            int colorToSet = c.getResources().getColor(R.color.colorAccent);
            int colorNotHold = c.getResources().getColor(R.color.colorMyGray);

            holder.updView.setBackgroundColor(contieneMC ? colorToSet : colorNotHold);
            holder.downdView.setBackgroundColor(contieneMCDown ? colorToSet : colorNotHold);
            holder.weekPointImageView.setImageResource(contieneMC ? R.drawable.ic_current_mesocycle : R.drawable.ic_future_mesocycle);
        }

        if (position == weeks.size() - 1)
            holder.downdView.setVisibility(View.GONE);
        else
            holder.downdView.setVisibility(View.VISIBLE);

        holder.activityTextView.setText(week.getActivity());
        holder.rangeDateTextView.setText(String.format("%s - %s", Funciones.formatDate(week.getStartDate()), Funciones.formatDate(week.getEndDate())));
        holder.descriptionTextView.setText(week.getDescription());

        holder.weekCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intentWeek = new Intent(context, WeekDetailActivity.class);
                intentWeek.putExtra("pictureUrl", getPictureUrl());
                intentWeek.putExtra("athleteId", getAthleteId());
                intentWeek.putExtras(week.toBundle());
                ((Activity)context).startActivityForResult(intentWeek,WeekDetailActivity.REQUEST_FOR_ACTIVITY_CODE_WEEK_DETAIL);
            }
        });
    }

    public String getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(String athleteId) {
        this.athleteId = athleteId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public int getItemCount() {
        return weeks.size();
    }

    public List<Week> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Week> weeks) {
        this.weeks = weeks;
    }

    class WeekViewHolder extends RecyclerView.ViewHolder{
        View updView;
        View downdView;
        ImageView weekPointImageView;
        CardView weekCardView;
        TextView activityTextView;
        TextView rangeDateTextView;
        TextView descriptionTextView;
        public WeekViewHolder(View itemView) {
            super(itemView);
            updView = (View) itemView.findViewById(R.id.updView);
            downdView = (View) itemView.findViewById(R.id.downdView);
            weekPointImageView = (ImageView) itemView.findViewById(R.id.weekPointImageView);
            weekCardView = (CardView) itemView.findViewById(R.id.weekCardView);
            activityTextView = (TextView) itemView.findViewById(R.id.activityTextView);
            rangeDateTextView = (TextView) itemView.findViewById(R.id.rangeDateTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
