package main;

public class PlayerLauncher extends Launcher {
    
    public static void main(String[] args) {
        PlayerLauncher playerLauncher = new PlayerLauncher();
    }
    
    public PlayerLauncher() {
        
        long seed = (long) (Math.random() * Long.MAX_VALUE);
        Console console = new Console(System.out);
        PlayerInputProvider inputProvider = new PlayerInputProvider(console);
        
        Game game = new Game(seed, inputProvider, console, crops, fields);
        game.run();
    }

}
