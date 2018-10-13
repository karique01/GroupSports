package pe.edu.upc.groupsports.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.Mood;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {
    private List<Mood> moods;

    public MoodAdapter() {
    }

    public MoodAdapter(List<Mood> moods) {
        this.moods = moods;
    }

    @Override
    public MoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoodViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_athlete_latest_moods, parent, false));
    }

    @Override
    public void onBindViewHolder(final MoodViewHolder holder, final int position) {
        final Mood mood = moods.get(position);
        holder.dateTextView.setText(Funciones.formatDate(mood.getDayOfMood()));
        holder.hourTextView.setText(Funciones.formatDateToHour(mood.getDayOfMood()));

        boolean pertenece = mood.isSelected();
        holder.cardImageView.setImageResource(pertenece ? mood.getCardDrawableResource() : R.drawable.ic_eye_black);
        holder.moodCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean pertenece = mood.isSelected();
                holder.cardImageView.setImageResource(pertenece ? mood.getCardDrawableResource() : R.drawable.ic_eye_black);
                mood.setSelected(!pertenece);
                notifyItemChanged(position);
            }
        });
        holder.moodCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(mood);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return moods.size();
    }

    public List<Mood> getMoods() {
        return moods;
    }

    public void setMoods(List<Mood> moods) {
        this.moods = moods;
    }

    class MoodViewHolder extends RecyclerView.ViewHolder{
        CardView moodCardView;
        ImageView cardImageView;
        TextView dateTextView;
        TextView hourTextView;
        public MoodViewHolder(View itemView) {
            super(itemView);
            moodCardView = (CardView) itemView.findViewById(R.id.moodCardView);
            cardImageView = (ImageView) itemView.findViewById(R.id.cardImageView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            hourTextView = (TextView) itemView.findViewById(R.id.hourTextView);
        }
    }

    public interface OnLongClickListener {
        void OnLongClicked(Mood mood);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
