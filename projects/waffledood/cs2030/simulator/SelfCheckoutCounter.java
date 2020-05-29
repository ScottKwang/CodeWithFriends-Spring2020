package cs2030.simulator;

import java.util.List;
import java.util.ArrayList;

public class SelfCheckoutCounter extends Server {
    private static List<Customer> waitingList;

    public SelfCheckoutCounter() {
        super();
    }

    public SelfCheckoutCounter(int id) {
        super(id);
    }

    public boolean isGoingToRest() {
        return false;
    }

    public void rest() { }

    /**Method to serve the next Customer. 
     * @param nextCustomer Customer the Self-Checkout Counter will serve 
     */
    public void serve(Customer nextCustomer) {
        super.customer = nextCustomer; 
        super.nextServiceTime = nextCustomer.getArrivalTime();
        super.customer.setState(Status.SERVED);

        if (this.waitingList.contains(nextCustomer)) {
            this.waitingList.remove(nextCustomer);
        } 
    }
    
    /**Method to serve the next Customer waiting in line. 
     */
    public void serveWait() {
        if (!this.waitingList.isEmpty()) {
            super.customer = waitingList.get(0); 
        } 
    } 

    /**Method to generate the service time of the Customer being served.
     * @param nextCustomer The Customer being served 
     */
    public void serveWaitDone(Customer nextCustomer) {
        double timeToServe = r1.genServiceTime();
        super.nextServiceTime += timeToServe;
    } 

    /**Method to remove Customer from the waiting list.
     * @param nextCustomer Customer that the Self-Checkout Counter will serve  
     */
    public void serveWaitRemove(Customer nextCustomer) {
        if (!this.waitingList.isEmpty()) {
            this.waitingList.remove(0);
        }
    }
    
    /**Method to accept a Customer to the waiting list.
     * @param nextCustomer Customer that the Self-Checkout Counter will accept
     */ 
    public void acceptWait(Customer nextCustomer) {
        this.waitingList.add(nextCustomer);
    }

    /**Method to check if the Self-Checkout Counter can serve the Customer.
     * @param nextCustomer Customer that the Self-Checkout Counter will check
     */
    public boolean canServe(Customer nextCustomer) {
        if (super.customer == null) {
            return true;
        } else if (this.waitingList.size() > 0) {
            return false;
        } else if (super.nextServiceTime <= nextCustomer.getArrivalTime()) {
            return true;
        } else {
            return false;
        }
    } 

    /**Method to check if the Self-Checkout Counter can have the Customer waiting.
     * @param nextCustomer Customer that the Self-Checkout Counter will check
     */
    public boolean canHaveWaiting(Customer nextCustomer) {
        if (this.waitingList.size() < numWaitingCust) {
            return true;
        } else {
            return false;
        }
    }
    
    /**Method to check if the Self-Checkout Counter has a waiting Customer.
     * @return boolean value that indicates if the Self-Checkout Counter has a waiting Customer
     */
    public boolean hasWaitingCust() {
        return !(this.waitingList.isEmpty());
    }

    /**Method to set the RandomGenerator of the Self-Checkout Counter.
     * @param r1 RandomGenerator being passed into the Self-Checkout Counter
     */
    public void setRG(RandomGenerator r1) {
        super.r1 = r1;
    }

    /**Method to set the probability of the Self-Checkout Counter resting.
     * @param num The probability of the Server resting
     */
    public void setProbResting(double num) {
        super.probResting = num;
    }

    /**Method to initialize the waiting list of Self-Checkout Counter.
     * @param num The size of the waiting list
     */
    public void setWaitingList(int num) {
        this.waitingList = new ArrayList<Customer>(num);
        super.numWaitingCust = num;
    }

    /**Method to intialize the Self-Checkout Counter, its RandomGenerator, 
     * probability of resting and size of the waitingList.
     * 
     */
    public SelfCheckoutCounter initialize(RandomGenerator r1, double prob, int qMax) {
        setRG(r1);
        setProbResting(prob);
        setWaitingList(qMax);
        return this;
    }

    public boolean isHuman() {
        return false;
    }

    public int numberWaitingCust() {
        return waitingList.size();
    }

    public void setNST(double time) {
        this.nextServiceTime = time;
    }

    public int getNumWaitingCust() {
        return this.waitingList.size();
    }

    @Override
    public String toString() {
        return String.format("self-check %d", super.id);
    }

}







