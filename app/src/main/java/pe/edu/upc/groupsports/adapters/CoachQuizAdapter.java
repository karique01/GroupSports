package pe.edu.upc.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.CoachQuiz;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class CoachQuizAdapter extends RecyclerView.Adapter<CoachQuizAdapter.CoachQuizViewHolder> {
    private List<CoachQuiz> coachQuizs;
    Context context;

    public CoachQuizAdapter() {
    }

    public CoachQuizAdapter(List<CoachQuiz> coachQuizs) {
        this.coachQuizs = coachQuizs;
    }

    @Override
    public CoachQuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new CoachQuizViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_coach_quiz, parent, false));
    }

    @Override
    public void onBindViewHolder(CoachQuizViewHolder holder, final int position) {
        final CoachQuiz coachQuiz = coachQuizs.get(position);
        // TODO: Assign value to ImageView
        holder.binnacleDetailTextView.setText(coachQuiz.getQuizTitle());
        holder.dateTextView.setText(Funciones.formatDate(coachQuiz.getDate()));
        holder.quizCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCardClickedListener != null)
                    onCardClickedListener.onCardClicked(coachQuiz);
                if (onCardClickedListenerPos != null)
                    onCardClickedListenerPos.onCardClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coachQuizs.size();
    }

    public List<CoachQuiz> getCoachQuizs() {
        return coachQuizs;
    }

    public void setCoachQuizs(List<CoachQuiz> coachQuizs) {
        this.coachQuizs = coachQuizs;
    }

    class CoachQuizViewHolder extends RecyclerView.ViewHolder{
        TextView binnacleDetailTextView;
        TextView dateTextView;
        CardView quizCardView;
        public CoachQuizViewHolder(View itemView) {
            super(itemView);
            binnacleDetailTextView = (TextView) itemView.findViewById(R.id.binnacleDetailTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            quizCardView = (CardView) itemView.findViewById(R.id.quizCardView);
        }
    }

    public interface OnCardClickedListener {
        void onCardClicked(CoachQuiz coachQuiz);
    }

    private OnCardClickedListener onCardClickedListener;

    public void setOnCardClickedListener(OnCardClickedListener onCardClickedListener) {
        this.onCardClickedListener = onCardClickedListener;
    }

    public interface OnCardClickedListenerPos {
        void onCardClicked(int pos);
    }

    private OnCardClickedListenerPos onCardClickedListenerPos;

    public void setOnCardClickedListenerPos(OnCardClickedListenerPos onCardClickedListener) {
        this.onCardClickedListenerPos = onCardClickedListener;
    }
}
























