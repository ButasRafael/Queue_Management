package Logic;

import java.util.List;
import Model.Server;
import Model.Task;
public interface Strategy {
    void addTask(List<Server> servers, Task t);
}


