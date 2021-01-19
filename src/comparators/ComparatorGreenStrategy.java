package comparators;

import people.Producer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ComparatorGreenStrategy implements Comparator<Producer> {
    @Override
    /**
     *  fhwksjeh
     */
    public int compare(Producer o1, Producer o2) {
        List<String> renewable = new ArrayList<>();

        renewable.add("WIND");
        renewable.add("HYDRO");
        renewable.add("SOLAR");

        String type1 = o1.getEnergyType();
        String type2 = o2.getEnergyType();

        if (renewable.contains(type1) && (!renewable.contains(type2))) {
            return 1;
        }
        if (renewable.contains(type2) && (!renewable.contains(type1))) {
            return -1;
        }
        if ((renewable.contains(type1) && renewable.contains(type2))
                || ((!renewable.contains(type1)) && (!renewable.contains(type2)))) {
            if (o1.getPriceKW() < o2.getPriceKW()) {
                return 1;
            } else {
                if (o1.getPriceKW() > o2.getPriceKW()) {
                    return -1;
                } else {
                    if (o1.getEnergyPerDistributor() > o2.getEnergyPerDistributor()) {
                        return 1;
                    } else {
                        if (o1.getEnergyPerDistributor() < o2.getEnergyPerDistributor()) {
                            return -1;
                        }
                        return 0;
                    }
                }
            }
        }

        return 0;
    }
}
