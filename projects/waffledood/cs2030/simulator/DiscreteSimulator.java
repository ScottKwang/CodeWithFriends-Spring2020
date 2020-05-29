package cs2030.simulator;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.InputMismatchException;

/** Encapsulates a Discrete Simulator to sort the different Events.
 *  The 'DiscreteSimulator' class supports operators such as:
 *  (i) sets the number of Servers in the ArrayList of Servers
 *  (ii) orders and prints out the different Events
 */ 

public class DiscreteSimulator {
  
    private ArrayList<Server> serverList;

    private Event firstEvent;
    private Event nextEvent;
    private double endTime = 0;

    // A Stats object to help keep track of relevant statistics & print out the statement
    private Stats stat = new Stats();
  
    public DiscreteSimulator() {}
    
    /**Method to set the number of servers in the ArrayList of Servers.
     * @param ls An empty ArrayList of Servers
     */ 
    public void setNumbServer(ArrayList<Server> ls) {
        try {
            serverList = new ArrayList<Server>(ls.size());
            for (int i = 0; i < ls.size(); i++) {
                Server sv = ls.get(i);
                serverList.add(sv);
            }
        } catch (InputMismatchException e) {
            System.err.println("Error with Server inputted: " + e.getMessage());
        }
    }

    /**Method to print out events taken in, matching available Servers to Customers.
     * @param eventsList A PriorityQueue of Events
     */

    public void print(PriorityQueue<Event> eventsList) { 
        while (!eventsList.isEmpty()) {
            
            firstEvent = eventsList.poll();
            if (firstEvent.getStatus() != Status.SERVER_REST && 
                firstEvent.getStatus() != Status.SERVER_BACK) {
                System.out.println(firstEvent); 
            }
            
            Server currentServer; 
            Customer currentCustomer = firstEvent.getCustomer();
            double svTime;
            double waitTime; 
            int servID;
            
            switch (firstEvent.getStatus()) {
                case SERVER_REST:
                    servID = firstEvent.getServer().getID() - 1;
                    currentServer = serverList.get(servID);

                    currentServer.rest();

                    nextEvent = new Event(currentServer.getNST(), currentServer.getCust(), 
                        firstEvent.getServer(), Status.SERVER_BACK);

                    eventsList.add(nextEvent);
                    break;

                case SERVER_BACK:

                    servID = firstEvent.getServer().getID() - 1;
                    currentServer = serverList.get(servID);
                    
                    if (currentServer.hasWaitingCust()) {
                        currentServer.serveWait();

                        endTime = currentServer.getNST();

                        nextEvent = new Event(endTime, currentServer.getCust(), 
                            firstEvent.getServer(), Status.SERVED);

                        eventsList.add(nextEvent);

                        currentServer.serveWaitRemove(currentServer.getCust());
                    }
                    break;

                case ARRIVES:
                
                    boolean check = false;

                    // check if the ServerList is empty 
                    if (serverList.isEmpty()) {
                        nextEvent = new Event(firstEvent.customer.getArrivalTime(), 
                            firstEvent.customer, Status.LEAVES);
                        stat.countC();
                        eventsList.add(nextEvent);
                        break;
                    } 

                    // checks if the current Customer is Greedy 
                    if (currentCustomer.isGreedy()) {
                        // ID of Server the Greedy Customer will go to
                        servID = shortestQueueScanner(currentCustomer); 
                        currentServer = serverList.get(servID);
                        // if Server can serve the Greedy Customer
                        if (currentServer.canServe(currentCustomer)) {
                            currentServer.serve(currentCustomer);
                            nextEvent = new Event(currentCustomer.getArrivalTime(), 
                                currentCustomer, currentServer, Status.SERVED);
                            eventsList.add(nextEvent);
                            stat.countA();
                            break;
                        } else if (currentServer.canHaveWaiting(currentCustomer)) { 
                            // if Server can have the Greedy Customer waiting
                            currentServer.acceptWait(currentCustomer);
                            nextEvent = new Event(currentCustomer.getArrivalTime(), 
                                currentCustomer, currentServer, Status.WAITS);
                            eventsList.add(nextEvent);
                            stat.countB();
                            break;
                        }
                    }
                    
                    
                    // check if any of the Servers can SERVE the Customer 
                    for (int i = 0; i < serverList.size(); i++) {
                        currentServer = serverList.get(i);
                        
                        // check if any of the Servers can serve the Customer
                        if (currentServer.canServe(firstEvent.customer)) {
                            // current Customer will be Served
                            currentServer.serve(firstEvent.customer);
                            
                            // creates & adds next Event
                            nextEvent = new Event(firstEvent.customer.getArrivalTime(), 
                                firstEvent.customer, currentServer, Status.SERVED);
                            eventsList.add(nextEvent);
                            
                            check = true;
                            stat.countA();
                            break;
                        }
                    }
                    
                    if (check) {
                        break;
                    }
                    
                    // check if any of the Servers can have the Customer WAITS 
                    for (int j = 0; j < serverList.size(); j++) {
                        currentServer = serverList.get(j);
                        
                        // check if any of the Servers can add the Customer to their waiting List 
                        if (currentServer.canHaveWaiting(firstEvent.customer)) {
                            // current Customer will be added to the Server's waiting List 
                            currentServer.acceptWait(firstEvent.customer);
                            
                            // creates & adds next Event
                            nextEvent = new Event(firstEvent.customer.getArrivalTime(),
                                firstEvent.customer, currentServer, Status.WAITS);
                            eventsList.add(nextEvent);
                            
                            check = true;
                            stat.countB();
                            break;
                        }
                    }
                    
                    if (check) {
                        break;
                    }
                    
                    nextEvent = new Event(firstEvent.customer.getArrivalTime(), 
                        firstEvent.customer, Status.LEAVES);
                    eventsList.add(nextEvent);
                    stat.countC();
                    break;

                case WAITS:
                
                    break;

                case SERVED:
                    servID = firstEvent.getServer().getID() - 1;

                    svTime = serverList.get(servID).getNST();
                    serverList.get(servID).serveWaitDone(firstEvent.customer);
                    endTime = serverList.get(servID).getNST();
                    
                    // creates Done Event for the current Customer
                    nextEvent = new Event(endTime, firstEvent.customer, firstEvent.getServer(),
                            Status.DONE); 

                    eventsList.add(nextEvent);

                    stat.countWaitTime(svTime - firstEvent.customer.getArrivalTime());

                    break;

                case DONE:
                    servID = firstEvent.getServer().getID() - 1;
                    currentServer = serverList.get(servID); 
                    
                    boolean flag = false;
                    
                    // if the Server is going to Rest
                    if (currentServer.isGoingToRest()) {
                        nextEvent = new Event(currentServer.getNST(), currentServer.getCust(), 
                        firstEvent.getServer(), Status.SERVER_REST);
                        flag = true;
                        eventsList.add(nextEvent);
                    }

                    if (flag) {
                        break;
                    }
                    
                    // if the Server has a waiting Customer, serve him
                    if (currentServer.hasWaitingCust()) {
                        currentServer.serveWait();
                        
                        endTime = currentServer.getNST();
                        
                        nextEvent = new Event(endTime, currentServer.getCust(),
                            firstEvent.getServer(), Status.SERVED);
                        
                        currentServer.serveWaitRemove(currentServer.getCust());
                        
                        eventsList.add(nextEvent);
                    }
                    
                    break;

                default: 
                    // do nothing
            }
        }
        //Print out relevant statistics
        stat.printStats();
    }

    /**Returns the ID of the Server with the shortest number of waiting Customers.
     * @param cust The Greedy Customer  
     */    
    public int shortestQueueScanner(Customer cust) {
        
        int min = Integer.MAX_VALUE;
        int servID = -1;
        
        // checks which of the Servers are available & have the shortest queue
        for (int i = this.serverList.size() - 1; i >= 0; i--) {
            Server server = serverList.get(i);

            if (server.numberWaitingCust() <= min && server.canServe(cust)) {
                min = server.numberWaitingCust();
                servID = i; 
            } 
        } 

        // if none of the Servers are available, check which has the shortest queue
        if (servID == -1) {
            min = Integer.MAX_VALUE;
            for (int i = this.serverList.size() - 1; i >= 0; i--) {
                Server server = serverList.get(i);
                if (server.numberWaitingCust() <= min) {
                    min = server.numberWaitingCust();
                    servID = i;
                }
            }
        }
        return servID;
    }

}
