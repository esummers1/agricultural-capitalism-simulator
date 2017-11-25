package ai;

import java.util.List;

import main.Launcher;

public class AiLauncher extends Launcher {

    public static void main(String[] args) {
        new AiLauncher();
    }
    
    public AiLauncher() {
        
        StrategyEvolver algorithm = new StrategyEvolver(crops, fields);
        List<Strategy> strategies = algorithm.run();
        
        // Print top 5 strategies found
        for (int i = 1; i <= 5; i++) {
        	strategies.get(i).printStrategy();
        	System.out.println();
        	System.out.println();
        }
        
    }

}
