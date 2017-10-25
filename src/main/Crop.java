package main;

public class Crop {
    
    private int id;
    private String name;
    private String description;
    private int cost;
    private int salePrice;
    
    /**
     * The heat value that will produce the maximum yield.
     */
    private double idealHeat;

    /**
     * The wetness value that will produce the maximum yield.
     */
    private double idealWetness;

    /**
     * The weighting given to heat in the yield calculation.
     * 
     * 0 = crop is unaffected by heat
     * 1 = crop is normally affected by heat
     * 2 = crop is heavily affected by heat
     */
    private double heatFactor;

    /**
     * The weighting given to wetness in the yield calculation.
     * 
     * 0 = crop is unaffected by wetness
     * 1 = crop is normally affected by wetness
     * 2 = crop is heavily affected by wetness
     */
    private double wetnessFactor;

    /**
     * The overall multiplier applied to the yield.
     */
    private double fertility;
    
    public Crop() {
        // No-argument constructor for reflection
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public int getCost() {
        return cost;
    }
    
    public int getSalePrice() {
        return salePrice;
    }

    public double getHeatFactor() {
        return heatFactor;
    }
    
    public double getWetnessFactor() {
        return wetnessFactor;
    }
    
    public double getIdealHeat() {
        return idealHeat;
    }
    
    public double getIdealWetness() {
        return idealWetness;
    }
    
    public double getFertility() {
        return fertility;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
