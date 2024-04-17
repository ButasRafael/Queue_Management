package Model;

import Logic.TimeManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private int maxTasksPerServer;
    private final TimeManager timeManager;
    private final AtomicBoolean isRunning;



    public Server(TimeManager timeManager) {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.timeManager=timeManager;
        this.isRunning = new AtomicBoolean(true);
    }
    public void stop() {
        isRunning.set(false);
    }


    public void setMaxTasksPerServer(int maxTasksPerServer) {
        this.maxTasksPerServer = maxTasksPerServer;
    }
    public void addTask(Task task) {
        boolean added = tasks.offer(task);
        if (added &&tasks.size()<=maxTasksPerServer) {
            waitingPeriod.addAndGet(task.getServiceTime());
        }
    }

    @Override
    public void run() {
        try {
            while (isRunning.get()) {
                Task task = tasks.peek();
                if (task != null && task.getServiceTime() > 0) {
                    task.setStartTime(timeManager.getCurrentTime());
                    while (task.getServiceTime() > 0) {
                        Thread.sleep(1000);
                        task.decrementServiceTime();
                        waitingPeriod.decrementAndGet();
                        task.incrementInitialServiceTime();
                    }
                    tasks.poll();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Server thread interrupted: " + e.getMessage());
        }
    }


    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }

    public int getQueueSize() {
      return tasks.size();

    }
    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

}

