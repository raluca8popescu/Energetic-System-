package inputfiles;

import changes.DistributorChanges;
import changes.ProducerChanges;
import com.fasterxml.jackson.annotation.JsonProperty;
import comparators.ComparatorIdOrderDistributor;
import contract.ContractFactory;
import contract.GeneralContract;
import outputfiles.MonthlyStats;
import people.Consumer;
import people.Distributor;
import people.Producer;
import strategy.ProducerGreenStrategy;
import strategy.ProducerPriceStrategy;
import strategy.ProducerQuantityStrategy;
import strategy.ProducerStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InitialData {
    private final List<Consumer> consumers;
    private final List<Distributor> distributors;
    private final List<Producer> producers;
    private final List<Consumer> latePayers;
    private final List<Consumer> bankruptConsumers;
    private final List<Distributor> bankruptDistributors;
    private  boolean endOfGame;

    public static final int MAX_NUMBER = 100000;
    public static final double CONST_2 = 1.2;

    public InitialData(@JsonProperty("consumers")final List<Consumer> consumers,
                       @JsonProperty("distributors")
                       final List<Distributor> distributors,
                       @JsonProperty("producers") final List<Producer> producers) {
        this.consumers = consumers;
        this.distributors = distributors;
        this.producers = producers;
        this.latePayers = new ArrayList<>();
        this.bankruptConsumers = new ArrayList<>();
        this.bankruptDistributors = new ArrayList<>();
        this.endOfGame = false;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public List<Distributor> getDistributors() {
        return distributors;
    }

    public List<Consumer> getBankruptConsumers() {
        return bankruptConsumers;
    }

    public List<Distributor> getBankruptDistributors() {
        return bankruptDistributors;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public boolean isEndOfGame() {
        return endOfGame;
    }


    /**
     *  Alege producatorii pentru fiecare distribuitor care are
     *  lista de producatori goala, in functie de strategia corespunzatoare
     */
    public void choiceOfProducer() {
        for (Distributor distributor : distributors) {
            if (!bankruptDistributors.contains(distributor)) {
                if (distributor.getMyProducers().isEmpty()) {
                    if (distributor.getProducerStrategy().equals("GREEN")) {
                        ProducerStrategy strategy = new
                                ProducerGreenStrategy(producers, distributor);
                        List<Producer> thisProducers = strategy.getProducers();
                        distributor.setMyProducers(thisProducers);
                    }
                    if (distributor.getProducerStrategy().equals("PRICE")) {
                        ProducerStrategy strategy = new
                                ProducerPriceStrategy(producers, distributor);
                        List<Producer> thisProducers = strategy.getProducers();
                        distributor.setMyProducers(thisProducers);
                    }
                    if (distributor.getProducerStrategy().equals("QUANTITY")) {
                        ProducerStrategy strategy = new
                                ProducerQuantityStrategy(producers, distributor);
                        List<Producer> thisProducers = strategy.getProducers();
                        distributor.setMyProducers(thisProducers);
                    }
                }
            }
        }
    }
    /**
     *  Pentru fiecare distribuitor, se apeleaza functia care calculeaza
     *  toate costurile in functie de producatorii sai
     */
    public void setDistributorscosts() {
        for (Distributor distributor : distributors) {
            distributor.setAllCosts(distributor.getMyProducers());
        }
    }

    /**
     * Functia reseteaza contractele la inceputul fiecarei runde
     */
    public void updateContract() {
        for (Distributor distributor : distributors) {
            distributor.resetDistributor(0);
        }
    }

    /**
     * Modifica un distribuitor cu noile date si il reseteaza
     */
    public void modifyDistributor(DistributorChanges distributorChanges) {
        for (Distributor distributor : distributors) {
            if (distributor.getId() ==  distributorChanges.getId()) {
                distributor.setInfrastructureCost(distributorChanges.getInfrastructureCost());
                distributor.resetDistributor(0);
            }
        }
    }
    /**
     *  Returneaza producatorul caruia ii corespunde id-ul dat
     *  ca parametru
     */
    public Producer producerId(int id) {
        for (Producer producer : producers) {
            if (producer.getId() == id) {
                return producer;
            }
        }
        return null;
    }
    /**
     *  Se modifica producatorul in functie de update-urile primite
     *  si se apeleaza functia care notifica observatorii ca trebuie
     *  sa faca update-uri
     */
    public void modifyProducer(ProducerChanges producerChanges) {
        Producer producer = producerId(producerChanges.getId());
        producer.setEnergyPerDistributor(producerChanges.getEnergyPerDistributor());
        List<Distributor> myDistributors = producer.getMyDistributors();
        if (!myDistributors.isEmpty()) {
            producer.updateNeeded();
        }
    }

    /**
     * Consumatorilor aflati in runda 0 sau celor care nu mai au distribuitor si
     * nu sunt falimentati li se va crea un contract si se va adauga consumatorul
     * in lista de clienti a distribuitorului
     */
    public void contractSetter() {
        Distributor dist;
        for (Consumer consumer : consumers) {
                if (consumer.getContract() == null) {
                    consumer.setContract(bestDistributor());
                    dist = getDistributor(consumer.getContract().getDistributorId());
                    dist.getClients().add(consumer);
                    dist.setNumberOfConsumers(dist.getNumberOfConsumers() + 1);
                } else {
                    if (!consumer.getContract().isFrozen()) {
                        if (consumer.getContract().getEmpty()) {
                            consumer.setContract(bestDistributor());
                            dist = getDistributor(consumer.getContract().getDistributorId());
                            if (!dist.getClients().contains(consumer)) {
                                dist.getClients().add(consumer);
                                dist.setNumberOfConsumers(dist.getNumberOfConsumers() + 1);
                            }
                        }
                    }
                }
        }
    }
    /**
     * Returneaza distribuitorul cu cel mai bun pret al contractului din lista
     * de distribuitori, mai putin cei aflati in faliment
     */
    public GeneralContract bestDistributor() {
        int minim = MAX_NUMBER;
        int id = 0;
        int duration = 0;
        GeneralContract bestGeneralContract;
        for (Distributor distributor : distributors) {
            if (!bankruptDistributors.contains(distributor)) {
                if (distributor.getContractFinalPrice() < minim) {
                    minim = distributor.getContractFinalPrice();
                    id = distributor.getId();
                    duration = distributor.getContractLength();
                }
            }
        }
        bestGeneralContract = (GeneralContract) ContractFactory.getFactory().
                getContract("GeneralContract", -1, minim, duration, id);
        return bestGeneralContract;
    }

    /**
     * Returneaza distribuitorul cu id-ul dat ca parametru functiei
     */
    public Distributor getDistributor(final int id) {
        for (Distributor distributor : distributors) {
            if (distributor.getId() == id) {
                return distributor;
            }
        }
        return null;
    }

    /**
     * Scoate din lista de clienti a unui distribuitor toti consumatorii
     * care au dat faliment
     */
    public void removeBankrupt() {
        for (Consumer consumer : consumers) {
            if (bankruptConsumers.contains(consumer) && (!consumer.getContract().isFrozen())) {
                consumer.getContract().setFrozen(true);
                Distributor dist = getDistributor(consumer.getContract().getDistributorId());
                dist.getClients().remove(consumer);
                dist.setNumberOfConsumers(dist.getNumberOfConsumers() - 1);
            }
        }
    }

    /**
     * Adaugarea venitului lunar pentru fiecare consumator care
     * nu este in faliment
     */
    public void addMonthlyIncome() {
        for (Consumer consumer : consumers) {
            if (!bankruptConsumers.contains(consumer)) {
                consumer.setBudget(consumer.getBudget() + consumer.getMonthlyIncome());
            }
        }
    }

    /**
     * Functia realizeaza platile lunare pentru toti consumatorii care nu sunt in faliment
     * Daca un consumator este in lista de latePayers are 2 variante:
     * - nu poate sa plateasca si intra in faliment
     * - plateste si nu mai este latePayer
     * Daca un consumator nu este in lista latePayers are 2 variante:
     * - nu poate sa plateasca si ajunge in lista de latePayers
     * - plateste
     */
    public void payContractPrice() {
        for (Distributor distributor : distributors) {
            for (int i = 0; i < distributor.getClients().size(); i++) {
                Consumer client = distributor.getClients().get(i);
                if (!bankruptConsumers.contains(client)) {
                    /*
                        Pentru cei aflati in lista cu consumatorii
                        care nu si-au putut plati taxa pe luna anterioara
                     */
                    if (latePayers.contains(client)) {
                        int latePrice = (int) Math.round(Math.
                                floor(CONST_2 * client.getExFinalPrice()))
                                + client.getContract().getPrice();
                        /*
                            Consumatorii care nu pot plati nici aceasta luna,
                            intra in faliment, iar ceilalti sunt scosi de la
                            consumatori intarziati
                         */
                        if (client.getBudget() - latePrice < 0) {
                            client.getContract().setRemainedContractMonths(-1);
                            latePayers.remove(client);
                            bankruptConsumers.add(client);
                        } else {
                            client.setBudget(client.getBudget() - latePrice);
                            if (client.getContract().getDistributorId()
                                    != client.getExId()) {
                                distributor.addPayment(client.getContract()
                                        .getPrice());
                                Distributor dist = getDistributor(client.getExId());
                                dist.addPayment((int) Math.round(Math.floor(CONST_2
                                        * client.getExFinalPrice())));
                            } else {
                                distributor.addPayment(latePrice);
                            }
                            latePayers.remove(client);
                        }
                    } else {
                        /*
                            Daca nu poate sa plateasca taxa, este adaugat la lista de
                            consumatori intarziati
                         */
                        if (client.getBudget() - client.getContract().getPrice() < 0) {
                            latePayers.add(client);
                            client.setExFinalPrice(client.getContract().getPrice());
                            client.setExId(client.getContract().getDistributorId());
                        } else {
                            client.setBudget(client.getBudget()
                                    - client.getContract().getPrice());
                            distributor.addPayment(client.getContract().getPrice());
                        }
                    }
                }
            }
        }
    }

    /**
     * Functia realizeaza platile lunare ale distribuitorilor
     * Daca bugetul unui distribuitor ajunge negativ, acesta va da faliment
     * si toti consumatorii sunt eliberati de contract
     * Daca toti distribuitorii ajung in faliment endOfGame va deveni true si se
     * va incheia jocul
     */
    public void payMonthlyCost() {
        for (Distributor distributor : distributors) {
            if (!bankruptDistributors.contains(distributor)) {
                distributor.resetDistributor(1);
                distributor.setBudget(distributor.getBudget() - distributor.getTotalMonthlyCost());
                /*
                    Adaugarea la lista de falimentati a distribuitorilor
                 */
                if (distributor.getBudget() < 0) {
                    bankruptDistributors.add(distributor);
                    for (int i = 0; i < distributor.getClients().size(); i++) {
                        Consumer client = distributor.getClients().get(i);
                        client.getContract().setEmpty(true);
                    }
                   for (Producer producer : distributor.getMyProducers()) {
                       producer.getMyDistributors().remove(distributor);
                       producer.setNumberOfDistributors(producer.getNumberOfDistributors() - 1);
                   }
                    distributor.getClients().removeAll(distributor.getClients());
                    distributor.setNumberOfConsumers(0);
                }
            }
        }
        /*
            Daca toti distribuitorii au falimentat
         */
        if (bankruptDistributors.size() == distributors.size()) {
            endOfGame = true;
        }
    }

    /**
     * Scade cu 1 numarul lunilor de contract ramase pentru toti consumatorii
     */
    public void lowerTurn() {
        for (Consumer consumer : consumers) {
            if (!consumer.getContract().getEmpty() && !bankruptConsumers.contains(consumer)) {
                consumer.getContract().setRemainedContractMonths(
                        consumer.getContract().getRemainedContractMonths() - 1);
            }
        }
    }
    /**
     * Elimina toti consumatorii din lista Clients care si-au terminat contractul
     * cu un distribuitor si elibereaza contractul
     */
    public void removeContracts() {
        for (Consumer consumer : consumers) {
            if (consumer.getContract().getRemainedContractMonths() == 0
                    && (!consumer.getContract().isFrozen())) {
                Distributor dist = getDistributor(consumer.getContract().getDistributorId());
                dist.getClients().remove(consumer);
                dist.setNumberOfConsumers(dist.getNumberOfConsumers() - 1);
                consumer.getContract().setEmpty(true);
            }
        }
    }

    /**
     * Creeaza lista  lunara de distribuitori  pentru fiecare producator
     */
    public void monthlyStats(int month) {
        for (Producer producer : producers) {
            List<MonthlyStats> stats = producer.getMonthlyStats();
            List<Distributor> myDistributors = producer.getMyDistributors();
            List<Integer> distributorsIds = new ArrayList<>();

            myDistributors.sort(Collections.reverseOrder(new ComparatorIdOrderDistributor()));
            for (Distributor distributor : myDistributors) {
                distributorsIds.add(distributor.getId());
            }
            stats.add(new MonthlyStats(month, distributorsIds));
        }
    }
}
