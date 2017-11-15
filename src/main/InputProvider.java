package main;

import java.util.List;

import actions.Action;

public interface InputProvider {
    
    public Action getNextAction(List<Action> actions); 
    
    public Field getFieldToPlant(List<Field> fields);
    
    public Crop getCropToPlant(Field field, int balance, List<Crop> crops);
    
    public int getCropQuantity(int maximum);
    
    public Field getFieldToBuy(List<Field> fields);

    public void close();

    public void waitForEnter();

}
