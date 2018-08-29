package pe.edu.upc.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.R;
import pe.edu.upc.groupsports.models.MesocycleType;

/**
 * Created by karique on 3/05/2018.
 */

public class MesocycleTypesRepository {
    private static MesocycleTypesRepository mesocycleTypesRepository;
    private List<MesocycleType> mesocycleTypes;

    private MesocycleTypesRepository() {
        mesocycleTypes = new ArrayList<>();
    }

    public static MesocycleTypesRepository getInstance(){
        if (mesocycleTypesRepository == null)
            mesocycleTypesRepository = new MesocycleTypesRepository();
        return mesocycleTypesRepository;
    }

    public List<MesocycleType> getMesocycleTypes() {
        return mesocycleTypes;
    }

    public void setMesocycleTypes(List<MesocycleType> mesocycleTypes) {
        this.mesocycleTypes = mesocycleTypes;
    }

    public MesocycleType getMesocycleByTypeId(String id){
        for (int i = 0; i < mesocycleTypes.size(); i++) {
            if (mesocycleTypes.get(i).getId().equals(id)){
                return mesocycleTypes.get(i);
            }
        }
        return null;
    }

    public int getPosById(String id){
        for (int i = 0; i < mesocycleTypes.size(); i++) {
            if (mesocycleTypes.get(i).getId().equals(id)){
                return i;
            }
        }
        return 0;
    }
}
