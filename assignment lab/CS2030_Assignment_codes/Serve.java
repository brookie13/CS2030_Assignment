import java.util.function.Supplier;

class Serve implements Events {
    private final Customer cust;
    private final Server server;
    private final int iterationZero = 0;
    private final int lengthOne = 1;

    Serve(Customer cust, Server server) {
        this.cust = cust;
        this.server = server;
    }
    
    @Override
    public Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
           ImList<Double>>>, Supplier<Double>> nextEvent(ImList<Server> serverList, 
                   ImList<Double> tracker, Supplier<Double> serviceTime, 
                   Supplier<Double> restTime) {

        int serverIndex = iterationZero;
        for (Server server : serverList) {
            if (server.getServer() == this.cust.getServerNum()) {
                serverIndex = serverList.indexOf(server);
                break;
            }
        }
        Server server = serverList.get(serverIndex);
        double serveTime = serviceTime.get();
        double newAvail = server.newAvail(serveTime);
        server = server.newServer(newAvail);
        int tempIndex = iterationZero;      

        Customer newCust = this.cust.updateCustTime(this.cust.getArrival() + serveTime);

        serverList = serverList.set(serverIndex,server);
        Events event = new Done(newCust,server, serveTime);

        Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList, tracker);
        Pair<Events, Supplier<Double>> newPair = new Pair<>(event, serviceTime);
        Pair<Pair<Events,Supplier<Double>>, Pair<ImList<Server>, 
            ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);
        Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
            ImList<Double>>>, 
            Supplier<Double>> eventsPair = new Pair<>(newPair2, restTime);

        return eventsPair;
    }

    @Override 
    public Events executeEvent() {
        return this;
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
    public Customer getCust() {
        return this.cust;
    }

    @Override
    public String toString() {
        return String.format("%.3f",this.cust.getArrival()) + " " + 
            this.getCust() + " serves by " + this.server; 
    }

}
