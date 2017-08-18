package main;

import java.util.ArrayList;
import java.util.List;

public class Game {
    
    private List<Crop> crops = new ArrayList<>();
    
    public Game() {
        crops.add(new Crop(0, "Potato", 10, 12, 2, 0.5, 1));
        crops.add(new Crop(1, "Carrot", 8, 14, 1.5, 0.6, 1));
    }

    public void run() {
        while (true){
            pollInput();
            doLogic();
            render();
        }
    }
    
    
    private void pollInput() {
        
    }
    
    private void doLogic() {
        
    }
    private void render() {
        
    }
    
    
    
    

}
