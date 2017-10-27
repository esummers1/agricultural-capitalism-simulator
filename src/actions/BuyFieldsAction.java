package actions;

import main.Game;

public class BuyFieldsAction extends Action {

    public BuyFieldsAction(Game game) {
        super(game);
    }

    @Override
    public void execute() {
        game.buyFields();
    }

    @Override
    public String getPrompt() {
        return "Acquire new fields";
    }

}
