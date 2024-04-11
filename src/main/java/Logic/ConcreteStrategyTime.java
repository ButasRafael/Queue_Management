package Logic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        int index = 0;
        Server shortestTimeServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod() < shortestTimeServer.getWaitingPeriod()) {
                shortestTimeServer = server;
            }
        }
        shortestTimeServer.addTask(t);
    }
}