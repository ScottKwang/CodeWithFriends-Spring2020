package cs2030.simulator;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**Encapsulates a Shop to simulate the environment of the Servers serving Customers.
 * The Shop class supports operators such as:
 * (i) Setting up the environment of the Shop.
 * (ii) Setting up the arrival of Customers
 * (iii) Setting up the list of Servers, Human and Self-Checkout Counters
 */

public class Shop {
    private RandomGenerator r1; 
    private PriorityQueue<Event> customerList = new PriorityQueue<Event>();
    private ArrayList<Server> serverList; 
    private DiscreteSimulator ds = new DiscreteSimulator();

    private int seed;
    private int numServer;
    private int nSelf;
    private int qMax;
    private int numCust;
    private double lambda;
    private double mu;
    private double rho;
    private double probServerRest;
    private double probCustomerGreedy;

    public Shop() { }

    /**Initializes the arrival of Customers in the Shop.  
     * @param seed Seed for RandomGenerator
     * @param numServer The number of HumanServers
     * @param nSelf The number of Self-Checkout Counters
     * @param numCust The number of Customers 
     */
    public void setShopParam(int seed, int numServer, int nSelf, int qMax, int numCust, 
        double lambda, double mu, double rho) throws InputMismatchException {
        if (seed < 0) {
            throw new InputMismatchException("Seed cannot be lesser than zero.");
        }
        if (numServer < 0) {
            throw new InputMismatchException("Number of Servers cannot be lesser than zero.");
        }
        if (nSelf < 0) {
            throw new InputMismatchException(
                "Number of Self-Checkout Counters cannot be lesser than zero.");
        }
        if (qMax < 0) {
            throw new InputMismatchException("Max queue for Servers cannot be lesser than zero");
        }
        if (numCust < 0) {
            throw new InputMismatchException("Number of customers cannot be lesser than zero");
        }
        if (lambda < 0) {
            throw new InputMismatchException("Lambda value cannot be lesser than zero");
        }
        if (mu < 0) {
            throw new InputMismatchException("Mu value cannot be lesser than zero");
        }
        if (rho < 0) {
            throw new InputMismatchException("Rho value cannot be lesser than zero");
        }
        this.seed = seed;
        this.numServer = numServer;
        this.nSelf = nSelf;
        this.qMax = qMax;
        this.numCust = numCust;
        this.lambda = lambda;
        this.mu = mu;
        this.rho = rho;
    }

    /**Initializes the arrival of Customers in the Shop.
     */ 
    public void setUpShop() {
        r1 = new RandomGenerator(this.seed, this.lambda, this.mu, this.rho);
      
        // Arrival events generation
        int i = 0;
        double time = 0;

        while (i < numCust) {
            double genTime = r1.genCustomerType();
            // generates a greedy Customer
            if (genTime < probCustomerGreedy) {
                customerList.add(new Event(new GreedyCustomer(i + 1, time)));
            } else {
                customerList.add(new Event(new NormalCustomer(i + 1, time))); 
            } 
            time += r1.genInterArrivalTime();
            i++;
        }

    }

    /**Sets up and intializes the Human Servers and Self-Checkout Counters.
     */
    public void setServer() {
        try {
            serverList = new ArrayList<Server>(numServer);
            int store = 0;
            for (int i = 0; i < numServer; i++, store++) {
                serverList.add(new HumanServer().createHumanServer(i + 1)
                    .initialize(r1, probServerRest, qMax));
            }

            for (int j = store; j < store + nSelf; j++) {
                serverList.add(new SelfCheckoutCounter(j + 1).initialize(r1, probServerRest, qMax));
            }
        } catch (InputMismatchException e) {
            System.err.println("Error with Server inputted: " + e.getMessage());
        }
    }

    /**Sets the probability of the Human Server resting.
     * @param value A double indicating the probability of the Human Server resting
     */
    public void setServerRestParameter(double value) {
        this.probServerRest = value;
    }

    /**Sets the probability of a Customer being Greedy.
     * @param value A double indicating the probability of the Customer being Greedy
     */
    public void setCustomerGreedyParameter(double value) {
        this.probCustomerGreedy = value;
    }

    /**Starts up the simulation in the Shop.
     */
    public void start() {
        ds.setNumbServer(serverList);
        ds.print(customerList);
    }

}
