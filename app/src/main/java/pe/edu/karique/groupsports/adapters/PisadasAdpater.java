package pe.edu.karique.groupsports.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.Pisada;

public class PisadasAdpater extends RecyclerView.Adapter<PisadasAdpater.PisadasViewHolder> {
    private List<Pisada> pisadas;
    private ContextProvider cp;

    public PisadasAdpater(List<Pisada> pisadas, ContextProvider cp) {
        this.pisadas = pisadas;
        this.cp = cp;
    }

    public interface ContextProvider {
        Context getContext();
    }

    @Override
    public PisadasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PisadasViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_pisada, parent, false));
    }

    @Override
    public void onBindViewHolder(PisadasViewHolder holder, final int position) {
        final Pisada p = pisadas.get(holder.getAdapterPosition());
        // TODO: Assign value to ImageView
        holder.fuerzaTextView.setText(String.format("Fuerza: %s", String.valueOf(p.getFuerza())));
        holder.tiempoTextView.setText(String.format("Tiempo: %s", String.valueOf(p.getTiempoRespectoAPisadaPrevia())));
        holder.pisadaCardView.setCardBackgroundColor(cp.getContext().getResources().getColor(R.color.colorWhite));
        holder.fuerzaTextView.setTextColor(cp.getContext().getResources().getColor(R.color.colorSecondaryText));
        holder.tiempoTextView.setTextColor(cp.getContext().getResources().getColor(R.color.colorSecondaryText));
        holder.fuerzaImageView.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        holder.tiempoImageView.setImageResource(R.drawable.ic_time);

        if (p.pisadaEnTramo){
            holder.pisadaCardView.setCardBackgroundColor(cp.getContext().getResources().getColor(R.color.colorAccent));
            holder.fuerzaTextView.setTextColor(Color.WHITE);
            holder.tiempoTextView.setTextColor(Color.WHITE);
            holder.fuerzaImageView.setImageResource(R.drawable.ic_arrow_white);
            holder.tiempoImageView.setImageResource(R.drawable.ic_time_white);
        }
    }

    @Override
    public int getItemCount() {
        return pisadas.size();
    }

    class PisadasViewHolder extends RecyclerView.ViewHolder{
        TextView fuerzaTextView;
        TextView tiempoTextView;

        CardView pisadaCardView;
        AppCompatImageView fuerzaImageView;
        AppCompatImageView tiempoImageView;

        public PisadasViewHolder(View itemView) {
            super(itemView);
            fuerzaTextView = (TextView) itemView.findViewById(R.id.fuerzaTextView);
            tiempoTextView = (TextView) itemView.findViewById(R.id.tiempoTextView);

            pisadaCardView = (CardView) itemView.findViewById(R.id.pisadaCardView);
            fuerzaImageView = (AppCompatImageView) itemView.findViewById(R.id.fuerzaImageView);
            tiempoImageView = (AppCompatImageView) itemView.findViewById(R.id.tiempoImageView);
        }
    }
}
