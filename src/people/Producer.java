package people;

import com.fasterxml.jackson.annotation.JsonProperty;
import outputfiles.MonthlyStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public final class Producer extends Observable {
    private int id;
    private String energyType;
    private int maxDistributors;
    private int numberOfDistributors;
    private List<Distributor> myDistributors;
    private Double priceKW;
    private int energyPerDistributor;
    private List<MonthlyStats> monthlyStats;

    public Producer(@JsonProperty("id")int id, @JsonProperty("energyType")String energyType,
                    @JsonProperty("maxDistributors")int maxDistributors,
                    @JsonProperty("priceKW")Double priceKW,
                    @JsonProperty("energyPerDistributor")int energyPerDistributor) {
        this.id = id;
        this.energyType = energyType;
        this.maxDistributors = maxDistributors;
        this.priceKW = priceKW;
        this.energyPerDistributor = energyPerDistributor;
        this.numberOfDistributors = 0;
        this.myDistributors = new ArrayList<>();
        this.monthlyStats = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(int maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public Double getPriceKW() {
        return priceKW;
    }

    public void setPriceKW(Double priceKW) {
        this.priceKW = priceKW;
    }

    public int getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(int energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public int getNumberOfDistributors() {
        return numberOfDistributors;
    }

    public void setNumberOfDistributors(int numberOfDistributors) {
        this.numberOfDistributors = numberOfDistributors;
    }

    public List<Distributor> getMyDistributors() {
        return myDistributors;
    }

    public void setMyDistributors(List<Distributor> myDistributors) {
        this.myDistributors = myDistributors;
    }

    public List<MonthlyStats> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(List<MonthlyStats> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    public void addObserverInList(Distributor distributor) {
        addObserver(distributor);
    }

    public void deleteObserverFromList(Distributor distributor) {
        deleteObserver(distributor);
    }

    public void updateNeeded() {
        setChanged();
        notifyObservers();
    }
}
