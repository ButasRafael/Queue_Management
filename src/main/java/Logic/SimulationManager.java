package Logic;
import GUI.TextFile;
import Model.Task;
import GUI.QueueEvolutionGUI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int numberOfServers;
    public int numberOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int maxProcessingTime;
    public int minProcessingTime;


    public SelectionPolicy selectionPolicy;

    private final Scheduler scheduler;
    private final TextFile file;
    private List<Task> generatedTasks;
    private final TimeManager timeManager;
    private final List<Task> generatedTasksCopy;
    private final QueueEvolutionGUI queueEvolutionPanel;



    public SimulationManager(int timeLimit, int numberOfServers, int numberOfClients,
                             int minArrivalTime, int maxArrivalTime, int maxProcessingTime,
                             int minProcessingTime, SelectionPolicy selectionPolicy) {
        this.timeLimit = timeLimit;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.selectionPolicy = selectionPolicy;
        String logFileName;
        if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE) {
            logFileName = "simulation_log_queueStrategy.txt";
        } else {
            logFileName = "simulation_log_timeStrategy.txt";
        }
        timeManager = new TimeManager(timeLimit);
        Thread timeManagerThread = new Thread(timeManager);
        timeManagerThread.start();
        scheduler = new Scheduler(numberOfServers, 10000,timeManager);
        scheduler.changeStrategy(selectionPolicy);
        file = new TextFile(logFileName);
        generateNRandomTasks();
        generatedTasksCopy = new ArrayList<>(generatedTasks);
        queueEvolutionPanel = new QueueEvolutionGUI();
    }

    private void generateNRandomTasks() {
        generatedTasks = new ArrayList<>();
        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = (int) (Math.random() * (maxProcessingTime - minProcessingTime + 1) + minProcessingTime);
            int arrivalTime = (int) (Math.random() * (maxArrivalTime - minArrivalTime + 1) + minArrivalTime);
            Task task = new Task(i, arrivalTime, processingTime);
            generatedTasks.add(task);
        }
        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
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
                queueEvolutionPanel.updateQueueEvolution(timeManager.getCurrentTime(), scheduler.getServers(), generatedTasks);
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
            queueEvolutionPanel.displayEndDetails(avgWaitingTime, peakSecond, avgServiceTime);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            scheduler.stop();
            file.close();
        }
    }

}
