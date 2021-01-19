package changes;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class DistributorChanges {
    private int id;
    private int infrastructureCost;

    public DistributorChanges(@JsonProperty("id")int id,
                              @JsonProperty("infrastructureCost")int infrastructureCost) {
        this.id = id;
        this.infrastructureCost = infrastructureCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }
}
