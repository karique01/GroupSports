package pe.edu.upc.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.AthleteCategory;
import pe.edu.upc.groupsports.models.MoodColor;

/**
 * Created by karique on 3/05/2018.
 */

public class MoodColorsRepository {
    private static MoodColorsRepository moodColorRepository;
    private List<MoodColor> moodColors;

    private MoodColorsRepository() {
        moodColors = new ArrayList<>();
        moodColors.add(new MoodColor("1"));
        moodColors.add(new MoodColor("2"));
        moodColors.add(new MoodColor("3"));
        moodColors.add(new MoodColor("4"));
        moodColors.add(new MoodColor("5"));
    }

    public static MoodColorsRepository getInstance(){
        if (moodColorRepository == null)
            moodColorRepository = new MoodColorsRepository();
        return moodColorRepository;
    }

    public List<MoodColor> getMoodColors() {
        return moodColors;
    }
}
