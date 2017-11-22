package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

import main.Console;
import main.Crop;
import main.Field;
import main.Game;

/**
 * Evolutionary algorithm that attempts to find the best strategies for ACS.
 */
public class StrategyEvolver {

    private static final long[] SEEDS = new long[]{
            1000,
            2000,
            3000,
            4000,
            5000,
            6000,
            7000,
            8000,
            9000,
            10000
    };
    
    private static final int NUM_GENERATIONS = 1;
    private static final int POPULATION_SIZE = 10;
    
    private List<Crop> crops;
    private List<Field> fields;

    // Suppress output
    private Console console = new Console(null);

    public StrategyEvolver(List<Crop> crops, List<Field> fields) {
        this.crops = crops;
        this.fields = fields;
    }

    public List<Strategy> run() {

        List<Strategy> currentGeneration = generateInitialPopulation();
        
        // Iterate for a given number of generations
        for (int i = 0; i < NUM_GENERATIONS; i++) {
            
            // Rate the current generation by fitness
            determineFitness(currentGeneration);
            Collections.sort(currentGeneration);
            
            // Create the next generation
            List<Strategy> nextGeneration = breed(currentGeneration);
            mutate(nextGeneration);
            currentGeneration = nextGeneration;
        }

        // Sort and return the last generation
        Collections.sort(currentGeneration);
        return currentGeneration;
    }

    private List<Strategy> generateInitialPopulation() {
        
        List<Strategy> strategies = new ArrayList<>();
        
        for (int i = 0; i < POPULATION_SIZE; i++) {
            
            int[] cropWeightings;
            
            // TODO: work out weightings and pass into strategy
            Strategy strategy = new Strategy();
            
            
        }
        
        return strategies;        
    }

    public void determineFitness(List<Strategy> currentGeneration) {

        for (Strategy strategy : currentGeneration) {
            
            AiInputProvider inputProvider = new AiInputProvider(strategy);

            // Play the game with each seed
            List<Integer> scores = new ArrayList<>();
            for (Long seed : SEEDS) {
                Game game = new Game(seed, inputProvider, console, crops, 
                        fields);
                int score = game.run();
                scores.add(score);
            }

            // Determine the average score for this Strategy
            double avg = scores
                    .stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .getAsDouble();
            
            strategy.setFitness((int) avg);
        }
    }

    private List<Strategy> breed(List<Strategy> currentGeneration) {
        return null;
    }

    private void mutate(List<Strategy> nextGeneration) {
        // TODO Auto-generated method stub
        
    }

}
