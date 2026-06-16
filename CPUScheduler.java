import java.util.ArrayList;
import java.util.List;

public class CPUScheduler {

    // First Come First Serve: run processes in arrival order, each to completion.
    public static void runFCFS(List<Process> processList) {

        if (processList.isEmpty()) {
            System.out.println("No processes to schedule. Add some first.");
            return;
        }

        List<Process> queue = new ArrayList<>(processList);
        queue.sort((a, b) -> a.getArrivalTime() - b.getArrivalTime());

        int currentTime = 0;
        double totalWaiting = 0;
        double totalTurnaround = 0;

        for (Process p : queue) {
            if (currentTime < p.getArrivalTime()) {
                currentTime = p.getArrivalTime();
            }

            int completionTime = currentTime + p.getBurstTime();
            int turnaroundTime = completionTime - p.getArrivalTime();
            int waitingTime    = turnaroundTime - p.getBurstTime();

            p.setSchedulingResults(completionTime, turnaroundTime, waitingTime);
            p.setState("TERMINATED");

            currentTime = completionTime;
            totalWaiting += waitingTime;
            totalTurnaround += turnaroundTime;
        }

        printResults("FCFS", queue, totalWaiting, totalTurnaround);
    }

    // Shortest Job First (non-preemptive): when the CPU is free,
    // pick the shortest job among those that have already arrived.
    public static void runSJF(List<Process> processList) {

        if (processList.isEmpty()) {
            System.out.println("No processes to schedule. Add some first.");
            return;
        }

        List<Process> remaining = new ArrayList<>(processList);
        List<Process> finished  = new ArrayList<>();

        int currentTime = 0;
        double totalWaiting = 0;
        double totalTurnaround = 0;

        while (!remaining.isEmpty()) {

            // Find the shortest job that has already arrived.
            Process shortest = null;
            for (Process p : remaining) {
                if (p.getArrivalTime() <= currentTime) {
                    if (shortest == null || p.getBurstTime() < shortest.getBurstTime()) {
                        shortest = p;
                    }
                }
            }

            // Nothing arrived yet — CPU idle, advance clock and retry.
            if (shortest == null) {
                currentTime++;
                continue;
            }

            int completionTime = currentTime + shortest.getBurstTime();
            int turnaroundTime = completionTime - shortest.getArrivalTime();
            int waitingTime    = turnaroundTime - shortest.getBurstTime();

            shortest.setSchedulingResults(completionTime, turnaroundTime, waitingTime);
            shortest.setState("TERMINATED");

            currentTime = completionTime;
            totalWaiting += waitingTime;
            totalTurnaround += turnaroundTime;

            remaining.remove(shortest);
            finished.add(shortest);
        }

        printResults("SJF", finished, totalWaiting, totalTurnaround);
    }

    // Shared results printer — works for any algorithm.
    private static void printResults(String name, List<Process> queue, double totalWaiting, double totalTurnaround) {
        System.out.println("\n--- " + name + " Scheduling Results ---");
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