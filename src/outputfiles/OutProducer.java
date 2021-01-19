package outputfiles;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "id", "maxDistributors", "priceKW",
        "energyType", "energyPerDistributor", "energyPerDistributor", "monthlyStats" })
public final class OutProducer {
    private final int id;
    private final int maxDistributors;
    private final Double priceKW;
    private final String energyType;
    private final int energyPerDistributor;
    private final List<MonthlyStats> monthlyStats;

    public OutProducer(int id, int maxDistributors, Double priceKW,
                       String energyType, int energyPerDistributor,
                       List<MonthlyStats> monthlyStats) {
        this.id = id;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyType = energyType;
        this.energyPerDistributor = energyPerDistributor;
        this.monthlyStats = monthlyStats;
    }

    public int getId() {
        return id;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public Double getPriceKW() {
        return priceKW;
    }

    public String getEnergyType() {
        return energyType;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public List<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }
}
