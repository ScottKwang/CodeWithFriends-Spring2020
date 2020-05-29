package cs2030.simulator;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**Encapsulates an Event that serves as a link between Servers and Customers.
 * The 'Event' class supports operator such as:
 * (i) creating an Event
 * (ii) basis of comparison between Events 
 **/

public class Event implements Comparable<Event> {
    private int numbServers;
    private int waitingCustomerID = 0;
    private Event firstEvent;
    private Event nextEvent;
    private double endTime = 0; // indicates the time a server is available   
    
    // details regarding each Event
    private double eventTime = 0;
    private double serverAvailTime = 0; // time when Server is done serving its current Customer
    private double serverWaitTime = 0; // time when waiting customer has been done serving
    private int customerID = 0;
    public Customer customer;
    private int serverID = 0;
    private Server server;
    private Status status; 

    /**Constructor to create an Event.
     */
    public Event() {}

    /**Constructor to create an Event, for when a Customer just arrives.
      *@param cust The customer that forms part of an Event
      */ 
    public Event(Customer cust) {
        this.eventTime = cust.getArrivalTime();
        this.customerID = cust.getId();
        this.customer = cust;
        this.status = Status.ARRIVES;
    }
    
    /**Constructor to create an Event, for a Customer of any status.
     * @param time The time the Event occurs
     * @param cust The Customer that is part of the Event
     * @param server The Server that is part of the Event
     * @param status The Status of the Event
     */ 
    public Event(double time, Customer cust, Server server, Status status) {
        this.eventTime = time;
        this.customerID = cust.getId();
        this.customer = cust;
        this.serverID = server.getID();
        this.server = server;
        this.status = status;
    }

    /**Constructor to create an Event, for a Customer. 
     * @param time The time the Event occurs
     * @param cust The Customer part of the Event 
     * @param status The Status of the Event
     */ 
    public Event(double time, Customer cust, Status status) {
        this.eventTime = time;
        this.customerID = cust.getId();
        this.customer = cust; 
        this.server = null;
        this.status = status;
    }
    
    /**compareTo method that forms the basis of ordering of Events, with order
     * of priority from Event Time, to Customer ID.
     * @param ev The Event that is to be compared to
     */ 
    @Override 
    public int compareTo(Event ev) {
        if (this.eventTime > ev.eventTime) {
            return 1;
        } else if (this.eventTime < ev.eventTime) {
            return -1;
        } else {
            if (this.customerID > ev.customerID) {
                return 1;
            } else if (this.customerID < ev.customerID) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
    /**Sets the Status of the Customer in the Event.
     * @param status The status the Customer in this event will be
     */ 
    public void setStatus(Status status) {
        this.status = status;
    }

    /**Sets the time of the Event.
     * @param time The time the Event will be
     */ 
    public void setTime(double time) {
        this.eventTime = time;
    }
    
    /**Gets the time of the Event.
     * @return A double representing the time of the Event
     */ 
    public double getTime() {
        return this.eventTime;
    }
    
    /**Gets the ID of the Event.
     * @return A double representing the ID of the Customer in the Event
     */ 
    public int getID() {
        return this.customerID;
    }
    
    /**Gets the Server of the Event.
     * @return A Server that is linked to the Event
     */ 
    public Server getServer() {
        return this.server;
    }

    /**Gets the Customer of the Event.
     * @return A Customer that is linked to the Event
     */
    public Customer getCustomer() {
        return this.customer;
    }

    /**Gets the Status of the Customer linked to the Event.
     * @return The Status of the Customer linked to the Event
     */ 
    public Status getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        if (this.status == Status.ARRIVES) {
            return String.format("%.3f ", this.eventTime) + this.customer.toString() 
                + String.format(" arrives");
        } else if (this.status == Status.LEAVES) {
            return String.format("%.3f ", this.eventTime) + this.customer.toString() 
                + String.format(" leaves");
        }
        
        if (this.status == Status.SERVED) {
            return String.format("%.3f ", this.eventTime) + this.customer.toString() 
                + String.format(" served by ") + this.server.toString();
        } else if (this.status == Status.WAITS) {
            return String.format("%.3f ", this.eventTime) + this.customer.toString() 
                + String.format(" waits to be served by ") + this.server.toString();
        } else if (this.status == Status.DONE) {
            return String.format("%.3f ", this.eventTime) + this.customer.toString() 
                + String.format(" done serving by ") + this.server.toString(); 
        } else {
            return String.format("");
        }
        
    }
}
