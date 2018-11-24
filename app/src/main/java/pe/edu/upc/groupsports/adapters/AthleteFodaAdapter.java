package pe.edu.upc.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.AthleteFodaItem;
import pe.edu.upc.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class AthleteFodaAdapter extends RecyclerView.Adapter<AthleteFodaAdapter.AthleteFodaViewHolder> {
    private List<AthleteFodaItem> athleteFodaItems;
    Context context;

    public AthleteFodaAdapter() {
    }

    public AthleteFodaAdapter(List<AthleteFodaItem> athleteFodaItems) {
        this.athleteFodaItems = athleteFodaItems;
    }

    @Override
    public AthleteFodaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AthleteFodaViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_foda_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final AthleteFodaViewHolder holder, final int position) {
        final AthleteFodaItem athleteFodaItem = athleteFodaItems.get(position);

        holder.fodaItemTextView.setText(athleteFodaItem.getFodaItemValue());
        holder.editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEditFodaItemListener != null) {
                    onEditFodaItemListener.OnEditFodaItemClicked(athleteFodaItem);
                }
            }
        });

        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteFodaItemListener != null) {
                    onDeleteFodaItemListener.OnDeleteFodaItemClicked(athleteFodaItem);
                }
            }
        });
        holder.dateTextView.setText(Funciones.formatDate(athleteFodaItem.getDate()));
    }

    @Override
    public int getItemCount() {
        return athleteFodaItems.size();
    }

    class AthleteFodaViewHolder extends RecyclerView.ViewHolder {
        TextView fodaItemTextView;
        ImageButton deleteImageButton;
        ImageButton editImageButton;
        TextView dateTextView;

        public AthleteFodaViewHolder(View itemView) {
            super(itemView);
            fodaItemTextView = (TextView) itemView.findViewById(R.id.fodaItemTextView);
            deleteImageButton = (ImageButton) itemView.findViewById(R.id.deleteImageButton);
            editImageButton = (ImageButton) itemView.findViewById(R.id.editImageButton);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
        }
    }

    public interface OnEditFodaItemListener {
        void OnEditFodaItemClicked(AthleteFodaItem athleteFodaItem);
    }
    public interface OnDeleteFodaItemListener {
        void OnDeleteFodaItemClicked(AthleteFodaItem athleteFodaItem);
    }

    OnEditFodaItemListener onEditFodaItemListener;
    OnDeleteFodaItemListener onDeleteFodaItemListener;

    public OnEditFodaItemListener getOnEditFodaItemListener() {
        return onEditFodaItemListener;
    }

    public void setOnEditFodaItemListener(OnEditFodaItemListener onEditFodaItemListener) {
        this.onEditFodaItemListener = onEditFodaItemListener;
    }

    public OnDeleteFodaItemListener getOnDeleteFodaItemListener() {
        return onDeleteFodaItemListener;
    }

    public void setOnDeleteFodaItemListener(OnDeleteFodaItemListener onDeleteFodaItemListener) {
        this.onDeleteFodaItemListener = onDeleteFodaItemListener;
    }
}
