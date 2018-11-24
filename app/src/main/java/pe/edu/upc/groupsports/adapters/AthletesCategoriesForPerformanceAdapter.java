package pe.edu.upc.groupsports.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.AthleteCategory;

/**
 * Created by karique on 4/05/2018.
 */

public class AthletesCategoriesForPerformanceAdapter extends RecyclerView.Adapter<AthletesCategoriesForPerformanceAdapter.AthleteCategoryViewHolder>{

    private List<AthleteCategory> athleteCategories;
    private Context c;

    public AthletesCategoriesForPerformanceAdapter() {
    }

    public AthletesCategoriesForPerformanceAdapter(List<AthleteCategory> athleteCategories) {
        this.athleteCategories = athleteCategories;
    }

    @Override
    public AthleteCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        c = parent.getContext();
        return new AthleteCategoryViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_category_for_performance, parent, false));
    }

    @Override
    public void onBindViewHolder(final AthleteCategoryViewHolder holder, int position) {
        final AthleteCategory athleteCat = athleteCategories.get(position);
        // TODO: Assign value to ImageView
        holder.categoryImageView.setImageResource(athleteCat.getPictureId());
        holder.categoryNameTextView.setText(athleteCat.getCategoryName());
        holder.ageTextView.setText(
                athleteCat.getEdadFin().equals("Mayores") ? ">= 30" : String.format("< %s", athleteCat.getEdadFin())
        );

        boolean pertenece = athleteCat.isSelected();
        holder.detailCardView.setCardBackgroundColor(pertenece ? c.getResources().getColor(R.color.colorAccent) : Color.WHITE);
        holder.detailCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean pertenece = athleteCat.isSelected();
                holder.detailCardView.setCardBackgroundColor(pertenece ? c.getResources().getColor(R.color.colorAccent) : Color.WHITE);
                deseleccionarTodo();
                athleteCat.setSelected(pertenece ? pertenece : !pertenece);
                notifyDataSetChanged();

                if (onCategorySelectedListener != null)
                    onCategorySelectedListener.categorySelected();
            }
        });
    }

    public void setSelectedAthlete(int pos){
        athleteCategories.get(pos).setSelected(true);
    }

    public int getSelectedCategoryPos(){
        for (int i = 0; i < athleteCategories.size(); i++) {
            if (athleteCategories.get(i).isSelected())
                return i;
        }
        return 0;
    }

    public void deseleccionarTodo(){
        for (int i = 0; i < athleteCategories.size(); i++) {
            athleteCategories.get(i).setSelected(false);
        }
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

    public interface OnCategorySelectedListener {
        void categorySelected();
    }

    private OnCategorySelectedListener onCategorySelectedListener;

    public void setOnCategorySelectedListener(OnCategorySelectedListener onCategorySelectedListener) {
        this.onCategorySelectedListener = onCategorySelectedListener;
    }
}
