package actions;

import main.Game;

public class StatusAction extends Action {

    public StatusAction(Game game) {
        super(game);
    }

    @Override
    public void execute() {
        game.reportStatus();
    }

    @Override
    public String getPrompt() {
        return "Give current farm status";
    }

}
