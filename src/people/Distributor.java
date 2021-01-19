package people;

import com.fasterxml.jackson.annotation.JsonProperty;
import contract.ContractFactory;
import contract.GeneralContract;
import contract.OutContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public final class Distributor implements Observer {
    private final int id;
    private final int contractLength;
    private int budget;
    private int infrastructureCost;
    private int contractFinalPrice;
    private int profit;
    private int totalMonthlyCost;
    private int productionCost;
    private final List<Consumer> clients;
    private int numberOfConsumers;
    private List<Producer> myProducers;
    private int energyNeededKW;
    private String producerStrategy;


    private static final double CONST_1 = 0.2;
    private static final int CONST_2 = 10;

    public Distributor(@JsonProperty("id") final int id,
                       @JsonProperty("contractLength") final int contractLength,
                       @JsonProperty("initialBudget") final int initialBudget,
                       @JsonProperty("initialInfrastructureCost")
                       final int initialInfrastructureCost,
                       @JsonProperty("energyNeededKW") final int energyNeededKW,
                       @JsonProperty("producerStrategy") final String producerStrategy) {
        this.id = id;
        this.contractLength = contractLength;
        this.budget = initialBudget;
        this.infrastructureCost = initialInfrastructureCost;
        this.numberOfConsumers = 0;
        this.energyNeededKW = energyNeededKW;
        this.producerStrategy = producerStrategy;
        this.clients = new ArrayList<>();
        this.myProducers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getContractLength() {
        return contractLength;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getInfrastructureCost() {
        return infrastructureCost;
    }

    public void setInfrastructureCost(int infrastructureCost) {
        this.infrastructureCost = infrastructureCost;
    }

    public List<Producer> getMyProducers() {
        return myProducers;
    }

    public void setMyProducers(List<Producer> myProducers) {
        this.myProducers = myProducers;
    }

    public int getContractFinalPrice() {
        return contractFinalPrice;
    }

    public int getTotalMonthlyCost() {
        return totalMonthlyCost;
    }

    public int getNumberOfConsumers() {
        return numberOfConsumers;
    }

    public void setNumberOfConsumers(int numberOfConsumers) {
        this.numberOfConsumers = numberOfConsumers;
    }

    public List<Consumer> getClients() {
        return clients;
    }

    public int getEnergyNeededKW() {
        return energyNeededKW;
    }

    public void setEnergyNeededKW(int energyNeededKW) {
        this.energyNeededKW = energyNeededKW;
    }

    public String getProducerStrategy() {
        return producerStrategy;
    }

    public void setProducerStrategy(String producerStrategy) {
        this.producerStrategy = producerStrategy;
    }

    public void setAllCosts(List<Producer> producerList) {
        int cost = 0;
        for (Producer producer : producerList) {
            cost += producer.getEnergyPerDistributor() * producer.getPriceKW();
        }
        this.productionCost = (int) Math.round(Math.floor(cost / CONST_2));
        this.profit = calculateProfit(productionCost);
        this.contractFinalPrice = calculateContractFinalPrice(infrastructureCost, productionCost,
                profit, numberOfConsumers);
        this.totalMonthlyCost = calculateTotalMonthlyCost(infrastructureCost,
                productionCost, numberOfConsumers);
    }

    /**
     * Calculeaza profitul dupa formula
     */
    public int calculateProfit(final int costProduction) {
        return (int) Math.round(Math.floor(CONST_1 * costProduction));
    }
    /**
     * Calculeaza pretul unui contract dupa formula
     */
    public int calculateContractFinalPrice(final int costInfrastructure, final int costProduction,
                                           final int profitt, final int consumers) {
        if (consumers != 0) {
            return (int) Math.round(Math.floor(costInfrastructure / consumers)
                    + costProduction + profitt);
        } else {
            return (costInfrastructure + productionCost + profitt);
        }
    }
    /**
     * Calculeaza costurile totale ale distribuitorului dupa formula
     */
    public int calculateTotalMonthlyCost(final int costInfrastructure,
                                         final int costProduction, final int consumers) {
        return (costInfrastructure + costProduction * consumers);
    }

    /**
     * In funtie de situatie, recalculeaza profitul, pretul si costurile totale.
     * In cazul in situation = 0, se recalculeaza toate, dar daca situatia este 1,
     * se vor recalcula doar profitul si costurile totale deoarece contractele se
     * refac doar la inceput de luna
     */
    public void resetDistributor(final int situation) {
        if (situation == 0) {
            this.profit = calculateProfit(productionCost);
            this.contractFinalPrice = calculateContractFinalPrice(infrastructureCost,
                    productionCost, profit, numberOfConsumers);
            this.totalMonthlyCost = calculateTotalMonthlyCost(infrastructureCost,
                    productionCost, numberOfConsumers);
        } else {
            this.profit = calculateProfit(productionCost);
            this.totalMonthlyCost = calculateTotalMonthlyCost(infrastructureCost,
                    productionCost, numberOfConsumers);
        }
    }

    /**
     * Adauga banii primiti in bugetul distribuitorului
     */
    public void addPayment(final int money) {
        this.budget += money;
    }

    /**
     * Returneaza lista de contracte de format OutContract care i-au mai ramas distribuitorului dupa
     * terminarea celor numberOfTurnes + 1 runde
     */
    public List<OutContract> returnContracts() {
        List<OutContract> outContracts = new ArrayList<>();
        for (Consumer client : clients) {
            GeneralContract generalContract = client.getContract();
            outContracts.add((OutContract) ContractFactory.getFactory().getContract(
                    "OutContract", client.getId(), generalContract.getPrice(),
                    generalContract.getRemainedContractMonths(), -1));
        }
        return outContracts;
    }

    public void removeFromProducers(List<Producer> producerList, Distributor dist) {
        for (Producer producer : producerList) {
            if (producer.getMyDistributors().contains(dist)) {
                producer.getMyDistributors().remove(dist);
                producer.deleteObserverFromList(dist);
                producer.setNumberOfDistributors(producer.getNumberOfDistributors() - 1);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        myProducers.clear();
    }
}
