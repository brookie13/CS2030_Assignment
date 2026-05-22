import java.util.function.Supplier;

interface Events {
    Pair<Pair<Pair<Events, Supplier<Double>>, Pair<ImList<Server>,ImList<Double>>>,
        Supplier<Double>> nextEvent(ImList<Server> serverList, ImList<Double> tracker, 
                Supplier<Double> serviceTime, Supplier<Double> restTime);

    Events executeEvent();
    
    Customer getCust();

    boolean isArrive();

    boolean canPrint();

    //Supplier<Double> getRest();

}
