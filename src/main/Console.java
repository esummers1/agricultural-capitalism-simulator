package main;

import java.io.PrintStream;

public class Console {

    private static final String SECTION_BREAK = 
            "===================================";

    private PrintStream out;
    
    public Console(PrintStream out) {
        this.out = out;
    }
    
    /**
     * Prints the given String to the console.
     * 
     * @param msg
     */
    public void print(String msg) {
        if (out == null) {
            return;
        }
        out.println(msg);
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

}
