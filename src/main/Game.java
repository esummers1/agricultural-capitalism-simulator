package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
    
    private Scanner in = new Scanner(System.in);
    private Log log = new Log();
    
    public Game() {

        try {
            crops = readCropData(CROP_DATA_FILENAME);
        } catch (JsonIOException |
                JsonSyntaxException |
                FileNotFoundException e) {
            e.printStackTrace();
        }
        
        // fieldId, name, maxCropQuantity, soilQuality
        fields.add(new Field(0, "Basic Field", 100, 1.0));
        fields.add(new Field(1, "Lush Field", 50, 1.1));
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
            
            if (exiting) {
                break;
            }
            
            processGame();
        }
        
        finish();
    }
    
    /**
     * Performs any clean-up before exiting the game.
     */
    private void finish() {
        log.print("Bye!");
        in.close();
    }

    /**
     * Explains the game to the user.
     */
    private void introduce() {
        log.print("Welcome to Agricultural Capitalism Simulator!");
        log.newLine();
    }
    
    /**
     * Prompts the user for all necessary input for the round.
     */
    private void pollInput() {
    	
        while (true) {
            
            log.print("What would you like to do?");
            log.newLine();
            log.print("list - List available crops for purchase");
            log.print("status - Give current farm status");
            log.print("plant - Buy and plant crops");
            log.print("play - Advance to next year's harvest");
            log.print("exit - Stop playing");
            log.newLine();
            
            String input = in.nextLine();
            log.newLine();
            
            if (input.equals("list")) {
            	listCrops();
            	waitForEnter();
                log.sectionBreak();
            	continue;
            }
            
            if (input.equals("status")) {
                reportStatus();
                waitForEnter();
                log.sectionBreak();
                continue;
            }
            
            if (input.equals("plant")) {
                plant();
                continue;
            }
            
            if (input.equals("play")) {
            	break;
            }
            
            if (input.equals("exit")) {
                exiting = true;
                break;
            }
        }
    }
    
    private void plant() {
        
        log.print("Your available fields:");
        
        List<Field> emptyFields = fields.stream()
                .filter(f -> f.isEmpty())
                .collect(Collectors.toList());
        
        for (int i = 0; i < emptyFields.size(); i++) {
            log.print((i + 1) + ") " + emptyFields.get(i).getName());
        }
        
        log.newLine();
        log.print("Which field would you like to plant in?");
        
        int fieldChoice = -1;
        while (fieldChoice < 0 || fieldChoice >= emptyFields.size()) {
            fieldChoice = in.nextInt() - 1;
        }
        
        log.newLine();
        log.print("Available crops for planting:");
        
        for (int i = 0; i < crops.size(); i++) {
            log.print((i + 1) + ") " + crops.get(i).getName());
        }
        
        log.newLine();
        log.print("Which crop would you like to plant?");
        
        int cropChoice = -1;
        while (cropChoice < 0 || cropChoice >= crops.size()) {
            cropChoice = in.nextInt() - 1;
        }
        
        Field field = emptyFields.get(fieldChoice);
        Crop crop = crops.get(cropChoice);
        
        // Can't exceed field capacity or spend more money than we have
        int maxVolume = Math.min(
                field.getMaxCropQuantity(),
                (int) (money / crop.getCost()));
        
        log.newLine();
        log.print("How many units would you like to purchase (maximum " 
                + maxVolume + ")?");

        int quantity = -1;
        while (quantity < 0 || quantity > maxVolume) {
            quantity = in.nextInt();
        }
        
        if (quantity == 0) {
            return;
        }
        
        field.setCrop(crop);
        field.setCropQuantity(quantity);
        
        money -= quantity * crop.getCost();
    }
    
    private void waitForEnter() {
        log.print("Press ENTER to continue.");
        in.nextLine();
    }
    
    /**
     * List crops that are available for purchase.
     */
    private void listCrops() {
    	for (Crop crop : crops) {
    		log.print(crop.getName());
    		log.print(crop.getDescription());
    		log.print("Cost: " + crop.getCost());
    		log.print("Sale Price: " + crop.getSalePrice());
    		log.newLine();
    	}
    }
    
    private void reportStatus() {
        
        log.print("Year: " + year);
        log.print("Balance: " + money);
        log.newLine();
        
        log.print("Fields:");
        
        for (Field field: fields) {
            
            Crop crop = field.getCrop();
            
            if (crop == null) {
                log.print(field.getName() + " is empty!");
            } else {
                log.print(field.getName() + " - " +
                        crop.getName() + " (" +
                        field.getCropQuantity() + " / " +
                        field.getMaxCropQuantity() + ")");
            }
        }

        log.newLine();
    }

    /**
     * Performs the end-of-round logic.
     */
    private void processGame() {
        
        double wetness = generateWetness();
        double heat = generateHeat();
        long profit = calculateProfit(wetness, heat);
        
        money += profit;
        year++;
        
        showResults(wetness, heat, profit);
        
        for (Field field : fields) {
            field.clear();
        }
    }

    /**
     * Generates a random value for wetness.
     * 
     * @return
     */
    private double generateWetness() {
        return ThreadLocalRandom.current().nextDouble(0.75, 1.25);
    }

    /**
     * Generates a random value for heat.
     * 
     * @return
     */
    private double generateHeat() {
        return ThreadLocalRandom.current().nextDouble(0.75, 1.25);
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
            
            profit += yield * field.getCropQuantity() * 
                    crop.getSalePrice() * field.getSoilQuality();
        }
        
        return profit;
    }
    
    /**
     * Displays the results of the round and the current state of the game.
     * @param heat 
     * @param wetness 
     * @param profit 
     */
    private void showResults(double wetness, double heat, long profit) {
        
        log.print("Wetness: " + wetness);
        log.print("Heat: " + heat);
        
        if (profit > 0) {
            log.print("You made a profit of " + profit + "!");
        } else if (profit == 0) {
            log.print("You broke even!");
        } else {
            log.print("You made a loss of " + profit + "!");
        }
    }

}
