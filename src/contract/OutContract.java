package contract;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"consumerId", "price", "remainedContractMonths"})
public final class OutContract extends Contract {
    private final int consumerId;

    public OutContract(final int consumerId, final int price,
                         final int remainedContractMonths) {
        super(price, remainedContractMonths);
        this.consumerId = consumerId;
    }

    public int getConsumerId() {
        return consumerId;
    }

}
