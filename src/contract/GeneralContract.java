package contract;

public final class GeneralContract extends Contract {
    private final int distributorId;
    private boolean isEmpty;
    private boolean frozen;

    public GeneralContract(final int distributorId,
                           final int remainedContractMonths, final int price) {
        super(price, remainedContractMonths);
        this.distributorId = distributorId;
        this.isEmpty = false;
        this.frozen = false;
    }
    public int getDistributorId() {
        return distributorId;
    }

    public boolean getEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
