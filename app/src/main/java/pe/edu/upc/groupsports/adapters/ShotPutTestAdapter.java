package pe.edu.upc.groupsports.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.ShotPutTest;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class ShotPutTestAdapter extends RecyclerView.Adapter<ShotPutTestAdapter.ShotPutTestViewHolder> {
    private List<ShotPutTest> shotPutTests;

    public ShotPutTestAdapter() {
    }

    public ShotPutTestAdapter(List<ShotPutTest> shotPutTests) {
        this.shotPutTests = shotPutTests;
    }

    @Override
    public ShotPutTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShotPutTestViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_shot_put_test, parent, false));
    }

    @Override
    public void onBindViewHolder(ShotPutTestViewHolder holder, int position) {
        final ShotPutTest shotPutTest = shotPutTests.get(position);
        // TODO: Assign value to ImageView
        holder.resultTextView.setText(String.format("%s metros", shotPutTest.getResult()));
        holder.ballWeightTextView.setText(String.format("%s kg", shotPutTest.getBallWeight()));
        holder.shotPutTypeTextView.setText(shotPutTest.getType());
        holder.shotPutCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(shotPutTest);
                }
                return true;
            }
        });
        holder.dateTextView.setText(Funciones.formatDate(shotPutTest.getDate()));
    }

    @Override
    public int getItemCount() {
        return shotPutTests.size();
    }

    class ShotPutTestViewHolder extends RecyclerView.ViewHolder{
        TextView resultTextView;
        TextView ballWeightTextView;
        TextView shotPutTypeTextView;
        CardView shotPutCardView;
        TextView dateTextView;
        public ShotPutTestViewHolder(View itemView) {
            super(itemView);
            resultTextView = (TextView) itemView.findViewById(R.id.resultTextView);
            ballWeightTextView = (TextView) itemView.findViewById(R.id.ballWeightTextView);
            shotPutTypeTextView = (TextView) itemView.findViewById(R.id.shotPutTypeTextView);
            shotPutCardView = (CardView) itemView.findViewById(R.id.shotPutCardView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
        }
    }

    public interface OnLongClickListener {
        void OnLongClicked(ShotPutTest shotPutTest);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
