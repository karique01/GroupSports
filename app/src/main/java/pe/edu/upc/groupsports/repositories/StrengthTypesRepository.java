package pe.edu.upc.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.models.SaltabilityTestType;
import pe.edu.upc.groupsports.models.StrengthTestType;

/**
 * Created by karique on 3/05/2018.
 */

public class StrengthTypesRepository {
    private static StrengthTypesRepository strengthTypesRepository;
    private List<StrengthTestType> strengthTestTypes;

    private StrengthTypesRepository() {
        strengthTestTypes = new ArrayList<>();
    }

    public static StrengthTypesRepository getInstance(){
        if (strengthTypesRepository == null)
            strengthTypesRepository = new StrengthTypesRepository();
        return strengthTypesRepository;
    }

    public List<StrengthTestType> getStrengthTestTypes() {
        return strengthTestTypes;
    }

    public void setStrengthTestTypes(List<StrengthTestType> strengthTestTypes) {
        this.strengthTestTypes = strengthTestTypes;
    }

    public StrengthTestType getStrengthTestTypeByPos(int pos){
        return strengthTestTypes.get(pos);
    }
}
