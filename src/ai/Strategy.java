package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.Crop;

public class Strategy implements Comparable<Strategy> {

    private class CropChance implements Comparable<CropChance> {
        
        private Crop crop;
        private double chance;
        
        public CropChance(Crop crop, double chance) {
            this.crop = crop;
            this.chance = chance;
        }

        @Override
        public int compareTo(CropChance o) {
            return Double.compare(o.chance, chance);
        }
        
        @Override
        public String toString() {
            return crop.getName() + ": " + 
                    String.valueOf((int) (chance * 100) + "%");
        }
        
    }
    
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
    
    /**
     * Print a list of the weightings used in this strategy, expressed as
     * percentages of the total weighting.
     */
    @Override
    public String toString() {
    	
        StringBuilder sb = new StringBuilder();
        
        // Report average score in final generation for this strategy
        sb.append("SCORE: " + fitness + ". ");
        
        List<CropChance> cropChances = new ArrayList<>();
        
        for (Entry<Crop, Double> entry : chanceToPlant.entrySet()) {
            cropChances.add(new CropChance(entry.getKey(), entry.getValue()));
        }
        
        Collections.sort(cropChances);
        
        for (CropChance cropChance : cropChances) {
            sb.append(cropChance.toString() + ", ");
        }
        
    	return sb.toString();
    }
    
    public double getChanceToPlant(Crop crop) {
        return chanceToPlant.get(crop);
    }
    
    public Map<Crop, Integer> getCropWeightings() {
    	return cropWeightings;
    }
    
    public int getFitness() {
    	return fitness;
    }
    
    @Override
    public int compareTo(Strategy o) {
    	return Integer.compare(o.fitness, fitness);
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    
    public void setCropWeightings(Map<Crop, Integer> cropWeightings) {
    	this.cropWeightings = cropWeightings;
    }

}
