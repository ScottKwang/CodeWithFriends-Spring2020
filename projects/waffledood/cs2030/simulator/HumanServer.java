package cs2030.simulator;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;  

public class HumanServer extends Server {
  
    private List<Customer> waitingList;

    // Normal Constructor to instantiate a Server
    public HumanServer() { 
        super(); 
    }

    // Normal Constructor to instantiate a Server along with its ID
    public HumanServer(int id) {
        super(id);
    }

    /**Static factory method to create a Server with an ID.
     * @param id The ID of the Server
     * @return A new Server with an ID
     */ 
    public HumanServer createHumanServer(int id) throws ArithmeticException {
        if (id < 0) {
            throw new ArithmeticException("A Server's ID cannot be negative");
        } else {
            return new HumanServer(id);
        }
    } 
    
    /**Boolean method to return if a Human Server is going to rest.
     * @return A boolean reflecting if a Server is going to Rest  
     */
    public boolean isGoingToRest() {
        double probability = r1.genRandomRest();
        if (probability < probResting) {
            return true;
        } else {
            return false;
        }
    }

    /**Void method to let the Human Server rest.
     */
    public void rest() {
        double restTime = r1.genRestPeriod();
        super.nextServiceTime += restTime;
    }

    /**Serves the next Customer and sets its Customer with updated details.
     * @param nextCustomer The next Customer the Server serves
     */ 
    public void serve(Customer nextCustomer) {
        super.customer = nextCustomer; 
        super.nextServiceTime = nextCustomer.getArrivalTime(); 
        super.customer.setState(Status.SERVED);

        if (this.waitingList.contains(nextCustomer)) {
            this.waitingList.remove(nextCustomer);
        }
    }

    /**Serves the first waiting Customer in the Server's waiting queue.
     */ 
    public void serveWait() {
        if (!this.waitingList.isEmpty()) {
            super.customer = waitingList.get(0);
        } 
    }

    /**Serves the next Customer that was previously waiting.
     * @param nextCustomer The next Customer to be served
     */
    public void serveWaitDone(Customer nextCustomer) {
        double timeToServe = r1.genServiceTime(); 
        super.nextServiceTime += timeToServe;
    }

    /**Removes the Customer from the Server's waiting list.
     * @param nextCustomer The next Customer that will be removed
     */
    public void serveWaitRemove(Customer nextCustomer) {
        if (!this.waitingList.isEmpty()) { 
            this.waitingList.remove(0);
        } 
    }
    
    /**Accepts a waiting Customer.
     * @param nextCustomer The Customer that gets added to the waiting list
     */
    public void acceptWait(Customer nextCustomer) {
        this.waitingList.add(nextCustomer);
    }

    /**Indicates if the Server can serve the Customer.
     * @param nextCustomer The next Customer that comes to the Server
     * @return A boolean indicating if the Server can serve the Customer
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

    /**Indicates if the Server can have a waiting Customer.
     * @param nextCustomer The next Customer that comes to the Server
     * @return A boolean indicating if the Server can have a waiting Customer
     */
    public boolean canHaveWaiting(Customer nextCustomer) {
        if (this.waitingList.size() < numWaitingCust) {
            return true;
        } else {  
            return false;
        }
    }

    /**Returns a boolean indicating if the Server has waiting Customers.
     * @return A boolean indicating if the Server has waiting Customers
     */
    public boolean hasWaitingCust() {
        return !(this.waitingList.isEmpty());
    }

    /**Sets the RandomGenerator of the Server.
     * @param r1 The RandomGenerator being passed in
     * @return HumanServer with the same properties and RandomGenerator initialized
     */
    public HumanServer setRG(RandomGenerator r1) {
        super.r1 = r1;
        return this;
    }

    /**Sets the probability of HumanServer resting.
     * @param num The probability of the HumanServer resting
     * @return HumanServer with the same properties and probability initialized
     */
    public HumanServer setProbResting(double num) {
        super.probResting = num;
        return this;
    }
    
    /**Sets the number of waiting Customers the Server can have. 
     * @param num The number of waiting Customers the Server can have
     * @return HumanServer with the same properties and qMax initialized 
     */
    public HumanServer setWaitingList(int num) {
        this.waitingList = new ArrayList<Customer>(num);
        super.numWaitingCust = num;
        return this;
    }
    
    /**Initialized the HumanServer's details.
     * @param r1 The RandomGenerator it can accept
     * @param prob The probability of the Server resting
     * @param qMax the max size of the waiting Customer the HumanServer can have
     * @return HumanServer with the same properties and these fields initialized
     */
    public HumanServer initialize(RandomGenerator r1, double prob, int qMax) {
        setRG(r1);
        setProbResting(prob);
        setWaitingList(qMax);
        return this;
    }

    /**Checks if the Server is Human.
     * @return A boolean indicating the Server is Human
     */
    public boolean isHuman() {
        return true;
    }

    /**Returns the number of waiting Customers the HumanServer has.
     * @return An int indicating the number of waiting Customers
     */
    public int numberWaitingCust() {
        return this.waitingList.size();
    }

    /** Sets the Server's next available time to server a Customer that arrives. 
     *  @param time The time a Server will be available to server a Customer that arrives
     */ 
    public void setNST(double time) {
        this.nextServiceTime = time;
    }

    /**Returns the number of waiting Customers.
     * @return An int indicating the number of waiting Customers the HumanServer has
     */
    public int getNumWaitingCust() {
        return this.waitingList.size();
    }

    /**Returns the Customer the HumanServer is serving.
     * @return Customer The Customer the HumanServer is currently serving
     */
    public Customer getCust() {
        return this.customer;
    }

    /** Gets the Server's next time it is available again.
     *  @return A double indicating the time the Server is available again
     */
    public double getNST() {
        return this.nextServiceTime;
    }

    @Override
    public String toString() {
        return String.format("server %d", this.id);
    }

}
