import java.io.File;

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
        TurnsSimulator simulator = new TurnsSimulator(new File(args[0]), new File(args[1]));
        simulator.startGame();
    }
}
