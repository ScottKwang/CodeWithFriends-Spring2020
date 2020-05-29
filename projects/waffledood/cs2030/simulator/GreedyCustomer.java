package cs2030.simulator;

public class GreedyCustomer extends Customer implements Behavior {
    
    public GreedyCustomer(int id, double arrv) {
        super(id, arrv);
    }

    public GreedyCustomer(int id, double arrv, Status state) {
        super(id, arrv, state);
    }

    public boolean isGreedy() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%d(greedy)", super.id);
    }
}
