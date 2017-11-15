package ai;

import java.util.List;

import actions.Action;
import main.Crop;
import main.Field;
import main.InputProvider;

public class AiInputProvider implements InputProvider {

    private Strategy strategy;
    
    public AiInputProvider(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Action getNextAction(List<Action> actions) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Field getFieldToPlant(List<Field> fields) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Crop getCropToPlant(Field field, int balance, List<Crop> crops) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getCropQuantity(int maximum) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Field getFieldToBuy(List<Field> fields) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() {}

    @Override
    public void waitForEnter() {}

}
