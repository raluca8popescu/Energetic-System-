package outputfiles;

import java.util.List;

public final class Output {
    private final List<OutConsumer> consumers;
    private final List<OutDistributor> distributors;
    private final List<OutProducer> energyProducers;

    public Output(final List<OutConsumer> consumers,
                  final List<OutDistributor> distributors,
                  final List<OutProducer> energyProducers) {
        this.consumers = consumers;
        this.distributors = distributors;
        this.energyProducers = energyProducers;
    }
    public List<OutConsumer> getConsumers() {
        return consumers;
    }

    public List<OutDistributor> getDistributors() {
        return distributors;
    }

    public List<OutProducer> getEnergyProducers() {
        return energyProducers;
    }
}
