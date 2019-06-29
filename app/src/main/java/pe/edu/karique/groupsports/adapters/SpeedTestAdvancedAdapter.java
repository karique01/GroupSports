package pe.edu.karique.groupsports.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.SpeedTest;
import pe.edu.karique.groupsports.models.SpeedTestAdvanced;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class SpeedTestAdvancedAdapter extends RecyclerView.Adapter<SpeedTestAdvancedAdapter.SpeedTestViewHolder> {
    private List<SpeedTestAdvanced> speedTestAdvanceds;

    public SpeedTestAdvancedAdapter() {
    }

    public SpeedTestAdvancedAdapter(List<SpeedTestAdvanced> speedTestAdvanceds) {
        this.speedTestAdvanceds = speedTestAdvanceds;
    }

    @Override
    public SpeedTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpeedTestViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_speed_test_advanced, parent, false));
    }

    @Override
    public void onBindViewHolder(SpeedTestViewHolder holder, int position) {
        final SpeedTestAdvanced st = speedTestAdvanceds.get(position);
        holder.velocidadPromedioTextView.setText(String.format("Velocidad promedio (m/s): %s", st.getVelocidadPromedio()));
        holder.tiempoTextView.setText(String.format("Tiempo: %s ms", st.getTiempoTotal()));
        holder.pisadasDerechasValueTextView.setText(String.format("PD: %s", st.getPisadasDerechas()));
        holder.pisadasIzquierdasValueTextView.setText(String.format("PI: %s", st.getPisadasIzquierdas()));

        holder.speedTestAdvancedCardView.setOnLongClickListener(new View.OnLongClickListener() {
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
        return speedTestAdvanceds.size();
    }

    class SpeedTestViewHolder extends RecyclerView.ViewHolder{
        TextView velocidadPromedioTextView;
        TextView tiempoTextView;
        TextView pisadasDerechasValueTextView;
        TextView pisadasIzquierdasValueTextView;
        CardView speedTestAdvancedCardView;
        public SpeedTestViewHolder(View itemView) {
            super(itemView);
            velocidadPromedioTextView = (TextView) itemView.findViewById(R.id.velocidadPromedioTextView);
            tiempoTextView = (TextView) itemView.findViewById(R.id.tiempoTextView);
            pisadasDerechasValueTextView = (TextView) itemView.findViewById(R.id.pisadasDerechasValueTextView);
            pisadasIzquierdasValueTextView = (TextView) itemView.findViewById(R.id.pisadasIzquierdasValueTextView);
            speedTestAdvancedCardView = (CardView) itemView.findViewById(R.id.speedTestAdvancedCardView);
        }
    }

    public interface OnLongClickListener {
        void OnLongClicked(SpeedTestAdvanced speedTestAdvanced);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
