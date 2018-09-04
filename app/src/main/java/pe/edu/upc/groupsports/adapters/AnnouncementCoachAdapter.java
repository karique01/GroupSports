package pe.edu.upc.groupsports.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.Announcement;

/**
 * Created by karique on 3/05/2018.
 */

public class AnnouncementCoachAdapter extends RecyclerView.Adapter<AnnouncementCoachAdapter.AnnouncementViewHolder> {
    private List<Announcement> announcements;

    public AnnouncementCoachAdapter() {
    }

    public AnnouncementCoachAdapter(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    @Override
    public AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnnouncementViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_coach_announcement, parent, false));
    }

    @Override
    public void onBindViewHolder(final AnnouncementViewHolder holder, final int position) {
        final Announcement announcement = announcements.get(position);
        // TODO: Assign value to ImageView
        holder.tittleTextView.setText(announcement.getAnnouncementTitle());
        holder.descriptionTextView.setText(announcement.getAnnouncementDetail());
        holder.athletesTextView.setText(announcement.getConcatenatedNames());
        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteAnnouncementListener.deleteAnnouncement(announcement.getId());
            }
        });

        holder.athletesTextView.setVisibility(announcement.isSelected() ? View.VISIBLE : View.GONE);
        holder.seeMoreButton.setText(announcement.isSelected() ? "Ver menos" : "Ver mas");

        holder.seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                announcement.setSelected(!announcement.isSelected());
                holder.athletesTextView.setVisibility(announcement.isSelected() ? View.VISIBLE : View.GONE);
                holder.seeMoreButton.setText(announcement.isSelected() ? "Ver menos" : "Ver mas");
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    class AnnouncementViewHolder extends RecyclerView.ViewHolder{
        TextView tittleTextView;
        TextView descriptionTextView;
        TextView athletesTextView;
        ImageButton deleteImageButton;
        Button seeMoreButton;
        public AnnouncementViewHolder(View itemView) {
            super(itemView);
            tittleTextView = (TextView) itemView.findViewById(R.id.tittleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            athletesTextView = (TextView) itemView.findViewById(R.id.athletesTextView);
            deleteImageButton = (ImageButton) itemView.findViewById(R.id.deleteImageButton);
            seeMoreButton = (Button) itemView.findViewById(R.id.seeMoreButton);
        }
    }

    public interface OnDeleteAnnouncementListener {
        void deleteAnnouncement(String announcementId);
    }

    private OnDeleteAnnouncementListener onDeleteAnnouncementListener;

    public void setOnDeleteAnnouncementListener(OnDeleteAnnouncementListener onDeleteAnnouncementListener) {
        this.onDeleteAnnouncementListener = onDeleteAnnouncementListener;
    }
}
