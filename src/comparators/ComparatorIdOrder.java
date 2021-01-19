package comparators;

import people.Producer;

import java.util.Comparator;

public final class ComparatorIdOrder implements Comparator<Producer> {
    @Override
    public int compare(Producer o1, Producer o2) {
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
