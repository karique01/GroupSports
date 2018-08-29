package pe.edu.upc.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.AthleteCategory;
import pe.edu.upc.groupsports.models.WeekType;

/**
 * Created by karique on 3/05/2018.
 */

public class WeekTypeRepository {
    private static WeekTypeRepository weekTypeRepository;
    private List<WeekType> weekTypes;

    private WeekTypeRepository() {
        weekTypes = new ArrayList<>();
        weekTypes.add(new WeekType(1,"Intenso"));
        weekTypes.add(new WeekType(2,"Descanso"));
    }

    public static WeekTypeRepository getInstance(){
        if (weekTypeRepository == null)
            weekTypeRepository = new WeekTypeRepository();
        return weekTypeRepository;
    }

    public List<WeekType> getWeekTypes() {
        return weekTypes;
    }
}
