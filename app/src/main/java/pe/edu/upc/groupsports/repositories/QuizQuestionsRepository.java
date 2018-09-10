package pe.edu.upc.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.models.QuizQuestion;

/**
 * Created by karique on 3/05/2018.
 */

public class QuizQuestionsRepository {
    private static QuizQuestionsRepository quizQuestionsRepository;
    private List<QuizQuestion> quizQuestions;

    private QuizQuestionsRepository() {
        quizQuestions = new ArrayList<>();
        quizQuestions.add(new QuizQuestion());
    }

    public static QuizQuestionsRepository getInstance(){
        if (quizQuestionsRepository == null)
            quizQuestionsRepository = new QuizQuestionsRepository();
        return quizQuestionsRepository;
    }

    public int size(){
        return quizQuestions.size();
    }

    public QuizQuestion getQQ(int pos){
        return quizQuestions.get(pos);
    }

    public void delete(int pos){
        quizQuestions.remove(pos);
    }

    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public void addQuestion(){
        quizQuestions.add(new QuizQuestion());
    }

    public boolean areValidQuestions(){
        for (int i = 0; i < quizQuestions.size(); i++) {
            if (quizQuestions.get(i).getQuestionTitle().length() == 0)
                return false;
        }
        return true;
    }
}
