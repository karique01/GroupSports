package pe.edu.karique.groupsports.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import pe.edu.karique.groupsports.R;
import pe.edu.karique.groupsports.models.Announcement;

/**
 * Created by karique on 3/05/2018.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {
    private List<Announcement> announcements;

    public AnnouncementAdapter() {
    }

    public AnnouncementAdapter(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    @Override
    public AnnouncementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnnouncementViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_announcement, parent, false));
    }

    @Override
    public void onBindViewHolder(AnnouncementViewHolder holder, int position) {
        final Announcement announcement = announcements.get(position);
        // TODO: Assign value to ImageView
        holder.tittleTextView.setText(announcement.getAnnouncementTitle());
        holder.descriptionTextView.setText(announcement.getAnnouncementDetail());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteAnnouncementListener.deleteAnnouncement(announcement.getId());
            }
        });
        holder.seeMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowDetailsAnnouncementListener.showAnnouncementDetails(announcement);
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
        Button deleteButton;
        Button seeMoreButton;
        public AnnouncementViewHolder(View itemView) {
            super(itemView);
            tittleTextView = (TextView) itemView.findViewById(R.id.tittleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButton);
            seeMoreButton = (Button) itemView.findViewById(R.id.seeMoreButton);
        }
    }

    public interface OnDeleteAnnouncementListener {
        void deleteAnnouncement(String announcementId);
    }
    public interface OnShowDetailsAnnouncementListener {
        void showAnnouncementDetails(Announcement announcement);
    }

    private OnDeleteAnnouncementListener onDeleteAnnouncementListener;

    public void setOnDeleteAnnouncementListener(OnDeleteAnnouncementListener onDeleteAnnouncementListener) {
        this.onDeleteAnnouncementListener = onDeleteAnnouncementListener;
    }

    private OnShowDetailsAnnouncementListener onShowDetailsAnnouncementListener;

    public void setOnShowDetailsAnnouncementListener(OnShowDetailsAnnouncementListener onShowDetailsAnnouncementListener) {
        this.onShowDetailsAnnouncementListener = onShowDetailsAnnouncementListener;
    }
}
