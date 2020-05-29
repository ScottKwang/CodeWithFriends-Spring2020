package cs2030.simulator;

/** Encapsulates a Customer.
 *  The 'Customer' class support operators such as:
 *  (i) creating a customer
 *  (ii) setting different fields of the Customer, such as its state and time
 *  (iii) getting differnt fields of the Customer, such as its state and time
 */ 

public abstract class Customer implements Behavior {
    protected final int id;
    protected final double arrivalTime;
    protected Status state = Status.ARRIVES;

    /**Constructor to create a Customer.
     * @param id The id of the Customer
     * @param arrv The arrival time of the Customer
     */
    protected Customer(int id, double arrv) {
        this.id = id;
        this.arrivalTime = arrv;
    }
  
    /**Constructor to create a Customer, with their state.
     * @param id The id of the Customer
     * @param arrv The arrival time of the Customer
     * @param state The Status of the Customer
     */
    protected Customer(int id, double arrv, Status state) {
        this.id = id;
        this.arrivalTime = arrv;
        this.state = state;
    }

    /**Gets the Customer's ID.
     * @return An int representing the Customer's ID.
     */
    public int getId() { 
        return this.id;
    }
    
    /**Gets the Customer's time. 
     * @return A double representing the Customer's time
     */ 
    public double getArrivalTime() {
        return this.arrivalTime;
    }
    
    /**Gets the state of the Customer.
     * @return An enum representing the Customer's state
     */
    public Status getState() {
        return this.state;
    } 

    /**Checks if the Customer's state is ARRIVES.
     * @return A boolean representing if the Customer has arrived or not
     */ 
    public boolean isArrives() {
        if (this.state == Status.ARRIVES) {
            return true;
        } else {
            return false;
        }
    }

    /**Checks if the Customer's state is SERVED.
     * @return A boolean representing if the Customer has been served or not
     */ 
    public boolean isServed() {
        if (this.state == Status.SERVED) {
            return true;
        } else {
            return false;
        }
    }

    /**Checks if the Customer's state is LEAVES.
     * @return A boolean representing if the Customer has left or not
     */
    public boolean isLeaves() {
        if (this.state == Status.LEAVES) {
            return true;
        } else {
            return false;
        }
    }

    /**Checks if the Customer's state is WAITS.
     * @return A boolean representing if the Customer is waiting or not
     */ 
    public boolean isWaits() {
        if (this.state == Status.WAITS) {
            return true;
        } else {
            return false;
        }
    }

    /**Checks if the Customer's state is DONE.
     * @return A boolean representing if the Customer is done or not
     */
    public boolean isDone() {
        if (this.state == Status.DONE) {
            return true;
        } else {
            return false;
        }
    }

    /**Sets the Customer's state.
     * @param num The customer's new state
     */
    public void setState(Status num) {
        this.state = num;
    }  

    @Override
    public String toString() {
        return String.format("%.3f %d", this.arrivalTime, this.id);
    }
}
