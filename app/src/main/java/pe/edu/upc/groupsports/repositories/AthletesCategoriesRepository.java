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
        athleteCategories.add(new AthleteCategory("Masters", "Mayores", R.drawable.atleta_experimentado,"masters"));
        athleteCategories.add(new AthleteCategory("U30", "30", R.drawable.atleta_profesional,"sub30"));
        athleteCategories.add(new AthleteCategory("U23", "23", R.drawable.atleta_profesional,"sub23"));
        athleteCategories.add(new AthleteCategory("U20", "20", R.drawable.atleta_junior,"sub20"));
        athleteCategories.add(new AthleteCategory("U18", "18", R.drawable.atleta_menor,"sub18"));
        athleteCategories.add(new AthleteCategory("U16", "16", R.drawable.atleta_menor,"sub16"));
        athleteCategories.add(new AthleteCategory("U14", "14", R.drawable.atleta_menor,"sub14"));
        athleteCategories.add(new AthleteCategory("U12", "12", R.drawable.atleta_menor,"sub12"));
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
