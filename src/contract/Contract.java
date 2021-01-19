package contract;

public abstract class Contract {
    private final int price;
    private int remainedContractMonths;

    public Contract(final int price, final int remainedContractMonths) {
        this.price = price;
        this.remainedContractMonths = remainedContractMonths;
    }

    public final int getPrice() {
        return price;
    }

    public final int getRemainedContractMonths() {
        return remainedContractMonths;
    }

    public final void setRemainedContractMonths(int remainedContractMonths) {
        this.remainedContractMonths = remainedContractMonths;
    }
}
