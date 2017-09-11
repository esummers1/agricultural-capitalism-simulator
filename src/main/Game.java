package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    
	private long money = 1000;
	private int year = 1;
	
	private List<Field> fields = new ArrayList<>();
    private List<Crop> crops = new ArrayList<>();
    
    public Game() {
        // cost, salePrice, yieldMultiplierWetness, yieldMultiplierHeat, fertility
        crops.add(new Crop(Crop.ID_POTATO, "Potato", 10, 12, 2  , 0.5, 1));
        crops.add(new Crop(Crop.ID_CARROT, "Carrot", 8 , 14, 1.5, 0.6, 1));
        crops.add(new Crop(Crop.ID_TURNIP, "Turnip", 8 , 14, 1.5, 0.6, 1)); // TODO
        
        // fieldID, cropID, name, contents, area
        // uncertain what to set second parameter to before crop is decided
        fields.add(new Field(1, 256, "Hilly Field", 0, 100));
        fields.add(new Field(2, 256, "Vegetable Patch", 0, 50));
    }

    public void run() {
        while (true){
        	
        	if (year == 1) {
        		introduce();
        	}
        	
            pollInput();
            doLogic();
            render();
        }
    }
    
    private void introduce() {
    	
    	// explain setup of game
    	// give similar summary to render
    	
    }
    
    private void pollInput() {
    	
    	// give purchase options
    	// ask for and receive input
        
    }
    
    private double generateWetness() {
    	double wetness;
    	// generate
    	return wetness;
    }
    
    private double generateHeat() {
    	double heat;
    	// generate
    	return heat;
    }
    
    private void calculateYields(double wetness, double heat) {
    	// iterate through all fields, calculating yields of their contents
    }
    
    private void doLogic() {
    	
    	// it might make more sense to do this with a Weather object, but can't
    	// get my head around it atm
    	calculateYields(generateWetness(), generateHeat());
    	year++;
        
    }
    
    private void render() {
    	
    	// give results & current state of game
        
    }

}
