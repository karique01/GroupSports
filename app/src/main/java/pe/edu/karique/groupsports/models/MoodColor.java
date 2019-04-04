package pe.edu.karique.groupsports.models;

import pe.edu.karique.groupsports.R;

/**
 * Created by karique on 2/09/2018.
 */

public class MoodColor {
    private String id;
    private boolean selected = false;

    public int getCardDrawableResource(){
        return id.equals("1") ? R.drawable.ic_card_choosed_red :
               id.equals("2") ? R.drawable.ic_card_choosed_yellow :
               id.equals("3") ? R.drawable.ic_card_choosed_blue :
               id.equals("4") ? R.drawable.ic_card_choosed_black :
                                R.drawable.ic_card_choosed_green;
    }

    public MoodColor() {
    }

    public MoodColor(String id) {
        this.id = id;
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
