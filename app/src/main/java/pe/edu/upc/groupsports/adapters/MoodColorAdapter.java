package pe.edu.upc.groupsports.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.Mood;
import pe.edu.upc.groupsports.models.MoodColor;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class MoodColorAdapter extends RecyclerView.Adapter<MoodColorAdapter.MoodColorViewHolder> {
    private List<MoodColor> moodColors;

    public MoodColorAdapter() {
    }

    public MoodColorAdapter(List<MoodColor> moodColors) {
        this.moodColors = moodColors;
    }

    @Override
    public MoodColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoodColorViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_mood_color, parent, false));
    }

    @Override
    public void onBindViewHolder(final MoodColorViewHolder holder, int position) {
        final MoodColor moodColor = moodColors.get(position);
        holder.cardImageView.setImageResource(moodColor.getCardDrawableResource());

        boolean pertenece = moodColor.isSelected();
        holder.selectedCardView.setVisibility(pertenece ? View.VISIBLE : View.INVISIBLE);
        holder.imageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean pertenece = moodColor.isSelected();
                holder.selectedCardView.setVisibility(pertenece ? View.VISIBLE : View.INVISIBLE);
                deseleccionarTodo();
                moodColor.setSelected(!pertenece);
                notifyDataSetChanged();
            }
        });
    }

    public MoodColor getSelected(){
        for (int i = 0; i < moodColors.size(); i++) {
            if (moodColors.get(i).isSelected())
                return moodColors.get(i);
        }
        return null;
    }

    private void deseleccionarTodo(){
        for (int i = 0; i < moodColors.size(); i++) {
            moodColors.get(i).setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return moodColors.size();
    }

    public List<MoodColor> getMoodsMoodColors() {
        return moodColors;
    }

    public void setMoodColors(List<MoodColor> moodColors) {
        this.moodColors = moodColors;
    }

    class MoodColorViewHolder extends RecyclerView.ViewHolder{
        CardView imageCardView;
        ImageView cardImageView;
        View selectedCardView;
        public MoodColorViewHolder(View itemView) {
            super(itemView);
            imageCardView = (CardView) itemView.findViewById(R.id.imageCardView);
            cardImageView = (ImageView) itemView.findViewById(R.id.cardImageView);
            selectedCardView = itemView.findViewById(R.id.selectedCardView);
        }
    }
}
