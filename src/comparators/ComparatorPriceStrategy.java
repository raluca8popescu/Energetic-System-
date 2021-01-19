package comparators;

import people.Producer;

import java.util.Comparator;

public final class ComparatorPriceStrategy implements Comparator<Producer> {
    @Override
    public int compare(Producer o1, Producer o2) {
        if (o1.getPriceKW() < o2.getPriceKW()) {
            return 1;
        }
        if (o1.getPriceKW() > o2.getPriceKW()) {
            return -1;
        }
        if (o1.getPriceKW().equals(o2.getPriceKW())) {
            if (o1.getEnergyPerDistributor() > o2.getEnergyPerDistributor()) {
                return 1;
            } else {
                if (o1.getEnergyPerDistributor() < o2.getEnergyPerDistributor()) {
                    return -1;
                }
            }
        }
        return 0;
    }
}
