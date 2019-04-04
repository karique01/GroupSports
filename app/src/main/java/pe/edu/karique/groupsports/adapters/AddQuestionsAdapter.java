package pe.edu.karique.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.libizo.CustomEditText;

import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.QuizQuestion;
import pe.edu.karique.groupsports.repositories.QuizQuestionsRepository;

/**
 * Created by karique on 3/05/2018.
 */

public class AddQuestionsAdapter extends RecyclerView.Adapter<AddQuestionsAdapter.AthleteViewHolder> {
    //private List<QuizQuestion> quizQuestions;
    Context context;

    public AddQuestionsAdapter() {
    }

    public AddQuestionsAdapter(List<QuizQuestion> quizQuestions) {
        //this.quizQuestions = quizQuestions;
    }

    @Override
    public AthleteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AthleteViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_coach_question, parent, false));
    }

    @Override
    public void onBindViewHolder(final AthleteViewHolder holder, final int position) {
        final QuizQuestion quizQuestion = QuizQuestionsRepository.getInstance().getQQ(position);
        holder.detailCustomEditText.setText(quizQuestion.getQuestionTitle());
        holder.detailCustomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int gap = holder.getAdapterPosition();
                QuizQuestion qq = QuizQuestionsRepository.getInstance().getQQ(gap);
                qq.setQuestionTitle(holder.detailCustomEditText.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizQuestionsRepository.getInstance().delete(position);
                notifyItemRangeRemoved(0, QuizQuestionsRepository.getInstance().size() + 1);
                notifyItemRangeInserted(0, QuizQuestionsRepository.getInstance().size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return QuizQuestionsRepository.getInstance().size();
    }

    class AthleteViewHolder extends RecyclerView.ViewHolder{
        CustomEditText detailCustomEditText;
        ImageButton deleteImageButton;

        public AthleteViewHolder(View itemView) {
            super(itemView);
            detailCustomEditText = (CustomEditText) itemView.findViewById(R.id.detailCustomEditText);
            deleteImageButton = (ImageButton) itemView.findViewById(R.id.deleteImageButton);
        }
    }
}
