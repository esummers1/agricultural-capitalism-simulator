package main;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import actions.Action;

public class PlayerInputProvider implements InputProvider {
    
    private Scanner in = new Scanner(System.in);
    
    private Console console;
    
    public PlayerInputProvider(Console console) {
        this.console = console;
    }
    
    @Override
    public Action getNextAction(List<Action> actions, Game game) {
        
        int input = 0;
        while (input < 1 || input > actions.size()) {
            input = nextInt();
        }
        
        console.newLine();
        
        return actions.get(input - 1);
        
    }
    
    private int nextInt() {
        
        boolean validEntry = false;
        int value = 0;
        
        // Catch a non-integer entry and ignore it
        while (!validEntry) {
            try {
                value = in.nextInt();
                validEntry = true;
            } catch (InputMismatchException e) {
                break;
            }
        }
        
        // Skip over any unconsumed input
        in.nextLine();
        
        return value;
    }

    /**
     * Wait for a prompt before returning to menu.
     */
    public void waitForEnter() {
        console.print("Press ENTER to continue.");
        in.nextLine();
    }
    
    public void close() {
        in.close();
    }

    @Override
    public Field getFieldToPlant(List<Field> fields) {
        
        int fieldChoice = -1;
        while (fieldChoice < 0 || fieldChoice >= fields.size()) {
            fieldChoice = nextInt() - 1;
        }
        
        return fields.get(fieldChoice);
        
    }

    @Override
    public Crop getCropToPlant(Field field, int balance, List<Crop> crops) {
        
        int cropChoice = -1;
        while (cropChoice < 0 || cropChoice >= crops.size()) {
            cropChoice = nextInt() - 1;
        }
        
        return crops.get(cropChoice);
    }

    @Override
    public Field getFieldToBuy(List<Field> fields) {
        
        int fieldChoice = -1;
        while (fieldChoice < 0 || fieldChoice > fields.size()) {
            fieldChoice = nextInt() - 1;
        }
        
        if (fieldChoice == fields.size()) {
            return null;
        }
        
        return fields.get(fieldChoice);
    }

    @Override
    public int getCropQuantity(int maximum) {
        
        int quantity = -1;
        while (quantity < 0 || quantity > maximum) {
            quantity = nextInt();
        }
        
        return quantity;
    }

}
