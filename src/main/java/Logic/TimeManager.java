package Logic;

public class TimeManager implements Runnable {
    private int currentTime;
    private int timeLimit;

    public TimeManager(int timeLimit) {
        this.currentTime = -1;
        this.timeLimit = timeLimit;
    }

    @Override
    public void run() {
        try {
            while (currentTime <= timeLimit) {
                currentTime++;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public boolean isTimeUp() {
        return currentTime > timeLimit;
    }
}
