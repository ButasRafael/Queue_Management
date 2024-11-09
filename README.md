# Queue Management System

A Java application designed to simulate and manage queues, optimizing for minimized client waiting time. Users can set parameters, view real-time queue evolution, and analyze queue performance through calculated metrics.

## Features

- **Simulation Setup**: Set parameters for clients, queues, service times, and queue assignment strategy.
- **Queue Assignment Strategies**: Select between the shortest queue or shortest time strategy to manage task distribution.
- **Real-time Queue Display**: Visualize queue evolution with updates on client arrivals, queue lengths, and service completions.
- **Summary Metrics**: Review end-of-simulation metrics, including average waiting time, average service time, and peak hour.

## Project Structure

### Main Components

1. **Task**: Represents a client with an ID, arrival time, and service time.
2. **Server**: Manages the queue and processes tasks assigned to it.
3. **Scheduler**: Dispatches tasks to servers according to the selected strategy.
4. **SimulationManager**: Manages simulation settings, generates tasks, and coordinates task dispatching.
5. **QueueEvolutionGUI**: Displays queue evolution during simulation.
6. **SimulationSetupGUI**: Enables users to input simulation parameters and launch the simulation.

### Strategy Pattern

This application employs a strategy pattern to implement queue assignment:
- **Shortest Queue**: Assigns tasks to the server with the fewest clients.
- **Shortest Time**: Assigns tasks to the server with the shortest remaining service time.

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven (for dependencies)

### Installation

1. Clone the repository:
```
git clone https://github.com/ButasRafael/Queue_Management.git
cd Queue_Management
```
2. Build the project with Maven:
```
mvn clean install
```
3. Run the application:
```
java -jar target/Queue_Management.jar
```
## Usage
* Launch the application: Enter simulation parameters in the GUI (number of clients, number of queues, etc.).
* Start the simulation: Observe queue updates and real-time evolution in a separate GUI window.
* View results: At the end of the simulation, view average waiting and service times, as well as the peak hour.
### Testing
JUnit tests are provided to ensure the correctness of the simulation logic. The following tests cover key functionalities:
* Task Assignment: Ensures tasks are correctly assigned based on chosen strategy.
* Queue Evolution: Verifies the real-time queue display for accurate simulation.
* Metrics Calculation: Tests computation of average waiting and service times.
* To run tests:
```
mvn test
```

## Future Enhancements
### Potential improvements include:
* Enhanced visualizations such as interactive charts and real-time analytics.
* Additional queue management models, such as priority-based queues.
* Optimizations for handling larger datasets and more complex scenarios.
* Enhanced GUI with real-time visual effects.
