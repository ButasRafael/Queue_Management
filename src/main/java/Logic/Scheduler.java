package Logic;
import Model.Server;
import Model.Task;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTasksPerServer,TimeManager timeManager) {
        this.servers = new ArrayList<>();
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
    public void stop() {
        for (Server server : servers) {
            server.stop();
        }
    }
}