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
import pe.edu.upc.groupsports.models.JumpTest;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class SaltabilityTestAdapter extends RecyclerView.Adapter<SaltabilityTestAdapter.ShotPutTestViewHolder> {
    private List<JumpTest> jumpTests;
    Context context;

    public SaltabilityTestAdapter() {
    }

    public SaltabilityTestAdapter(List<JumpTest> jumpTests) {
        this.jumpTests = jumpTests;
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
        final JumpTest jumpTest = jumpTests.get(position);
        // TODO: Assign value to ImageView
        holder.distanceTextView.setText(String.format("%s m", jumpTest.getDistanceResult()));
        holder.dateTextView.setText(Funciones.formatDate(jumpTest.getDate()));
        holder.saltabilityTestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.saltabilityTestCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(jumpTest);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return jumpTests.size();
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
        void OnLongClicked(JumpTest jumpTest);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
