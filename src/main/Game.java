package main;

import java.util.ArrayList;
import java.util.List;

public class Game {
    
    private List<Crop> crops = new ArrayList<>();
    
    public Game() {
        // cost, salePrice, yieldMultiplierWetness, yieldMultiplierHeat, fertility
        crops.add(new Crop(Crop.ID_POTATO, "Potato", 10, 12, 2  , 0.5, 1));
        crops.add(new Crop(Crop.ID_CARROT, "Carrot", 8 , 14, 1.5, 0.6, 1));
        crops.add(new Crop(Crop.ID_TURNIP, "Turnip", 8 , 14, 1.5, 0.6, 1)); // TODO
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
