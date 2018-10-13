package pe.edu.upc.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.models.MesocycleType;
import pe.edu.upc.groupsports.models.SaltabilityTestType;

/**
 * Created by karique on 3/05/2018.
 */

public class SaltabilityTypesRepository {
    private static SaltabilityTypesRepository saltabilityTypesRepository;
    private List<SaltabilityTestType> saltabilityTestTypes;

    private SaltabilityTypesRepository() {
        saltabilityTestTypes = new ArrayList<>();
    }

    public static SaltabilityTypesRepository getInstance(){
        if (saltabilityTypesRepository == null)
            saltabilityTypesRepository = new SaltabilityTypesRepository();
        return saltabilityTypesRepository;
    }

    public List<SaltabilityTestType> getSaltabilityTestTypes() {
        return saltabilityTestTypes;
    }

    public void setSaltabilityTestTypes(List<SaltabilityTestType> saltabilityTestTypes) {
        this.saltabilityTestTypes = saltabilityTestTypes;
    }

    public SaltabilityTestType getSaltabilityTestTypeByPos(int pos){
        return saltabilityTestTypes.get(pos);
    }
}
