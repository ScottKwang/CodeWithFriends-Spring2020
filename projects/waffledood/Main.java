//package cs2030.simulator;
import cs2030.simulator.Customer;
import cs2030.simulator.DiscreteSimulator;
import cs2030.simulator.Event;
import cs2030.simulator.Server;
import cs2030.simulator.Stats;
import cs2030.simulator.Status;
import cs2030.simulator.Shop;

import cs2030.simulator.RandomGenerator;

import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Main {

    private static ArrayList<Server> serverList = new ArrayList<>(); //list of Servers
    private static Shop shop = new Shop();

    /**Scans for user input of values for different parameters of the Discrete Event Simulator.
     */
    public static void mainInput() {
        try {
            Scanner in = new Scanner(System.in);
            
            System.out.print("Enter seed value (1 or 2): ");
            int seed = in.nextInt();

            System.out.print("Enter an int for total number of Servers: ");
            int numServer = in.nextInt();

            System.out.print("Enter an int for number of self-checkout Counters: ");
            int numSelf = in.nextInt();

            System.out.print("Enter an int for maximum queue length: ");
            int qMax = in.nextInt();

            System.out.print("Enter an int for total number of Customers: ");
            int numCust = in.nextInt();

            System.out.print("Enter a double for lambda value (0.0 <= x <= 10.0): ");
            double lambda = in.nextDouble();

            System.out.print("Enter a double for mu value (0.0 <= x <= 1.0): ");
            double mu = in.nextDouble();

            System.out.print("Enter a double for rho value (0.0 <= x <= 1.0): ");
            double rho = in.nextDouble();
            
            //shop.setShopParam(in.nextInt(), in.nextInt(), in.nextInt(), in.nextInt(), 
            //    in.nextInt(), in.nextDouble(), in.nextDouble(), in.nextDouble());
            shop.setShopParam(seed, numServer, numSelf, qMax, numCust, lambda, mu, rho);

            System.out.print("Enter the probability (0.0 <= x <= 1.0) of a Human Server resting: ");
            shop.setServerRestParameter(in.nextDouble());

            System.out.print("Enter the probability (0.0 <= x <= 1.0) of a Customer being Greedy: ");
            shop.setCustomerGreedyParameter(in.nextDouble());
            System.out.println("____________________________________________________________________");

            shop.setUpShop();
            shop.setServer();
            shop.start();
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
    }

    /** This is the Main class that drives the program.
     *  The Main class supports 3 operations which include
     *  (i) scanning user input, (ii) running through various events
     *  and (iii) printing the statistics
     */ 
    public static void main(String[] args) {
        try {
            DiscreteSimulator ds = new DiscreteSimulator();
            mainInput();            
        } catch (InputMismatchException e) {
            System.err.println("Error with input: " + e.getMessage());
        }
    }
}






