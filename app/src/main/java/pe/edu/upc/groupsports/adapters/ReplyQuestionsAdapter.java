package pe.edu.upc.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libizo.CustomEditText;

import org.json.JSONArray;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.QuizQuestionByAthlete;
import pe.edu.upc.groupsports.repositories.QuizQuestionsRepository;

/**
 * Created by karique on 3/05/2018.
 */

public class ReplyQuestionsAdapter extends RecyclerView.Adapter<ReplyQuestionsAdapter.ReplyQuestionViewHolder> {
    private List<QuizQuestionByAthlete> quizQuestionByAthletes;
    Context context;

    public ReplyQuestionsAdapter() {
    }

    public ReplyQuestionsAdapter(List<QuizQuestionByAthlete> quizQuestionByAthletes) {
        this.quizQuestionByAthletes = quizQuestionByAthletes;
    }

    @Override
    public ReplyQuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ReplyQuestionViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_reply_question, parent, false));
    }

    @Override
    public void onBindViewHolder(final ReplyQuestionViewHolder holder, final int position) {
        final QuizQuestionByAthlete quizQuestionByAthlete = quizQuestionByAthletes.get(position);
        holder.questionTextView.setText(quizQuestionByAthlete.getQuestionTitle());
        holder.detailCustomEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int gap = holder.getAdapterPosition();
                QuizQuestionByAthlete qq = quizQuestionByAthletes.get(gap);
                qq.setAnswer(holder.detailCustomEditText.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public boolean answerAreValids(){
        for (int i = 0; i < quizQuestionByAthletes.size(); i++) {
            QuizQuestionByAthlete qq = quizQuestionByAthletes.get(i);
            if(qq.getAnswer().equals(""))
                return false;
        }
        return true;
    }

    public JSONArray getAnswerJsonArray(){
        JSONArray answerJsonArray = new JSONArray();
        for (int i = 0; i < quizQuestionByAthletes.size(); i++) {
            answerJsonArray.put(quizQuestionByAthletes.get(i).toJSONJsonObject());
        }

        return answerJsonArray;
    }

    @Override
    public int getItemCount() {
        return quizQuestionByAthletes.size();
    }

    class ReplyQuestionViewHolder extends RecyclerView.ViewHolder{
        CustomEditText detailCustomEditText;
        TextView questionTextView;

        public ReplyQuestionViewHolder(View itemView) {
            super(itemView);
            detailCustomEditText = (CustomEditText) itemView.findViewById(R.id.detailCustomEditText);
            questionTextView = (TextView) itemView.findViewById(R.id.questionTextView);
        }
    }
}
