import java.util.function.Supplier;

class Arrive implements Events {
    private final Customer cust;
    private final int iterationZero = 0;
    private final int addOne = 1;


    Arrive(Customer cust) {
        this.cust = cust;
    }

    @Override
    public Events executeEvent() {
        return this;
    }

    @Override 
    public Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>,ImList<Double>>>, 
           Supplier<Double>> nextEvent(ImList<Server> serverList, ImList<Double> tracker, 
                   Supplier<Double> serviceTime, Supplier<Double> restTime) {
        boolean serveStat = false;
        int serveIndex = iterationZero;
        boolean queueStat = false;
        int tempIndex = iterationZero;

        //check for free server
        for (int i = 0; i < serverList.size(); i++) {
            Server server = serverList.get(i);
            if (server.getAvailTime() <= this.cust.getArrival()) {
                serveStat = true;
                serveIndex = i;
                break;
            }
        }

        if (serveStat) {
            Server server = serverList.get(serveIndex);
            server = server.newServer(this.cust.getArrival());
            Events event = new Serve(this.cust.updateCust(server.getServer()), server);
            serverList = serverList.set(serveIndex, server);

            Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList,tracker);
            Pair<Events, Supplier<Double>> newPair = new Pair<>(event, serviceTime);
            Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
                ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);

            Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
                Supplier<Double>> eventPair = new Pair<>(newPair2, restTime);
            return eventPair;

        } else {
            //check for queue
            tempIndex = serverList.size();

            // find where the first selfcheckout counter is
            for (int i = 0; i < serverList.size(); i++) {
                Server server = serverList.get(i);
                if (server.getSelfServeStat()) {
                    tempIndex = i + addOne;
                    break;
                }
            }

            for (int i = 0; i < tempIndex; i++) {
                Server server = serverList.get(i);
                if (server.getQueueLength() < server.getMaxQueue()) {
                    queueStat = true;
                    serveIndex = i;
                    break;
                }
            }
            
            if (queueStat) { 
                Server server = serverList.get(serveIndex);
                server = server.newServerQueue(server.getQueueLength() + addOne);
                
                Events event = new Wait(this.cust.updateCust(server.getServer()).updateCustWait(), 
                        server);
                serverList = serverList.set(serveIndex, server);
                Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList, tracker);
                Pair<Events, Supplier<Double>> newPair = new Pair<>(event, serviceTime);
                Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
                    ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);

                Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
                    Supplier<Double>> eventPair = new Pair<>(newPair2, restTime);
                return eventPair;    

            } else {

                //leave
                Events event = new Leave(this.cust);
                Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList, tracker);
                Pair<Events, Supplier<Double>> newPair = new Pair<>(event, serviceTime);
                Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
                    ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);
                Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
                    Supplier<Double>> eventPair = new Pair<>(newPair2, restTime);
                return eventPair;
            }
            
        }

    }

    @Override
    public boolean canPrint() {
        return true;
    }
    
    @Override
    public boolean isArrive() {
        return true;
    }

    @Override
    public Customer getCust() {
        return this.cust;
    }

    @Override
    public String toString() {
        return String.format("%.3f",this.cust.getArrival()) + " " + this.cust + " arrives"; 
    }
    

}
