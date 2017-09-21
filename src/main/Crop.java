package main;
public class Crop {

    public static final int ID_POTATO = 0;
    public static final int ID_CARROT = 1;
    public static final int ID_TURNIP = 2;
    public static final int ID_WHEAT = 3;
    
    public static final String DESCRIPTION_POTATO = "Tuber of the Nightshade"
    		+ " family which prefers a cool, wet climate. Considered a staple"
    		+ " food of many nations due to its starch content.";
    public static final String DESCRIPTION_CARROT = "Nutritious root vegetable"
    		+ " of the Carrot family, cheap to grow and thriving in sunny"
    		+ " weather.";
    public static final String DESCRIPTION_TURNIP = "Tasty root vegetable of"
    		+ " the Cabbage family, benefiting from high yields but suffering"
    		+ " in hot climates.";
    public static final String DESCRIPTION_WHEAT = "Grain of the Grass family"
    		+ " grown worldwide as a staple food due to its high protein"
    		+ " content. Produces large yields and is weather-resistant, "
    		+ " but requires large plantings to be profitable.";
    
    private int id;
    private String name;
    private String description;
    private int cost;
    private int salePrice;
    private double yieldMultiplierWetness;
    private double yieldMultiplierHeat;
    private double fertility;
    
    public Crop(
            int id, 
            String name,
            String description,
            int cost, 
            int salePrice, 
            double yieldMultiplierWetness, 
            double yieldMultiplierHeat, 
            double fertility) {
        
        this.id = id;
        this.name = name;
        this.description = description;
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
    
}
