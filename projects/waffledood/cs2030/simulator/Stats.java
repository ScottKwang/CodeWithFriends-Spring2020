package cs2030.simulator;

import java.lang.ArithmeticException;

/** Encapsulates a Stats model to track and compute relevant statistics.
 *  The 'Stats' class support operators such as:
 *  (i) counting the different transitions a Customer can go through
 *  (ii) computing the total waiting time of Customers who wait
 *  (iii) computing the average waiting time of Customers who wait
 *  (iv) printing the relevant statistics to the user
 */ 

public class Stats {
    private static int transitionA = 0; // tracks Customers: ARRIVES -> SERVED -> DONE
    private static int transitionB = 0; // tracks Customers: ARRIVES -> WAITS -> SERVED -> DONE
    private static int transitionC = 0; // tracks Customers: ARRIVES -> LEAVES
    private static double totalWaitingTime = 0; // tracks total waiting time of Customers
    private static double avgTime = 0; // tracks average waiting time of Customers

    Stats() {}

    public void countA() {
        this.transitionA++;
    }

    public void countB() {
        this.transitionB++;
    }

    public void countC() {
        this.transitionC++;
    }

    /** Method to log the waiting time of all waiting Customers.
     *  @param time The double representing the time a Customer has been waiting for.
     */ 
    public void countWaitTime(double time) throws ArithmeticException {
        if (time < 0) {
            throw new ArithmeticException("A double lesser than 0 was entered");
        } else {
            this.totalWaitingTime += time;
        }
    }

    /** Prints out the relevant statistics. 
     */ 
    public void printStats() {
        if (transitionA + transitionB == 0) {
            avgTime = 0;
        } else {
            avgTime = totalWaitingTime / (transitionA + transitionB);
        }
        System.out.println(String.format("[%.3f %d %d]", this.avgTime, 
            this.transitionA + this.transitionB, this.transitionC));
    }

}
