package cs2030.simulator;

public class NormalCustomer extends Customer implements Behavior {
    
    public NormalCustomer(int id, double arrv) {
        super(id, arrv);
    }

    public NormalCustomer(int id, double arrv, Status state) {
        super(id, arrv, state);
    }

    public boolean isGreedy() {
        return false;
    }

    @Override 
    public String toString() {
        return String.format("%d", super.id);
    }
}
