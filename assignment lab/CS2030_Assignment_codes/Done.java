import java.util.function.Supplier;

class Done implements Events {
    private final Customer cust;
    private final Server server;
    private final double service;
    private final int iterationZero = 0;
    private final int iterationOne = 1;

    Done(Customer cust, Server server, double service) {
        this.cust = cust;
        this.server = server;
        this.service = service;
        
    }
    
    @Override
    public Events executeEvent() {
        return this;
    }

    @Override
    public Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
           Supplier<Double>> nextEvent(ImList<Server> serverList, ImList<Double> tracker, 
                   Supplier<Double> serviceTime, Supplier<Double> restTime) {
        int serverIndex = iterationZero;
        for (Server server : serverList) {
            if (server.getServer() == this.cust.getServerNum()) {
                serverIndex = serverList.indexOf(server);
                break;
            }
        }
        
        Server server = serverList.get(serverIndex);
        
        if (!server.getSelfServeStat()) {
            double resting = restTime.get();
            server = server.newServer(server.newAvail(resting));
        }
       
        serverList = serverList.set(serverIndex, server);
        tracker = tracker.set(iterationOne, tracker.get(iterationOne) + iterationOne);
        Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList, tracker);
        Pair<Events, Supplier<Double>> newPair = new Pair<>(this, serviceTime);
        Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
            ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);
        Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
            Supplier<Double>> eventsPair = new Pair<>(newPair2, restTime);
         
        return eventsPair;
    }
   
    @Override
    public Customer getCust() {
        return this.cust;
    }

    @Override
    public boolean isArrive() {
        return false;
    }
    
    @Override
    public boolean canPrint() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("%.3f",this.cust.getArrival()) + " " + 
            this.cust + " done serving by " + this.server;
    }
}
