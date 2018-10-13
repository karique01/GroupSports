package pe.edu.upc.groupsports.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.SpeedTest;
import pe.edu.upc.groupsports.util.Funciones;

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
        final SpeedTest st = speedTests.get(position);
        // TODO: Assign value to ImageView
        holder.resultTextView.setText(
                String.format("%s:%s:%s,%s",
                        String.format(Locale.getDefault(),"%02d", st.getHoursInt()),
                        String.format(Locale.getDefault(),"%02d", st.getMinutesInt()),
                        String.format(Locale.getDefault(),"%02d", st.getSecondsInt()),
                        String.format(Locale.getDefault(),"%02d", st.getMillisecondsInt())
                )
        );
        holder.metersTextView.setText(String.format("%s metros", st.getMeters()));
        holder.dateTextView.setText(Funciones.formatDate(st.getDate()));
        holder.speedCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(st);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return speedTests.size();
    }

    class SpeedTestViewHolder extends RecyclerView.ViewHolder{
        TextView resultTextView;
        TextView metersTextView;
        TextView dateTextView;
        CardView speedCardView;
        public SpeedTestViewHolder(View itemView) {
            super(itemView);
            resultTextView = (TextView) itemView.findViewById(R.id.resultTextView);
            metersTextView = (TextView) itemView.findViewById(R.id.metersTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            speedCardView = (CardView) itemView.findViewById(R.id.speedCardView);
        }
    }

    public interface OnLongClickListener {
        void OnLongClicked(SpeedTest speedTest);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
