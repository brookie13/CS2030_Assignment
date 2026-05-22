class Customer {
    private final int custNum;
    private final double arrivalTime;
    //private final double serviceTime;
    private final String server;
    private final boolean waitStat;

    Customer(int custNum, double arrivalTime) {
        this.custNum = custNum;
        this.arrivalTime = arrivalTime;
        //this.serviceTime = serviceTime;
        this.server = " ";
        this.waitStat = false;
    }

    Customer(int custNum, double arrivalTime, String server, boolean waitStat) {
        this.custNum = custNum;
        this.arrivalTime = arrivalTime;
        //this.serviceTime = serviceTime;
        this.server = server;
        this.waitStat = waitStat;
    }

    int getCustNum() {
        return this.custNum;
    }

    double getArrival() {
        return this.arrivalTime;
    }

    /*double getServiceTime() {
        return this.serviceTime;
    }*/

    String getServerNum() {
        return this.server;
    }

    boolean getWaitStat() {
        return this.waitStat;
    }
    
    /*boolean beServed(Server s) {
        boolean newServe = s.isAvail(this.getArrival(), this.getServiceTime());
        return newServe;
    }*/

    Customer updateCustTime(double time) {
        return new Customer(this.getCustNum(), time, 
                this.getServerNum(), this.getWaitStat());
    }

    Customer updateCust(String server) {
        return new Customer(this.getCustNum(), this.getArrival(), server, this.getWaitStat());
    }

    Customer updateCustWait() {
        return new Customer(this.getCustNum(), this.getArrival(), this.getServerNum(), true);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.custNum);
    }



}
