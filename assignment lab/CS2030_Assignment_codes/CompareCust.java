import java.util.Comparator;

class CompareCust implements Comparator<Events> {
    
    public int compare(Events a, Events b) {
        if (a.getCust().getArrival() < b.getCust().getArrival()) {
            return -1;
        } else if (a.getCust().getArrival() == b.getCust().getArrival()) {
            return (a.getCust().getCustNum() - b.getCust().getCustNum());
        } else {
            return 1;
        }
    }
    
}
