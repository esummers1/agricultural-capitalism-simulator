package main;

public class Crop {
    
    private int id;
    private String name;
    private int cost;
    private int salePrice;
    private double yieldMultiplierWetness;
    private double yieldMultiplierHeat;
    private double fertility;
    
    public Crop(
            int id, 
            String name,
            int cost, 
            int salePrice, 
            double yieldMultiplierWetness, 
            double yieldMultiplierHeat, 
            double fertility) {
        
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.salePrice = salePrice;
        this.yieldMultiplierWetness = yieldMultiplierWetness;
        this.yieldMultiplierHeat = yieldMultiplierHeat;
        this.fertility = fertility;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
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
    
}
