package main;

public class Crop {
    
    private int id;
    private String name;
    private String description;
    private int cost;
    private int salePrice;
    private double yieldMultiplierWetness;
    private double yieldMultiplierHeat;
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
    
    public double getYieldMultiplierWetness() {
        return yieldMultiplierWetness;
    }
    
    public double getYieldMultiplierHeat() {
        return yieldMultiplierHeat;
    }
    
    public double getFertility() {
        return fertility;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
