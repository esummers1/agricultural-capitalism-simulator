package ai;

import java.util.List;

import actions.Action;
import actions.BuyCropsAction;
import actions.BuyFieldsAction;
import actions.PlayAction;
import main.Crop;
import main.Field;
import main.Game;
import main.InputProvider;

public class AiInputProvider implements InputProvider {

    private Strategy strategy;
    
    public AiInputProvider(Strategy strategy) {
        this.strategy = strategy;
    }
    
    @Override
    public Action getNextAction(List<Action> actions, Game game) {

        // If field price is less than 1/2 of money - BUY IT!
        for (Action action : actions) {
            if (action instanceof BuyFieldsAction) {
                for (Field field : game.getAvailableFields()) {
                    if (field.getPrice() < game.getMoney() / 2) {
                        System.out.println("buying field");
                        return action;
                    }
                }
            }
        }
            
        // If it's possible to buy crops, do that
        for (Action action : actions) {
            if (action instanceof BuyCropsAction) {
                return action;
            }
        }
        
        for (Action action : actions) {
            if (action instanceof PlayAction) {
                return action;
            }
        }
        
        throw new IllegalStateException();
    }
    
    /**
     * For now, just choose the first available field.
     */
    @Override
    public Field getFieldToPlant(List<Field> fields) {
        return fields.get(0);
    }

    /**
     * Choose crop to plant based on the additive probability of each crop
     * in the strategy, i.e. calculate a random double between 0 and 1 and add
     * the probabilities of each crop together in turn until this total exceeds
     * the random number. N.B. the crop probabilities will sum to 1 - if there
     * are rounding errors, just choose the last crop.
     */
    @Override
    public Crop getCropToPlant(Field field, int balance, List<Crop> crops) {
        
        double r = Math.random();
        double chanceToPickThisCrop = 0;
        
        for (Crop crop : crops) {
            
            // Add the probability of the current crop to running total
            chanceToPickThisCrop += strategy.getChanceToPlant(crop);
            
            if (r < chanceToPickThisCrop) {
                return crop;
            }
        }
        
        return crops.get(crops.size() - 1);
    }

    @Override
    public int getCropQuantity(int maximum) {
        return maximum;
    }

    /**
     * For now, return the first field in the list. At the moment this will not
     * be used.
     */
    @Override
    public Field getFieldToBuy(List<Field> fields) {
        return fields.get(0);
    }

    @Override
    public void close() {}

    @Override
    public void waitForEnter() {}

}
