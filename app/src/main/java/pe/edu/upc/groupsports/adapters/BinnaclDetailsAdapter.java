package pe.edu.upc.groupsports.adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.activities.AthleteDetailActivity;
import pe.edu.upc.groupsports.models.BinnacleDetail;

/**
 * Created by karique on 3/05/2018.
 */

public class BinnaclDetailsAdapter extends RecyclerView.Adapter<BinnaclDetailsAdapter.BinnacleDetailViewHolder> {
    private List<BinnacleDetail> binnacleDetails;

    public BinnaclDetailsAdapter() {
    }

    public BinnaclDetailsAdapter(List<BinnacleDetail> binnacleDetails) {
        this.binnacleDetails = binnacleDetails;
    }

    @Override
    public BinnacleDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BinnacleDetailViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_binnacle_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(BinnacleDetailViewHolder holder, int position) {
        final BinnacleDetail binnacleDetail = binnacleDetails.get(position);
        // TODO: Assign value to ImageView
        holder.binnacleDetailTextView.setText(binnacleDetail.getDetail());
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBinnacleDetailDeleted.BinnacleDetailDeleted(binnacleDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return binnacleDetails.size();
    }

    public List<BinnacleDetail> getBinnacleDetails() {
        return binnacleDetails;
    }

    public void setBinnacleDetails(List<BinnacleDetail> binnacleDetails) {
        this.binnacleDetails = binnacleDetails;
    }

    class BinnacleDetailViewHolder extends RecyclerView.ViewHolder{
        ImageButton deleteImageButton;
        TextView binnacleDetailTextView;
        public BinnacleDetailViewHolder(View itemView) {
            super(itemView);
            deleteImageButton = (ImageButton) itemView.findViewById(R.id.deleteImageButton);
            binnacleDetailTextView = (TextView) itemView.findViewById(R.id.binnacleDetailTextView);
        }
    }

    public interface OnBinnacleDetailDeleted {
        void BinnacleDetailDeleted(BinnacleDetail binnacleDetail);
    }

    private OnBinnacleDetailDeleted onBinnacleDetailDeleted;

    public void setOnBinnacleDetailDeleted(OnBinnacleDetailDeleted onBinnacleDetailDeleted) {
        this.onBinnacleDetailDeleted = onBinnacleDetailDeleted;
    }
}
