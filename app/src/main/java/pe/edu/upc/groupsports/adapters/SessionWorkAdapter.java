package pe.edu.upc.groupsports.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.activities.BinnacleActivity;
import pe.edu.upc.groupsports.activities.WeekDetailActivity;
import pe.edu.upc.groupsports.models.BinnacleDetail;
import pe.edu.upc.groupsports.models.SessionWork;
import pe.edu.upc.groupsports.models.Week;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class SessionWorkAdapter extends RecyclerView.Adapter<SessionWorkAdapter.SessionWorkViewHolder> {
    private List<SessionWork> sessionWorks;
    Context context;

    public SessionWorkAdapter() {
    }

    public SessionWorkAdapter(List<SessionWork> sessionWorks) {
        this.sessionWorks = sessionWorks;
    }

    @Override
    public SessionWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SessionWorkViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_session_work, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(SessionWorkViewHolder holder, final int position) {
        final SessionWork sessionWork = sessionWorks.get(position);
        if (sessionWork != null) {
            holder.shiftSessionTextView.setText(sessionWork.getShiftName());
            holder.intensityPercentageTextView.setText(String.valueOf(sessionWork.getIntensityPercentage()));
            holder.attendanceTextView.setText(sessionWork.getAttendance() ? "Asistió" : "Sesion programada");
            int size = sessionWork.getBinnacleDetails().size();
            holder.seeBinnacleButton.setText(String.format("Ver Bitacora %s", (size > 0) ? ("(" + size + " entradas)") : ("")));

            if (sessionWork.getAttendance())
                holder.intensityPercentageTextView.setVisibility(View.VISIBLE);
            else holder.intensityPercentageTextView.setVisibility(View.GONE);

            holder.addSessionCardView.setVisibility(View.GONE);
            holder.sessionCardView.setVisibility(View.VISIBLE);
        }
        else {
            holder.addSessionCardView.setVisibility(View.VISIBLE);
            holder.shiftTextView.setText(position == 0 ? "Turno mañana" : position == 1 ? "Turno tarde" : "Turno noche");
            holder.sessionCardView.setVisibility(View.GONE);
        }

        //agregar sesion
        holder.addWorkSessionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddSessionListener.OnAddSessionClicked(position + 1);
            }
        });
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionWork != null) {
                    onDeleteSessionListener.OnDeleteSessionClicked(sessionWork);
                }
            }
        });
        holder.seeBinnacleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionWork != null) {
                    Intent intent = new Intent(context,BinnacleActivity.class);
                    intent.putExtras(sessionWork.toBundle());
                    ((Activity)context).startActivityForResult(
                            intent, BinnacleActivity.REQUEST_FOR_ACTIVITY_CODE_BINNACLE_DETAIL
                    );
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessionWorks.size();
    }

    public List<SessionWork> getSessionWorks() {
        return sessionWorks;
    }

    public void setSessionWorks(List<SessionWork> sessionWorks) {
        this.sessionWorks = sessionWorks;
    }

    class SessionWorkViewHolder extends RecyclerView.ViewHolder{
        CardView addSessionCardView;
        TextView newSessionTextView;
        TextView shiftTextView;
        ImageButton addWorkSessionImageButton;

        CardView sessionCardView;
        TextView shiftSessionTextView;
        TextView intensityPercentageTextView;
        TextView attendanceTextView;
        ImageButton deleteImageButton;

        Button seeBinnacleButton;
        public SessionWorkViewHolder(View itemView) {
            super(itemView);
            addSessionCardView = (CardView) itemView.findViewById(R.id.addSessionCardView);
            newSessionTextView = (TextView) itemView.findViewById(R.id.newSessionTextView);
            shiftTextView = (TextView) itemView.findViewById(R.id.shiftTextView);
            addWorkSessionImageButton = (ImageButton) itemView.findViewById(R.id.addWorkSessionImageButton);

            sessionCardView = (CardView) itemView.findViewById(R.id.sessionCardView);
            shiftSessionTextView = (TextView) itemView.findViewById(R.id.shiftSessionTextView);
            intensityPercentageTextView = (TextView) itemView.findViewById(R.id.intensityPercentageTextView);
            attendanceTextView = (TextView) itemView.findViewById(R.id.attendanceTextView);
            deleteImageButton = (ImageButton) itemView.findViewById(R.id.deleteImageButton);

            seeBinnacleButton = (Button) itemView.findViewById(R.id.seeBinnacleButton);
        }
    }

    public interface OnAddSessionListener{
        void OnAddSessionClicked(int shiftId);
    }

    private OnAddSessionListener onAddSessionListener;

    public OnAddSessionListener getOnAddSessionListener() {
        return onAddSessionListener;
    }

    public void setOnAddSessionListener(OnAddSessionListener onAddSessionListener) {
        this.onAddSessionListener = onAddSessionListener;
    }

    public interface OnDeleteSessionListener{
        void OnDeleteSessionClicked(SessionWork sessionWork);
    }

    private OnDeleteSessionListener onDeleteSessionListener;

    public OnDeleteSessionListener getOnDeleteSessionListener() {
        return onDeleteSessionListener;
    }

    public void setOnDeleteSessionListener(OnDeleteSessionListener onDeleteSessionListener) {
        this.onDeleteSessionListener = onDeleteSessionListener;
    }
}
