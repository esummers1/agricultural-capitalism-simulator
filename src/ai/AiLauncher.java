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
        
        // Print best strategies found
        for (Strategy strategy : strategies) {
            System.out.println(strategy);
        }   
    }

}
