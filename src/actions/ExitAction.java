package actions;

import main.Game;

public class ExitAction extends Action {

    public ExitAction(Game game) {
        super(game);
    }

    @Override
    public void execute() {
        game.exit();
    }

    @Override
    public String getPrompt() {
        return "Stop playing";
    }
    
    @Override
    public boolean shouldEndRound() {
    	return true;
    }

}
