package contract;

public final class ContractFactory {
    private static ContractFactory factoryContract;

    /**
     * Pattern-ul Singleton pentru a crea doar o instanta de Factory
     */
    public static ContractFactory getFactory() {
        if (factoryContract == null) {
            factoryContract = new ContractFactory();
        }
        return factoryContract;
    }

    /**
     * Construieste un tip de contract in functie de string-ul pe care il primeste functia
     */
    public Contract getContract(final String contractType, final int consumerId,
                                       final int price, final int remainedContractMonths,
                                       final int distributorId) {
        return switch (contractType) {
            case "GeneralContract" -> new GeneralContract(distributorId,
                                    remainedContractMonths, price);
            case "OutContract" -> new OutContract(consumerId, price, remainedContractMonths);
            default -> throw new IllegalStateException("Unexpected value: " + contractType);
        };
    }

}
