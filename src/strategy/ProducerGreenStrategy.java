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
    public List<Producer> getProducers() {
        List<Producer> producersForDistributor = new ArrayList<>();
        int energy = distributor.getEnergyNeededKW();

        producers.sort(Collections.reverseOrder(new ComparatorGreenStrategy()));
        distributor.removeFromProducers(producers, distributor);
        for (Producer producer : producers) {
//            if(producer.getMyDistributors().contains(distributor)){
//                producer.getMyDistributors().remove(distributor);
//                producer.setNumberOfDistributors(producer.getNumberOfDistributors() - 1);
//            }

            if (producer.getNumberOfDistributors() < producer.getMaxDistributors()) {
                energy -= producer.getEnergyPerDistributor();
                producersForDistributor.add(producer);
                producer.getMyDistributors().add(distributor);
                producer.setNumberOfDistributors(producer.getNumberOfDistributors() + 1);
                if (energy <= 0) {
                    return producersForDistributor;
                }
            }
        }
        return null;
    }
}
