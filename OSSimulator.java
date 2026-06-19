import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OSSimulator {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Process> processList = new ArrayList<>();
        boolean running = true;

        while (running) {
            System.out.println("\n===== OS SIMULATOR =====");
            System.out.println("1. Add a process");
            System.out.println("2. View all processes");
            System.out.println("3. Run FCFS Scheduling");
            System.out.println("4. Run SJF Scheduling");
            System.out.println("5. Run Round Robin Scheduling");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            if (choice == 1) {
                addProcess(scanner, processList);
            } else if (choice == 2) {
                viewProcesses(processList);
            } else if (choice == 3) {
                CPUScheduler.runFCFS(processList);
            } else if (choice == 4) {
                CPUScheduler.runSJF(processList);
            } else if (choice == 5) {
                System.out.print("Enter time quantum: ");
                int quantum = scanner.nextInt();
                CPUScheduler.runRoundRobin(processList, quantum);
            } else if (choice == 6) {
                running = false;
                System.out.println("Exiting simulator. Goodbye!");
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void addProcess(Scanner scanner, List<Process> processList) {
        System.out.print("Enter Process ID (number): ");
        int pid = scanner.nextInt();
        System.out.print("Enter Arrival Time: ");
        int arrival = scanner.nextInt();
        System.out.print("Enter Burst Time: ");
        int burst = scanner.nextInt();
        System.out.print("Enter Priority: ");
        int priority = scanner.nextInt();

        Process p = new Process(pid, arrival, burst, priority);
        processList.add(p);
        System.out.println("Process P" + pid + " added successfully.");
    }

    private static void viewProcesses(List<Process> processList) {
        if (processList.isEmpty()) {
            System.out.println("No processes added yet.");
            return;
        }
        System.out.println("\n--- Current Processes ---");
        for (Process p : processList) {
            System.out.println(p);
        }
    }
}