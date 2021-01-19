import com.fasterxml.jackson.databind.ObjectMapper;
import comparators.ComparatorIdOrder;
import contract.OutContract;
import inputfiles.InitialData;
import inputfiles.Input;
import inputfiles.MonthlyUpdates;
import outputfiles.OutConsumer;
import outputfiles.OutDistributor;
import outputfiles.OutProducer;
import outputfiles.Output;
import people.Consumer;
import people.Distributor;
import people.Producer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TurnsSimulator {
    private final File fileIn;
    private final File fileOut;

    public TurnsSimulator(File fileIn, File fileOut) {
        this.fileIn = fileIn;
        this.fileOut = fileOut;
    }

    /**
     * Crearea claselor de input, output si rularea rundelor simularii
     */
    public void startGame() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Input input = mapper.readValue(fileIn, Input.class);
        InitialData data = input.getInitialData();
        List<Consumer> consumers = data.getConsumers();
        List<Distributor> distributors = data.getDistributors();
        List<Producer> producers = data.getProducers();

        /*
            Runda 0
         */
        data.choiceOfProducer();
        data.setDistributorscosts();
        data.addMonthlyIncome();
        data.contractSetter();
        data.payContractPrice();
        data.payMonthlyCost();
        data.lowerTurn();

        /*
            Derularea rundelor
         */

        for (int i = 1; i <= input.getNumberOfTurns(); i++) {
            data.updateContract();
            MonthlyUpdates updates = input.getMonthlyUpdates().get(i - 1);
            if (updates.getDistributorChanges().size() != 0) {
                for (int j = 0; j < updates.getDistributorChanges().size(); j++) {
                    data.modifyDistributor(updates.getDistributorChanges().get(j));
                }
            }
            data.removeContracts();
            if (updates.getNewConsumers().size() != 0) {
                consumers.addAll(updates.getNewConsumers());
            }

            data.addMonthlyIncome();
            data.contractSetter();
            data.payContractPrice();
            data.payMonthlyCost();
            if (updates.getProducerChanges().size() != 0) {
                for (int j = 0; j < updates.getProducerChanges().size(); j++) {
                    data.modifyProducer(updates.getProducerChanges().get(j));
                }
            }

            data.choiceOfProducer();

            if (i != input.getNumberOfTurns()) {
                data.setDistributorscosts();
            }
            data.monthlyStats(i);
            data.lowerTurn();
                data.removeBankrupt();
            if (data.isEndOfGame()) {
                break;
            }
        }

        producers.sort(Collections.reverseOrder(new ComparatorIdOrder()));
        /*
            Construirea claselor de output pentru fisierele json
         */
        List<OutConsumer> outConsumers = new ArrayList<>();
        List<OutDistributor> outDistributors = new ArrayList<>();
        List<OutProducer> outProducers = new ArrayList<>();

        for (Consumer consumer : consumers) {
            boolean isBankrupt;
            isBankrupt = data.getBankruptConsumers().contains(consumer);
            outConsumers.add(new OutConsumer(consumer.getId(), isBankrupt, consumer.getBudget()));
        }

        List<OutContract> contracts;
        for (Distributor distributor : distributors) {
            boolean isBankrupt;
            isBankrupt = data.getBankruptDistributors().
                    contains(distributor);
            contracts = distributor.returnContracts();
            outDistributors.add(new OutDistributor(distributor.getId(),
                    distributor.getEnergyNeededKW(),
                    distributor.getContractFinalPrice(), distributor.getBudget(),
                    distributor.getProducerStrategy(), isBankrupt, contracts));
        }

        for (Producer producer : producers) {
            outProducers.add(new OutProducer(producer.getId(),
                    producer.getMaxDistributors(), producer.getPriceKW(),
                    producer.getEnergyType(), producer.getEnergyPerDistributor(),
                    producer.getMonthlyStats()));
        }
        Output output = new Output(outConsumers, outDistributors, outProducers);

        mapper.writerWithDefaultPrettyPrinter().writeValue(fileOut, output);
    }

}
