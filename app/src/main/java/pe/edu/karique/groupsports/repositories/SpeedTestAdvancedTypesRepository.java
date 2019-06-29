package pe.edu.karique.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.karique.groupsports.models.SpeedTestAdvanceType;
import pe.edu.karique.groupsports.models.WeekType;

public class SpeedTestAdvancedTypesRepository {

    private static SpeedTestAdvancedTypesRepository speedTestAdvancedTypesRepository;
    private List<SpeedTestAdvanceType> speedTestAdvanceTypes;

    private SpeedTestAdvancedTypesRepository() {
        speedTestAdvanceTypes = new ArrayList<>();
        speedTestAdvanceTypes.add(new SpeedTestAdvanceType("1","10m"));
        speedTestAdvanceTypes.add(new SpeedTestAdvanceType("2","20m"));
        speedTestAdvanceTypes.add(new SpeedTestAdvanceType("3","30m"));
        speedTestAdvanceTypes.add(new SpeedTestAdvanceType("4","40m"));
        speedTestAdvanceTypes.add(new SpeedTestAdvanceType("5","50m"));
        speedTestAdvanceTypes.add(new SpeedTestAdvanceType("6","100m"));
    }

    public static SpeedTestAdvancedTypesRepository getInstance(){
        if (speedTestAdvancedTypesRepository == null)
            speedTestAdvancedTypesRepository = new SpeedTestAdvancedTypesRepository();
        return speedTestAdvancedTypesRepository;
    }

    public List<SpeedTestAdvanceType> getSpeedTestAdvanceTypes() {
        return speedTestAdvanceTypes;
    }

}
