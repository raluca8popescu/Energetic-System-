package outputfiles;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import contract.OutContract;

import java.util.List;

@JsonPropertyOrder({ "id", "energyNeededKW", "contractCost",
        "budget", "producerStrategy", "isBankrupt", "contracts" })
public final class OutDistributor {
    private final int id;
    private final int energyNeededKW;
    private final int contractCost;
    private final int budget;
    private final String producerStrategy;
    private final boolean isBankrupt;
    private final List<OutContract> contracts;

    public OutDistributor(final int id, final int energyNeededKW, final int contractCost,
                          final int budget, final String producerStrategy, final boolean isBankrupt,
                          final List<OutContract> contracts) {
        this.id = id;
        this.energyNeededKW = energyNeededKW;
        this.contractCost = contractCost;
        this.budget = budget;
        this.producerStrategy = producerStrategy;
        this.isBankrupt = isBankrupt;
        this.contracts = contracts;
    }

    public int getId() {
        return id;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public int getContractCost() {
        return contractCost;
    }

    public int getBudget() {
        return budget;
    }

    public String getProducerStrategy() {
        return producerStrategy;
    }

    public boolean getIsBankrupt() {
        return isBankrupt;
    }

    public List<OutContract> getContracts() {
        return contracts;
    }

}
