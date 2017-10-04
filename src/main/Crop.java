package main;

public class Crop {

    public static final int ID_POTATO = 0;
    public static final int ID_CARROT = 1;
    public static final int ID_TURNIP = 2;
    public static final int ID_WHEAT = 3;
    
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
