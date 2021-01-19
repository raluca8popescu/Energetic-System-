package changes;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ProducerChanges {
    private int id;
    private int energyPerDistributor;

    public ProducerChanges(@JsonProperty("id")int id,
                           @JsonProperty("energyPerDistributor")int energyPerDistributor) {
        this.id = id;
        this.energyPerDistributor = energyPerDistributor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }
}
