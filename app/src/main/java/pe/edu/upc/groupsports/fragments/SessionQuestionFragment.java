package pe.edu.upc.groupsports.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.activities.SessionHistoryActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SessionQuestionFragment extends Fragment {


    public SessionQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_session_question, container, false);
        Button sessionsHistoryButton = (Button) view.findViewById(R.id.sessionsHistoryButton);
        sessionsHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), SessionHistoryActivity.class));
            }
        });
        return view;
    }

}
