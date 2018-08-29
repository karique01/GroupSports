package pe.edu.upc.groupsports.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.Athlete;
import pe.edu.upc.groupsports.models.SpeedTest;

/**
 * Created by karique on 3/05/2018.
 */

public class SpeedTestAdapter extends RecyclerView.Adapter<SpeedTestAdapter.SpeedTestViewHolder> {
    private List<SpeedTest> speedTests;

    public SpeedTestAdapter() {
    }

    public SpeedTestAdapter(List<SpeedTest> speedTests) {
        this.speedTests = speedTests;
    }

    @Override
    public SpeedTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpeedTestViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_velocity_test, parent, false));
    }

    @Override
    public void onBindViewHolder(SpeedTestViewHolder holder, int position) {
        final SpeedTest speedTest = speedTests.get(position);
        // TODO: Assign value to ImageView
        holder.resultTextView.setText(speedTest.getResult());
        holder.metersTextView.setText(speedTest.getMeters() + " metros");
    }

    @Override
    public int getItemCount() {
        return speedTests.size();
    }

    class SpeedTestViewHolder extends RecyclerView.ViewHolder{
        TextView resultTextView;
        TextView metersTextView;
        public SpeedTestViewHolder(View itemView) {
            super(itemView);
            resultTextView = (TextView) itemView.findViewById(R.id.resultTextView);
            metersTextView = (TextView) itemView.findViewById(R.id.metersTextView);
        }
    }
}
