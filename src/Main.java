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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() { }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File fileIn = new File(args[0]);
        File fileOut = new File(args[1]);

//        File fileIn = new File("/home/raluca/Documents/poo/ProiectEtapa2/teme-proiect-etapa2-2020/teme/proiect-etapa2-energy-system/checker/resources/in/basic_12.json");
//        File fileOut = new File("/home/raluca/Documents/poo/ProiectEtapa2/teme-proiect-etapa2-2020/teme/proiect-etapa2-energy-system/checker/resources/out.json");

        Input input = mapper.readValue(fileIn, Input.class);
        InitialData data = input.getInitialData();
        List<Consumer> consumers = data.getConsumers();
        List<Distributor> distributors = data.getDistributors();
        List<Producer> producers = data.getProducers();



        data.choiceOfProducer();
        data.setDistributorscosts();
        data.addMonthlyIncome();
        data.contractSetter();
        data.payContractPrice();
        data.payMonthlyCost();
        data.lowerTurn();
        data.removeBankrupt();

//        System.out.println("RUNDA " + 0);
//        System.out.println("Distributor");
//        System.out.println("Buget " + distributors.get(0).getBudget());
//        System.out.println("Cost productie: " + distributors.get(0).getProductionCost());
//        System.out.println("Infrastructura: " + distributors.get(0).getInfrastructureCost());
//        System.out.println("Profit: " + distributors.get(0).getProfit());
//        System.out.println("Contract:" + distributors.get(0).getContractFinalPrice());
//        System.out.println("Clienti: " + distributors.get(0).getNumberOfConsumers());
//        System.out.println("Producatori" + distributors.get(0).getMyProducers());
//        System.out.println("\n");
//        System.out.println("Consumatori");

//        for (int j = 0; j < consumers.size(); j++) {
//            System.out.println(consumers.get(j));
//        }
//        System.out.println("\n");
//
//        System.out.println("Producatori");
//
//        System.out.println("\n");

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

//
//                System.out.println("Distributors");
//                for(Distributor distributor : distributors){
//                    System.out.print("Distributor " + distributor.getId());
//                    System.out.print("\n");
//                    for(Producer producer : distributor.getMyProducers()){
//                        System.out.print(producer.getId() + " ");
//                    }
//                    System.out.print("\n");
//                }
//                System.out.println("Producatori" + distributors.get(0).getMyProducers());
//                System.out.println("Buget " + distributors.get(0).getBudget());
//                System.out.println("Cost productie: " +
//                distributors.get(0).getProductionCost());
//                System.out.println("Infrastructura: " +
//                distributors.get(0).getInfrastructureCost());
//                System.out.println("Profit: " + distributors.get(0).getProfit());
//                System.out.println("Contract:" + distributors.get(0).getContractFinalPrice());
//                System.out.println("Clienti: " + distributors.get(0).getNumberOfConsumers());
//                System.out.println("\n");
//                System.out.println("Consumatori");
//                for(int j = 0; j < consumers.size(); j++){
//                    System.out.println(consumers.get(j));
//                }
//                System.out.println("\n");
//
//                System.out.println("Producatori");
//                for (int j = 0; j < producers.size(); j++) {
//                    System.out.println(producers.get(j));
//                }
//            System.out.println("\n");

//                System.out.println(distributors.get(0).getContractFinalPrice());
//                System.out.println("Producatori" + distributors.get(0).getMyProducers());
//                System.out.println("Infrastructura: " + distributors.get(0).getInfrastructureCost());
//                System.out.println("Profit: " + distributors.get(0).getProfit());
//                System.out.println("Consumatori: " + distributors.get(0).getNumberOfConsumers());
                data.lowerTurn();
                if (i != input.getNumberOfTurns()) {
                    data.removeBankrupt();
                }
                if (data.isEndOfGame()) {
                    break;
                }
        }

        producers.sort(Collections.reverseOrder(new ComparatorIdOrder()));
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
