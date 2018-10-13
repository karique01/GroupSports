package pe.edu.upc.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.JumpTest;
import pe.edu.upc.groupsports.models.SaltabilityTestType;

/**
 * Created by karique on 3/05/2018.
 */

public class SaltabilityTestTypeAdapter extends RecyclerView.Adapter<SaltabilityTestTypeAdapter.ShotPutTestViewHolder> {
    private List<SaltabilityTestType> saltabilityTestTypes;
    Context context;

    public SaltabilityTestTypeAdapter() {
    }

    public SaltabilityTestTypeAdapter(List<SaltabilityTestType> saltabilityTestTypes) {
        this.saltabilityTestTypes = saltabilityTestTypes;
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
        final SaltabilityTestType saltabilityTestType = saltabilityTestTypes.get(position);
        // TODO: Assign value to ImageView
        holder.descriptionTextView.setText(saltabilityTestType.getDescription());
        holder.saltabilityTestTypeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.saltabilityTestRecyclerView.setHasFixedSize(true);
        holder.saltabilityTestRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        SaltabilityTestAdapter saltabilityTestAdapter = new SaltabilityTestAdapter(saltabilityTestType.getJumpTests());
        saltabilityTestAdapter.setLongClickListener(new SaltabilityTestAdapter.OnLongClickListener() {
            @Override
            public void OnLongClicked(JumpTest jumpTest) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(jumpTest);
                }
            }
        });
        holder.saltabilityTestRecyclerView.setAdapter(saltabilityTestAdapter);
        holder.saltabilityTestRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return saltabilityTestTypes.size();
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
        void OnLongClicked(JumpTest jumpTest);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
