package main;

public class PlayerLauncher {
    
    public static void main(String[] args) {
        
        long seed = (long) (Math.random() * Long.MAX_VALUE);
        Console console = new Console(System.out);
        PlayerInputProvider inputProvider = new PlayerInputProvider(console);
        
        Game game = new Game(seed, inputProvider, console);
        game.run();
        
    }

}
