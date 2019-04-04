package pe.edu.karique.groupsports.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.activities.AthletesByCategoryActivity;
import pe.edu.karique.groupsports.models.AthleteCategory;

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
        holder.ageTextView.setText(
                athlete.getEdadFin().equals("Mayores") ? ">= 30" : String.format("< %s", athlete.getEdadFin())
        );

        holder.detailCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AthletesByCategoryActivity.categoria = athlete.getCategoryApi();
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
        CircleImageView categoryImageView;
        TextView categoryNameTextView;
        TextView ageTextView;
        CardView detailCardView;
        public AthleteCategoryViewHolder(View itemView) {
            super(itemView);
            categoryImageView = (CircleImageView) itemView.findViewById(R.id.categoryImageView);
            categoryNameTextView = (TextView) itemView.findViewById(R.id.categoryNameTextView);
            ageTextView = (TextView) itemView.findViewById(R.id.ageTextView);
            detailCardView = (CardView) itemView.findViewById(R.id.detailCardView);
        }
    }
}
