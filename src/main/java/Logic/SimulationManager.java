package Logic;
import GUI.TextFile;
import Model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationManager implements Runnable {
    public int timeLimit = 60;
    public int numberOfServers = 5;
    public int numberOfClients = 50;
    public int minArrivalTime = 2;
    public int maxArrivalTime = 40;
    public int maxProcessingTime = 1;
    public int minProcessingTime = 7;


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
        for (Task task : generatedTasks) {
            totalServiceTime += task.getServiceTime();
        }
        return (double) totalServiceTime / generatedTasks.size();
    }

    public double computeAverageWaitingTime() {
        int totalWaitingTime = 0;
        int taskCount = 0;
        for (Task task : generatedTasksCopy) {
            if (task.getStartTime() != -1) {
                totalWaitingTime += (task.getStartTime() - task.getArrivalTime());
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
            double avgServiceTime = calculateAverageServiceTime();
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
            double avgWaitingTime=computeAverageWaitingTime();
            file.printSimulationEnd(avgWaitingTime,peakSecond,avgServiceTime);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            file.close();
            System.exit(0);
        }
    }




    public static void main(String[] args) {
        SimulationManager simulationManager = new SimulationManager();
        Thread t = new Thread(simulationManager);
        t.start();
    }
}
