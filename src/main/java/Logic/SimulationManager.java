package Logic;
import GUI.TextFile;
import Model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationManager implements Runnable {
    public int timeLimit = 200;
    public int numberOfServers = 20;
    public int numberOfClients = 1000;
    public int minArrivalTime = 10;
    public int maxArrivalTime = 100;
    public int maxProcessingTime = 3;
    public int minProcessingTime = 9;


    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME ;

    private Scheduler scheduler;
    private TextFile file;
    private final String logFileName = "simulation_log_timeStrategy.txt";
    private List<Task> generatedTasks;
    private TimeManager timeManager;
    private List<Task> generatedTasksCopy;



    public SimulationManager() {
        timeManager = new TimeManager(timeLimit);
        Thread timeManagerThread = new Thread(timeManager);
        timeManagerThread.start();
        scheduler = new Scheduler(numberOfServers, 1000,timeManager);
        scheduler.changeStrategy(selectionPolicy);
        file = new TextFile(logFileName);
        generateNRandomTasks();
        generatedTasksCopy = new ArrayList<>(generatedTasks);
    }

    private void generateNRandomTasks() {
        generatedTasks = new ArrayList<>();
        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = (int) (Math.random() * (maxProcessingTime - minProcessingTime + 1) + minProcessingTime);
            int arrivalTime = (int) (Math.random() * (maxArrivalTime - minArrivalTime + 1) + minArrivalTime);
            Task task = new Task(i, arrivalTime, processingTime);
            generatedTasks.add(task);
        }
        Collections.sort(generatedTasks, (t1, t2) -> t1.getArrivalTime() - t2.getArrivalTime());
    }

    private double calculateAverageServiceTime() {
        int totalServiceTime = 0;
        int count = 0;
        for (Task task : generatedTasksCopy) {
            if (task.getStartTime() != -1) {
                count++;
                if (task.getStartTime() + task.getInitialServiceTime() <= timeLimit) {
                    totalServiceTime += task.getInitialServiceTime();
                } else {
                    totalServiceTime += timeLimit - task.getStartTime();
                }
            }
        }
        if (count == 0) {
            return 0;
        }
        return (double) totalServiceTime / count;
    }

    public double computeAverageWaitingTime() {
        int totalWaitingTime = 0;
        int taskCount = 0;
        for (Task task : generatedTasksCopy) {
            if (task.getStartTime() != -1) {
                totalWaitingTime += (task.getStartTime() - task.getArrivalTime());
                taskCount++;
            }
            else if(task.getArrivalTime()<=timeLimit){
                totalWaitingTime+=timeLimit-task.getArrivalTime();
                taskCount++;
            }
        }

        if (taskCount == 0) {
            return 0;
        }
        return (double) totalWaitingTime / taskCount;
    }

    @Override
    public void run() {
        try {
            int maxSum=0;
            int peakSecond=0;
            while (!timeManager.isTimeUp() && (!generatedTasks.isEmpty() || scheduler.hasTasksInService())) {
                for (Task task : new ArrayList<>(generatedTasks)) {
                    if (task.getArrivalTime() == timeManager.getCurrentTime()) {
                        scheduler.dispatchTask(task);
                        generatedTasks.remove(task);
                    }
                }

                file.update(timeManager.getCurrentTime(), scheduler.getServers(), generatedTasks);
                int maxSum2 = maxSum;
                maxSum=scheduler.computeMaxSum(maxSum);
                if(maxSum2 != maxSum){
                    peakSecond=timeManager.getCurrentTime();
                }
                Thread.sleep(1000);
            }
            double avgServiceTime = calculateAverageServiceTime();
            double avgWaitingTime=computeAverageWaitingTime();
            file.printSimulationEnd(avgWaitingTime,peakSecond,avgServiceTime);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            scheduler.stop();
            file.close();
        }
    }


    public static void main(String[] args) {
        SimulationManager simulationManager = new SimulationManager();
        Thread t = new Thread(simulationManager);
        t.start();
    }
}
