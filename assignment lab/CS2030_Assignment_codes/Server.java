class Server {
    private final String server;
    private final double availTime;
    private final int maxQueue;
    private final int qLength;
    private final boolean selfServe;

    Server(String server, int maxQueue) {
        this.server = server;
        this.maxQueue = maxQueue;
        this.availTime = 0;
        this.qLength = 0;
        this.selfServe = false;
    }

    Server(String server, int maxQueue, boolean selfServe) {
        this.server = server;
        this.maxQueue = maxQueue;
        this.availTime = 0;
        this.qLength = 0;
        this.selfServe = selfServe;
    }

    Server(String server, int qLength, int maxQueue, double availTime) {
        this.server = server;
        this.maxQueue = maxQueue;
        this.availTime = availTime;
        this.qLength = qLength;
        this.selfServe = false;
    }

    Server(String server, int qLength, int maxQueue, double availTime, boolean selfServe) {
        this.server = server;
        this.maxQueue = maxQueue;
        this.availTime = availTime;
        this.qLength = qLength;
        this.selfServe = selfServe;
    }

    String getServer() {
        return this.server;
    }

    int getMaxQueue() {
        return this.maxQueue;
    }

    double getAvailTime() {
        return this.availTime;
    }

    int getQueueLength() {
        return this.qLength;
    }

    boolean getSelfServeStat() {
        return this.selfServe;
    }

    //other functions
    
    //check whether server can serve
    boolean isAvail(double arrival, double service) {
        return arrival >= this.getAvailTime();
    }

    //calculate new avail time for server
    double newAvail(double serviceTime) {
        return this.getAvailTime() + serviceTime;
    }
    
    // New server with updated avail timing
    Server newServer(double avail) {
        return new Server(this.getServer(), this.getQueueLength(), this.getMaxQueue(), avail);
    }

    //update queue of server
    Server newServerQueue(int qLength) {
        return new Server(this.getServer(), qLength, this.getMaxQueue(), this.getAvailTime(),
                this.getSelfServeStat());
    }

    @Override 
    public String toString() {
        return this.server;
    }

}
