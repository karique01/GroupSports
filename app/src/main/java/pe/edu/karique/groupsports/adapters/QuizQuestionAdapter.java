package pe.edu.karique.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.Session.SessionManager;
import pe.edu.karique.groupsports.models.AthletesQuestion;
import pe.edu.karique.groupsports.models.QuizQuestions;
import pe.edu.karique.groupsports.util.Constantes;

/**
 * Created by karique on 3/05/2018.
 */

public class QuizQuestionAdapter extends RecyclerView.Adapter<QuizQuestionAdapter.BinnacleDetailViewHolder> {
    private AthletesQuestion athletesQuestion;
    Context context;
    SessionManager sessionManager;

    public QuizQuestionAdapter() {
    }

    public QuizQuestionAdapter(AthletesQuestion athletesQuestion) {
        this.athletesQuestion = athletesQuestion;
    }

    @Override
    public BinnacleDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BinnacleDetailViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_quiz_question, parent, false));
    }

    @Override
    public void onBindViewHolder(BinnacleDetailViewHolder holder, int position) {
        final QuizQuestions quizQuestions = this.athletesQuestion.getQuizQuestions().get(position);

        holder.quizQuestionTextView.setText(quizQuestions.getQuestionTitle());

        boolean tipoAtleta = sessionManager.getuserType().equals(Constantes.USER_TYPE_ATLETA);
        Picasso.with(context)
                .load(tipoAtleta ? athletesQuestion.getPictureUrlCoach() : sessionManager.getPictureUrl())
                .placeholder(R.drawable.coach)
                .error(R.drawable.coach)
                .into(holder.coachCircleImageView);

        Picasso.with(context)
                .load(athletesQuestion.getPictureUrl())
                .placeholder(R.drawable.athlete)
                .error(R.drawable.athlete)
                .into(holder.athleteCircleImageView);


        holder.answerCardView.setVisibility(athletesQuestion.getAnsweredDate() != null ? View.VISIBLE : View.GONE);
        holder.athleteCircleImageView.setVisibility(athletesQuestion.getAnsweredDate() != null ? View.VISIBLE : View.GONE);
        if (athletesQuestion.getAnsweredDate() != null)
            holder.answerTextView.setText(quizQuestions.getAnswer());
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public AthletesQuestion getAthletesQuestion() {
        return athletesQuestion;
    }

    public void setAthletesQuestion(AthletesQuestion athletesQuestion) {
        this.athletesQuestion = athletesQuestion;
    }

    @Override
    public int getItemCount() {
        return athletesQuestion.getQuizQuestions().size();
    }

    class BinnacleDetailViewHolder extends RecyclerView.ViewHolder{
        CircleImageView coachCircleImageView;
        TextView quizQuestionTextView;
        CardView answerCardView;
        TextView answerTextView;
        CircleImageView athleteCircleImageView;
        public BinnacleDetailViewHolder(View itemView) {
            super(itemView);
            coachCircleImageView = (CircleImageView) itemView.findViewById(R.id.coachCircleImageView);
            quizQuestionTextView = (TextView) itemView.findViewById(R.id.quizQuestionTextView);
            answerCardView = (CardView) itemView.findViewById(R.id.answerCardView);
            answerTextView = (TextView) itemView.findViewById(R.id.answerTextView);
            athleteCircleImageView = (CircleImageView) itemView.findViewById(R.id.athleteCircleImageView);
        }
    }
}
