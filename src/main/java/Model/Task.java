package Model;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int startTime;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.startTime=-1;
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

}

