import java.util.function.Supplier;

class Leave implements Events {
    private final Customer cust;
    private final int iterationTwo = 2;
    private final int addOne = 1;

    Leave(Customer cust) {
        this.cust = cust;
        //this.server = server;
    }

    @Override
    public Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
           Supplier<Double>> nextEvent(ImList<Server> serverList, ImList<Double> tracker, 
                   Supplier<Double> serviceTime, Supplier<Double> restTime) {
        // add to leave count
        tracker = tracker.set(iterationTwo, tracker.get(iterationTwo) + addOne); 
        Pair<ImList<Server>, ImList<Double>> miscPair = new Pair<>(serverList, tracker);
        Pair<Events, Supplier<Double>> newPair = new Pair<>(this, serviceTime);
        Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
            ImList<Double>>> newPair2 = new Pair<>(newPair, miscPair);
        Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, ImList<Double>>>, 
            Supplier<Double>> eventPair = new Pair<>(newPair2, restTime);
        return eventPair;
    }

    @Override
    public Events executeEvent() {
        return this;
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
        return String.format("%.3f", this.cust.getArrival()) + " " + this.cust + " leaves";
    }



}
