package ai;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import main.Crop;

public class Strategy implements Comparable<Strategy> {

    private Map<Crop, Integer> cropWeightings = new HashMap<>();
    
    private Map<Crop, Double> chanceToPlant = new HashMap<>();
    
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
        return 0;
    }

}
