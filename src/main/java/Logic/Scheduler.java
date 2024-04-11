package Logic;
import Model.Server;
import Model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    private BlockingQueue<Task> taskQueue;
    private AtomicInteger waitingPeriod;
    private TimeManager timeManager;

    public Scheduler(int maxNoServers, int maxTasksPerServer,TimeManager timeManager) {
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.timeManager=timeManager;
        this.servers = new ArrayList<>();
        this.taskQueue = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(timeManager);
            server.setMaxTasksPerServer(maxTasksPerServer);
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }
    public int computeMaxSum(int maxQueueSizeSum){
        int queueSizeSum=0;
        for(Server server:servers){
            queueSizeSum+=server.getQueueSize();
        }
        if(queueSizeSum>maxQueueSizeSum){
            maxQueueSizeSum=queueSizeSum;
        }
        return maxQueueSizeSum;
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        } else if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask(Task t) {
        if (strategy != null) {
            strategy.addTask(servers, t);
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public boolean hasTasksInService() {
        for (Server server : servers) {
            if (server.getQueueSize() > 0 || server.getWaitingPeriod() > 0) {
                return true;
            }
        }
        return false;
    }
}