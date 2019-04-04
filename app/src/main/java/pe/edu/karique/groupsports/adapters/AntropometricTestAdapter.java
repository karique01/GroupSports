package pe.edu.karique.groupsports.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.AntropometricTest;
import pe.edu.karique.groupsports.util.Funciones;

/**
 * Created by karique on 3/05/2018.
 */

public class AntropometricTestAdapter extends RecyclerView.Adapter<AntropometricTestAdapter.AntropometricTestViewHolder> {
    private List<AntropometricTest> antropometricTests;
    Context context;

    public AntropometricTestAdapter() {
    }

    public AntropometricTestAdapter(List<AntropometricTest> antropometricTests) {
        this.antropometricTests = antropometricTests;
    }

    @Override
    public AntropometricTestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AntropometricTestViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_antropometric_test, parent, false));
    }

    @Override
    public void onBindViewHolder(AntropometricTestViewHolder holder, int position) {
        final AntropometricTest antropometricTest = antropometricTests.get(position);
        // TODO: Assign value to ImageView
        holder.sizeButton.setText(String.format("%s\n%s", context.getString(R.string.size), antropometricTest.getSize()));
        holder.weightButton.setText(String.format("%s\n%s", context.getString(R.string.weight), antropometricTest.getWeight()));
        holder.wingsPanButton.setText(String.format("%s\n%s m", context.getString(R.string.wingspan_2), antropometricTest.getWingspan()));
        holder.bodyFatPercentageButton.setText(String.format("%s\n%s%%", context.getString(R.string.body_fat), antropometricTest.getBodyFatPercentage()));
        holder.leanBodyPercentageButton.setText(String.format("%s\n%s%%", context.getString(R.string.body_lean), antropometricTest.getLeanBodyPercentage()));
        holder.dateButton.setText(String.format("%s\n%s", context.getString(R.string.date), Funciones.formatDate(antropometricTest.getDate())));
        holder.antropometricTestCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.OnLongClicked(antropometricTest);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return antropometricTests.size();
    }

    class AntropometricTestViewHolder extends RecyclerView.ViewHolder{
        Button sizeButton;
        Button weightButton;
        Button wingsPanButton;
        Button bodyFatPercentageButton;
        Button leanBodyPercentageButton;
        Button dateButton;
        CardView antropometricTestCardView;
        public AntropometricTestViewHolder(View itemView) {
            super(itemView);
            sizeButton = (Button) itemView.findViewById(R.id.sizeButton);
            weightButton = (Button) itemView.findViewById(R.id.weightButton);
            wingsPanButton = (Button) itemView.findViewById(R.id.wingsPanButton);
            bodyFatPercentageButton = (Button) itemView.findViewById(R.id.bodyFatPercentageButton);
            leanBodyPercentageButton = (Button) itemView.findViewById(R.id.leanBodyPercentageButton);
            dateButton = (Button) itemView.findViewById(R.id.dateButton);
            antropometricTestCardView = (CardView) itemView.findViewById(R.id.antropometricTestCardView);
        }
    }

    public interface OnLongClickListener {
        void OnLongClicked(AntropometricTest antropometricTest);
    }

    private OnLongClickListener onLongClickListener;

    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
