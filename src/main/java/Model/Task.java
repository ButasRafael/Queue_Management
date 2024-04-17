package Model;

public class Task {
    private final int id;
    private final int arrivalTime;
    private int serviceTime;
    private int startTime;
    private int initialServiceTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.startTime=-1;
        this.initialServiceTime=0;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }
    public void decrementServiceTime() {
        if (serviceTime > 0) {
            serviceTime--;
        }
    }
    public int getStartTime() {
        return this.startTime;
    }

    public void setStartTime(int time) {
        this.startTime = time;
    }
    public void incrementInitialServiceTime() {
        initialServiceTime++;
    }

    public int getInitialServiceTime() {
        return initialServiceTime;
    }

}

