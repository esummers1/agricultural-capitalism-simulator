package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import main.Crop;

public class Strategy implements Comparable<Strategy> {

    private int fitness;
    
    /**
     * The likelihood to plant each Crop, expressed as an arbitrary weight.
     */
    private Map<Crop, Integer> cropWeightings = new HashMap<>();
    
    /**
     * The likelihood to plant each Crop, expressed as a relative double.
     * 
     * These should add to 1.
     */
    private Map<Crop, Double> chanceToPlant = new HashMap<>();
    
    /**
     * Creates a new Strategy using the given weightings.
     * 
     * @param cropWeightings
     */
    public Strategy(Map<Crop, Integer> cropWeightings) {
        this.cropWeightings = cropWeightings;
        recalculateChanceToPlant();
    }
    
    /**
     * Recalculates the chance to plant each crop based on the weightings.
     */
    public void recalculateChanceToPlant() {
        
        int totalWeight = cropWeightings.values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
        
        for (Entry<Crop, Integer> cropWeighting : cropWeightings.entrySet()) {
            double chance = (double) cropWeighting.getValue() / totalWeight;
            chanceToPlant.put(cropWeighting.getKey(), chance);
        }
    }
    
    public double getChanceToPlant(Crop crop) {
        return chanceToPlant.get(crop);
    }
    
    @Override
    public int compareTo(Strategy o) {
        return fitness - o.fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

}
