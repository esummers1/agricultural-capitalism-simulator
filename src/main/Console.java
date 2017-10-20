package main;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {

    private static final String SECTION_BREAK = 
            "===================================";

    private Scanner in = new Scanner(System.in);
    
    /**
     * Prints the given String to the console.
     * 
     * @param msg
     */
    public void print(String msg) {
        System.out.println(msg);
    }
    
    /**
     * Prints a new line.
     */
    public void newLine() {
        print("");
    }
    
    public void sectionBreak() {
        print(SECTION_BREAK);
    }

    public void close() {
        in.close();
    }

    public int nextInt() {
    	
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
        print("Press ENTER to continue.");
        in.nextLine();
    }
    
}
