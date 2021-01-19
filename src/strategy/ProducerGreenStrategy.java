package strategy;

import comparators.ComparatorGreenStrategy;
import people.Distributor;
import people.Producer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ProducerGreenStrategy implements ProducerStrategy {
    private List<Producer> producers;
    private Distributor distributor;

    public ProducerGreenStrategy(List<Producer> producers, Distributor distributor) {
        this.producers = producers;
        this.distributor = distributor;
    }

    @Override
    /**
     * Producatorii sunt sortati dupa strategia Green si se adauga
     * intr-o lista toti producatorii de care distribuitorul are nevoie
     */
    public List<Producer> getProducers() {
        List<Producer> producersForDistributor = new ArrayList<>();
        int energy = distributor.getEnergyNeededKW();

        producers.sort(Collections.reverseOrder(new ComparatorGreenStrategy()));
        distributor.removeFromProducers(producers, distributor);

        for (Producer producer : producers) {
            /*
                Se verifica daca producatorul nu a ajuns la capacitatea maxima
             */
            if (producer.getNumberOfDistributors() < producer.getMaxDistributors()) {
                energy -= producer.getEnergyPerDistributor();
                producersForDistributor.add(producer);
                /*
                    Se adauga distribuitorul in lista producatorului
                    si in cea a observatorilor acestuia
                 */
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
