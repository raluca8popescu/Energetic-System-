package inputfiles;

import changes.DistributorChanges;
import changes.ProducerChanges;
import com.fasterxml.jackson.annotation.JsonProperty;
import people.Consumer;

import java.util.List;

public final class MonthlyUpdates {
    private final List<Consumer> newConsumers;
    private List<DistributorChanges> distributorChanges;
    private List<ProducerChanges> producerChanges;

    public MonthlyUpdates(@JsonProperty("newConsumers") final List<Consumer> newConsumers,
                          @JsonProperty("distributorChanges")
                          final List<DistributorChanges> distributorChanges,
                          @JsonProperty("producerChanges")
                          final List<ProducerChanges> producerChanges) {
        this.newConsumers = newConsumers;
        this.distributorChanges = distributorChanges;
        this.producerChanges = producerChanges;
    }

    public List<Consumer> getNewConsumers() {
        return newConsumers;
    }

    public List<DistributorChanges> getDistributorChanges() {
        return distributorChanges;
    }

    public List<ProducerChanges> getProducerChanges() {
        return producerChanges;
    }
}
