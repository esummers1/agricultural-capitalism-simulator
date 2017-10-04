package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Game {
    
    private static final String CROP_DATA_FILENAME = "crops.dat";

    /**
     * The player's current balance.
     */
	private long money = 1000;
	
	/**
	 * The current year.
	 */
	private int year = 1;
	
	private List<Field> fields = new ArrayList<>();
    private List<Crop> crops = new ArrayList<>();
    
    private boolean exiting;
    private Scanner in;
    
    public Game() {

        try {
            crops = readCropData(CROP_DATA_FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // fieldId, name, maxCropQuantity, soilQuality
        fields.add(new Field(0, "Basic Field", 100, 1.0));
        fields.add(new Field(1, "Lush Field", 50, 1.1));
        
        in = new Scanner(System.in);
    }

    /**
     * Reads the crop data from the given file.
     * @return
     * @throws JsonIOException
     * @throws JsonSyntaxException
     * @throws FileNotFoundException
     */
    private List<Crop> readCropData(String filename) 
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Crop>>(){}.getType();
        return gson.fromJson(new JsonReader(
                new FileReader(filename)), type);
    }

    /**
     * The game loop.
     */
    public void run() {
        
        introduce();
        
        while (!exiting){
            pollInput();
            doLogic();
            showResults();
        }
        
        finish();
    }
    
    /**
     * Performs any clean-up before exiting the game.
     */
    private void finish() {
        in.close();
    }

    /**
     * Explains the game to the user.
     */
    private void introduce() {
        // TODO
    }
    
    /**
     * Prompts the user for all necessary input for the round.
     */
    private void pollInput() {
    	
        boolean done = false;
        
        while (!done) {
            
            output("What would you like to do? Here are your options:\n");
            output("list - List available crops for purchase");
            output("status - Give current farm status");
            output("plant - Buy and plant crops");
            output("play - Advance to next year's harvest");
            output("exit - Stop playing");
            
            String input = in.nextLine();
            
            // TODO: carry out user's choice
            
            if (input.equals("list")) {
            	listCrops();
            }
            
            /**
             * Require user to fill all fields. Need to test this logic.
             * TODO: what if user has spent all their money already?
             */
            if (input.equals("play")) {
            	boolean full = true;
            	
            	for (Field field : fields) {
            		if (field.getCrop() == null) {
            			full = false;
            		}
            	}
            	
            	if (full) {
            		done = true;
            	}
            }
            
            if (input.equals("exit")) {
                exiting = true;
                break;
            }
        }
    }
    
    /**
     * Prints the given String to the console.
     * 
     * @param msg
     */
    private void output(String msg) {
        System.out.println(msg);
    }
    
    /**
     * List crops that are available for purchase.
     */
    private void listCrops() {
    	output("\n");
    	
    	for (Crop crop : crops) {
    		output(crop.getName() + ": " + crop.getDescription());
    		output("Buy for: " + crop.getCost() + ", sell for: " + 
    			crop.getSalePrice());
    		output("\n");
    	}
    }

    /**
     * Performs the end-of-round logic.
     */
    private void doLogic() {
        money += calculateProfit(generateWetness(), generateHeat());
        year++;
    }

    /**
     * Generates a random value for wetness.
     * 
     * @return
     */
    private double generateWetness() {
        return Math.random(); // 0-1 (for now)
    }

    /**
     * Generates a random value for heat.
     * 
     * @return
     */
    private double generateHeat() {
        return Math.random(); // 0-1 (for now)
    }

    /**
     * Determines the profit for the round.
     * 
     * @param wetness
     * @param heat
     * @return
     */
    private long calculateProfit(double wetness, double heat) {
    	
        long profit = 0;
        
        for (Field field : fields) {
            
            Crop crop = field.getCrop();
            
            if (crop == null) {
                // Field is empty!
                continue;
            }
            
            double heatFactor =
                    heat * crop.getYieldMultiplierHeat();
            double wetnessFactor =
                    wetness * crop.getYieldMultiplierWetness();
            double yield = 
                    ((heatFactor + wetnessFactor) / 2) * crop.getFertility();
            
            profit += yield * crop.getSalePrice() * field.getSoilQuality();
        }
        
        return profit;
    }
    
    /**
     * Displays the results of the round and the current state of the game.
     */
    private void showResults() {
    	// TODO
    }

}
