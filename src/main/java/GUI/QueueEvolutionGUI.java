package GUI;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import Model.Server;
import Model.Task;

public class QueueEvolutionGUI extends JFrame {
    private final JTextArea queueTextArea;

    public QueueEvolutionGUI() {
        setTitle("Queue Evolution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        queueTextArea = new JTextArea();
        queueTextArea.setEditable(false);
        queueTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(queueTextArea);
        add(scrollPane, BorderLayout.CENTER);

        setSize(700, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateQueueEvolution(int currentTime, List<Server> servers, List<Task> remainingTasks) {
        queueTextArea.setText("");
        queueTextArea.append("Current Time: " + currentTime + "\n");
        queueTextArea.append("Waiting clients: ");
        for (int i = 0; i < remainingTasks.size(); i++) {
            Task task = remainingTasks.get(i);
            queueTextArea.append("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
            if ((i + 1) % 10 == 0 && i != remainingTasks.size() - 1) {
                queueTextArea.append("\n");
            } else {
                queueTextArea.append("; ");
            }
        }
        queueTextArea.append("\n");
        int serverIndex = 1;
        for (Server server : servers) {
            queueTextArea.append("Queue " + serverIndex + ": ");
            Task[] serverQueue = server.getTasks();
            if (serverQueue.length == 0) {
                queueTextArea.append("closed");
            } else {
                for (Task task : serverQueue) {
                    queueTextArea.append("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + "),");
                }
            }
            queueTextArea.append("\n");
            serverIndex++;
        }
        queueTextArea.append("\n");
    }


    public void displayEndDetails(double avgWaitingTime, int peakHour, double avgServiceTime) {
        queueTextArea.append("--------------------------------------\n");
        queueTextArea.append("Simulation ended.\n");
        queueTextArea.append("Average waiting time: " + avgWaitingTime + "\n");
        queueTextArea.append("Average service time: " + avgServiceTime + "\n");
        queueTextArea.append("Peak hour: " + peakHour + "\n");

    }
}
