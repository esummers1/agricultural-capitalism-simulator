package actions;

import main.Game;

public class PlayAction extends Action {

    public PlayAction(Game game) {
        super(game);
    }

    @Override
    public void execute() {
        // Do nothing!
    }

    @Override
    public String getPrompt() {
        return "Advance to next year's harvest";
    }
    
    @Override
    public boolean shouldEndRound() {
        return true;
    }

}
