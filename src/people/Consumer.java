package people;

import com.fasterxml.jackson.annotation.JsonProperty;
import contract.GeneralContract;

public final class Consumer {
    private final int id;
    private int budget;
    private final int monthlyIncome;
    private GeneralContract generalContract;
    private int exFinalPrice;
    private int exId;

    public Consumer(@JsonProperty("id")final int id,
                    @JsonProperty("initialBudget") final int initialBudget,
                    @JsonProperty("monthlyIncome") final int monthlyIncome) {
        this.id = id;
        this.budget = initialBudget;
        this.monthlyIncome = monthlyIncome;
    }

    public int getId() {
        return id;
    }

    public int getBudget() {
        return budget;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public GeneralContract getContract() {
        return generalContract;
    }

    public void setContract(GeneralContract contract) {
        this.generalContract = contract;
    }
    public int getExFinalPrice() {
        return exFinalPrice;
    }

    public void setExFinalPrice(int exFinalPrice) {
        this.exFinalPrice = exFinalPrice;
    }

    public int getExId() {
        return exId;
    }

    public void setExId(int exId) {
        this.exId = exId;
    }
}
