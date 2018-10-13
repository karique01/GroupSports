package pe.edu.upc.groupsports.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.CoachCurriculumDetail;

/**
 * Created by karique on 3/05/2018.
 */

public class CoachCurriculumDetailAdapter extends RecyclerView.Adapter<CoachCurriculumDetailAdapter.CoachCurriculumDetailViewHolder> {
    private List<CoachCurriculumDetail> coachCurriculumDetails;

    public CoachCurriculumDetailAdapter() {
    }

    public CoachCurriculumDetailAdapter(List<CoachCurriculumDetail> coachCurriculumDetails) {
        this.coachCurriculumDetails = coachCurriculumDetails;
    }

    @Override
    public CoachCurriculumDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CoachCurriculumDetailViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_curriculum_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(CoachCurriculumDetailViewHolder holder, int position) {
        final CoachCurriculumDetail coachCurriculumDetail = coachCurriculumDetails.get(position);
        // TODO: Assign value to ImageView
        holder.titleTextView.setText(coachCurriculumDetail.getTitle());
        holder.detailTextView.setText(coachCurriculumDetail.getDescription());
        holder.cvDetailCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onCVDetailLongClickListener != null) {
                    onCVDetailLongClickListener.OnCVDetailLongClicked(coachCurriculumDetail);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return coachCurriculumDetails.size();
    }

    class CoachCurriculumDetailViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;
        TextView detailTextView;
        CardView cvDetailCardView;
        public CoachCurriculumDetailViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            detailTextView = (TextView) itemView.findViewById(R.id.detailTextView);
            cvDetailCardView = (CardView) itemView.findViewById(R.id.cvDetailCardView);
        }
    }

    public interface OnCVDetailLongClickListener {
        void OnCVDetailLongClicked(CoachCurriculumDetail coachCurriculumDetail);
    }

    private OnCVDetailLongClickListener onCVDetailLongClickListener;

    public void setOnCVDetailLongClickListener(OnCVDetailLongClickListener onCVDetailLongClickListener) {
        this.onCVDetailLongClickListener = onCVDetailLongClickListener;
    }
}
