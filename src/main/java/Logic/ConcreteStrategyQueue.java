package Logic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) {
        int index = 0;
        int comp = servers.get(0).getQueueSize();
        for (Server server : servers) {
            if (server.getQueueSize() < comp) {
                index = servers.indexOf(server);
                comp = server.getQueueSize();
            }
        }
        servers.get(index).addTask(t);
    }
}
