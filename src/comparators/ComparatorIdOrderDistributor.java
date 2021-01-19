package comparators;

import people.Distributor;

import java.util.Comparator;

public final class ComparatorIdOrderDistributor implements Comparator<Distributor> {
    @Override
    public int compare(Distributor o1, Distributor o2) {
        if (o1.getId() < o2.getId()) {
            return 1;
        } else {
            if (o1.getId() > o2.getId()) {
                return -1;
            }
        }
        return 0;
    }
}
