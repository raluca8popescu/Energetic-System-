package strategy;

import people.Producer;

import java.util.List;

public interface ProducerStrategy {
    /**
     * Functia de returnare a producatorilor
     * ce va fi utilizata in cele 3 tipuri de strategie
     */
    List<Producer> getProducers();
}
