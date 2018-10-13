package pe.edu.upc.groupsports.repositories;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.groupsports.models.Size;
import pe.edu.upc.groupsports.models.WeekType;

/**
 * Created by karique on 3/05/2018.
 */

public class SizeRepository {
    private static SizeRepository sizeRepository;
    private List<Size> sizes;

    private SizeRepository() {
        sizes = new ArrayList<>();
        sizes.add(new Size("S"));
        sizes.add(new Size("M"));
        sizes.add(new Size("L"));
        sizes.add(new Size("XL"));
        sizes.add(new Size("XXL"));
    }

    public static SizeRepository getInstance(){
        if (sizeRepository == null)
            sizeRepository = new SizeRepository();
        return sizeRepository;
    }

    public List<Size> getSizes() {
        return sizes;
    }
}
