package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.Console;
import main.Crop;
import main.Field;
import main.Game;

/**
 * Evolutionary algorithm that attempts to find the best strategies for ACS.
 */
public class StrategyEvolver {

    private static final int NUM_GAMES = 20;
    
    private static final int NUM_GENERATIONS = 1000;
    
    private static final int POPULATION_SIZE = 100;

    private static final double CHANCE_TO_MUTATE = 0.2;
    
    /*
     * The initial weighting given to the best performer for the geometric
     * series used to pick the fittest strategies.
     * 
     * A value of 1 means all are equally likely to be picked.
     */
    private static final double SCORE_BIAS = 2;
    
    /*
     * How many generations to play between progress reports.
     */
    private static final int GENERATIONS_PER_SUMMARY = 20;
    
    private List<Crop> crops;
    private List<Field> fields;

    // Suppress output
    private Console console = new Console(null);

    public StrategyEvolver(List<Crop> crops, List<Field> fields) {
        this.crops = crops;
        this.fields = fields;
    }

    public List<Strategy> run() {
        
        System.out.println("Running...");

        List<Strategy> currentGeneration = generateInitialPopulation();
        
        // Iterate for a given number of generations
        for (int i = 0; i < NUM_GENERATIONS; i++) {
            
            // Rate the current generation by fitness
            determineFitness(currentGeneration);
            Collections.sort(currentGeneration);
            
            // Find average fitness (score) across whole generation
            int totalFitness = 0;
            
            for (Strategy strategy : currentGeneration) {
            	totalFitness += strategy.getFitness();
            }
            
            int averageFitness = (int) 
            		((double) totalFitness / (double) currentGeneration.size());
            
            // Give a progress report if we are on a reported generation
            if (i % GENERATIONS_PER_SUMMARY == 0) {
                System.out.println("Generation " + i + " - Average score: " + 
                		averageFitness);
                printTopStrategies(currentGeneration, 5);
            }
            
            if (NUM_GENERATIONS - i != 1) {
	            // Create the next generation
	            List<Strategy> nextGeneration = breed(currentGeneration);
	            mutate(nextGeneration);
	            currentGeneration = nextGeneration;
            }
        }

        // Return the last generation
        Collections.sort(currentGeneration);
        return currentGeneration;
    }
    
    /**
     * Print top strategies found.
     * @param strategies
     * @param num
     */
    public static void printTopStrategies(List<Strategy> strategies, int num) {
        for (int i = 0; i < num; i++) {
            System.out.println(strategies.get(i));
        }
        
        System.out.println();
    }

    /**
     * Randomly generate a population of Strategies.
     * @return
     */
    private List<Strategy> generateInitialPopulation() {
        
        List<Strategy> strategies = new ArrayList<>();
        
        for (int i = 0; i < POPULATION_SIZE; i++) {
            
            Map<Crop, Integer> cropWeightings = new HashMap<>();
            
            for (Crop crop : crops) {
                // Generate a random initial weighting for each crop
                int weighting = (int) (Math.random() * 100);
                cropWeightings.put(crop, weighting);
            }
            
            strategies.add(new Strategy(cropWeightings));
        }
        
        return strategies;        
    }
    
    /**
     * Determine the average fitness of each strategy across games played with
     * all elements of SEEDS.
     * @param currentGeneration
     */
    public void determineFitness(List<Strategy> currentGeneration) {

        for (Strategy strategy : currentGeneration) {
            
            AiInputProvider inputProvider = new AiInputProvider(strategy);

            // Play the game with each seed
            List<Integer> scores = new ArrayList<>();
            for (int i = 0; i < NUM_GAMES; i++) {
                long seed = (long) (Math.random() * Long.MAX_VALUE);
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
    
    /**
     * Combine members of a list of Strategies to produce a new list of the same
     * size.
     * @param generation
     * @return
     */
    private List<Strategy> breed(List<Strategy> generation) {
    	
    	List<Strategy> newGeneration = new ArrayList<>();
    	
    	while (newGeneration.size() < generation.size()) {
    		
    		Map<Crop, Integer> childWeightings = new HashMap<>();
    		
            // Set probability of selecting first item using score bias
            double initialProbability = 
                    SCORE_BIAS / (double) POPULATION_SIZE;
            
            // Get the common ratio for the geometric sequence so that it sums 
            // to 1
            double commonRatio = getCommonRatio((double) (POPULATION_SIZE), 
                    initialProbability);
    		
    		// Select parent strategies
    		Strategy father = 
    		        chooseParent(generation, initialProbability, commonRatio);
    		Strategy mother = 
    		        chooseParent(generation, initialProbability, commonRatio);
    		
			// Take odd number ID crop weightings from 'father' strategy
    		for (Entry<Crop, Integer> weighting : 
    				father.getCropWeightings().entrySet()) {
    			
    			if (weighting.getKey().getId() % 2 != 0) {
    				childWeightings.put(weighting.getKey(), 
    						weighting.getValue());
    			}
    		}
    		
			// Take even number ID crop weightings from 'mother' strategy
    		for (Entry<Crop, Integer> weighting : 
    				mother.getCropWeightings().entrySet()) {
    			
    			if (weighting.getKey().getId() % 2 == 0) {
    				childWeightings.put(weighting.getKey(), 
    						weighting.getValue());
    			}
    		}
    		
    		Strategy child = new Strategy(childWeightings);
    		newGeneration.add(child);
    	}
    	
        return newGeneration;
    }
    
    /**
     * Return a parent Strategy from the list at random - with earlier (i.e. 
     * superior) elements being advantaged.
     * @param generation
     * @param initialProbability 
     * @param commonRatio 
     * @return
     */
    private Strategy chooseParent(List<Strategy> generation, 
            double initialProbability, double commonRatio) {
    	
    	double r;
    	int counter;
    	
    	// Set running probability total to be the initial, for first iteration
    	double cumulativeProbability = initialProbability;

		// Set a new random value for each search
		r = Math.random();
		counter = 1;
		
		for (Strategy strategy : generation) {
    		
    		if (r < cumulativeProbability) {
    			return strategy;
    		}
    		
    		/*
    		 * Hard to explain - imagine listing the assigned probability of
    		 * each element in the list, as they decline geometrically
    		 * (according to the common ratio). This is how you sum all the
    		 * probabilities up to the one just considered in a single call.
    		 */
    		cumulativeProbability = cumulativeProbability + 
    				(initialProbability * Math.pow(commonRatio,  
    				(counter - 1)));
    		
    		counter++;
    	}
		
		return generation.get(generation.size() - 1);
    }
    
    /**
     * Use an iterative technique to derive the common ratio required for a
     * geometric sequence whose setSize terms sum to 1, i.e. a logarithmic
     * probability distribution. This is to be used in chooseParents as a means
     * of biasing high-performing strategies for selection.
     * @param setSize
     * @param initialProbability
     * @return
     */
    private double getCommonRatio(double setSize, double initialProbability) {
    	
    	 double thisR = initialProbability;
    	 double nextR = thisR;
    	 
    	 while (true) {
    		 
    		 nextR = ((setSize - 2) + 2 * (Math.pow(thisR, setSize))) / setSize;
    		 
    		 if (Math.abs(nextR - thisR) < 0.000001) {
    			 return thisR;
    		 } else {
    			 thisR = nextR;
    		 }
    	 }
    }
    
    /**
     * For each Strategy in passed generation, mutate some by re-generating the 
     * weighting for a random crop in its map.
     * @param generation
     */
    private void mutate(List<Strategy> generation) {
        
        for (Strategy strategy : generation) {
            
        	double r = Math.random();
            if (r < CHANCE_TO_MUTATE) {

            	int weightingToChange = (int) (Math.random() * crops.size());
            	int newWeighting = (int) (Math.random() * 100);
            	
            	strategy.getCropWeightings()
            			.put(crops.get(weightingToChange), newWeighting);
            }          
        }
    }

}
