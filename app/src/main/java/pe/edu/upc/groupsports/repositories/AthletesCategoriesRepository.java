package pe.edu.upc.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.AthleteCategory;

/**
 * Created by karique on 3/05/2018.
 */

public class AthletesCategoriesRepository {
    private static AthletesCategoriesRepository athletesCategoriesRepository;
    private List<AthleteCategory> athleteCategories;

    private AthletesCategoriesRepository() {
        athleteCategories = new ArrayList<>();
        athleteCategories.add(new AthleteCategory("Sub 18", "15", "17", R.drawable.atleta_menor));
        athleteCategories.add(new AthleteCategory("Sub 20", "18", "19", R.drawable.atleta_junior));
        athleteCategories.add(new AthleteCategory("Sub 23", "20", "22", R.drawable.atleta_profesional));
        athleteCategories.add(new AthleteCategory("Adultos", "23", "Mayores", R.drawable.atleta_experimentado));
    }

    public static AthletesCategoriesRepository getInstance(){
        if (athletesCategoriesRepository == null)
            athletesCategoriesRepository = new AthletesCategoriesRepository();
        return athletesCategoriesRepository;
    }

    public List<AthleteCategory> getAthleteCategories() {
        return athleteCategories;
    }
}
