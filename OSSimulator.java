import java.util.ArrayList;  // A resizable list — grows as we add processes
import java.util.List;
import java.util.Scanner;    // Reads input typed by the user in the terminal

public class OSSimulator {

    // 'main' is where every Java program starts running.
    public static void main(String[] args) {

        // Scanner connected to System.in (the keyboard).
        Scanner scanner = new Scanner(System.in);

        // Our list of processes. Starts empty; we add to it via the menu.
        List<Process> processList = new ArrayList<>();

        boolean running = true;  // Controls the menu loop. When false, program ends.

        // The main menu loop — keeps showing the menu until the user chooses Exit.
        while (running) {
            // Print the menu options
            System.out.println("\n===== OS SIMULATOR =====");
            System.out.println("1. Add a process");
            System.out.println("2. View all processes");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();  // Read the user's number

            // Decide what to do based on the choice.
            if (choice == 1) {
                addProcess(scanner, processList);
            } else if (choice == 2) {
                viewProcesses(processList);
            } else if (choice == 3) {
                running = false;  // Setting this false ends the while loop
                System.out.println("Exiting simulator. Goodbye!");
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();  // Always close the scanner when done — good housekeeping.
    }

    // --- Method to add a new process ---
    // We pass in the scanner and the list so this method can use them.
    private static void addProcess(Scanner scanner, List<Process> processList) {
        System.out.print("Enter Process ID (number): ");
        int pid = scanner.nextInt();

        System.out.print("Enter Arrival Time: ");
        int arrival = scanner.nextInt();

        System.out.print("Enter Burst Time: ");
        int burst = scanner.nextInt();

        System.out.print("Enter Priority: ");
        int priority = scanner.nextInt();

        // Build the Process object using the constructor, then add it to the list.
        Process p = new Process(pid, arrival, burst, priority);
        processList.add(p);

        System.out.println("Process P" + pid + " added successfully.");
    }

    // --- Method to display every process ---
    private static void viewProcesses(List<Process> processList) {
        if (processList.isEmpty()) {
            System.out.println("No processes added yet.");
            return;  // Exit the method early — nothing to show.
        }

        System.out.println("\n--- Current Processes ---");
        // A for-each loop: runs once for every process in the list.
        for (Process p : processList) {
            System.out.println(p);  // This automatically calls our toString() method
        }
    }
}