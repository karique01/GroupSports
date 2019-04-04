package pe.edu.karique.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.StrengthTest;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class StrengthTestAdapter extends RecyclerView.Adapter<StrengthTestAdapter.ShotPutTestViewHolder> {
    private List<StrengthTest> strengthTests;
    Context context;

    public StrengthTestAdapter() {
    }

    public StrengthTestAdapter(List<StrengthTest> strengthTests) {
        this.strengthTests = strengthTests;
    }

    @Override
    public ShotPutTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ShotPutTestViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_saltability_test, parent, false));
    }

    @Override
    public void onBindViewHolder(ShotPutTestViewHolder holder, int position) {
        final StrengthTest strengthTest = strengthTests.get(position);
        // TODO: Assign value to ImageView
        holder.distanceTextView.setText(String.format("%s kg", strengthTest.getMaxRepetitionWeightValue()));
        holder.dateTextView.setText(Funciones.formatDate(strengthTest.getDate()));
        holder.saltabilityTestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.saltabilityTestCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(strengthTest);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return strengthTests.size();
    }

    class ShotPutTestViewHolder extends RecyclerView.ViewHolder{
        TextView distanceTextView;
        TextView dateTextView;
        CardView saltabilityTestCardView;
        public ShotPutTestViewHolder(View itemView) {
            super(itemView);
            distanceTextView = (TextView) itemView.findViewById(R.id.distanceTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            saltabilityTestCardView = (CardView) itemView.findViewById(R.id.saltabilityTestCardView);
        }
    }

    public interface OnLongClickListener {
        void OnLongClicked(StrengthTest strengthTest);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
