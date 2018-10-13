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
import pe.edu.upc.groupsports.models.WeightTestBySession;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class WeightTestBySessionAdapter extends RecyclerView.Adapter<WeightTestBySessionAdapter.WeightTestBySessionViewHolder> {
    private List<WeightTestBySession> weightTestBySessions;

    public WeightTestBySessionAdapter() {
    }

    public WeightTestBySessionAdapter(List<WeightTestBySession> weightTestBySessions) {
        this.weightTestBySessions = weightTestBySessions;
    }

    @Override
    public WeightTestBySessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WeightTestBySessionViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_weight_test_by_session, parent, false));
    }

    @Override
    public void onBindViewHolder(WeightTestBySessionViewHolder holder, int position) {
        final WeightTestBySession weightTestBySession = weightTestBySessions.get(position);
        // TODO: Assign value to ImageView
        holder.sessionDayTextView.setText(String.format("Dia: %s", Funciones.formatDate(weightTestBySession.getSessionDay())));
        holder.shiftNameTextView.setText(String.format("Turno: %s", weightTestBySession.getShiftName()));
        holder.weightBeforeEditText.setText(String.format("%s kg", weightTestBySession.getWeightBefore()));
        holder.weightBeforeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWeightBeforeEditListener.onWeightBeforeEdited(weightTestBySession);
            }
        });
        holder.weightAfterEditText.setText(String.format("%s kg", weightTestBySession.getWeightAfter()));
        holder.weightAfterEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWeightAfterEditListener.onWeightAfterEdited(weightTestBySession);
            }
        });
        holder.weightTestCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(weightTestBySession);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return weightTestBySessions.size();
    }

    class WeightTestBySessionViewHolder extends RecyclerView.ViewHolder{
        TextView sessionDayTextView;
        TextView shiftNameTextView;
        TextView weightBeforeEditText;
        TextView weightAfterEditText;
        CardView weightTestCardView;
        public WeightTestBySessionViewHolder(View itemView) {
            super(itemView);
            sessionDayTextView = (TextView) itemView.findViewById(R.id.sessionDayTextView);
            shiftNameTextView = (TextView) itemView.findViewById(R.id.shiftNameTextView);
            weightBeforeEditText = (TextView) itemView.findViewById(R.id.weightBeforeEditText);
            weightAfterEditText = (TextView) itemView.findViewById(R.id.weightAfterEditText);
            weightTestCardView = (CardView) itemView.findViewById(R.id.weightTestCardView);
        }
    }


    public interface OnWeightBeforeEditListener {
        void onWeightBeforeEdited(WeightTestBySession weightTestBySession);
    }

    private OnWeightBeforeEditListener onWeightBeforeEditListener;

    public void setOnWeightBeforeEditListener(OnWeightBeforeEditListener onWeightBeforeEditListener) {
        this.onWeightBeforeEditListener = onWeightBeforeEditListener;
    }

    public interface OnWeightAfterEditListener {
        void onWeightAfterEdited(WeightTestBySession weightTestBySession);
    }

    private OnWeightAfterEditListener onWeightAfterEditListener;

    public void setOnWeightAfterEditListener(OnWeightAfterEditListener onWeightAfterEditListener) {
        this.onWeightAfterEditListener = onWeightAfterEditListener;
    }

    public interface OnLongClickListener {
        void OnLongClicked(WeightTestBySession weightTestBySession);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
