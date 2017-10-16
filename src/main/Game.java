package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private static final String FIELD_DATA_FILENAME = "fields.dat";
    
    /**
     * The player's current balance.
     */
	private long money = 1000;
	
	/**
	 * The player's yearly expenses.
	 */
	private long expenditure = 0;
	
	/**
	 * The player's assets acquired this year (for reporting).
	 */
	private long newAssets = 0;
	
	/**
	 * The current year.
	 */
	private int year = 1;
	
	private List<Field> allFields = new ArrayList<>();
    private List<Crop> crops = new ArrayList<>();
    
    private List<Field> playerFields = new ArrayList<>();
    
    private boolean exiting;
    
    private Scanner in = new Scanner(System.in);
    private Log log = new Log();
    private Random rand = new Random();
    
    public Game() {

        try {
            crops = readCropData(CROP_DATA_FILENAME);
        } catch (JsonIOException |
                JsonSyntaxException |
                FileNotFoundException e) {
            e.printStackTrace();
        }
        
        try {
        	allFields = readFieldData(FIELD_DATA_FILENAME);
        } catch (JsonIOException |
        		JsonSyntaxException |
        		FileNotFoundException e) {
        	e.printStackTrace();
        }
        
        // Give player initial fields
        playerFields.add(allFields.get(0));
        playerFields.add(allFields.get(1));
        
        // Mark player's fields owned in list of all fields
        allFields.get(0).setOwned(true);
        allFields.get(1).setOwned(true);
        
    }
    
    /**
     * Reads the crop data from the given file.
     * 
     * @param filename
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
     * Reads the field data from the given file.
     * 
     * @param filename
     * @return
     * @throws JsonIOException
     * @throws JsonSyntaxException
     * @throws FileNotFoundException
     */
    private List<Field> readFieldData(String filename)
    		throws JsonIOException, JsonSyntaxException, FileNotFoundException {
    	Gson gson = new Gson();
    	Type type = new TypeToken<List<Field>>(){}.getType();
    	return gson.fromJson(new JsonReader(
    			new FileReader(filename)), type);
    }
    
    /**
     * The game loop.
     */
    public void run() {
        
        introduce();
        
        while (!exiting){
        	
        	/**
        	 * End game if player has no remaining funds.
        	 * TODO: revise this once the player can e.g. sell assets
        	 */
            if (money < 1) {
            	log.print("You are bankrupt. You will have to find a job.");
            	log.newLine();
            	break;
            }
        	
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
     * Explains the game to the player.
     */
    private void introduce() {
        log.print("Welcome to Agricultural Capitalism Simulator!");
        log.newLine();
    }
    
    /**
     * Prompts the player for all necessary input for the round.
     */
    private void pollInput() {
    	
        while (true) {
            
            log.print("What would you like to do?");
            log.newLine();
            log.print("1) List available crops for purchase");
            log.print("2) Give current farm status");
            log.print("3) Buy and plant crops");
            log.print("4) Purchase new fields");
            log.print("5) Advance to next year's harvest");
            log.print("6) Stop playing");
            log.newLine();
            
            int input = 0;
            while (input < 1 || input > 5) {
            	input = in.nextInt();
            }
            
            log.newLine();
            
            if (input == 1) {
            	listCrops();
            	waitForEnter();
                log.sectionBreak();
                log.newLine();
            	continue;
            }
            
            if (input == 2) {
                reportStatus();
                waitForEnter();
                log.sectionBreak();
                log.newLine();
                continue;
            }
            
            if (input == 3) {
                plant();
                continue;
            }
            
            if (input == 4) {
            	buyFields();
            	continue;
            }
            
            if (input == 5) {
            	break;
            }
            
            if (input == 6) {
                exiting = true;
                break;
            }
        }
    }
    
    /**
     * Allow player to purchase and plant crops in available fields.
     */
    private void plant() {
                
        List<Field> emptyFields = playerFields.stream()
                .filter(f -> f.isEmpty())
                .collect(Collectors.toList());
        
        // Return to menu if no fields have available space
        if (emptyFields.size() == 0) {
        	log.print("Your fields are fully planted.");
        	log.newLine();
        	waitForEnter();
        	return;
        } 
        
        log.print("Your available fields:");
        
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
        
        log.newLine();
        
        if (quantity == 0) {
        	waitForEnter();
            return;
        }
        
        field.setCrop(crop);
        field.setCropQuantity(quantity);
        
        money -= quantity * crop.getCost();
        expenditure += quantity * crop.getCost();
    }
    
    /**
     * Allow player to buy new fields
     */
    private void buyFields() {
    	
    	// Get list of fields that are not owned by player
    	List<Field> availableFields = allFields.stream()
    			.filter(f -> f.isNotOwned())
    			.collect(Collectors.toList());
    	
    	// If there are no fields available, return player to menu
    	if (availableFields.size() == 0) {
    		log.print("There are no fields remaining for purchase!");
    		log.newLine();
    		waitForEnter();
    		return;
    	}
    	
    	log.print("Here are the fields available for purchase: ");
    	log.newLine();
    	
    	for (int i = 0; i < availableFields.size(); i++) {
    		log.print((i + 1) + ") " + availableFields.get(i).getName() + ", "
    				+ availableFields.get(i).getDescription());
    		log.print("Price: " + availableFields.get(i).getPrice());
    		log.newLine();
    	}
    	
    	log.print("Which field would you like to purchase? Enter " +
    			(availableFields.size() + 1) + " to return to menu.");
    	
    	int fieldChoice = -1;
    	while (fieldChoice < 0 || fieldChoice >= availableFields.size()) {
    		fieldChoice = in.nextInt() - 1;
    		
    		// If player has selected the return command, return to menu
        	if (fieldChoice == availableFields.size()) {
        		log.newLine();
        		waitForEnter();
        		return;
        	}
    	}
    	
    	if (availableFields.get(fieldChoice).getPrice() > money) {
    		log.print("Sorry, you have insufficient funds.");
    		log.newLine();
    		waitForEnter();
    		return;
    	} else {
    		
    		// Add to player's fields, mark it owned, deduct money
    		playerFields.add(availableFields.get(fieldChoice));
    		allFields.get(availableFields.get(fieldChoice).getFieldId())
    			.setOwned(true);
    		
    		long price = availableFields.get(fieldChoice).getPrice();
    		
    		money -= price;
    		expenditure += price;
    		newAssets += price;
    	}
    	
    	log.newLine();
    	
    }
    
    /**
     * Wait for a prompt before returning to menu.
     */
    private void waitForEnter() {
        log.print("Press ENTER to continue.");
        in.nextLine();
    }
    
    /**
     * List crops that are available for purchase.
     */
    private void listCrops() {
    	for (Crop crop : crops) {
    		log.print("**" + crop.getName());
    		log.print(crop.getDescription());
    		log.print("Cost: " + crop.getCost());
    		log.print("Sale Price: " + crop.getSalePrice());
    		log.newLine();
    	}
    }
    
    /**
     * Report farm status to player.
     */
    private void reportStatus() {
        
        log.print("Year: " + year);
        log.print("Balance: " + money);
        log.print("Asset value: " + calculateAssets());
        
        log.newLine();
        
        log.print("Fields:");
        
        for (Field field: playerFields) {
            
            Crop crop = field.getCrop();
            
            if (crop == null) {
                log.print(field.getName() + ", value " + 
                		field.getPrice() + ", is empty!");
            } else {
                log.print(field.getName() + ", value " + 
                		field.getPrice() + " - " + 
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
        
        for (Field field : playerFields) {
            field.clear();
        }
    }
    
    /**
     * Generates a random value for wetness.
     * Normally distributed about 1.0 +/- 0.1 with cutoffs at +/- 3 st devs.
     * 
     * @return
     */
    private double generateWetness() {
    	double wetness = 0;
    	
    	while (wetness < 0.7 || wetness > 1.3) {
    		wetness = (rand.nextGaussian() * 0.1 + 1);
    	}
    	
    	return wetness;
    }
    
    /**
     * Generates a random value for heat.
     * Normally distributed about 1.0 +/- 0.1 with cutoffs at +/- 3 st devs.
     * 
     * @return
     */
    private double generateHeat() {
    	double heat = 0;
    	
    	while (heat < 0.7 || heat > 1.3) {
    		heat = (rand.nextGaussian() * 0.1 + 1);
    	}
    	
    	return heat;
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
        
        for (Field field : playerFields) {
            
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
     * Return the total value of the player's assets.
     * 
     * @return
     */
    private long calculateAssets() {
    	
    	int assets = 0;
    	
    	for (Field field : playerFields) {
    		assets += field.getPrice();
    	}
    	
    	return assets;
    	
    }
    
    /**
     * Report the year's weather in plain English.
     * Non-average weather reports come at 1 stdev from mean.
     * 
     * @param wetness
     * @param heat
     */
    private void reportWeather(double wetness, double heat) {
    	
    	double lowerBound = 0.9;
    	double upperBound = 1.1;
    	    	
    	String report;
    	
    	if (heat < lowerBound) {
    		report = "This was a frigid year ";
    	} else if (heat > upperBound) {
    		report = "This was a scorching year ";
    	} else {
    		report = "This was a temperate year ";
    	}
    	
    	if (wetness < lowerBound) {
    		report = report + "with little precipitation.";
    	} else if (wetness > upperBound) {
    		report = report + "with torrential downpours.";
    	} else {
    		report = report + "with modest rainfall.";
    	}
    	
    	log.print(report);
    	
    }
    
    /**
     * Displays the results of the round and the current state of the game.
     * 
     * @param heat 
     * @param wetness 
     * @param profit 
     */
    private void showResults(double wetness, double heat, long profit) {
        
    	log.sectionBreak();
    	reportWeather(wetness, heat);
    	log.newLine();
    	    	
    	long netProfit = profit + newAssets - expenditure;
    	
    	log.print("Year " + (year - 1) + " performance:");
    	log.print("Asset acquisitions: " + newAssets);
    	log.print("Revenue: " + profit);
    	log.print("Expenses: " + expenditure);
    	log.print("---------------------");
    	
        if (netProfit > 0) {
            log.print("Congratulations! You made a net profit of " + netProfit + ".");
        } else if (netProfit == 0) {
            log.print("It could be worse; you broke even.");
        } else {
            log.print("Commiserations! You made a loss of " + netProfit + ".");
        }
        
        expenditure = 0;
        newAssets = 0;
        
        log.print("Year end balance: " + money);
        log.print("Total asset value: " + calculateAssets());
        log.sectionBreak();
        log.newLine();
                
    }

}
