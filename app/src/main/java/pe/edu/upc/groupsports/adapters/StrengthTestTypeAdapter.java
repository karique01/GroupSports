package pe.edu.upc.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.SaltabilityTestType;
import pe.edu.upc.groupsports.models.StrengthTest;
import pe.edu.upc.groupsports.models.StrengthTestType;

/**
 * Created by karique on 3/05/2018.
 */

public class StrengthTestTypeAdapter extends RecyclerView.Adapter<StrengthTestTypeAdapter.ShotPutTestViewHolder> {
    private List<StrengthTestType> strengthTestTypes;
    Context context;

    public StrengthTestTypeAdapter() {
    }

    public StrengthTestTypeAdapter(List<StrengthTestType> strengthTestTypes) {
        this.strengthTestTypes = strengthTestTypes;
    }

    @Override
    public ShotPutTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ShotPutTestViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_saltability_test_type, parent, false));
    }

    @Override
    public void onBindViewHolder(ShotPutTestViewHolder holder, int position) {
        final StrengthTestType strengthTestType = strengthTestTypes.get(position);
        // TODO: Assign value to ImageView
        holder.descriptionTextView.setText(strengthTestType.getDescription());
        holder.saltabilityTestTypeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.saltabilityTestRecyclerView.setHasFixedSize(true);
        holder.saltabilityTestRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        StrengthTestAdapter strengthTestAdapter = new StrengthTestAdapter(strengthTestType.getStrengthTests());
        strengthTestAdapter.setLongClickListener(new StrengthTestAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(StrengthTest strengthTest) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(strengthTest);
                }
            }
        });
        holder.saltabilityTestRecyclerView.setAdapter(strengthTestAdapter);
        holder.saltabilityTestRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return strengthTestTypes.size();
    }

    class ShotPutTestViewHolder extends RecyclerView.ViewHolder{
        TextView descriptionTextView;
        RecyclerView saltabilityTestRecyclerView;
        CardView saltabilityTestTypeCardView;
        public ShotPutTestViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            saltabilityTestRecyclerView = (RecyclerView) itemView.findViewById(R.id.saltabilityTestRecyclerView);
            saltabilityTestTypeCardView = (CardView) itemView.findViewById(R.id.saltabilityTestTypeCardView);
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
