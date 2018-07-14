package pe.edu.upc.groupsports.adapters;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.activities.AthletesByCategoryActivity;
import pe.edu.upc.groupsports.models.AthleteCategory;

/**
 * Created by karique on 4/05/2018.
 */

public class AthletesCategoriesAdapter extends RecyclerView.Adapter<AthletesCategoriesAdapter.AthleteCategoryViewHolder>{

    private List<AthleteCategory> athleteCategories;

    public AthletesCategoriesAdapter() {
    }

    public AthletesCategoriesAdapter(List<AthleteCategory> athleteCategories) {
        this.athleteCategories = athleteCategories;
    }

    @Override
    public AthleteCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AthleteCategoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_category, parent, false));
    }

    @Override
    public void onBindViewHolder(AthleteCategoryViewHolder holder, int position) {
        final AthleteCategory athlete = athleteCategories.get(position);
        // TODO: Assign value to ImageView
        holder.categoryImageView.setImageResource(athlete.getPictureId());
        holder.categoryNameTextView.setText(athlete.getCategoryName());
        holder.startAgeTextView.setText(athlete.getEdadInicio());
        holder.endAgeTextView.setText(athlete.getEdadFin());

        holder.detailCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AthletesByCategoryActivity.categoria = athlete.getCategoryName();
                view.getContext()
                        .startActivity(
                                new Intent(
                                        view.getContext(),
                                        AthletesByCategoryActivity.class
                                )
                        );
            }
        });
    }

    @Override
    public int getItemCount() {
        return athleteCategories.size();
    }

    class AthleteCategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImageView;
        TextView categoryNameTextView;
        TextView startAgeTextView;
        TextView endAgeTextView;
        CardView detailCardView;
        public AthleteCategoryViewHolder(View itemView) {
            super(itemView);
            categoryImageView = (ImageView) itemView.findViewById(R.id.categoryImageView);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.categoryNameTextView);
            startAgeTextView = (TextView) itemView.findViewById(R.id.startAgeTextView);
            endAgeTextView = (TextView) itemView.findViewById(R.id.endAgeTextView);
            detailCardView = (CardView) itemView.findViewById(R.id.detailCardView);
        }
    }
}
