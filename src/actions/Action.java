package actions;

import main.Game;

public abstract class Action {

    protected Game game;
    
    public Action(Game game) {
        this.game = game;
    }

    public abstract void execute();
    
    public abstract String getPrompt();

    public boolean shouldEndRound() {
        return false;
    }
    
}
