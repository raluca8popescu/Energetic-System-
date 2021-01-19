package strategy;

import comparators.ComparatorQuantityStrategy;
import people.Distributor;
import people.Producer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ProducerQuantityStrategy implements ProducerStrategy {
    private List<Producer> producers;
    private Distributor distributor;

    public ProducerQuantityStrategy(List<Producer> producers, Distributor distributor) {
        this.producers = producers;
        this.distributor = distributor;
    }

    @Override
    /**
     * Producatorii sunt sortati dupa strategia Quantity si se adauga
     * intr-o lista toti producatorii de care distribuitorul are nevoie
     */
    public List<Producer> getProducers() {
        List<Producer> producersForDistributor = new ArrayList<>();
        int energy = distributor.getEnergyNeededKW();

        producers.sort(Collections.reverseOrder(new ComparatorQuantityStrategy()));
        distributor.removeFromProducers(producers, distributor);
        for (Producer producer : producers) {
            if (producer.getNumberOfDistributors() < producer.getMaxDistributors()) {
                energy -= producer.getEnergyPerDistributor();
                producersForDistributor.add(producer);
                producer.getMyDistributors().add(distributor);
                producer.addObserverInList(distributor);
                producer.setNumberOfDistributors(producer.getNumberOfDistributors() + 1);
                if (energy <= 0) {
                    return producersForDistributor;
                }
            }
        }
        return null;
    }
}
