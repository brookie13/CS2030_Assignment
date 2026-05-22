import java.lang.StringBuilder;
import java.lang.Math;
import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final int qmax;
    private final ImList<Pair<Double,Supplier<Double>>> inputTimes;
    private static final int custAdd = 1;
    private final double startingZero = 0.0;
    private final int iterationZero = 0;
    private final int iterationOne = 1;
    private final int iterationTwo = 2;
    private final Supplier<Double> restTime;
    private final int numOfSelfChecks;

    Simulator(int numOfServers, int numOfSelfChecks, int qmax, 
            ImList<Pair<Double,Supplier<Double>>> inputTimes, Supplier<Double> restTime) {
        this.numOfServers = numOfServers;
        this.qmax = qmax;
        this.inputTimes = inputTimes;
        this.restTime = restTime;
        this.numOfSelfChecks = numOfSelfChecks;
    }

    StringBuilder simulate() {
        //creating Servers and their queues.
        ImList<Server> serverList = new ImList<Server>();
        for (int i = iterationOne; i <= this.numOfServers; i++) {
            Server s = new Server(Integer.toString(i), this.qmax);
            serverList = serverList.add(s);
        }

        //adding selfcheckout
        for (int i = iterationOne; i <= this.numOfSelfChecks; i++) {
            //i += iterationOne;
            Server s = new SelfCheckout(Integer.toString(this.numOfServers + i), this.qmax);
            serverList = serverList.add(s);
        }

        //creating Supplier
        Pair<Double, Supplier<Double>> firstPairSup = inputTimes.get(0);
        Supplier<Double> serviceTime = firstPairSup.second();
        
        //Creating customers
        PQ<Events> customers = new PQ<>(new CompareCust());
        int custNum = Simulator.custAdd;
        for (Pair<Double, Supplier<Double>> custInput : inputTimes) {
            double arrivalTime = custInput.first();
            //double serviceTime = custInput.second();
            Customer c = new Customer(custNum, arrivalTime);
            Events e = new Arrive(c);
            customers = customers.add(e);
            custNum += Simulator.custAdd;
        }

        ImList<Server> newServer = serverList;
        ImList<Double> tracker = new ImList<Double>();
        tracker = tracker.add(startingZero).add(startingZero).add(startingZero);
        //[Total wait, served, left]
        Supplier<Double> restTime = this.restTime;

        StringBuilder sb = new StringBuilder();
        while (!customers.isEmpty()) {
            serverList = newServer;
            Pair<Events, PQ<Events>> custEventPair = customers.poll();
            Events firstEvent = custEventPair.first();
            customers = custEventPair.second();
            
            if (firstEvent.canPrint()) {
                sb = sb.append(firstEvent.executeEvent() + "\n");
            }

            Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>,ImList<Double>>>, 
                Supplier<Double>> newEventPair = firstEvent.nextEvent(serverList,tracker, 
                        serviceTime, restTime);

            Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>, 
                ImList<Double>>> newEventPair2 = newEventPair.first();
            restTime = newEventPair.second();
            Pair<Events, Supplier<Double>> firstPair = newEventPair2.first();

            Events newEvent = firstPair.first();
            serviceTime = firstPair.second();

            Pair<ImList<Server>, ImList<Double>> miscPair = newEventPair2.second();
            newServer = miscPair.first();
            ImList<Double> newTracker = miscPair.second();
            tracker = newTracker;
            serviceTime = serviceTime;
  
            if (firstEvent != newEvent) {
                customers = customers.add(newEvent);
            }
            

        }
        tracker = tracker.set(iterationZero, 
                tracker.get(iterationZero) / tracker.get(iterationOne));
        double avgWait = tracker.get(iterationZero);
        double servedCustD = tracker.get(iterationOne);
        double leaveCustD = tracker.get(iterationTwo);
        int servedCust = (int)servedCustD;
        int leaveCust = (int)leaveCustD;
        String trackerList = "[" + String.format("%.3f", avgWait) + " " 
            + String.valueOf(servedCust) + " " + String.valueOf(leaveCust) + "]"; 
        sb = sb.append(trackerList);
        
        return sb;           
    }

    @Override
    public String toString() {
        return "simulator";
    }




}
