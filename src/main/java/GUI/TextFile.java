package GUI;

import Model.Server;
import Model.Task;

import java.util.List;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class TextFile {
    private PrintStream logStream;

    public TextFile(String logFileName) {
        try {
            logStream = new PrintStream(new FileOutputStream(logFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(int time, List<Server> servers, List<Task> tasks) {
        logStream.println("Time " + time);

        logStream.print("Waiting clients: ");
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                logStream.print("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + "); ");
            }
        }
        logStream.println();

        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            logStream.println("Queue " + (i + 1) + ": " + formatQueue(server));
        }

        logStream.println("--------------------------------------");
    }

    private String formatQueue(Server server) {
        if (server.getQueueSize() == 0) {
            return "closed ";
        } else {
            StringBuilder builder = new StringBuilder();
            Task[] tasks = server.getTasks();
            for (Task task : tasks) {
                builder.append("(").append(task.getId()).append(",").append(task.getArrivalTime()).append(",").append(task.getServiceTime());
                builder.append("); ");
            }
            return builder.toString();
        }
    }


    public void close() {
        if (logStream != null) {
            logStream.close();
        }
    }

    public void printSimulationEnd(double averageWaitingTime, int peakHour, double averageServiceTime) {
        logStream.println("Simulation ended.");
        logStream.println("Average waiting time: " + averageWaitingTime);
        logStream.println("Average service time: " + averageServiceTime);
        logStream.println("Peak hour: " + peakHour);
    }
}
