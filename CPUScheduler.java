import java.util.ArrayList;
import java.util.List;

public class CPUScheduler {

    // First Come First Serve: run processes in arrival order, each to completion.
    public static void runFCFS(List<Process> processList) {

        if (processList.isEmpty()) {
            System.out.println("No processes to schedule. Add some first.");
            return;
        }

        // Work on a COPY so the user's original order isn't scrambled.
        List<Process> queue = new ArrayList<>(processList);

        // Sort the copy by arrival time (earliest first).
        queue.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        int currentTime = 0;          // simulated clock
        double totalWaiting = 0;
        double totalTurnaround = 0;

        for (Process p : queue) {

            // If CPU is idle waiting for this process, jump the clock to its arrival.
            if (currentTime < p.getArrivalTime()) {
                currentTime = p.getArrivalTime();
            }

            int completionTime = currentTime + p.getBurstTime();
            int turnaroundTime = completionTime - p.getArrivalTime();
            int waitingTime    = turnaroundTime - p.getBurstTime();

            p.setSchedulingResults(completionTime, turnaroundTime, waitingTime);
            p.setState("TERMINATED");

            currentTime = completionTime;   // advance clock to when it finished

            totalWaiting += waitingTime;
            totalTurnaround += turnaroundTime;
        }

        printResults(queue, totalWaiting, totalTurnaround);
    }

    private static void printResults(List<Process> queue, double totalWaiting, double totalTurnaround) {
        System.out.println("\n--- FCFS Scheduling Results ---");
        System.out.println("PID\tArrival\tBurst\tCompletion\tTurnaround\tWaiting");

        for (Process p : queue) {
            System.out.println(
                  "P" + p.getPid()
                + "\t" + p.getArrivalTime()
                + "\t" + p.getBurstTime()
                + "\t" + p.getCompletionTime()
                + "\t\t" + p.getTurnaroundTime()
                + "\t\t" + p.getWaitingTime()
            );
        }

        int n = queue.size();
        System.out.printf("%nAverage Waiting Time: %.2f%n", totalWaiting / n);
        System.out.printf("Average Turnaround Time: %.2f%n", totalTurnaround / n);
    }
}