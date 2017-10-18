package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import actions.Action;
import actions.BuyCropsAction;
import actions.BuyFieldsAction;
import actions.ExitAction;
import actions.ListCropsAction;
import actions.PlayAction;
import actions.StatusAction;

public class Game {
    
    private static final String CROP_DATA_FILENAME = "crops.dat";
    private static final String FIELD_DATA_FILENAME = "fields.dat";

    private static final double WEATHER_LOW_BOUND = 0.9;
    private static final double WEATHER_UPPER_BOUND = 1.1;

    /*
     * Normally distributed about 1.0 +/- 0.1 with cutoffs at +/- 3 st devs.
     */
    private static final double WETNESS_DEVIATION = 0.1;
    private static final double MIN_WETNESS = 1 - 3 * WETNESS_DEVIATION;
    private static final double MAX_WETNESS = 1 + 3 * WETNESS_DEVIATION;

    /*
     * Normally distributed about 1.0 +/- 0.1 with cutoffs at +/- 3 st devs.
     */
    private static final double HEAT_DEVIATION = 0.1;
    private static final double MIN_HEAT = 1 - 3 * HEAT_DEVIATION;
    private static final double MAX_HEAT = 1 + 3 * HEAT_DEVIATION;
    
    /**
     * The player's current balance.
     */
	private int money = 1000;
	
	/**
	 * The player's yearly expenses.
	 */
	private int expenditure = 0;
	
	/**
	 * The player's assets acquired this year (for reporting).
	 */
	private int newAssets = 0;
	
	/**
	 * The current year.
	 */
	private int year = 1;
	
	private List<Field> availableFields = new ArrayList<>();
    private List<Crop> crops = new ArrayList<>();
    
    private List<Field> playerFields = new ArrayList<>();
    
    private boolean exiting;
    
    private Console console = new Console();
    private Random rand = new Random();
    
    public Game() {

        try {
            crops = readCropData(CROP_DATA_FILENAME);
            availableFields = readFieldData(FIELD_DATA_FILENAME);
        } catch (JsonIOException |
                JsonSyntaxException |
                FileNotFoundException e) {
            e.printStackTrace();
        }
        
        // Give player initial fields
        playerFields.add(availableFields.get(0));
        availableFields.remove(0);
        
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
            	console.print("You are bankrupt. You will have to find a job.");
            	console.newLine();
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
        console.print("Bye!");
        console.close();
    }
    
    /**
     * Explains the game to the player.
     */
    private void introduce() {
        console.print("Welcome to Agricultural Capitalism Simulator!");
        console.newLine();
    }
    
    /**
     * Prompts the player for all necessary input for the round.
     */
    private void pollInput() {
    	
        while (true) {
            
            List<Action> actions = new ArrayList<Action>();
            
            actions.add(new ListCropsAction(this));
            actions.add(new StatusAction(this));
            actions.add(new BuyCropsAction(this));
            actions.add(new BuyFieldsAction(this));
            actions.add(new PlayAction(this));
            actions.add(new ExitAction(this));
            
            console.print("What would you like to do?");
            console.newLine();
            
            for (int i = 0; i < actions.size(); i++) {
                Action action = actions.get(i);
                console.print((i + 1) + ") " + action.getPrompt());
            }
            console.newLine();
            
            int input = 0;
            while (input < 1 || input > actions.size()) {
            	input = console.nextInt();
            }
            
            console.newLine();
            
            Action action = actions.get(input - 1);
            action.execute();
            
            if (action.shouldEndRound()) {
                break;
            }
            
            console.waitForEnter();
            console.sectionBreak();
            console.newLine();
        }
    }
    
    /**
     * Allow player to purchase and plant crops in available fields.
     */
    public void buyCrops() {
                
        List<Field> emptyFields = playerFields.stream()
                .filter(f -> f.isEmpty())
                .collect(Collectors.toList());
        
        // Return to menu if no fields have available space
        if (emptyFields.size() == 0) {
        	console.print("Your fields are fully planted.");
        	console.newLine();
        	console.waitForEnter();
        	return;
        } 
        
        console.print("Your available fields:");
        
        for (int i = 0; i < emptyFields.size(); i++) {
            console.print((i + 1) + ") " + emptyFields.get(i).getName());
        }
        
        console.newLine();
        console.print("Which field would you like to plant in?");
        
        int fieldChoice = -1;
        while (fieldChoice < 0 || fieldChoice >= emptyFields.size()) {
            fieldChoice = console.nextInt() - 1;
        }
        
        console.newLine();
        console.print("Available crops for planting:");
        
        for (int i = 0; i < crops.size(); i++) {
            console.print((i + 1) + ") " + crops.get(i).getName());
        }
        
        console.newLine();
        console.print("Which crop would you like to plant?");
        
        int cropChoice = -1;
        while (cropChoice < 0 || cropChoice >= crops.size()) {
            cropChoice = console.nextInt() - 1;
        }
        
        Field field = emptyFields.get(fieldChoice);
        Crop crop = crops.get(cropChoice);
        
        // Can't exceed field capacity or spend more money than we have
        int maxVolume = Math.min(
                field.getMaxCropQuantity(),
                (int) (money / crop.getCost()));
        
        console.newLine();
        console.print("How many units would you like to purchase (maximum " 
                + maxVolume + ")?");

        int quantity = -1;
        while (quantity < 0 || quantity > maxVolume) {
            quantity = console.nextInt();
        }
        
        console.newLine();
        
        if (quantity == 0) {
        	console.waitForEnter();
            return;
        }
        
        field.setCrop(crop);
        field.setCropQuantity(quantity);
        
        int totalCost = quantity * crop.getCost();
        
        money -= totalCost;
        expenditure += totalCost;
    }
    
    /**
     * Allow player to buy new fields
     */
    public void buyFields() {
    	
    	// If there are no fields available, return player to menu
    	if (availableFields.size() == 0) {
    		console.print("There are no fields remaining for purchase!");
    		return;
    	}
    	
    	console.print("Here are the fields available for purchase:");
    	console.newLine();
    	
    	for (int i = 0; i < availableFields.size(); i++) {
    		console.print((i + 1) + ") " + 
    		        availableFields.get(i).getName() + ", " +
    				availableFields.get(i).getDescription());
    		console.print("Price: " + availableFields.get(i).getPrice());
    		console.newLine();
    	}
    	
    	console.print("Which field would you like to purchase? Enter " +
    			(availableFields.size() + 1) + " to return to menu.");
    	
    	int fieldChoice = -1;
    	while (fieldChoice < 0 || fieldChoice > availableFields.size()) {
    		fieldChoice = console.nextInt() - 1;
    	}

        // If player has selected the return command, return to menu
        if (fieldChoice == availableFields.size()) {
            return;
        }
    	
    	if (availableFields.get(fieldChoice).getPrice() > money) {
    		console.print("Sorry, you have insufficient funds.");
    		return;
    	}
    		
		// Add to player's fields, mark it owned, deduct money
		playerFields.add(availableFields.get(fieldChoice));
        int price = availableFields.get(fieldChoice).getPrice();
		availableFields.remove(fieldChoice);
		
		money -= price;
		expenditure += price;
		newAssets += price;
    	
    	console.newLine();
    }
    
    /**
     * List crops that are available for purchase.
     */
    public void listCrops() {
    	for (Crop crop : crops) {
    		console.print("**" + crop.getName());
    		console.print(crop.getDescription());
    		console.print("Cost: " + crop.getCost());
    		console.print("Sale Price: " + crop.getSalePrice());
    		console.newLine();
    	}
    }
    
    /**
     * Report farm status to player.
     */
    public void reportStatus() {
        
        console.print("Year: " + year);
        console.print("Balance: " + money);
        console.print("Asset value: " + calculateAssets());
        
        console.newLine();
        
        console.print("Fields:");
        
        for (Field field: playerFields) {
            
            Crop crop = field.getCrop();
            
            if (crop == null) {
                console.print(field.getName() + ", value " + 
                		field.getPrice() + ", is empty!");
            } else {
                console.print(field.getName() + ", value " + 
                		field.getPrice() + " - " + 
                		crop.getName() + " (" +
                        field.getCropQuantity() + " / " +
                        field.getMaxCropQuantity() + ")");
            }
        }

        console.newLine();
    }
    
    /**
     * Performs the end-of-round logic.
     */
    private void processGame() {
        
        double wetness = generateWetness();
        double heat = generateHeat();
        int profit = calculateProfit(wetness, heat);
        
        money += profit;
        year++;
        
        showResults(wetness, heat, profit);
        
        for (Field field : playerFields) {
            field.clear();
        }
    }
    
    /**
     * Generates a random value for wetness.
     * 
     * @return
     */
    private double generateWetness() {
    	double wetness = 0;
    	
    	while (wetness < MIN_WETNESS || wetness > MAX_WETNESS) {
    		wetness = rand.nextGaussian() * WETNESS_DEVIATION + 1;
    	}
    	
    	return wetness;
    }
    
    /**
     * Generates a random value for heat.
     * 
     * @return
     */
    private double generateHeat() {
    	double heat = 0;
    	
    	while (heat < MIN_HEAT || heat > MAX_HEAT) {
    		heat = rand.nextGaussian() * HEAT_DEVIATION + 1;
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
    private int calculateProfit(double wetness, double heat) {
    	
        int profit = 0;
        
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
    private int calculateAssets() {
    	
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
    	    	
    	String report;
    	
    	if (heat < WEATHER_LOW_BOUND) {
    		report = "This was a frigid year ";
    	} else if (heat > WEATHER_UPPER_BOUND) {
    		report = "This was a scorching year ";
    	} else {
    		report = "This was a temperate year ";
    	}
    	
    	if (wetness < WEATHER_LOW_BOUND) {
    		report += "with little precipitation.";
    	} else if (wetness > WEATHER_UPPER_BOUND) {
    		report += "with torrential downpours.";
    	} else {
    		report += "with modest rainfall.";
    	}
    	
    	console.print(report);
    	
    }
    
    /**
     * Displays the results of the round and the current state of the game.
     * 
     * @param heat 
     * @param wetness 
     * @param profit 
     */
    private void showResults(double wetness, double heat, int profit) {
        
    	console.sectionBreak();
    	reportWeather(wetness, heat);
    	console.newLine();
    	    	
    	int netProfit = profit + newAssets - expenditure;
    	
    	console.print("Year " + (year - 1) + " performance:");
    	console.print("Asset acquisitions: " + newAssets);
    	console.print("Revenue: " + profit);
    	console.print("Expenses: " + expenditure);
    	console.print("---------------------");
    	
        if (netProfit > 0) {
            console.print("Congratulations! You made a net profit of " + netProfit + ".");
        } else if (netProfit == 0) {
            console.print("It could be worse; you broke even.");
        } else {
            console.print("Commiserations! You made a loss of " + netProfit + ".");
        }
        
        expenditure = 0;
        newAssets = 0;
        
        console.print("Year end balance: " + money);
        console.print("Total asset value: " + calculateAssets());
        console.sectionBreak();
        console.newLine();
                
    }

    public void exit() {
        exiting = true;
    }

}
