package main;

public class Log {

    private static final String SECTION_BREAK = 
            "==============================";

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
    
    /**
     * Prints the given String to the console without a line break.
     * @param msg
     */
    public void printSame(String msg) {
    	System.out.printf(msg);
    }
    
}
