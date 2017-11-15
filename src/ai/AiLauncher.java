package ai;

import java.util.List;

public class AiLauncher {

    public static void main(String[] args) {
        StrategyEvolver algorithm = new StrategyEvolver();
        List<Strategy> strategies = algorithm.run();
        
        // Print best strategies found
        for (Strategy strategy : strategies) {
            System.out.println(strategy);
        }
    }

}
