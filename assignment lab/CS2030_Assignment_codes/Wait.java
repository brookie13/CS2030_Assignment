import java.util.function.Supplier;

class Wait implements Events {
    private final Customer cust;
    private final Server server;
    //private final double service;
    private final int iterationZero = 0;
    private final int iterationOne = 1;
    private final boolean printStat;

    Wait(Customer cust, Server server) {
        this.cust = cust;
        this.server = server;
        this.printStat = true;
        //this.service = service;
    }

    private Wait(Customer cust, Server server, boolean printStat) {
        this.cust = cust;
        this.server = server;
        this.printStat = printStat;
    }

    @Override
    public Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
           Supplier<Double>> nextEvent(ImList<Server> serverList, ImList<Double> tracker, 
                   Supplier<Double> serviceTime, Supplier<Double> restTime) {
        int selfCheckIndex = iterationZero;
        int serveIndex = iterationZero;

        //if customer waiting at self checkout       
        if (this.server.getSelfServeStat()) {
            //to find where the first self-checkout is in the server list
            for (int i = 0; i < serverList.size(); i++) {
                Server server = serverList.get(i);
                if (server.getServer() == this.server.getServer()) {
                    selfCheckIndex = i;
                    break;
                }
            }
        
            boolean serveStat = false;
            for (int i = selfCheckIndex; i < serverList.size(); i++) {
                Server server = serverList.get(i);
                if (this.cust.getArrival() >= server.getAvailTime()) {
                    serveStat = true;
                    serveIndex = i;
                    break;
                }
            }
            
            //if can serve lmao
            if (serveStat) {
                Server checkout = serverList.get(serveIndex);
                double waitTime = checkout.getAvailTime() - this.cust.getArrival();
                Customer newCust = this.cust.updateCust(checkout.getServer());
                double timing;
                if (newCust.getArrival() > checkout.getAvailTime()) {
                    timing = newCust.getArrival();
                } else {
                    timing = checkout.getAvailTime();
                }
                newCust = newCust.updateCustTime(timing);
                Events event = new Serve(newCust, checkout);  
                serverList = serverList.set(serveIndex, checkout);
                Server tempServer = serverList.get(selfCheckIndex);
                tempServer = tempServer.newServerQueue(tempServer.getQueueLength() - iterationOne);
                serverList = serverList.set(selfCheckIndex, tempServer);
                tracker = tracker.set(iterationZero, tracker.get(iterationZero) + waitTime);
               
                Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList, tracker);
                Pair<Events, Supplier<Double>> newPair = new Pair<>(event, serviceTime);
                Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
                    ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);
                Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
                    Supplier<Double>> eventsPair = new Pair<>(newPair2, restTime);
        
                return eventsPair; 

            } else {
                //return new wait event with server still the first self checkout
                Server lowestAvailServer = serverList.get(selfCheckIndex);
                double lowestAvail = lowestAvailServer.getAvailTime();

                //get the smallest avail time..
                for (int i = selfCheckIndex; i < serverList.size(); i++) {
                    Server server = serverList.get(i);
                    if (server.getAvailTime() < lowestAvail) {
                        lowestAvail = server.getAvailTime();
                        break;
                    }
                }
                
                double waitTime = lowestAvail - this.cust.getArrival();
                Customer newCust = this.cust.updateCustTime(lowestAvail);
                Events event = new Wait(newCust, this.server, false);
                tracker = tracker.set(iterationZero, tracker.get(iterationZero) + waitTime);

                Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList, tracker);
                Pair<Events, Supplier<Double>> newPair = new Pair<>(event, serviceTime);
                Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
                    ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);
                Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
                    Supplier<Double>> eventsPair = new Pair<>(newPair2, restTime);
        
                return eventsPair; 
                
            }

        } else {
            //if customer waiting at human server <copy from original ig>
            int serverIndex = iterationZero;
            for (int i = 0; i < serverList.size(); i++) {
                Server server = serverList.get(i);
                if (server.getServer() == this.cust.getServerNum()) {
                    serverIndex = i;
                    break;
                }
            }
            Server server = serverList.get(serverIndex);
            //double serveTiming = serviceTime.get();
            double waitTime = server.getAvailTime() - this.cust.getArrival();
            Customer newCust = this.cust.updateCustTime(server.getAvailTime());
            //server = server.newServer(server.newAvail(serveTiming));
            serverList = serverList.set(serverIndex, server);
            Events event;

            if (this.cust.getArrival() < server.getAvailTime()) {
                event = new Wait(this.cust.updateCustTime(server.getAvailTime()), 
                        server, false);
            } else {
                event = new Serve(newCust, server);
                server = server.newServerQueue(server.getQueueLength() - iterationOne);
            }

            serverList = serverList.set(serverIndex, server);
            tracker = tracker.set(iterationZero, tracker.get(iterationZero) + waitTime);
            Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList, tracker);
            Pair<Events, Supplier<Double>> newPair = new Pair<>(event, serviceTime);
            Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
                ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);
            Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
                Supplier<Double>> eventsPair = new Pair<>(newPair2, restTime);
        
            return eventsPair;

        }

         
    }

    @Override
    public Events executeEvent() {
        return this;
    }

    @Override
    public boolean canPrint() {
        return this.printStat;
    }

    @Override
    public boolean isArrive() {
        return false;
    }

    @Override
    public Customer getCust() {
        return this.cust;
    }

    @Override 
    public String toString() { 
        //System.out.println(this.server);
        return String.format("%.3f",this.cust.getArrival()) + " " + 
            this.cust + " waits at " + this.server;
    }

}
