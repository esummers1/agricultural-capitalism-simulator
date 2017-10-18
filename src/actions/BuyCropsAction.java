package actions;

import main.Game;

public class BuyCropsAction extends Action {

    public BuyCropsAction(Game game) {
        super(game);
    }

    @Override
    public void execute() {
        game.buyCrops();
    }

    @Override
    public String getPrompt() {
        return "Buy and plant crops";
    }

}
