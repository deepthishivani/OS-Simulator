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
            System.out.println("6. Run Page Replacement (Memory)");
            System.out.println("7. Run Banker's Algorithm (Deadlock)");
            System.out.println("8. Exit");
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
                runMemory(scanner);
            } else if (choice == 7) {
                runBanker(scanner);
            } else if (choice == 8) {
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

    private static void runMemory(Scanner scanner) {
        System.out.print("Enter number of frames: ");
        int frames = scanner.nextInt();
        System.out.print("How many page references? ");
        int count = scanner.nextInt();
        int[] ref = new int[count];
        System.out.println("Enter the " + count + " page numbers one by one:");
        for (int i = 0; i < count; i++) {
            ref[i] = scanner.nextInt();
        }
        MemoryManager.runPageReplacement(ref, frames);
    }

    // Collects the Banker's input grids, then hands off to Banker.
    private static void runBanker(Scanner scanner) {
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        System.out.print("Enter number of resource types: ");
        int m = scanner.nextInt();

        int[][] allocation = new int[n][m];
        System.out.println("Enter the Allocation matrix (" + n + " rows x " + m + " values):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                allocation[i][j] = scanner.nextInt();
            }
        }

        int[][] max = new int[n][m];
        System.out.println("Enter the Max matrix (" + n + " rows x " + m + " values):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                max[i][j] = scanner.nextInt();
            }
        }

        int[] available = new int[m];
        System.out.println("Enter the Available resources (" + m + " values):");
        for (int j = 0; j < m; j++) {
            available[j] = scanner.nextInt();
        }

        Banker.checkSafety(allocation, max, available);
    }
}