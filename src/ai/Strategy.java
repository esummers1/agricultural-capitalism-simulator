package ai;

public class Strategy implements Comparable<Strategy> {

    private int fitness;
    
    @Override
    public int compareTo(Strategy o) {
        return fitness - o.fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

}
