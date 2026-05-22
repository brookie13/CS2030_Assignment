class SelfCheckout extends Server {
    
    SelfCheckout(String name, int maxQueue) {
        super(name, maxQueue, true);
    }

    SelfCheckout(String name, int queueLength, int maxQueue, double avail, boolean selfServe) {
        super(name, queueLength, maxQueue, avail, selfServe);
    }
    
    @Override
    SelfCheckout newServer(double avail) {
        return new SelfCheckout(super.getServer(),super.getQueueLength(), super.getMaxQueue(), 
                avail, true);
    }

    @Override
    SelfCheckout newServerQueue(int qLength) {
        return new SelfCheckout(super.getServer(), qLength, super.getMaxQueue(), 
                super.getAvailTime(), true);
    }

    @Override 
    public String toString() {
        return "self-check " + super.getServer(); 
    }

}
