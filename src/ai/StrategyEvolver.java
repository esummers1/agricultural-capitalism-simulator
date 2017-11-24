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
    
    private static final int NUM_GENERATIONS = 1000;
    
    private static final int POPULATION_SIZE = 2;

    private static final double CHANCE_TO_MUTATE = 0.2;
    
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
    
    /**
     * Combine members of a list of Strategies to produce a new list of the same
     * size.
     * @param generation
     * @return
     */
    private List<Strategy> breed(List<Strategy> generation) {
    	
    	List<Strategy> newGeneration = new ArrayList<>();
    	
    	while (newGeneration.size() < generation.size()) {
    		
    		Strategy father;
    		Strategy mother;
    		
    		Map<Crop, Integer> childWeightings = new HashMap<>();
    		
    		// Acquire a list of two parent Strategies
    		List<Strategy> parentStrategies = chooseParents(generation);
    		
    		// Randomize order of which one is which
    		int r = (int) (Math.random() * 2);
    		
    		if (r == 0) {
    			father = parentStrategies.get(0);
    			mother = parentStrategies.get(1);
    		} else {
    			father = parentStrategies.get(1);
    			mother = parentStrategies.get(0);
    		}
    		
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
     * Return a pair of parent Strategies from the list at random - with earlier
     * (i.e. superior) elements being advantaged.
     * @param generation
     * @return
     */
    private List<Strategy> chooseParents(List<Strategy> generation) {
    	
    	List<Strategy> parents = new ArrayList<>();
    	
    	double r;
    	int counter;
    	
    	// Set probability of selecting first item as 2x the average probability
    	double initialProbability = 2 / POPULATION_SIZE;
    	
    	// Set running probability total to be the initial, for first iteration
    	double cumulativeProbability = initialProbability;
    	
    	// Get the common ratio for the geometric sequence so that it sums to 1
    	double commonRatio = getCommonRatio((double) (POPULATION_SIZE), 
    			initialProbability);
    	
    	while (parents.size() < 2) {
    		
    		// Set a new random value for each search
    		r = Math.random();
    		counter = 1;
    		
    		for (Strategy strategy : generation) {
        		
        		if (r < cumulativeProbability) {
        			parents.add(strategy);
        			break;
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
    	}
    	
    	return parents;
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
    	double nextR = 0;
    	
    	while (nextR - thisR > 0.000001) {
    		thisR = nextR;
    		nextR = (setSize - 2 + 2 * Math.pow(setSize, thisR)) / setSize;
    	}
    	
    	return nextR;    	
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
