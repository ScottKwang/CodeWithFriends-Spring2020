package cs2030.simulator;

import java.util.List;
import java.util.ArrayList;

/**Encapsulates the base properties of a Server.
 * The 'Server' class supports properties such as:
 * (i) Serving a Customer
 * (ii) Adding/Removing a Customer to a waiting list of Customers
 */

public abstract class Server {
    protected Customer customer;
    protected int numWaitingCust;
    protected RandomGenerator r1;
    protected double probResting;

    protected double nextServiceTime;
    protected int id;
    
    protected Server() { }
    
    protected Server(int id) {
        this.id = id;
    }

    /**A method to check if the Server is going to rest.
     * @return A boolean indicating if the Server is going to rest
     */
    public abstract boolean isGoingToRest();

    /**A method to let the Server rest.
     */
    public abstract void rest();

    /**Lets the Server serve the next Customer.
     * @param nextCustomer The next Customer the Server will serve
     */
    public abstract void serve(Customer nextCustomer);

    /**Lets the Server serve the first waiting Customer in line.
     */
    public abstract void serveWait();

    /**Serves the next Customer that was previously waiting.
     * @param nextCustomer The next Customer to be served
     */ 
    public abstract void serveWaitDone(Customer nextCustomer);

    /**Removes the Customer from the Server's waiting list.
     * @param nextCustomer The next Customer that will be removed
     */
    public abstract void serveWaitRemove(Customer nextCustomer);

    /**Accepts a Customer to its waiting list.
     * @param nextCustomer Customer to be accepted
     */
    public abstract void acceptWait(Customer nextCustomer);

    /**Checks if the Server can serve next Customer.
     * @param nextCustomer Customer the Server checks for
     */
    public abstract boolean canServe(Customer nextCustomer);

    /**Checks if the Server can add the Customer to its waiting list.
     * @param nextCustomer Customer the Server checks for
     */
    public abstract boolean canHaveWaiting(Customer nextCustomer);

    /**Checks if the Server has waiting Customers.
     * @return A boolean indicating if the Server has waiting Customers
     */
    public abstract boolean hasWaitingCust();

    /**Initializes the Server's details.
     * @param r1 The RandomGenerator it can accept
     * @param prob The probability of the Server resting
     * @param qMax The max size of waiting Customers the Server can have
     */
    public abstract Server initialize(RandomGenerator r1, double prob, int qMax);

    /**Abstract method to check if a Server is Human.
     * @return A boolean indicating if the Server is Human
     */
    public abstract boolean isHuman();

    /**Indicates the number of currently waiting Customers.
     * @return An int indicating the number of waiting Customers.
     */
    public abstract int numberWaitingCust();

    /**Gets the Customer the Server is currently serving.
     * @return A Customer that is currently being served by the Server
     */
    public Customer getCust() {
        return this.customer;
    }

    /**Gets the next Service Time of the Server.
     * @return A double indicating the next Service Time of the Server
     */
    public double getNST() {
        return this.nextServiceTime;
    }

    /**Gets the Server's ID.
     * @return An int indicating the Server's ID
     */ 
    public int getID() {
        return this.id;
    } 

    @Override
    public String toString() {
        return String.format("Server ID: " + this.id);
    }


}







